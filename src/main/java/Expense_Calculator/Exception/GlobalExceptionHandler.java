package Expense_Calculator.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

//    public String handleRuntimeException(RuntimeException exception){
//        return exception.getMessage();
//    }

    public Map<String,String> handleRuntimeException(RuntimeException exception){

        Map<String,String> error = new HashMap<>();
        error.put("exception", exception.getClass().getName());
        error.put("message", exception.getMessage());
        return error;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleResourceException(ResourceNotFoundException exception){

        Map<String,String> error = new HashMap<>();

        error.put("message", exception.getMessage());

        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult()  //give me validation results
                .getFieldErrors()    //Give me all field validation errors
                .forEach(error -> // for each error put the errors title and msg into map
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );



        return errors;
    }
}
