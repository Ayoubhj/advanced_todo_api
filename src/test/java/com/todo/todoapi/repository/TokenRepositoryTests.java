package com.todo.todoapi.repository;


import com.todo.todoapi.entities.Token;
import com.todo.todoapi.entities.User;
import com.todo.todoapi.enums.Role;
import com.todo.todoapi.repositories.TokenRepository;
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
public class TokenRepositoryTests {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    private Token token;
    private User user;
    @BeforeEach
    private void init(){
        user = User.builder().role(Role.ROLE_USER).email("ayoub@gmail.com").firstName("ayoub").lastName("hajouj").phone("0632736325").password("123456789").build();
        token = Token.builder().token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiO").user(user).expired(false).revoked(false).build();
    }
    @Test
    public void TokenRepository_findAllValidTokenByUser_ReturnToken(){


        user = userRepository.save(user);
        token = tokenRepository.save(token);

        var token1 = tokenRepository.findAllValidTokenByUser(user.getUserId());
        var token2 = tokenRepository.findAllValidTokenByUser(2L);

        Assertions.assertThat(token1.size()).isEqualTo(1);
        Assertions.assertThat(token2.size()).isEqualTo(0);

    }

    @Test
    public void TokenRepository_findByToken_ReturnToken(){


        user = userRepository.save(user);
        token = tokenRepository.save(token);

        var token1 = tokenRepository.findByToken(token.getToken());
        var token2 = tokenRepository.findByToken("");

        Assertions.assertThat(token1.isPresent()).isTrue();
        Assertions.assertThat(token2.isPresent()).isFalse();

    }
}
