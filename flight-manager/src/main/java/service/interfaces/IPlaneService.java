package service.interfaces;

import java.util.List;

import dto.PlaneDto;
import exceptions.CompanyException;
import exceptions.ValidatorException;
import modelHelper.CreatePlaneModel;
import modelHelper.EditLastRevisionPlaneModel;

public interface IPlaneService {

	List<PlaneDto> getAll();

	boolean createPlane(CreatePlaneModel createPlaneModel) throws ValidatorException;

	boolean editLastRevisionPlane(EditLastRevisionPlaneModel editLastRevisionPlaneModel);

	boolean removePlane(int tailNumber);

	boolean movePlaneToAnotherCompany(int tailNumber, String to_companyName) throws CompanyException;
}
