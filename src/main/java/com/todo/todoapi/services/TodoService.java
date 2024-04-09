package com.todo.todoapi.services;

import com.todo.todoapi.dto.Search;
import com.todo.todoapi.dto.TodoRequest;
import com.todo.todoapi.elasticRepositories.ElasticTodoRepository;
import com.todo.todoapi.entities.Todo;
import com.todo.todoapi.entities.User;
import com.todo.todoapi.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;
    private final ElasticTodoRepository elasticTodoRepository;



    public Page<Todo> getAllTodosPage(Pageable pageable, Search search){
        if(Objects.nonNull(search.getState()) && search.getState() != 2){
            return todoRepository.findByUserAndIsDoneAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(userService.getConnectedUser(),search.getState(),search.getSearch(),pageable);
        }
        return todoRepository.findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(userService.getConnectedUser(),search.getSearch(),pageable);
    }

    public Todo getTodoById(UUID id){
        return todoRepository.findByUser(userService.getConnectedUser()).orElseThrow();
    }

    public Todo createTodo(TodoRequest todoRequest){

        var todo = Todo.builder().title(todoRequest.getTitle()).descreption(todoRequest.getDescreption()).user(userService.getConnectedUser()).build();

        todoRepository.save(todo);
        elasticTodoRepository.save(todo);
        return todo;

    }

    public Todo MarkAsComplete(int state ,UUID id){

        var todo = todoRepository.findById(id);

        if(todo.isPresent()){
            Todo current = todo.get();
            current.setIsDone(state);
            elasticTodoRepository.save(current);
            return todoRepository.save(current);

        }

        return null;

    }


    public Todo updateTodo(TodoRequest todoRequest,UUID id){

        var todo = todoRepository.findById(id).orElseThrow();

        todo.setTitle(todoRequest.getTitle());
        todo.setDescreption(todoRequest.getDescreption());

        todoRepository.save(todo);
        elasticTodoRepository.save(todo);
        return  todo;

    }

    public Todo deleteTodo(UUID id){

        Optional<Todo> todo = todoRepository.findById(id);

        if (todo.isPresent()) {

            todoRepository.deleteById(id);
            elasticTodoRepository.deleteById(id);
            return todo.get();

        } else {
            return null ;
        }

    }

}
