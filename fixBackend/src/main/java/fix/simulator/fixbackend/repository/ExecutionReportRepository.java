package fix.simulator.fixbackend.repository;

import fix.simulator.fixbackend.model.ExecutionReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionReportRepository extends JpaRepository<ExecutionReportEntity, Long> {}
