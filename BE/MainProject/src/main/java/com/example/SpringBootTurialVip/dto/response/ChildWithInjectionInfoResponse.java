package com.example.SpringBootTurialVip.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChildWithInjectionInfoResponse {
    private Long id;
    private String fullname;
    private LocalDate birthDate;
    private String gender;
    private Double height;
    private Double weight;
    private List<InjectionInfo> injections;

    @Data
    public static class InjectionInfo {
        private String vaccineTitle;
        private LocalDateTime vaccinationDate;
        private List<String> reactionNotes;

        public InjectionInfo(String vaccineTitle, LocalDateTime vaccinationDate, List<String> reactionNotes) {
            this.vaccineTitle = vaccineTitle;
            this.vaccinationDate = vaccinationDate;
            this.reactionNotes = reactionNotes;
        }
    }
}
