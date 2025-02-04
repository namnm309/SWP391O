package com.example.DemoSelfTest1.service;

import com.example.DemoSelfTest1.model.Student;
import com.example.DemoSelfTest1.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppService {

    @Autowired//Tự đóng inject
    private AppRepository appRepository;

    //Fucntion trả về list student
    public List<Student> getStudent(){
        List<Student> obj= appRepository.findAll();
        return obj;
    }

    // Function thêm mới học sinh
    public Student addStudent(Student student) {
        return appRepository.save(student); // Lưu học sinh mới vào database
    }

    // Function cập nhật thông tin học sinh
    public Student updateStudent(Long id, Student updatedStudent) {
        return appRepository.findById(id)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setAge(updatedStudent.getAge());
                    student.setBirthday(updatedStudent.getBirthday());
                    System.out.println("Đã cập nhật thành công");
                    return appRepository.save(student);
                })
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }

    // Function xóa học sinh
    public void deleteStudent(Long id) {
        appRepository.deleteById(id); // Xóa học sinh theo ID
    }
}
