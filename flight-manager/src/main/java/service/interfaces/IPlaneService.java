package service.interfaces;

import dto.PlaneDto;
import exceptions.ValidatorException;
import modelHelper.CreatePlaneModel;

public interface IPlaneService {

	boolean createPlane(CreatePlaneModel createPlaneModel) throws ValidatorException;

	boolean editPlane(PlaneDto planeDto);

	boolean removePlane(int tailNumber);
}
