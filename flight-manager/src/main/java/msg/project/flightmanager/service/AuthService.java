package msg.project.flightmanager.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import msg.project.flightmanager.exceptions.AuthException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.model.RefreshToken;
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

				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(userName, password));
				
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
	public ResponseCookie logout() {
		
		//TODO
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return cookie;
	}

	@Override
	public void refreshToken() {
		// TODO Auto-generated method stub

	}


}
