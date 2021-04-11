package authorizationserver.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import authorizationserver.config.YAMLConfig;
import authorizationserver.entity.User;

public class UserConnectRepository {
	@Autowired
	YAMLConfig config;
	static String resouceServer;
	
	public UserConnectRepository() {
		this.resouceServer = config.getResoucesServer();
	}
}
