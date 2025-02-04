package com.example.DemoSelfTest1.repository;

import com.example.DemoSelfTest1.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//Báo với IOC Container rằng đây là tầng quản lí data sử dụng JPA để giao tiếp với Hibernate
public interface AppRepository
        extends JpaRepository<Student,Long>{
    //tham số truyền vào là model ánh xạ tới  va kiểu Integer là kiểu của khóa chính của model ánh xạ tới

}
