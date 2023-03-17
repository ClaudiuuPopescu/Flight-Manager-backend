package msg.project.flightmanager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import msg.project.flightmanager.exceptions.AuthException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.RefreshTokenException;
import msg.project.flightmanager.model.RefreshToken;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.interfaces.IAuthService;
import msg.project.flightmanager.service.utils.JwtUtils;

@Service
public class AuthService implements IAuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RefreshTokenService refreshTokenService;

	@Override
	public ResponseCookie login(String userName, String password) throws AuthException {

		Optional<User> userOptional = this.userRepository.findByUsername(userName);
		if (userOptional.isPresent()) {
			String encodedPassword = this.passwordEncoder.encode(password);
			User user = userOptional.get();
			if (user.getPassword().equals(encodedPassword)) {
				// creez cookie si token

				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

				SecurityContextHolder.getContext().setAuthentication(authentication);

				ResponseCookie jwtCookie = this.jwtUtils.generateJwtCookie(user);

				RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(user.getUsername());

				ResponseCookie jwtRefreshCookie = this.jwtUtils.generateRefreshCookie(refreshToken.getToken());

				return jwtCookie;

			} else
				throw new AuthException(String.format("Wrong password for %d", userName), ErrorCode.WRONG_PASSWORD);
		} else
			throw new AuthException(String.format("This username %d does not exists!", userName),
					ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
	}

	@Override
	public void logout() throws AuthException, RefreshTokenException {

		String userName;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		Optional<User> optionalUser = this.userRepository.findByUsername(userName);
		if (optionalUser.isPresent()) {
			this.refreshTokenService.deleteByUserName(userName);
		} else
			throw new AuthException(String.format("There is no user with the username %d logged in!", userName),
					ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);

	}

	@Override
	public void refreshToken(HttpServletRequest request) throws RefreshTokenException {
		String refreshToken = this.jwtUtils.getJwtRefreshFromCookies(request);

		if ((refreshToken != null) && (refreshToken.length() > 0)) {

			Optional<RefreshToken> refreshTokenOptional = this.refreshTokenService.findByToken(refreshToken);
			if (refreshTokenOptional.isPresent()
					&& this.refreshTokenService.verifyExpiration(refreshTokenOptional.get())) {

				User user = refreshTokenOptional.get().getUser();
				ResponseCookie jwtCookie = this.jwtUtils.generateJwtCookie(user);
			} else
				throw new RefreshTokenException("the token is not in the DB", ErrorCode.NOT_A_TOKEN_IN_THE_DB);

		}

	}

}
