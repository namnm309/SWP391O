package com.example.SpringBootTurialVip.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Data
public class OrderRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;

	@JsonIgnore
	private String paymentType;

	private List<Long> childId;

	//private Integer dosesAlreadyTaken = 0; // số mũi đã tiêm ở nơi khác

	private LocalDateTime vaccinationdate;

	//private List<ChildDoseRequest> children;



}
