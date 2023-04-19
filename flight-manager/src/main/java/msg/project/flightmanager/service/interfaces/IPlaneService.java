package msg.project.flightmanager.service.interfaces;

import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;
import msg.project.flightmanager.modelHelper.EditLastRevisionPlaneModel;

public interface IPlaneService {

	String getAll();

	boolean createPlane(CreatePlaneModel createPlaneModel) throws ValidatorException;

	boolean editLastRevisionPlane(EditLastRevisionPlaneModel editLastRevisionPlaneModel);

	boolean removePlane(int tailNumber);

	boolean movePlaneToAnotherCompany(int tailNumber, String to_companyName) throws CompanyException;
}
