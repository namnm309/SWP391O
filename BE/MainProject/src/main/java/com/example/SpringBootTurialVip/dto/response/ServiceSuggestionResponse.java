package com.example.SpringBootTurialVip.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ServiceSuggestionResponse {
    private String ageGroup;
    private String ageGroupLabel;
    private List<ProductSuggestionDTO> vaccines;
    private List<LiteChildResponse> matchingChildren;
}