package enums;

public enum RoleEnum {
	CREW("crew"), FM("flight_manager"), CM("company_manager"), ADM("administrator");

	private final String label;

	RoleEnum(String label) {
		this.label = label;
	}
}