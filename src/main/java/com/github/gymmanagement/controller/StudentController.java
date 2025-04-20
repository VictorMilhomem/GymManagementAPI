package com.github.gymmanagement.controller;

import com.github.gymmanagement.model.student.StudentDTO;
import com.github.gymmanagement.model.student.StudentResponseDTO;
import com.github.gymmanagement.service.StudentService;
import com.github.gymmanagement.utils.StudentExistsException;
import com.github.gymmanagement.utils.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<StudentResponseDTO<List<StudentDTO>>> getStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            StudentResponseDTO response = new StudentResponseDTO<List<StudentDTO>>("No students found", HttpStatus.NO_CONTENT);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(
                new StudentResponseDTO<>(
                        students,
                        String.format("Found %d students", students.size()),
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO<StudentDTO>> getStudentById(@PathVariable UUID id) {
        StudentDTO student;
        try {
            student = studentService.getStudentById(id);
        } catch (StudentNotFoundException e) {
            StudentResponseDTO<StudentDTO> response = new StudentResponseDTO<StudentDTO>(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(new StudentResponseDTO<StudentDTO>(
                student,
                String.format("Found student with ID %s", id),
                HttpStatus.OK
        ));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO<StudentDTO>> addStudent(@RequestBody StudentDTO student) {
        StudentDTO newStudent;
        try {
            newStudent = studentService.createStudent(student);
        } catch (StudentExistsException e) {
            StudentResponseDTO<StudentDTO> response = new StudentResponseDTO<StudentDTO>(e.getMessage(), HttpStatus.CONFLICT);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.ok(new StudentResponseDTO<StudentDTO>(
                newStudent,
                "Successfully added student",
                HttpStatus.CREATED
        ));
    }

    @PostMapping("/{id}")
    public ResponseEntity<StudentResponseDTO<StudentDTO>> updateStudent(@PathVariable UUID id, @RequestBody StudentDTO student) {
        StudentDTO newStudent;
        try {
            newStudent = studentService.updateStudent(student, id);
        } catch (StudentNotFoundException e) {
            StudentResponseDTO<StudentDTO> response = new StudentResponseDTO<StudentDTO>(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(new StudentResponseDTO<StudentDTO>(
                newStudent,
                String.format("Student with ID %s updated", id),
                HttpStatus.CREATED
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentResponseDTO<StudentDTO>> deleteStudent(@PathVariable UUID id) {
        try {
            studentService.deleteStudent(id);
        }catch (StudentNotFoundException e) {
            StudentResponseDTO<StudentDTO> response = new StudentResponseDTO<StudentDTO>(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(new StudentResponseDTO<StudentDTO>(
                String.format("Student %s deleted", id),
                HttpStatus.OK
        ));
    }

}
