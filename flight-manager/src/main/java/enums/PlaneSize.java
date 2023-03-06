package enums;

import org.springframework.http.HttpStatus;

import exceptions.FlightManagerException;
import lombok.Getter;

@Getter
public enum PlaneSize {

	SMALL("small"),
	MID("mid"),
	WIDE("wide-body"),
	JUMBO("jumbo");

	private String size;
	
	PlaneSize(String size) {
		this.size = size;
	}
	
	public String getSize() {
		return this.size;
	}
	
	public static PlaneSize fromSize(String size) {
		for (PlaneSize planeSize : PlaneSize.values()) {
			if (planeSize.getSize().equals(size)) {
				return planeSize;
			}
		}
		throw new FlightManagerException(HttpStatus.NOT_FOUND, "Invalid plane size: " + size);
	}
}
