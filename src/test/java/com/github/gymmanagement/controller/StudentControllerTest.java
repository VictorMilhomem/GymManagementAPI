package com.github.gymmanagement.controller;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gymmanagement.model.student.StudentDTO;
import com.github.gymmanagement.model.student.StudentResponseDTO;
import com.github.gymmanagement.service.StudentService;
import com.github.gymmanagement.utils.StudentExistsException;
import com.github.gymmanagement.utils.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@ActiveProfiles("test")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID studentId;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();

        testStudentDTO = new StudentDTO();
        testStudentDTO.setId(studentId);
        testStudentDTO.setName("Test Student");
        testStudentDTO.setEmail("test@example.com");
        testStudentDTO.setAge(20);
        testStudentDTO.setGender("Male");
        testStudentDTO.setAddress("Test Address");
        testStudentDTO.setPhone("1234567890");
        testStudentDTO.setBelt("Blue");
    }

    @Test
    void getStudents_ShouldReturnAllStudents() throws Exception {
        List<StudentDTO> students = new ArrayList<>();
        students.add(testStudentDTO);

        StudentDTO student2 = new StudentDTO();
        student2.setId(UUID.randomUUID());
        student2.setName("Student 2");
        student2.setEmail("student2@example.com");
        students.add(student2);

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(studentId.toString()))
                .andExpect(jsonPath("$.data[0].name").value("Test Student"))
                .andExpect(jsonPath("$.data[1].name").value("Student 2"))
                .andExpect(jsonPath("$.msg").value("Found 2 students"))
                .andExpect(jsonPath("$.status").value("OK"));

        verify(studentService).getAllStudents();
    }

    @Test
    void getStudents_ShouldReturnNotFound_WhenNoStudents() throws Exception {
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg").value("No students found"))
                .andExpect(jsonPath("$.status").value("NO_CONTENT"));

        verify(studentService).getAllStudents();
    }

    @Test
    void getStudentById_ShouldReturnStudent_WhenStudentExists() throws Exception {
        when(studentService.getStudentById(studentId)).thenReturn(testStudentDTO);

        mockMvc.perform(get("/api/students/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(studentId.toString()))
                .andExpect(jsonPath("$.data.name").value("Test Student"))
                .andExpect(jsonPath("$.msg").value("Found student with ID " + studentId))
                .andExpect(jsonPath("$.status").value("OK"));

        verify(studentService).getStudentById(studentId);
    }

    @Test
    void getStudentById_ShouldReturnNotFound_WhenStudentDoesNotExist() throws Exception {
        when(studentService.getStudentById(studentId))
                .thenThrow(new StudentNotFoundException(studentId));

        mockMvc.perform(get("/api/students/{id}", studentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));

        verify(studentService).getStudentById(studentId);
    }

    @Test
    void addStudent_ShouldReturnCreatedStudent() throws Exception {
        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(testStudentDTO);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(studentId.toString()))
                .andExpect(jsonPath("$.data.name").value("Test Student"))
                .andExpect(jsonPath("$.msg").value("Successfully added student"))
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(studentService).createStudent(any(StudentDTO.class));
    }

    @Test
    void addStudent_ShouldReturnConflict_WhenStudentExists() throws Exception {
        when(studentService.createStudent(any(StudentDTO.class)))
                .thenThrow(new StudentExistsException("Student already exists"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg").value("Student already exists"))
                .andExpect(jsonPath("$.status").value("CONFLICT"));

        verify(studentService).createStudent(any(StudentDTO.class));
    }

    @Test
    void updateStudent_ShouldReturnUpdatedStudent() throws Exception {
        StudentDTO updatedDTO = new StudentDTO();
        updatedDTO.setId(studentId);
        updatedDTO.setName("Updated Name");
        updatedDTO.setEmail("updated@example.com");

        when(studentService.updateStudent(any(StudentDTO.class), eq(studentId)))
                .thenReturn(updatedDTO);

        MvcResult result = mockMvc.perform(post("/api/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(studentId.toString()))
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.msg").value("Student with ID " + studentId + " updated"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andReturn();


        verify(studentService).updateStudent(any(StudentDTO.class), eq(studentId));
    }

    @Test
    void updateStudent_ShouldReturnNotFound_WhenStudentDoesNotExist() throws Exception {
        when(studentService.updateStudent(any(StudentDTO.class), eq(studentId)))
                .thenThrow(new StudentNotFoundException(studentId));

        mockMvc.perform(post("/api/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));

        verify(studentService).updateStudent(any(StudentDTO.class), eq(studentId));
    }


    @Test
    void deleteStudent_ShouldReturnSuccess() throws Exception {
        doNothing().when(studentService).deleteStudent(studentId);

        mockMvc.perform(delete("/api/students/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg").value("Student " + studentId + " deleted"))
                .andExpect(jsonPath("$.status").value("OK"));

        verify(studentService).deleteStudent(studentId);
    }

    @Test
    void deleteStudent_ShouldReturnNotFound_WhenStudentDoesNotExist() throws Exception {
        doThrow(new StudentNotFoundException(studentId))
                .when(studentService).deleteStudent(studentId);

        mockMvc.perform(delete("/api/students/{id}", studentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));

        verify(studentService).deleteStudent(studentId);
    }
}