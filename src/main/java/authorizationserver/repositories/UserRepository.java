package authorizationserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import authorizationserver.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String username);

    User findByEmail(String email);

    Boolean existsByUserName(String username);
}
