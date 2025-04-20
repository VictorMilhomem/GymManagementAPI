package com.github.gymmanagement.utils;

import java.util.UUID;

public class StudentNotFoundException extends RuntimeException {

    private final UUID id;
    private final String email;

    public StudentNotFoundException( UUID id) {
        super(String.format("Student not found Id: %s",  id));
        this.email = "";
        this.id = id;
    }

    public StudentNotFoundException( String email) {
        super(String.format("Student with email %s not found",  email));
        this.email = email;
        this.id = null;
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }
}
