package authorizationserver.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Hex;

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
    

//	public static String byte2hex(byte[] bytes) {
//		StringBuilder sign = new StringBuilder();
//		for (int i = 0; i < bytes.length; i++) {
//			String hex = Integer.toHexString(bytes[i] & 0xFF);
//			if (hex.length() == 1) {
//				sign.append("0");
//			}
//			sign.append(hex.toUpperCase());
//		}
//		return sign.toString();
//	}	
//	
//    
//    public static String encodeStringBase64Url(String raw) {
//        return Base64.getUrlEncoder()
//                .withoutPadding()
//                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
//    }
//    
//    public static String decodeBase64UrlString(String cypherText) {
//    	return Base64.getUrlDecoder().decode(cypherText.getBytes(StandardCharsets.UTF_8)).toString();
//    }
//    
//    public static String decodeBase64UrlSafeString(byte[] data) {
//        byte[] encode = Arrays.copyOf(data, data.length);
//        for (int i = 0; i < encode.length; i++) {
//            if (encode[i] == '-') {
//                encode[i] = '+';
//            } else if (encode[i] == '_') {
//                encode[i] = '/';
//            }
//        }
//        return new String(Base64.getUrlDecoder().decode(encode), StandardCharsets.UTF_8);
//    }
//    
//    public static String hexToBase64Url(String hex) {
//    	byte[] decodedHex = Hex.decode(hex);
//    	byte[] encodedHexB64 = Base64.getUrlEncoder().encode(decodedHex);
//    	
//    	return new String(encodedHexB64, StandardCharsets.UTF_8);
//    }
    
    public static void main(String[] args) {
    }
}