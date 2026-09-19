package com.fundoo.notes.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final List<String> errors;

    private ApiResponse(boolean success,String message,T data,List<String> errors){
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors =errors;
    }

    public static <T> ApiResponse<T> success(String message,T data){
        return new ApiResponse<>(
                true, message, data,null
        );
    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(
                false,message,null,null
        );
    }

    public static <T> ApiResponse<T> error(String message,List<String> errors){
        return new ApiResponse<>(
                false,message,null,errors
        );
    }

}
