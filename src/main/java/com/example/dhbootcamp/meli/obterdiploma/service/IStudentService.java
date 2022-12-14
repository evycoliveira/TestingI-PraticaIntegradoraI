package com.example.dhbootcamp.meli.obterdiploma.service;

import com.example.dhbootcamp.meli.obterdiploma.model.StudentDTO;

import java.util.Set;

public interface IStudentService {
    StudentDTO create(StudentDTO stu);
    StudentDTO read(Long id);
    void update(StudentDTO stu);
    void delete(Long id);
    Set<StudentDTO> getAll();
}
