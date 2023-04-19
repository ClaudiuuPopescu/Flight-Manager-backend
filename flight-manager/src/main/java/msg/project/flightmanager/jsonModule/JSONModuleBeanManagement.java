package msg.project.flightmanager.jsonModule;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JSONModuleBeanManagement {

	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	public LocalDateModule getLocalDateModule() {
		return new LocalDateModule();
	}
	public PlaneSizeModule getPlaneSizeEnumModule() {
		return new PlaneSizeModule();
	}
}
