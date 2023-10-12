package com.todo.todoapi.controllers;

import com.todo.todoapi.dto.Search;
import com.todo.todoapi.dto.TodoRequest;
import com.todo.todoapi.entities.Todo;
import com.todo.todoapi.services.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/todo")
@RequiredArgsConstructor
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
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Todo> updateTodo(@RequestBody @Valid TodoRequest todoRequest, @PathVariable UUID id){

        Todo todo  = todoService.updateTodo(todoRequest,id);

        if(todo == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable UUID id){

        Todo todo  = todoService.deleteTodo(id);

        if(todo == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
