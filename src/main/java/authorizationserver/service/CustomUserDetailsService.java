package authorizationserver.service;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import authorizationserver.config.YAMLConfig;
import authorizationserver.entity.RegistingRequest;
import authorizationserver.entity.User;
import authorizationserver.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private YAMLConfig config;
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		User user = repository.findByUserName(username);
		
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());
	}
	
	public boolean createUser(RegistingRequest registRequest) {
		// Kiem tra tinh hop le cua form tao user
		if(!validateRequest(registRequest))
			return false;
		// http://locahost:5001/createUser
	    String createPersonUrl = config.getResoucesServer()+config.getCreateUserPath();

	    // Tao request createUser goi den resouces server
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    JSONObject userJsonObject = new JSONObject();
	    userJsonObject.put("userName", registRequest.getUserName());
	    userJsonObject.put("password", registRequest.getPassword());
	    userJsonObject.put("email", registRequest.getEmail());
	    userJsonObject.put("phoneNumber", registRequest.getPhoneNumber());
	    userJsonObject.put("scope", registRequest.getScope());
	    HttpEntity<String> request = new HttpEntity<String>(userJsonObject.toString(), headers);
	    System.out.println(userJsonObject.toString());
	    ResponseEntity<String> responseEntityStr = restTemplate.
	    		postForEntity(createPersonUrl, request, String.class);
	    
	    if(responseEntityStr.getStatusCode() == HttpStatus.BAD_REQUEST) {
	    	return false;
	    }
	    
	    String body= responseEntityStr.getBody();
	    JSONObject newUser = new JSONObject(body);
	    repository.save(new User(
	    		newUser.getInt("id"),
	    		newUser.getString("userName"),
	    		newUser.getString("password"),
	    		newUser.getString("email"),
	    		newUser.getString("scope")));
	    System.out.println(body);
		return true;
	}
	
	private boolean validateRequest(RegistingRequest request) {
		if(repository.findByEmail(request.getEmail()) != null
				|| repository.findByUserName(request.getUserName()) != null
				|| !request.getPassword().equals(request.getConfirmPassword()))
			return false;
		return true;
	}
	
}
