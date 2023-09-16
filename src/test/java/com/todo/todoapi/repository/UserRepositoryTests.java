package com.todo.todoapi.repository;


import com.todo.todoapi.entities.User;
import com.todo.todoapi.enums.Role;
import com.todo.todoapi.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private  UserRepository userRepository;
    private User user;
    @BeforeEach
    public void init(){

        user = User.builder().role(Role.ROLE_USER).email("ayoub@gmail.com").firstName("ayoub").lastName("hajouj").phone("0632736325").password("123456789").build();

    }
    @Test
    public void UserRepository_Find_By_Email_ReturnUser(){


        user = userRepository.save(user);

        var user1 = userRepository.findByEmail(user.getEmail());
        var user2 = userRepository.findByEmail("azert@gmail.com");

        Assertions.assertThat(user1.isPresent()).isTrue();
        Assertions.assertThat(user2.isPresent()).isFalse();

    }

}
