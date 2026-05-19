package org.iker.proyectospringboot.service;

import org.iker.proyectospringboot.models.Student;
import org.iker.proyectospringboot.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public  StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student){
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new IllegalStateException("Email Taken");
        }
        // Si el email no esta registrado, lo guardamos
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId){
        boolean exists = studentRepository.existsById(studentId);
        if (!exists){
            throw new IllegalStateException("Student doesn't exists");
        }
        // Si existe, lo borramos
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, Student updatedStudent) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new IllegalStateException("Student doesn't exist"));

        // Actualizar nombre
        if (updatedStudent.getName() != null &&
                !updatedStudent.getName().isBlank() &&
                !updatedStudent.getName().equals(student.getName())) {
            student.setName(updatedStudent.getName());
        }

        // Actualizar email
        if (updatedStudent.getEmail() != null &&
                !updatedStudent.getEmail().isBlank() &&
                !updatedStudent.getEmail().equals(student.getEmail())) {

            Optional<Student> studentOptional =
                    studentRepository.findStudentByEmail(updatedStudent.getEmail());

            // Si existe otro estudiante con ese email, lanzar error
            if (studentOptional.isPresent() &&
                    !studentOptional.get().getId().equals(studentId)) {
                throw new IllegalStateException("Email already taken");
            }

            student.setEmail(updatedStudent.getEmail());
        }

        // Actualizar contraseña
        if (updatedStudent.getPassword() != null &&
                !updatedStudent.getPassword().isBlank() &&
                !updatedStudent.getPassword().equals(student.getPassword())) {
            student.setPassword(updatedStudent.getPassword());
        }

        // Guardar explícitamente los cambios en la base de datos
        studentRepository.save(student);
    }

}
