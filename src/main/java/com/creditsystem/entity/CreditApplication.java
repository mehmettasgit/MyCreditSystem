package com.creditsystem.entity;

import com.creditsystem.model.CreditResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data //Lombookun ürettği Equals ve HAshcodelar mevcut.
@Entity
@Table(name="credit_applications")
public class CreditApplication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_id", unique = true)
    @NotBlank(message = "National ID cannot be blank")
    @Size(min=11, max = 11, message = "National ID must be 11 characters")
    @Pattern(regexp = "\\d{11}", message = "National ID must contain exactly 11 digits")
    private String nationalId;

    @Column(name = "first_name")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Column(name = "surname")
    @NotBlank(message = "Surname cannot be blank")
    private String surName;

    @Column(name = "monthly_income")
    @DecimalMin(value ="0.0", inclusive = true, message = "Income must be ≥ 0 ")
    private Double monthlyIncome;

    @Column(name = "phone_number", unique = true)
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp="\\d{10,11}", message="Phone must be 10–11 digits")
    private String phoneNumber;

    @Column(name = "email_address", unique = true)
    @Email(message = "Invalid email address")
    private String emailAddress;

    @Column(name = "address")
    private String address;

    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="birth_date")
   // @NotBlank(message = "Birthdate cannot be blank")
    private LocalDate birthDate;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_result")
    private CreditResult creditResult;

    @Column(name = "credit_limit")
    private Double creditLimit;

}
