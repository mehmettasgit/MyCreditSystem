package com.creditsystem.service;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.exception.ResourceNotFoundException;
import com.creditsystem.repository.CreditApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CreditApplicationService {

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    public CreditApplication createCreditApplication(CreditApplication creditApplication) {
        log.info("Creating credit application for National ID: {} ", creditApplication.getNationalId());

        creditApplication.setCreditScore(generateCreditScore());
        String creditResult = determineCreditResult(creditApplication); // Credit result oluşturulur.
        creditApplication.setCreditResult(creditResult); // Sonuç burada set edilir.

        CreditApplication savedApp = creditApplicationRepository.save(creditApplication);
        log.info("Credit Application created with ID: {} ", savedApp.getId());
        return savedApp;
       // return creditApplicationRepository.save(creditApplication);
    }

    public CreditApplication findByNationalId(String nationalId) {
        log.info("Searching credit application by National ID: {}", nationalId);
        return creditApplicationRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("Credit application with National ID " + nationalId + " not found."));
    }

    public List<CreditApplication> findAll(){
        log.info("Fetching all credit applications");
        return creditApplicationRepository.findAll();
    }

    public CreditApplication updateCreditApplication(Long applicationId, CreditApplication updatedApplication){
        log.debug("Updating credit application with ID: {}", applicationId);
        CreditApplication existingApplication = creditApplicationRepository.findById(applicationId)
                .orElseThrow(()-> new RuntimeException("CreditApplication not found with id" + applicationId));

        existingApplication.setMonthlyIncome(updatedApplication.getMonthlyIncome());
        existingApplication.setFirstName(updatedApplication.getFirstName());

        existingApplication.setCreditScore(generateCreditScore());
        String newCreditResult = determineCreditResult(existingApplication);
        existingApplication.setCreditResult(newCreditResult);

        CreditApplication saved = creditApplicationRepository.save(existingApplication);
        log.info("Credit Application updated successfully for ID: {}", applicationId);

        return saved;
        //return  creditApplicationRepository.save(existingApplication);
    }

    public void deleteCreditApplication(Long applicationId){
        log.debug("Deleting credit application with ID: {}", applicationId);
        CreditApplication existingApplication = creditApplicationRepository.findById(applicationId)
                .orElseThrow(()-> new RuntimeException("Credit Application not found with ID: " + applicationId));
        creditApplicationRepository.delete(existingApplication);
        log.info("Credit Application deleted with ID: {}", applicationId);
    }


    public int generateCreditScore() {
        int score = (int) (Math.random() * 1000) + 1;
        log.debug("Generated credit score: {}",score);
        return score;
    }

    private String determineCreditResult(CreditApplication creditApplication) {

        int creditScore = creditApplication.getCreditScore();
        double monthlyIncome = creditApplication.getMonthlyIncome();
        double creditLimitMultiplier = 4.0;
        log.debug("Determining credit result for score: {} and monthly income: {}",creditScore,monthlyIncome,creditLimitMultiplier);

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
