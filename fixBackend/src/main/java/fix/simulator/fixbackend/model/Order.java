package fix.simulator.fixbackend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String clOrdID;   //tag 11
    private String orderID;
    @Column(nullable = false)
    private String symbol;    //tag 55
    @Column(nullable = false)
    private Double quantity;   //tag 38
    private Double price;    //tag 44
    @Enumerated(EnumType.STRING)
    private Side side;   //tag 54
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutionReportEntity> executionReports = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = OrderStatus.PENDING;
    }

    public enum Side { BUY, SELL }
    public enum OrderStatus { PENDING, NEW, PARTIALLY_FILLED, FILLED, REJECTED, CANCELED }
}