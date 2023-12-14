package ru.kata.spring.boot_security.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
