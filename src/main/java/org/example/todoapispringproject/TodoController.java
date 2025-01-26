package org.example.todoapispringproject;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.function.SupplierUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/todos")
public class TodoController {

    private static List<Todo> todoList;

    public TodoController(){
        todoList = new ArrayList<>();
        todoList.add(new Todo(1,false,"Todo 1",1));
        todoList.add(new Todo(2,true,"Todo 2",2));
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getTodo(){
        return ResponseEntity.ok(todoList);
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo newTodo){

//       We can use this annotation to set the status code - @ResponseStatus(HttpStatus.CREATED)


        todoList.add(newTodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<?> getTodoById(@PathVariable Long todoId){
        for(Todo todo : todoList){
            if(todo.getId() == todoId){
                return ResponseEntity.ok(todo);
            }
        }

        ErrorResponse errorResponse = new ErrorResponse("Todo with ID : " + todoId + " is not found"
                ,HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> deleteById(@PathVariable Long todoId){
        for(Todo todo : todoList){
            if(todo.getId() == todoId){
                SuccessResponse successResponse = new SuccessResponse("Deleted Succesfully",HttpStatus.OK.value());
                todoList.remove(todo);
                return ResponseEntity.status(HttpStatus.OK.value()).body(successResponse);
            }
        }
        ErrorResponse errorResponse = new ErrorResponse("Todo with ID : " + todoId + " is not found"
                ,HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @PatchMapping("/{todoId}")
    public ResponseEntity<?> updateById(@PathVariable Long todoId,@RequestBody Map<String,Object> updates){

        for(Todo todo : todoList){
            if(todo.getId() == todoId){
                Todo originalTodo = new Todo(todo.getId(), todo.isCompleted(),todo.getTitle(),todo.getUserId());

                try{
                    updates.forEach( (key, value) -> {
                        switch(key){
                            case "id" : todo.setId((Integer) value);
                                break;
                            case "completed" : todo.setCompleted((Boolean) value);
                                break;
                            case "title" : todo.setTitle((String) value);
                                break;
                            case "userId" : todo.setUserId((Integer) value);
                                break;
                            default: throw new IllegalArgumentException("Invalid field : " + key);
                        }
                    });

                    SuccessResponse succesResponse = new SuccessResponse("Updated Successfully",HttpStatus.OK.value());
                    return ResponseEntity.ok().body(succesResponse);

                }catch (IllegalArgumentException e){
                    todo.setId(originalTodo.getId());
                    todo.setTitle(originalTodo.getTitle());
                    todo.setCompleted(originalTodo.isCompleted());
                    todo.setUserId(originalTodo.getUserId());

                    ErrorResponse errorResponse = new ErrorResponse(e.getMessage()
                            ,HttpStatus.BAD_REQUEST.value());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }
        }

        ErrorResponse errorResponse = new ErrorResponse("Todo with ID : " + todoId + " is not found"
                ,HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
