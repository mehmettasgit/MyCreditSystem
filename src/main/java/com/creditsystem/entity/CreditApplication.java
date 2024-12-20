package com.creditsystem.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="credit_applications")
public class CreditApplication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String surName;

    @Column(name = "monthly_income")
    private Double monthlyIncome;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_address")
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
