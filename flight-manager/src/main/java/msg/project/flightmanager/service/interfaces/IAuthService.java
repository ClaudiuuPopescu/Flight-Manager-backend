package msg.project.flightmanager.service.interfaces;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletRequest;
import msg.project.flightmanager.exceptions.AuthException;
import msg.project.flightmanager.exceptions.RefreshTokenException;

public interface IAuthService {

	ResponseCookie login(String userName, String password) throws AuthException;
	void logout() throws AuthException, RefreshTokenException;
	void refreshToken(HttpServletRequest request) throws RefreshTokenException;
}
