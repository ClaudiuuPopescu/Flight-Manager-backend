package service;

import org.springframework.beans.factory.annotation.Autowired;

import converter.PlaneConverter;
import dto.PlaneDto;
import exceptions.ValidatorException;
import modelHelper.CreatePlaneModel;
import msg.project.flightmanager.model.Plane;
import repository.PlaneRepository;
import service.interfaces.IPlaneService;
import validator.PlaneValidator;

public class PlaneService implements IPlaneService {
	@Autowired
	private PlaneRepository planeRepository;
	@Autowired
	private PlaneValidator planeValidator;
	@Autowired
	private PlaneConverter planeConverter;

	@Override
	public boolean createPlane(CreatePlaneModel createPlaneModel) throws ValidatorException {

		// TODO verficare rol current user
		this.planeValidator.validateCreatePlaneModel(createPlaneModel);

		Plane plane = this.planeConverter.createModelToEntity(createPlaneModel);

		this.planeRepository.save(plane);
		return true;
	}

	@Override
	public boolean editPlane(PlaneDto planeDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removePlane(int tailNumber) {
		// TODO Auto-generated method stub
		return false;
	}

}
