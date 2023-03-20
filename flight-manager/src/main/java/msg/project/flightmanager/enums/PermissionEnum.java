package msg.project.flightmanager.enums;

import org.springframework.http.HttpStatus;

import msg.project.flightmanager.exceptions.FlightManagerException;

public enum PermissionEnum {
	PERSONAL_DATA_MANAGEMENT("personal_data_management"),

	CREW_MANAGEMENT("crew_management"), // CM, ADM

	FLIGHT_MANAGER_MANAGEMENT("flight_manager_management"), // ADM
	
	COMPANY_MANAGER_MANAGEMENT("company_manager_management"), // ADM

	ADMINISTRATOR_MANAGEMENT("administrator_management"), // ADM

	AIRPLANE_MANAGEMENT("airplane_management"), // CM, ADM

	FLIGHT_MANAGEMENT("flight_management"), // FM, ADM

	FLIGHT_HISTORY_MANAGEMENT("flight_history_management"), // FM, CM, ADM

	FLIGHT_TEMPLATE_MANAGEMENT("flight_template_management"), // FM, ADM

	ITINERARY_MANAGEMENT("itinerary_management"), // CM, ADM

	COMPANY_MANAGEMENT("company_management"), // ADM

	AIRPORT_MANAGEMENT("airport_management"), // CM, ADM

	GENERATE_REPORT("generate_report"), // CM, ADM

	EXPORT_DATA("export_data"), // ADM

	IMPORT_DATA("import_data"); // ADM

	private final String label;

	PermissionEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public static PermissionEnum fromLabel(String label) {
		for (PermissionEnum permissionEnum : PermissionEnum.values()) {
			if (permissionEnum.getLabel().equals(label)) {
				return permissionEnum;
			}
		}
		throw new FlightManagerException(HttpStatus.NOT_FOUND, "Invalid permission label: " + label);
	}
}
