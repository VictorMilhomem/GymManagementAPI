package com.github.gymmanagement.service;

import com.github.gymmanagement.model.student.Student;
import com.github.gymmanagement.model.student.StudentDTO;
import com.github.gymmanagement.repository.IStudentRepository;
import com.github.gymmanagement.utils.StudentExistsException;
import com.github.gymmanagement.utils.StudentNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Validated
public class StudentService {

    private final IStudentRepository studentRepository;

    public StudentService(IStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentDTO getStudentById(@NonNull UUID id) throws StudentNotFoundException {
        Objects.requireNonNull(id, "Student ID cannot be null");
        Student student = studentRepository.findStudentById(id);
        if (student == null) {
            throw new StudentNotFoundException(id);
        }

        return StudentDTO.fromEntity(student);
    }

    public StudentDTO getStudentByEmail(@NonNull String email) throws StudentNotFoundException {
        Objects.requireNonNull(email, "Student email cannot be null");
        Student student = studentRepository.findStudentByEmail(email);

        if (student == null) {
            throw new StudentNotFoundException(email);
        }
        return StudentDTO.fromEntity(student);
    }

    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> studentDTOS = new ArrayList<>();

        for (Student student : students) {
            studentDTOS.add(StudentDTO.fromEntity(student));
        }

        return studentDTOS;
    }

    public StudentDTO createStudent(@NonNull StudentDTO student) throws StudentExistsException {
        try {
            Objects.requireNonNull(student, "Student data cannot be null");
        }catch (NullPointerException e){
            throw new StudentNotFoundException(student.getId());
        }

        if (studentRepository.findStudentByEmail(student.getEmail()) != null) {
            throw new StudentExistsException(student.getId(), student.getName());
        }
        Student newStudent = studentRepository.save(student.toEntity());
        return StudentDTO.fromEntity(newStudent);
    }

    public StudentDTO updateStudent(@NonNull StudentDTO student, @NonNull UUID id) throws StudentNotFoundException {
        try {
            Objects.requireNonNull(student, "Student data cannot be null");
            Objects.requireNonNull(student, "Student ID cannot be null");
        }catch (NullPointerException e){
            throw new StudentNotFoundException(id);
        }

        Student stud = studentRepository.findStudentById(id);
        if (stud == null) {
            throw new StudentNotFoundException(id);
        }

        BeanUtils.copyProperties(student, stud, "id");
        Student updatedStudent = studentRepository.save(stud);
        return StudentDTO.fromEntity(updatedStudent);
    }

    public void deleteStudent(@NonNull UUID id) throws StudentNotFoundException {
        Objects.requireNonNull(id, "Student ID cannot be null");

        Student student = studentRepository.findStudentById(id);
        if (student == null) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.delete(student);
    }

}
