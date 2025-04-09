package com.example.SpringBootTurialVip.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChildVaccinationGroup {
    private Long childId;
    private String childName;
    private List<VaccineItem> vaccines;
}
