package msg.project.flightmanager.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.ReportDto;
import msg.project.flightmanager.model.Report;

@Component
public class ReportConverter implements IConverter<Report, ReportDto> {
	@Autowired
	private FlightConverter flightConverter;

	@Override
	public ReportDto convertToDTO(Report entity) {
		return ReportDto.builder()
				.reportCode(entity.getReportCode())
				.reportType(entity.getReportType().getLabel())
				.generatedAt(entity.getGeneratedAt())
				.content(entity.getContent())
				.reporteByUsername(entity.getReportedBy().getUsername())
				.flightDto(this.flightConverter.convertToDTO(entity.getFlight()))
				.build();
	}

	@Override
	public Report convertToEntity(ReportDto dto) {
		return Report.builder()
				.content(dto.getContent())
				.build();
	}

}
