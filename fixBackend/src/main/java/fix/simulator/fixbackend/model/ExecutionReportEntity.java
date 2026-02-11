package fix.simulator.fixbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="execution_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ExecutionReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String execID;   //tag 17
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id", nullable = false)
    private Order order;     //tag 37
    private String ordStatus;  //tag 39
    private Double lastShares;  //tag 32
    private Double cumQty;   //tag 14
    private Double lastPx;   //tag 31
    private Double avgPx;    //tag 6
    @CreationTimestamp
    private LocalDateTime receivedAt;




}
