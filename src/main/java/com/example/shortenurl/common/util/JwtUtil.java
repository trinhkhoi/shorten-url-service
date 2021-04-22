package com.example.shortenurl.common.util;

import com.example.shortenurl.common.exception.BusinessException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Jwt util class.
 * Date 2021-04-18 11:12:22
 * @author khoitd
 */
@Component
public class JwtUtil {
	private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	private static final String ID_CUSTOMER_PARAM = "idCustomer";
	private static final String GENERATED_TIME_PARAM = "date";
	private static String SECRET_KEY_CUSTOMER = "IMFjdkYY7TTy6RfewQR4PK5jtm0iO3jfldasLJLH2332NLlhhLtuufFFgiioip90322XXXoifsolaHGGkafTUTu";
	private static int expireTime = 360000000;

	public static String generateTokenForUser(Long idCustomer) {
		String token = null;
		try {
			// Create HMAC signer
			JWSSigner signer = new MACSigner(generateShareSecretCustomer());

			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			builder.claim(ID_CUSTOMER_PARAM, idCustomer);
			builder.claim(GENERATED_TIME_PARAM, formatDate(LocalDateTime.now()));
			builder.expirationTime(generateExpirationDate());

			JWTClaimsSet claimsSet = builder.build();
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

			// Apply the HMAC protection
			signedJWT.sign(signer);

			// Serialize to compact form, produces something like
			token = signedJWT.serialize();
			logger.info("Token: " + token);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	public static Long getIdCustomerFromToken(String token) throws BusinessException {
		try {
			JWTClaimsSet claims = getClaimsForCustomerToken(token);
			Long idCustomer = claims.getLongClaim(ID_CUSTOMER_PARAM);
			return idCustomer;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(HttpStatus.FORBIDDEN, "The session is invalid");
		}
	}

	public static JWTClaimsSet getClaimsForCustomerToken(String token) {
		JWTClaimsSet claims = null;
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWSVerifier verifier = new MACVerifier(generateShareSecretCustomer());
			if (signedJWT.verify(verifier)) {
				claims = signedJWT.getJWTClaimsSet();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return claims;
	}

	private static Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expireTime);
	}

	private static Date getExpirationDateFromToken(String token) {
		JWTClaimsSet claims = getClaimsForCustomerToken(token);
		Date expiration = claims.getExpirationTime();
		return expiration;
	}

	private static byte[] generateShareSecretCustomer() {
		// Generate 256-bit (32-byte) shared secret
		byte[] sharedSecret = new byte[32];
		sharedSecret = SECRET_KEY_CUSTOMER.getBytes();
		return sharedSecret;
	}

	private static Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public static Boolean validateTokenLogin(String token) throws BusinessException {
		if (token == null || token.trim().length() == 0) {
			return false;
		}
		Long idCustomer = getIdCustomerFromToken(token);

		if (idCustomer == null) {
			return false;
		}
		return !isTokenExpired(token);
	}

	private static String formatDate(LocalDateTime date) {
		return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
