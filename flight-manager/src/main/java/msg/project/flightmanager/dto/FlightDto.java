package msg.project.flightmanager.dto;

import java.sql.Time;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {

	private Long idFlight;
	private String flightName;
	private LocalDate date;
	private String gate;
	private Time boardingTime;
	private double duration;
	private String from;
	private String to;
	//tailNumber
	private int plane;
	private Long flightTemplateID;

}
