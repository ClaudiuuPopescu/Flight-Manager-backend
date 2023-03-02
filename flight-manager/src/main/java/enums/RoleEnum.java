package enums;

import org.springframework.http.HttpStatus;

import exceptions.FlightManagerException;

public enum RoleEnum {
	CREW("crew"), FM("flight_manager"), CM("company_manager"), ADM("administrator");

	private final String label;

	RoleEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public static RoleEnum fromLabel(String label) {
		for (RoleEnum roleEnum : RoleEnum.values()) {
			if (roleEnum.getLabel().equals(label)) {
				return roleEnum;
			}
		}
		throw new FlightManagerException(HttpStatus.NOT_FOUND, "Invalid role label: " + label);
	}
}