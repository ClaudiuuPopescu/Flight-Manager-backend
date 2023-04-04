package msg.project.flightmanager.modelHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EditLastRevisionPlaneModel {

	private int tailNumber;
	private String newRevision;
}
