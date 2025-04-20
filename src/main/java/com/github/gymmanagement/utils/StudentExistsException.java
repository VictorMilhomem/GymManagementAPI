package com.github.gymmanagement.utils;

import java.util.UUID;

public class StudentExistsException extends RuntimeException {
    private final UUID id;
    private final String name;

    public StudentExistsException( UUID id, String name) {
        super(String.format("Student %s already exists Id: %s", name, id));
        this.name = name;
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
