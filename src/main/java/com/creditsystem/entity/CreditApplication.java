package com.creditsystem.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Table(name="credit_applications")
public class CreditApplication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_id")
    @NotBlank(message = "National ID cannot be blank")
    @Size(min=11, max = 11, message = "National ID must be 11 characters")
    private String nationalId;

    @Column(name = "first_name")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Column(name = "surname")
    @NotBlank(message = "Surname cannot be blank")
    private String surName;

    @Column(name = "monthly_income")
    @Min(value = 0, message = "Monthly income must be greater than or equal to 0")
    private Double monthlyIncome;

    @Column(name = "phone_number")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    @Column(name = "email_address")
    @Email(message = "Invalid email address")
    private String emailAddress;

    @Column(name = "address")
    private String address;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "credit_result")
    private String creditResult;

    @Column(name = "credit_limit")
    private Double creditLimit;

}
