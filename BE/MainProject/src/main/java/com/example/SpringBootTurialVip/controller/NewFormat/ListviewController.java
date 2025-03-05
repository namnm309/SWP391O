package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.service.StaffService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
@Tag(name="[ListView]",description = "")
public class ListviewController {

    @Autowired
    private StaffService staffService;




}
