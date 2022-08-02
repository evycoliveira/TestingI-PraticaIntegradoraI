package com.example.dhbootcamp.meli.obterdiploma.repository;

import com.example.dhbootcamp.meli.obterdiploma.model.StudentDTO;

import java.util.Set;

public interface IStudentRepository {
    Set<StudentDTO> findAll();
}
