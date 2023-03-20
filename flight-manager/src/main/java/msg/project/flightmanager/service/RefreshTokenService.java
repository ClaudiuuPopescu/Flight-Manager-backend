package msg.project.flightmanager.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.RefreshTokenException;
import msg.project.flightmanager.model.RefreshToken;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.RefreshTokenRepositoy;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.interfaces.IRefreshTokenService;

@Service
public class RefreshTokenService implements IRefreshTokenService {

	@Value("${spring.app.jwtRefreshExpirationMs}")
	private int refreshTokenTime;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RefreshTokenRepositoy refreshTokenRepositoy;

	@Autowired
	private TokenService tokenService;

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return this.refreshTokenRepositoy.getRefreshTokenByToken(token);
	}

	@Override
	public RefreshToken createRefreshToken(String username) {
		Optional<User> userOptional = this.userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			String token = this.tokenService.getJWTToken(username, user.getRole().getTitle());
			RefreshToken refreshToken = new RefreshToken(user, Instant.now().plusMillis(refreshTokenTime), token);

			this.refreshTokenRepositoy.save(refreshToken);
			return refreshToken;
		}
		return null;
	}

	@Override
	public boolean verifyExpiration(RefreshToken token) throws RefreshTokenException {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			this.refreshTokenRepositoy.delete(token);
			throw new RefreshTokenException(
					String.format("Refresh token for the user %s was expired", token.getUser().getUsername()),
					ErrorCode.EXPIRED);
		}
		return true;
	}

	@Override
	public void deleteByUserName(String username) throws RefreshTokenException {

		Optional<User> user = this.userRepository.findByUsername(username);
		if (user.isPresent()) {
			Optional<RefreshToken> refreshTokenOptional = this.refreshTokenRepositoy.getRefreshTokenByUser(user.get());
			if(refreshTokenOptional.isPresent()) {
				this.refreshTokenRepositoy.delete(refreshTokenOptional.get());
			}
			else
				throw new RefreshTokenException(String.format("there is no User with the username: %s, that has this refresh token!", username), ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
		}
		else
			throw new RefreshTokenException(String.format("there is no User with the username: %s", username), ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
		
	}

}
