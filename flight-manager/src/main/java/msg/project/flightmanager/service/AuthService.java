package msg.project.flightmanager.service;

import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;

import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.AuthException;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.interfaces.IAuthService;
import msg.project.flightmanager.service.utils.JwtUtils;

public class AuthService implements IAuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	JwtUtils jwtUtils;

	@Override
	public String login(String userName, String password) throws AuthException {

		Optional<User> user = this.userRepository.findByUsername(userName);
		if (user.isPresent()) {
			String encodedPassword = this.passwordEncoder.encode(password);
			if (user.get().getPassword().equals(encodedPassword)) {
				// creez cookie si token

				ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

				return null;
			} else
				throw new AuthException(String.format("Wrong password for %d", userName), ErrorCode.WRONG_PASSWORD);
		} else
			throw new AuthException(String.format("This username %d does not exists!", userName),
					ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshToken() {
		// TODO Auto-generated method stub

	}

}
