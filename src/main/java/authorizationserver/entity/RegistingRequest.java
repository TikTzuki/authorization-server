package authorizationserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistingRequest {
    @NotBlank
    @Size(min = 3, max = 200)
    private String userName;
    private String password;
    private String confirmPassword;
    private String email;
    private String phoneNumber;
    private String scope;
}
