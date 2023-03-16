package msg.project.flightmanager.service.interfaces;

import org.springframework.http.ResponseCookie;

import msg.project.flightmanager.exceptions.AuthException;

public interface IAuthService {

	ResponseCookie login(String userName, String password) throws AuthException;
	ResponseCookie logout();
	void refreshToken();
}
