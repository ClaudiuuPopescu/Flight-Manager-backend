package converter;

import org.springframework.beans.factory.annotation.Autowired;

import dto.AirportDto;
import msg.project.flightmanager.model.Airport;

public class AirportConverter implements IConverter<Airport, AirportDto> {
	@Autowired
	private AddressConverter addressConverter;

	@Override
	public AirportDto convertToDTO(Airport airport) {
		return AirportDto.builder().airportName(airport.getAirportName())
				.codeIdentifier(airport.getCodeIdentifier()).runWarys(airport.getRunWays())
				.gateWays(airport.getGateWays())
				.addressDto(this.addressConverter.convertToDTO(airport.getAddress())).build();
	}

	@Override
	public Airport convertToEntity(AirportDto airportDto) {
		 return Airport.builder().airportName(airportDto.getAirportName())
					.runWays(airportDto.getRunWarys()).gateWays(airportDto.getGateWays())
					.address(this.addressConverter.convertToEntity(airportDto.getAddressDto())).build();
	}
}