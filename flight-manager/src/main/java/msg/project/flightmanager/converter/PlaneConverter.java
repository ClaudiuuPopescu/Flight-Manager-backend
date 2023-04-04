package msg.project.flightmanager.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.PlaneDto;
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
				.manufacturingDate(plane.getManufacturingDate().toString())
				.firstFlight(plane.getFirstFlight().toString())
				.lastRevision(plane.getLastRevision().toString())
				.size(plane.getSize().toString().toLowerCase()).build();

		return planeDto;
	}

	@Override
	public Plane convertToEntity(PlaneDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Plane createModelToEntity(CreatePlaneModel createPlaneModel) {

		Plane plane = Plane.builder()
				.model(createPlaneModel.getModel())
				.tailNumber(createPlaneModel.getTailNumber())
				.capacity(createPlaneModel.getCapacity())
				.fuelTankCapacity(createPlaneModel.getFuelTankCapacity())
				.manufacturingDate(LocalDate.parse(createPlaneModel.getManufacturingDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.build();

		return plane;
	}
}