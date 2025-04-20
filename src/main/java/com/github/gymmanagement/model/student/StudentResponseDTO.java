package com.github.gymmanagement.model.student;

import org.springframework.http.HttpStatus;

import java.util.List;

public class StudentResponseDTO<T> {
    private final String msg;
    private final HttpStatus status;
    private T data;


    public StudentResponseDTO(T data, String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    public StudentResponseDTO(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
