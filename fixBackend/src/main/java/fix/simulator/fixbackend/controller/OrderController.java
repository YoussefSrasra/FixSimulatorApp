package fix.simulator.fixbackend.controller;

import fix.simulator.fixbackend.dto.OrderDTO;
import fix.simulator.fixbackend.model.Order;
import fix.simulator.fixbackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/send")
    public ResponseEntity<Order> sendNewOrder(@RequestBody Order order) {
        Order sentOrder = orderService.createAndSendOrder(order);
        return ResponseEntity.ok(sentOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(orders);
    }
}