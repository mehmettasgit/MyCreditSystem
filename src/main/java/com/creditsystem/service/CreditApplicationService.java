package com.creditsystem.service;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.exception.ResourceNotFoundException;
import com.creditsystem.repository.CreditApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditApplicationService {

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    public CreditApplication createCreditApplication(CreditApplication creditApplication) {
        creditApplication.setCreditScore(generateCreditScore());
        String creditResult = determineCreditResult(creditApplication); // Credit result oluşturulur.
        creditApplication.setCreditResult(creditResult); // Sonuç burada set edilir.
        return creditApplicationRepository.save(creditApplication);
    }

    public CreditApplication findByNationalId(String nationalId) {
        return creditApplicationRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("Credit application with National ID " + nationalId + " not found."));
    }

    public List<CreditApplication> findAll(){
        return creditApplicationRepository.findAll();
    }

    public CreditApplication updateCreditApplication(Long applicationId, CreditApplication updatedApplication){
        CreditApplication existingApplication = creditApplicationRepository.findById(applicationId)
                .orElseThrow(()-> new RuntimeException("CreditApplication not found with id" + applicationId));

        existingApplication.setMonthlyIncome(updatedApplication.getMonthlyIncome());
        existingApplication.setFirstName(updatedApplication.getFirstName());

        existingApplication.setCreditScore(generateCreditScore());
        String newCreditResult = determineCreditResult(existingApplication);
        existingApplication.setCreditResult(newCreditResult);

        return  creditApplicationRepository.save(existingApplication);
    }

    public void deleteCreditApplication(Long applicationId){
        CreditApplication existingApplication = creditApplicationRepository.findById(applicationId)
                .orElseThrow(()-> new RuntimeException("Credit Application not found with ID: " + applicationId));
        creditApplicationRepository.delete(existingApplication);
    }


    public int generateCreditScore() {
        return (int) (Math.random() * 1000) + 1;
    }

    private String determineCreditResult(CreditApplication creditApplication) {

        int creditScore = creditApplication.getCreditScore();
        double monthlyIncome = creditApplication.getMonthlyIncome();
        double creditLimitMultiplier = 4.0;

        if (creditScore < 500) {
            creditApplication.setCreditLimit(0.0);
            return "Recejtion!";
        } else if (creditScore < 1000) {
            if (monthlyIncome < 500) {
                creditApplication.setCreditLimit(10000.0);
            } else {
                creditApplication.setCreditLimit(20000.0);
            }
            return "Accept!";
        } else {
            creditApplication.setCreditLimit(monthlyIncome * creditLimitMultiplier);
            return "Accept!";
        }
    }
}
