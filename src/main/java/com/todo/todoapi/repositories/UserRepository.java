package com.todo.todoapi.repositories;

import com.todo.todoapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * findByEmail is a function  for find user by email
     */
    Optional<User> findByEmail(String email);
}
