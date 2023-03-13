package msg.project.flightmanager.modelHelper;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditLastRevisionPlaneModel {

	private int tailNumber;
	private LocalDate newRevision;
}
