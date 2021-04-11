package authorizationserver.controller;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.authentication.AuthenticationManager;

import authorizationserver.entity.AuthRequest;
import authorizationserver.entity.RegistingRequest;
import authorizationserver.entity.User;
import authorizationserver.repositories.UserRepository;
import authorizationserver.service.CustomUserDetailsService;
import authorizationserver.util.JwtUtil;

@RestController
@RequestMapping(value = "connect")
public class AuthenticateController {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/authenticate")
	public ResponseEntity<Object> generateToken(@RequestBody AuthRequest authRequest, @RequestHeader Map<String, String> headers) throws Exception {
		System.out.println(authRequest);
		//Kiem tra thong tin user
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
			);
		} catch (Exception e) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		//Tra ve access token
		return new ResponseEntity<Object>(new Object() {
						public String token = jwtUtil.generateToken(authRequest.getUserName(), headers.get("origin"));
						public String refreshToken = "";
						}, HttpStatus.OK);
	}
	
	@GetMapping("/userinfo")
	public ResponseEntity<User> getUSerInfor(@RequestHeader Map<String, String> headers){
		headers.forEach((key, value) ->{
			Logger.getLogger(getClass().getName()).info(String.format("Header '%s' = %s", key, value));
		});
		// Lay ra token tu header
		String authorizationHeader = headers.get("authorization");
		String token = authorizationHeader.startsWith("Bearer ")? authorizationHeader.substring(7) : null;
		if(token!=null) {
			String userName = jwtUtil.extractUsername(token);
			//Tim user trong database bang username
			User user = userRepository.findByUserName(userName);
			// Tra ve user lay tu database
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<Object> createAccount(@RequestBody RegistingRequest request, @RequestHeader Map<String, String> headers) {
		//Tao user moi
		if(userDetailService.createUser(request))
			//Tra ve token
			return new ResponseEntity<Object>(new Object() {
						public String token = jwtUtil.generateToken(request.getUserName(), headers.get("origin"));
						public String refreshToken = "";
						}, HttpStatus.OK);
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
