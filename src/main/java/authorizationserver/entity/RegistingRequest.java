package authorizationserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistingRequest {
	private String userName;
	private String password;
	private String confirmPassword;
	private String email;
	private String phoneNumber;
	private String scope;
}
