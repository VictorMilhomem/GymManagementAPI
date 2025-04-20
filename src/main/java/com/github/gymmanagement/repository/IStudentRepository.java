package com.github.gymmanagement.repository;

import com.github.gymmanagement.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IStudentRepository extends JpaRepository<Student, UUID> {
    public Student findStudentById(UUID id);
    public Student findStudentByEmail(String email);
}
