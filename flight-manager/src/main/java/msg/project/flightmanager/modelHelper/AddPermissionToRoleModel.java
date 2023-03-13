package msg.project.flightmanager.modelHelper;

import lombok.Getter;

@Getter
public class AddPermissionToRoleModel {

	private String roleTitle;
	private String permissionTitle;
	
	public AddPermissionToRoleModel(String roleTitle, String permissionTitle) {
		this.roleTitle = roleTitle;
		this.permissionTitle = permissionTitle;
	}
}
