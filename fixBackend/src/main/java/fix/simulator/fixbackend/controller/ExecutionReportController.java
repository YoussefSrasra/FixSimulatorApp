package fix.simulator.fixbackend.controller;

import fix.simulator.fixbackend.model.ExecutionReportEntity;
import fix.simulator.fixbackend.repository.ExecutionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ExecutionReportController {

    @Autowired
    private ExecutionReportRepository reportRepository;

    @GetMapping("/order/{clOrdID}")
    public List<ExecutionReportEntity> getReportsByOrder(@PathVariable String clOrdID) {
        return reportRepository.findAll();
    }
}