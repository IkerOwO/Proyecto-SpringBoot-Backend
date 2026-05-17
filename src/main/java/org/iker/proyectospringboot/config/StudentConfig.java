package org.iker.proyectospringboot.config;

import org.iker.proyectospringboot.models.Student;
import org.iker.proyectospringboot.repositories.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            Student test = new Student(
                    "Test",
                    "test@gmail.com",
                    "12345I"
            );
            studentRepository.saveAll(
                    List.of(test)
            );
        };
    }
}
