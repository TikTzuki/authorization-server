package authorizationserver.service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
public class SignatureAlgorithm {
	/**
	 * Sign the API request with body.
	 */
	public static String signRequest(String header, String payload, String appSecret) throws IOException {

		// next : sign the whole request
		byte[] bytes = null;
		
		bytes = encryptHMACSHA256(header+"."+payload, appSecret);

		return Base64.getUrlEncoder().encodeToString(bytes);
	}

	//secret not base64encode
	private static byte[] encryptHMACSHA256(String data, String secret) throws IOException  {
    	byte[] bytes = null;
    	try {
	        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

	        Mac mac = Mac.getInstance(secretKey.getAlgorithm());

	        mac.init(secretKey);

	        bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    	} catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
}
