package authorizationserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;

import authorizationserver.entity.AuthRequest;
import authorizationserver.entity.RegistingRequest;
import authorizationserver.service.CustomUserDetailsService;
import authorizationserver.util.JwtUtil;

@RestController
public class WelcomeController {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailService;
	
	@GetMapping("/")
	public String welcome() {
		return "welcome to Authentication Server";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
		System.out.println(authRequest);
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
			);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(jwtUtil.generateToken(authRequest.getUserName()), HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> createAccount(@RequestBody RegistingRequest request) {
		if(userDetailService.createUser(request))
			return new ResponseEntity<String>(jwtUtil.generateToken(request.getUserName()), HttpStatus.ACCEPTED);
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
}
