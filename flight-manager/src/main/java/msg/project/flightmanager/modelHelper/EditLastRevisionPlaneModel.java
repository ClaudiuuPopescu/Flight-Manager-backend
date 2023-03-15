package msg.project.flightmanager.modelHelper;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EditLastRevisionPlaneModel {

	private int tailNumber;
	private LocalDate newRevision;
}
