package msg.project.flightmanager.service;

import java.util.Optional;

import msg.project.flightmanager.model.RefreshToken;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.service.interfaces.IRefreshTokenService;

public class RefreshTokenService implements IRefreshTokenService{

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public RefreshToken createRefreshToken(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verifyExpiration(RefreshToken token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int deleteByUserName(String username) {
		// TODO Auto-generated method stub
		return 0;
	}

}
