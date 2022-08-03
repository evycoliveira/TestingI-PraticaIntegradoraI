package com.example.dhbootcamp.meli.obterdiploma.repository;

import com.example.dhbootcamp.meli.obterdiploma.exception.StudentNotFoundException;
import com.example.dhbootcamp.meli.obterdiploma.model.StudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Repository
public class StudentDAO implements IStudentDAO {

    private String SCOPE;

    private static List<StudentDTO> students;

    public StudentDAO() {
        Properties properties = new Properties();

        try {
            properties.load(new ClassPathResource("application.properties").getInputStream());
            this.SCOPE = "develop";
            this.loadData();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StudentDTO save(StudentDTO studentReceived) {
        boolean studentNotExist = !exists(studentReceived);
        boolean studentHasId = studentReceived.getId() != null;

        if (studentHasId && studentNotExist) {
            throw new StudentNotFoundException(studentReceived.getId());
        }

        StudentDTO studentToBeSaved = new StudentDTO(studentReceived);

        if (studentNotExist) {
            Long largerIndex = 0L;
            Optional<StudentDTO> studentCompared = students.stream().max(Comparator.comparing(StudentDTO::getId));
            if (studentCompared.isPresent()) {
                largerIndex = studentCompared.get().getId();
            }
            studentToBeSaved.setId((largerIndex + 1L ));
        }

        students.add(studentToBeSaved);

        this.saveData();

        return studentToBeSaved;
    }

    @Override
    public void delete(Long id) {
        StudentDTO found = this.findById(id);

        students.remove(found.getId());
        this.saveData();
    }

    public boolean exists(StudentDTO stu) {
        boolean ret = false;

        try {
            ret = this.findById(stu.getId()) != null;
        } catch (StudentNotFoundException e) {}
        return ret;
    }

    @Override
    public StudentDTO findById(Long id) {
        loadData();
        Optional <StudentDTO> studentDTO = students.stream().filter(i -> i.getId().equals(id)).findFirst();
        if (studentDTO.isPresent()) {
            return studentDTO.get();
        }
        throw new StudentNotFoundException(id);
    }

    private void loadData() {
        List<StudentDTO> loadedData = null;

        ObjectMapper objectMapper = new ObjectMapper();
        File file;
        try {
            file = ResourceUtils.getFile("./src/main/resources/" + SCOPE + "/users.json");

            if (file.exists()) {
                loadedData = Arrays.asList(objectMapper.readValue(file, StudentDTO[].class));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed while initializing DB, check your resources files.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed while initializing DB, check your JSON formatting.");
        }
        this.students = loadedData;
    }

    private void saveData() {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            File file = ResourceUtils.getFile("./src/" + SCOPE + "/resources/users.json");
            objectMapper.writeValue(file, this.students);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed while writing to DB, check your resources files.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed while writing to DB, check your JSON formatting.");
        }
    }
}
