package fix.simulator.fixbackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String clOrdID;
    private String symbol;
    private Double quantity;
    private Double price;
    private String side;
    private String status;
    private List<ExecutionReportDTO> reports;

    @Data
    public static class ExecutionReportDTO {
        private String execID;
        private String ordStatus;
        private Double lastQty;
        private Double lastPx;
    }
}