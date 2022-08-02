package com.example.dhbootcamp.meli.obterdiploma.repository;

import com.example.dhbootcamp.meli.obterdiploma.exception.StudentNotFoundException;
import com.example.dhbootcamp.meli.obterdiploma.model.StudentDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

@Repository
public class StudentDAO implements IStudentDAO {

    private String SCOPE;

    private static HashMap<Long, StudentDTO> students;

    public StudentDAO() {
        Properties properties = new Properties();

        try {
            properties.load(new ClassPathResource("application.properties").getInputStream());
            this.SCOPE = properties.getProperty("api.scope");
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
            Long largerIndex = (students.size() > 0) ? Collections.max(students.keySet()) : 0L;
            studentToBeSaved.setId((largerIndex + 1L ));
        }

        students.put(studentToBeSaved.getId(), studentToBeSaved);

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
        StudentDTO studentDTO = students.get(id);
        if (studentDTO != null) {
            return studentDTO;
        }
        throw new StudentNotFoundException(id);
    }

    private void loadData() {
        HashMap<Long, StudentDTO> loadedData = null;

        ObjectMapper objectMapper = new ObjectMapper();
        File file;
        try {
            file = ResourceUtils.getFile("./src/" + SCOPE + "/resources/users.json");
            loadedData = objectMapper.readValue(file, new TypeReference<HashMap<Long, StudentDTO>>() {});
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed while initializing DB, check your resources files.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed while initializing DB, check your JSON formatting.");
        }
        students = loadedData;
    }

    private void saveData() {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            File file = ResourceUtils.getFile("./src/" + SCOPE + "/resources/users.json");
            objectMapper.writeValue(file, students);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed while writing to DB, check your resources files.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed while writing to DB, check your JSON formatting.");
        }
    }
}
