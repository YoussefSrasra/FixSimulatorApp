package fix.simulator.fixbackend.service;

import fix.simulator.fixbackend.dto.OrderDTO;
import fix.simulator.fixbackend.fix.FixInitiatorApp;
import fix.simulator.fixbackend.model.Order;
import fix.simulator.fixbackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FixInitiatorApp fixInitiatorApp;

    public Order createAndSendOrder(Order order) {
        if (order.getClOrdID() == null) {
            order.setClOrdID(UUID.randomUUID().toString());
        }
        order.setStatus(Order.OrderStatus.PENDING);

        Order savedOrder = saveOrderInNewTransaction(order);

        fixInitiatorApp.sendOrder(savedOrder);

        return savedOrder;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Order saveOrderInNewTransaction(Order order) {
        return orderRepository.saveAndFlush(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setClOrdID(order.getClOrdID());
        dto.setSymbol(order.getSymbol());
        dto.setQuantity(order.getQuantity());
        dto.setPrice(order.getPrice());
        dto.setSide(order.getSide().name());
        dto.setStatus(order.getStatus().name());

        if (order.getExecutionReports() != null) {
            List<OrderDTO.ExecutionReportDTO> reportDTOs = order.getExecutionReports().stream()
                    .map(report -> {
                        OrderDTO.ExecutionReportDTO rDto = new OrderDTO.ExecutionReportDTO();
                        rDto.setExecID(report.getExecID());
                        rDto.setOrdStatus(report.getOrdStatus());
                        rDto.setLastQty(report.getLastShares());
                        rDto.setLastPx(report.getLastPx());
                        return rDto;
                    })
                    .collect(Collectors.toList());
            dto.setReports(reportDTOs);
        }

        return dto;
    }
}