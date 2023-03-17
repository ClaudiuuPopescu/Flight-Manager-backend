package msg.project.flightmanager.service.interfaces;

import java.util.Optional;

import msg.project.flightmanager.exceptions.RefreshTokenException;
import msg.project.flightmanager.model.RefreshToken;

public interface IRefreshTokenService {

	Optional<RefreshToken> findByToken(String token);
	RefreshToken createRefreshToken(String username);
	boolean verifyExpiration(RefreshToken token) throws RefreshTokenException;
	void deleteByUserName(String username);
}
