package msg.project.flightmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import msg.project.flightmanager.enums.ReportTypeEnum;
import msg.project.flightmanager.model.Report;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {
	
	 @Query("SELECT MAX(r.id) FROM Report r")
	 Optional<Long> getLastReportId();
	 
	 @Query("SELECT MAX(r.id) FROM Report r WHERE r.type = :type")
	 Optional<Long> findLastIdByType(@Param("type") ReportTypeEnum type);

}
