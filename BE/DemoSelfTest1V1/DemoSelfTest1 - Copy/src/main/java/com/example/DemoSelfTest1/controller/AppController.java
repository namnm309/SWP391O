package com.example.DemoSelfTest1.controller;

import com.example.DemoSelfTest1.model.Student;
import com.example.DemoSelfTest1.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController//báo rằng đây là tầng controller = @Controller + @Respone
@RequestMapping("/api")
@Tag(name="Các api chức năng app")
public class AppController {

    @Autowired
    private AppService appService;

    public String test="succesful";


    @Operation(summary = "Lấy thông tin học sinh",description = "Cần parameter id từ fe để nhận đc ")
    //URL tới get Student
    @GetMapping("/Student")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<String> getStudent(){
        //return appService.getStudent();
        return Arrays.asList("sdsa", "sadsa");
    }

    // POST: Thêm học sinh
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return appService.addStudent(student);
    }

    // PUT: Cập nhật học sinh
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return appService.updateStudent(id, student);
    }

    // DELETE: Xóa học sinh
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        appService.deleteStudent(id);
        return "Student với ID " + id + " đã đc xóa .";
    }




}
