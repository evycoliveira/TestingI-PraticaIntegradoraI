package com.example.dhbootcamp.meli.obterdiploma.repository;

import com.example.dhbootcamp.meli.obterdiploma.model.StudentDTO;

public interface IStudentDAO {
    StudentDTO save(StudentDTO stu);

    void delete(Long id);
    boolean exists(StudentDTO stu);

    StudentDTO findById(Long id);
}
