package service.interfaces;

import java.util.List;

import dto.PlaneDto;
import exceptions.ValidatorException;
import modelHelper.CreatePlaneModel;

public interface IPlaneService {

	List<PlaneDto> getAll();

	boolean createPlane(CreatePlaneModel createPlaneModel) throws ValidatorException;

	boolean editPlane(PlaneDto planeDto);

	boolean removePlane(int tailNumber);
}
