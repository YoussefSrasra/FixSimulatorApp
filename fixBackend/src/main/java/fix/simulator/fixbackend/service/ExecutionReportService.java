package fix.simulator.fixbackend.service;

import fix.simulator.fixbackend.dto.OrderDTO;
import fix.simulator.fixbackend.model.ExecutionReportEntity;
import fix.simulator.fixbackend.model.Order;
import fix.simulator.fixbackend.repository.ExecutionReportRepository;
import fix.simulator.fixbackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExecutionReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ExecutionReportRepository executionReportRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void processExecutionReport(String clOrdID, String brokerOrderID, String execID, String status, Double cumQty, Double avgPx, Double lastPx, Double lastQty) {

        Optional<Order> orderOpt = orderRepository.findByClOrdID(clOrdID);

        if (orderOpt.isEmpty()) {
            try {
                Thread.sleep(100);
                orderOpt = orderRepository.findByClOrdID(clOrdID);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderID(brokerOrderID);
            order.setStatus(mapStatus(status));
            orderRepository.save(order);

            ExecutionReportEntity report = ExecutionReportEntity.builder()
                    .execID(execID)
                    .order(order)
                    .ordStatus(status)
                    .cumQty(cumQty)
                    .avgPx(avgPx)
                    .lastPx(lastPx)
                    .lastShares(lastQty)
                    .build();

            executionReportRepository.save(report);

            OrderDTO orderDTO = convertToDTO(order);

            messagingTemplate.convertAndSend("/topic/orders", orderDTO);

            System.out.println("WebSocket: Ordre " + clOrdID + " mis à jour et diffusé.");
        } else {
            System.err.println("ERREUR CRITIQUE: Execution Report reçu pour un ClOrdID inconnu en base: " + clOrdID);
        }
    }

    private Order.OrderStatus mapStatus(String fixStatus) {
        return switch (fixStatus) {
            case "2" -> Order.OrderStatus.FILLED;
            case "1" -> Order.OrderStatus.PARTIALLY_FILLED;
            case "8" -> Order.OrderStatus.REJECTED;
            default -> Order.OrderStatus.NEW;
        };
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