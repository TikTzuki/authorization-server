package authorizationserver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import authorizationserver.entity.User;
import authorizationserver.repositories.UserRepository;

@SpringBootApplication
public class AuthorizationServerApplication {
	@Autowired
	UserRepository repository;

    @PostConstruct
    public void initUsers() {
        List<User> users = Stream.of(
				new User(101, "tiktuzki", "password", "tranphanthanhlong18@gmail.com", "openid product order users"),
				new User(102, "huong", "password", "huong@gmail.com", "openid product read:order"),
				new User(103, "huyen", "password", "huyen@gmail.com", "openid read:product order")
        ).collect(Collectors.toList());
        repository.saveAll(users);
    }
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

}
