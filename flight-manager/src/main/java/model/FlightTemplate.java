package model;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "FlightTemplate")
@Table(name = "flight_template")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties("hibernateLazyInitializer")
public class FlightTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "idFlightTemplate")
	private Long idFlightTemplate;

	@Column(name = "name")
	private boolean name = false;
	
	@Column(name = "from")
	private boolean from = false;

	@Column(name = "to")
	private boolean to = false;
	
	@Column(name = "flight")
	private boolean flight = false;
	
	@Column(name = "date")
	private boolean date = false;
	
	@Column(name = "seat")
	private boolean seat = false;
	
	@Column(name = "gate")
	private boolean gate = false;
	
	@Column(name = "boarding_time")
	private boolean boardingTime = false;

}
