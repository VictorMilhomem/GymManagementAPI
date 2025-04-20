package com.github.gymmanagement.service;

import com.github.gymmanagement.model.student.Student;
import com.github.gymmanagement.model.student.StudentDTO;
import com.github.gymmanagement.repository.IStudentRepository;
import com.github.gymmanagement.utils.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private IStudentRepository studentRepository;

    private Student studentA;
    private Student studentB;
    private UUID studentAId;
    private UUID studentBId;
    private UUID nonExistentStudentId;

    private String studentAEmail;
    private String nonExistentStudentEmail;

    @BeforeEach
    void setup() {
        studentRepository.deleteAll();
        nonExistentStudentId = UUID.randomUUID();
        nonExistentStudentEmail = "test@gmail.com";

        studentA = new Student(
                "Student A",
                20,
                "Male",
                "Street 6",
                "3511292920",
                "studa@gmail.com",
                "Blue"
        );

        studentB = new Student(
                "Student B",
                25,
                "Female",
                "Street 7",
                "3511292922",
                "studb@gmail.com",
                "Purple"
        );

        Student savedStudentA = studentRepository.save(studentA);
        studentAId = savedStudentA.getId();
        studentAEmail = savedStudentA.getEmail();

        Student savedStudentB = studentRepository.save(studentB);
        studentBId = savedStudentB.getId();
    }

    @Test
    void getStudentById_ExistingStudent() {
        StudentDTO foundStudent = studentService.getStudentById(studentAId);

        assertNotNull(foundStudent);
        assertEquals(studentAId, foundStudent.getId());
    }

    @Test
    void getStudentById_ShouldThrowException_WhenStudentDoesNotExist() {
        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(nonExistentStudentId);
        });

        assertTrue(exception.getMessage().contains(nonExistentStudentId.toString()));
    }

    @Test
    void getStudentByEmail_ExistingStudent() {
        StudentDTO foundStudent = studentService.getStudentByEmail(studentAEmail);
        assertNotNull(foundStudent);
        assertEquals(studentAId, foundStudent.getId());
    }

    @Test
    void getStudentByEmail_ShouldThrowException_WhenStudentDoesNotExist() {
        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentByEmail(nonExistentStudentEmail);
        });
        assertTrue(exception.getMessage().contains(nonExistentStudentEmail));
    }

    @Test
    void getAllStudents() {
        List<StudentDTO> foundStudents = studentService.getAllStudents();

        assertNotNull(foundStudents);
        assertEquals(studentAId, foundStudents.get(0).getId());
        assertEquals(studentBId, foundStudents.get(1).getId());
    }

    @Test
    void createStudent() {
        StudentDTO studentDTO = StudentDTO.fromEntity(new Student(
                "Student C",
                28,
                "Male",
                "Street 8",
                "351676767",
                "studc@gmail.com",
                "Black"
        ));
        studentService.createStudent(studentDTO);
        List<StudentDTO> foundStudents = studentService.getAllStudents();
        assertNotNull(foundStudents);
        assertEquals(studentDTO.getEmail(), foundStudents.get(2).getEmail());
    }

    @Test
    void updateStudent() {
        StudentDTO studentDTO = StudentDTO.fromEntity(new Student(
                "Student A",
                21,
                "Male",
                "Street 2",
                "351676767",
                "studa@gmail.com",
                "Blue"
        ));
        studentService.updateStudent(studentDTO, studentAId);
        StudentDTO foundStudent = studentService.getStudentById(studentAId);
        assertNotNull(foundStudent);
        assertEquals(studentAId, foundStudent.getId());
    }

    @Test
    void deleteStudent() {
        studentService.deleteStudent(studentBId);
        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(nonExistentStudentId);
        });
        assertTrue(exception.getMessage().contains(nonExistentStudentId.toString()));
    }
}