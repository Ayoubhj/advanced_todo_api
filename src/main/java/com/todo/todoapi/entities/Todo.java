package com.todo.todoapi.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Data
@Entity
@Table(name = "todos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "todo")
public class Todo extends BaseEntity{

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(columnDefinition = "VARCHAR(36)")

    private UUID id;

    @Field(type = FieldType.Text, includeInParent = true)
    private String title;

    @Lob
    @Field(type = FieldType.Text, includeInParent = true)
    private String descreption;

    @Field(type = FieldType.Integer, includeInParent = true)
    private int isDone = 0;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @Field(type = FieldType.Nested, includeInParent = true)
    private User user;


}
