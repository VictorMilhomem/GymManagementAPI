package com.github.gymmanagement.model.student;


import org.springframework.beans.BeanUtils;

import java.util.UUID;


public class StudentDTO {

    private UUID id;
    private String name;
    private int age;
    private String gender;
    private String email;
    private String belt;
    private String address;
    private String phone;

    public StudentDTO() {}

    public static StudentDTO fromEntity(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.id = student.getId();
        studentDTO.name = student.getName();
        studentDTO.age = student.getAge();
        studentDTO.gender = student.getGender();
        studentDTO.email = student.getEmail();
        studentDTO.belt = student.getBelt();
        studentDTO.address = student.getAddress();
        studentDTO.phone = student.getPhone();
        return studentDTO;
    }

    public Student toEntity() {
        Student student = new Student();
        BeanUtils.copyProperties(this, student);
        return student;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBelt() {
        return belt;
    }

    public void setBelt(String belt) {
        this.belt = belt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
