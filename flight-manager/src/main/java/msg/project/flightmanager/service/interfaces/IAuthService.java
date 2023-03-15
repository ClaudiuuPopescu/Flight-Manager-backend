package msg.project.flightmanager.service.interfaces;

import msg.project.flightmanager.exceptions.AuthException;

public interface IAuthService {

	String login(String userName, String password) throws AuthException;
	void logout();
	void refreshToken();
}
