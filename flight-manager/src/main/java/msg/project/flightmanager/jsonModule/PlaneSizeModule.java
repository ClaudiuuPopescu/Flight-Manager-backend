package msg.project.flightmanager.jsonModule;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import msg.project.flightmanager.enums.PlaneSize;

public class PlaneSizeModule extends SimpleModule{

	private static final long serialVersionUID = 2L;
	
	public PlaneSizeModule() {
        addSerializer(PlaneSize.class, new PlaneSizeSerializer());
        addDeserializer(PlaneSize.class, new PlaneSizeDeserializer());
	}

	private static class PlaneSizeSerializer extends JsonSerializer<PlaneSize>{
		@Override
	    public void serialize(PlaneSize value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
	        gen.writeString(value.getSize());
	    }
	}

	private static class PlaneSizeDeserializer extends JsonDeserializer<PlaneSize>{
	
		@Override
		public PlaneSize deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			String planeSizeLabel = p.getText().toUpperCase();
			return PlaneSize.valueOf(planeSizeLabel);
		}
	}
}
