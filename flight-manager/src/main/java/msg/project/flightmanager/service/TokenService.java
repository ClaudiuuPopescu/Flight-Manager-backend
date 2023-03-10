package msg.project.flightmanager.service;

import java.security.Key;
import java.sql.Date;
import java.util.Base64;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Service
public class TokenService {

    private static final String AUTHORIZATION = "authorization";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String AUTHORITIES = "authorities";
    private static final String ID = "flightManagerJWT";
    private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS512);
    static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);
    private static final Long tokenDurationMillis = 60000000L;

    public String getJWTToken(String userName, String roleTitle) {
    	
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(ROLE_USER);
        
        return Jwts.builder()
                .setId(ID)
                .setSubject(userName)
                .claim(AUTHORITIES, roleTitle)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenDurationMillis))
                .signWith(SignatureAlgorithm.HS512, base64SecretBytes)
                .compact();
    }
    

    public String getCurrentUserUsername(String token) {
        return decodeToken(token).getSubject();
    }
    
    public String getCurrentRol(String token) {
    	return (String) decodeToken(token).get(AUTHORIZATION);
    }
    
    public Claims decodeToken(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(base64SecretBytes)
                .parseClaimsJws(token)
                .getBody();
        return claims;

    }
}