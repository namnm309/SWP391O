package com.example.DemoSelfTest1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity//ánh xạ class Student này với bảng cùng tên vs class or đc define bằng @Table
@Table(name="demo1")
@Getter
@Setter
public class Student {
    @Id //báo rằng đây la Primary key của bang TEST1
    //@GeneratedValue(strategy = GenerationType.AUTO)//thuộc tính này tự tăng
    //@GeneratedValue(strategy = GenerationType.IDENTITY)//thuộc tính này tự tăng
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @SequenceGenerator(name = "student_seq", sequenceName = "demo1_seq", allocationSize = 1)

    private Long id;

    private String name;
    private Integer age;
    private Date birhtday;

    public Student() {

    }

    public Student(Long id, String name, Integer age, Date birhtday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birhtday = birhtday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birhtday;
    }

    public void setBirthday(Date birhtday) {
        this.birhtday = birhtday;
    }
}
