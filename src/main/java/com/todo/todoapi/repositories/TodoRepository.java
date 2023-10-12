package com.todo.todoapi.repositories;

import com.todo.todoapi.entities.Todo;
import com.todo.todoapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TodoRepository extends JpaRepository<Todo, UUID> {

    Page<Todo> findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(User user, String title, Pageable pageable);
    Page<Todo> findByUserAndIsDoneAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(User user,int isDone,String title,Pageable pageable);

    Optional<Todo> findByUser(User user);
}
