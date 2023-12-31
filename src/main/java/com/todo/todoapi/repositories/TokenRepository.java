package com.todo.todoapi.repositories;

import java.util.List;
import java.util.Optional;

import com.todo.todoapi.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {


    /**
     * findAllValidTokenByUser is a function  for find the valid token by user id passed
     */
    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Long id);


    /**
     * findByToken is a function  for find token
     */
    Optional<Token> findByToken(String token);
}
