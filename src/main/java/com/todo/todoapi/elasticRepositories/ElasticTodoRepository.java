package com.todo.todoapi.elasticRepositories;

import com.todo.todoapi.entities.Todo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;


public interface ElasticTodoRepository extends ElasticsearchRepository<Todo ,UUID> {
}
