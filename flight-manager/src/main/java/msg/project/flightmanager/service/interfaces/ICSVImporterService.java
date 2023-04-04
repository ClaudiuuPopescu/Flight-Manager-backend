package msg.project.flightmanager.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import msg.project.flightmanager.exceptions.CompanyException;

public interface ICSVImporterService {
	
	boolean hasCSVFormat(MultipartFile file);
	void csvToEntity(Class<?> clazz, MultipartFile file) throws CompanyException;
}
