package com.example.dhbootcamp.meli.obterdiploma.service;

import com.example.dhbootcamp.meli.obterdiploma.model.StudentDTO;
import com.example.dhbootcamp.meli.obterdiploma.model.SubjectDTO;
import com.example.dhbootcamp.meli.obterdiploma.repository.IStudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class ObterDiplomaService implements IObterDiplomaService {

    @Autowired
    IStudentDAO studentDAO;

    @Override
    public StudentDTO analyzeScores(Long studentId) {
        StudentDTO stu = studentDAO.findById(studentId);

        stu.setAverageScore(calculateAverage(stu.getSubjects()));
        stu.setMessage(getGreetingMessage(stu.getStudentName(), stu.getAverageScore()));

        return stu;
    }

    private String getGreetingMessage(String studentName, Double average) {
        return "O aluno " + studentName + " obteve uma média de " + new DecimalFormat("#.##").format(average)
                + ((average > 9) ? ". Parabéns!" : ". Você pode ficar melhor.");
    }

    private Double calculateAverage(List<SubjectDTO> scores) {
        return scores.stream()
                .reduce(0D, (partialSum, score) -> partialSum + score.getScore(), Double::sum)
                / scores.size();
    }
}
