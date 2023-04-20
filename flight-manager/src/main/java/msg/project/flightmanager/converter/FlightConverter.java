package msg.project.flightmanager.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.repository.FlightTemplateRepository;

@Component
public class FlightConverter implements IConverter<Flight, FlightDto>{

	@Autowired
	private AirportConverter airportConverter;
	
	@Autowired
	private PlaneConverter planeConverter;
	
	@Autowired
	private FlightTemplateConverter flightTemplateConverter;
	
	@Autowired
	private FlightTemplateRepository flightTemplateRepository;
	
	@Override
	public FlightDto convertToDTO(Flight flight) {
		
		return FlightDto.builder()
				.idFlight(flight.getIdFlight())
				.flightName(flight.getFlightName())
				.date(flight.getDate())
				.gate(flight.getGate())
				.boardingTime(flight.getBoardingTime())
				.duration(flight.getDuration())
				.from(flight.getFrom().getAirportName())
				.to(flight.getTo().getAirportName())
				.plane(flight.getPlane().getTailNumber())
				.flightTemplateID(flight.getFlightTemplate().getIdFlightTemplate())
				.build();
	}

	@Override
	public Flight convertToEntity(FlightDto flightDto) {

		return Flight.builder()
				.idFlight(flightDto.getIdFlight())
				.flightName(flightDto.getFlightName())
				.date(flightDto.getDate())
				.gate(flightDto.getGate())
				.boardingTime(flightDto.getBoardingTime())
				.duration(flightDto.getDuration())
				.flightTemplate(this.flightTemplateRepository.findById(flightDto.getFlightTemplateID()).get())
				.build();
	}

}
