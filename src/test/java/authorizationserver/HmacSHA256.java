package authorizationserver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import authorizationserver.util.Sha256;
import authorizationserver.util.SignatureAlgorithm;

public class HmacSHA256 {
	@Test
	public void sign() {
		String header = "eyJhbGciOiJIUzI1NiJ9";
		String payload = "eyJzdWIiOiJ0aWt0dXpraSIsInNjb3BlIjoib3BlbmlkIHByb2R1Y3Qgb3JkZXIgdXNlcnMiLCJpc3MiOiJBdXRoIFNlcnZlciIsImV4cCI6MTYxNzUzNzg1MCwiaWF0IjoxNjE3NTAxODUwfQ";
		String signature = "";
		try {
			signature = SignatureAlgorithm.signRequest(header, payload, "secret");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(signature);
	}
	
}
