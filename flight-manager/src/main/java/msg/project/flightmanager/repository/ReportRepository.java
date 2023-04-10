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
	
	 @Query("SELECT MAX(r.id_report) FROM Report r")
	 Optional<Long> getLastReportId();
	 
	 @Query("SELECT MAX(r.id_report) FROM Report r WHERE r.reportType = :reportType")
	 Optional<Long> findLastIdByType(@Param("reportType") ReportTypeEnum reportType);
	 
	 @Query("SELECT r from Report r where r.reportCode = :reportCode")
	 Optional<Report> findByReportCode(@Param("reportCode") String reportCode);

}
