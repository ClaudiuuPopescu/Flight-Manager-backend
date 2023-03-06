package converter;

import org.springframework.beans.factory.annotation.Autowired;

import dto.AirportDto;
import msg.project.flightmanager.model.Airport;

<<<<<<< HEAD
public class AirportConverter implements IConverter<Airport, AirportDto>{
	
=======
public class AirportConverter implements IConverter<Airport, AirportDto> {
>>>>>>> main
	@Autowired
	private AddressConverter addressConverter;

	@Override
	public AirportDto convertToDTO(Airport airport) {
<<<<<<< HEAD
		
		return AirportDto.builder().airportName(airport.getAirportName())
				.addressDto(this.addressConverter.convertToDTO(airport.getAddress()))
				.build();
=======
		return AirportDto.builder().airportName(airport.getAirportName())
				.codeIdentifier(airport.getCodeIdentifier()).runWarys(airport.getRunWays())
				.gateWays(airport.getGateWays())
				.addressDto(this.addressConverter.convertToDTO(airport.getAddress())).build();
>>>>>>> main
	}

	@Override
	public Airport convertToEntity(AirportDto airportDto) {
<<<<<<< HEAD
		
		return Airport.builder().airportName(airportDto.getAirportName())
				.address(this.addressConverter.convertToEntity(airportDto.getAddressDto()))
				.build();
	}

}
=======
		 return Airport.builder().airportName(airportDto.getAirportName())
					.runWays(airportDto.getRunWarys()).gateWays(airportDto.getGateWays())
					.address(this.addressConverter.convertToEntity(airportDto.getAddressDto())).build();
	}
}
>>>>>>> main
