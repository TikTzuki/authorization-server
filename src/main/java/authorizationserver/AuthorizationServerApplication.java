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
				new User(1, "tiktzuki", "P@ssword1", "tranphanthanhlong18@gmail.com", "openid read:user write:user"),
				new User(2, "huong", "P@ssword1", "huong@gmail.com", "openid read:user"),
				new User(3, "huyen", "P@ssword1", "huyen@gmail.com", "openid write:user")
        ).collect(Collectors.toList());
        repository.saveAll(users);
    }
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

}
