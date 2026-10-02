package service;

import model.Student;
import repository.DataStore;
import repository.StudentDAO;

public class StudentService {

    private StudentDAO studentDAO = new StudentDAO();

    public Student registerStudent(String name,
                                   String phoneNumber,
                                   String email,
                                   String password) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Student existingStudent =
                studentDAO.findByPhone(phoneNumber);

        if (existingStudent != null) {
            throw new IllegalArgumentException(
                    "Phone number already registered"
            );
        }

        Student student = new Student(
                null,
                name,
                phoneNumber,
                email,
                password
        );

        Student savedStudent =
                studentDAO.addStudent(student);

        if (savedStudent == null) {
            throw new IllegalArgumentException(
                    "Student registration failed"
            );
        }

        DataStore.students.put(
                savedStudent.getStudentId(),
                savedStudent
        );

        return savedStudent;
    }

    public Student loginStudent(String phoneNumber,
                                String password) {

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Phone number cannot be empty"
            );
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }

        Student student =
                studentDAO.findByPhone(phoneNumber);

        if (student != null &&
            student.getPassword().equals(password)) {

            DataStore.students.put(
                    student.getStudentId(),
                    student
            );

            return student;
        }

        return null;
    }

    public Student findStudentById(Integer studentId) {

        if (studentId == null) {
            throw new IllegalArgumentException(
                    "Student ID cannot be null"
            );
        }

        Student student =
                studentDAO.findStudentById(studentId);

        if (student != null) {
            DataStore.students.put(
                    student.getStudentId(),
                    student
            );
        }

        return student;
    }
}