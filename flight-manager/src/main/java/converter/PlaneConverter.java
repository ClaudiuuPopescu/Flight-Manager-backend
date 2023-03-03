package converter;

import org.springframework.stereotype.Component;

import dto.PlaneDto;
import msg.project.flightmanager.model.Plane;

@Component
public class PlaneConverter implements IConverter<Plane, PlaneDto> {

	@Override
	public PlaneDto convertToDTO(Plane plane) {
	
		return null;
	}

	@Override
	public Plane convertToEntity(PlaneDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}