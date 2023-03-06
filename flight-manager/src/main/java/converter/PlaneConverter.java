package converter;

import org.springframework.stereotype.Component;

import dto.PlaneDto;
import modelHelper.CreatePlaneModel;

import msg.project.flightmanager.model.Plane;

@Component
public class PlaneConverter implements IConverter<Plane, PlaneDto> {

	@Override
	public PlaneDto convertToDTO(Plane plane) {

		PlaneDto planeDto = PlaneDto.builder().model(plane.getModel()).tailNumber(plane.getTailNumber())
				.capacity(plane.getCapacity()).fuelTankCapacity(plane.getFuelTankCapacity())
				.manufacturingDate(plane.getManufacturingDate()).firstFlight(plane.getFirstFlight())
				.lastRevision(plane.getLastRevision()).size(plane.getSize()).build();

		return planeDto;
	}

	@Override
	public Plane convertToEntity(PlaneDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Plane createModelToEntity(CreatePlaneModel createPlaneModel) {

		Plane plane = Plane.builder().model(createPlaneModel.getModel()).tailNumber(createPlaneModel.getTailNumber())
				.capacity(createPlaneModel.getCapacity()).fuelTankCapacity(createPlaneModel.getFuelTankCapacity())
				.manufacturingDate(createPlaneModel.getManufacturingDate()).size(createPlaneModel.getSize()).build();

		return plane;
	}
}