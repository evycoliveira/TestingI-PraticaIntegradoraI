package com.example.dhbootcamp.meli.obterdiploma.controller;

import com.example.dhbootcamp.meli.obterdiploma.model.StudentDTO;
import com.example.dhbootcamp.meli.obterdiploma.service.IObterDiplomaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ObterDiplomaController {

    @Autowired
    IObterDiplomaService service;

    @GetMapping("/analyzeScores/{studentId}")
    public StudentDTO analyzeScores(@PathVariable Long studentId) { return service.analyzeScores(studentId); }

}
