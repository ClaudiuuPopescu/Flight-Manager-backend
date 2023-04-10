package msg.project.flightmanager.enums;

import org.springframework.http.HttpStatus;

import msg.project.flightmanager.exceptions.FlightManagerException;

public enum ReportTypeEnum {
	
	FR("functional"),
	IR("informational");

	private final String label;

	ReportTypeEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public static ReportTypeEnum fromLabel(String label) {
		for (ReportTypeEnum reportType : ReportTypeEnum.values()) {
			if (reportType.getLabel().equals(label)) {
				return reportType;
			}
		}
		throw new FlightManagerException(HttpStatus.NOT_FOUND, "Invalid report type label: " + label);
	}
}
