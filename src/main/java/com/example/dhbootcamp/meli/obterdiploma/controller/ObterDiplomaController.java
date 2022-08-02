package com.example.dhbootcamp.meli.obterdiploma.controller;

import com.example.dhbootcamp.meli.obterdiploma.model.StudentDTO;
import com.example.dhbootcamp.meli.obterdiploma.service.IObterDiplomaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ObterDiplomaController {

    @Autowired
    IObterDiplomaService service;

    @PostMapping("/analyzeScores")
    public StudentDTO analyzeScores(@RequestBody StudentDTO rq) { return service.analyzeScores(rq); }

}
