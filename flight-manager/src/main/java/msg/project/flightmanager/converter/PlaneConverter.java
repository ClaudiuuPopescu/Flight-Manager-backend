package msg.project.flightmanager.converter;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Size;
import msg.project.flightmanager.dto.PlaneDto;
import msg.project.flightmanager.enums.PlaneSize;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;

@Component
public class PlaneConverter implements IConverter<Plane, PlaneDto> {

	@Override
	public PlaneDto convertToDTO(Plane plane) {

		PlaneDto planeDto = PlaneDto.builder()
				.model(plane.getModel())
				.tailNumber(plane.getTailNumber())
				.capacity(plane.getCapacity())
				.fuelTankCapacity(plane.getFuelTankCapacity())
				.manufacturingDate(plane.getManufacturingDate())
				.firstFlight(plane.getFirstFlight())
				.lastRevision(plane.getLastRevision())
				.size(plane.getSize().toString().toLowerCase()).build();

		return planeDto;
	}

	@Override
	public Plane convertToEntity(PlaneDto planeDto) {
		Plane plane = Plane.builder()
				.model(planeDto.getModel())
				.tailNumber(planeDto.getTailNumber())
				.capacity(planeDto.getCapacity())
				.fuelTankCapacity(planeDto.getFuelTankCapacity())
				.manufacturingDate(planeDto.getManufacturingDate())
				.firstFlight(planeDto.getFirstFlight())
				.lastRevision(planeDto.getLastRevision())
				.size(PlaneSize.fromSize(planeDto.getSize()))
				.build();
	
		return plane;
	}

	public Plane createModelToEntity(CreatePlaneModel createPlaneModel) {

		Plane plane = Plane.builder()
				.model(createPlaneModel.getModel())
				.tailNumber(createPlaneModel.getTailNumber())
				.capacity(createPlaneModel.getCapacity())
				.fuelTankCapacity(createPlaneModel.getFuelTankCapacity())
				.manufacturingDate(createPlaneModel.getManufacturingDate())
				.build();

		return plane;
	}
}