package com.todo.todoapi.controllers;

import com.todo.todoapi.dto.Search;
import com.todo.todoapi.dto.TodoRequest;
import com.todo.todoapi.entities.Todo;
import com.todo.todoapi.services.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/todo")
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/all/page")
    public ResponseEntity<Page<Todo>> getAllTodosPage(@RequestBody Search search, Pageable pageable){
        return new ResponseEntity<>(todoService.getAllTodosPage(pageable,search), HttpStatus.OK);
    }

    @GetMapping("/mark/{state}/{id}")
    public ResponseEntity<Todo> MarkAsComplete(@PathVariable int state,@PathVariable UUID id){
        return new ResponseEntity<>(todoService.MarkAsComplete(state,id), HttpStatus.OK);
    }


    @GetMapping("/get-by/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable UUID id){
        return new ResponseEntity<>(todoService.getTodoById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Todo> createTodo(@RequestBody @Valid TodoRequest todoRequest){

        Todo todo  = todoService.createTodo(todoRequest);

        if(todo == null){
            log.info("there is a conflict ");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        log.info("todo created successfully");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Todo> updateTodo(@RequestBody @Valid TodoRequest todoRequest, @PathVariable UUID id){

        Todo todo  = todoService.updateTodo(todoRequest,id);

        if(todo == null){
            log.info("todo not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("todo updated successfully");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable UUID id){

        Todo todo  = todoService.deleteTodo(id);

        if(todo == null){
            log.info("todo not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("todo deleted successfully");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
