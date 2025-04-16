package com.creditsystem.service;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.exception.ResourceNotFoundException;
import com.creditsystem.model.CreditResult;
import com.creditsystem.repository.CreditApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service  // Bsuiness rules
public class CreditApplicationService {

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    public CreditApplication createCreditApplication(CreditApplication creditApplication) {
        log.info("Creating credit application for National ID: {} ", creditApplication.getNationalId());

        creditApplication.setCreditScore(generateCreditScore());
        CreditResult result = determineCreditResult(creditApplication);
        creditApplication.setCreditResult(result);       // Sonuç burada set edilir.

        CreditApplication savedApp = creditApplicationRepository.save(creditApplication);
        log.info("Credit Application created with ID: {} ", savedApp.getId());
        return savedApp;
       // return creditApplicationRepository.save(creditApplication);
    }

    public CreditApplication findByNationalId(String nationalId) {
        log.info("Searching credit application by National ID: {}", nationalId);
        return creditApplicationRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("Credit application with National ID " + nationalId + " not found."));
        //I create an exception object
    }

    public List<CreditApplication> findAll(){
        log.info("Fetching all credit applications");
        return creditApplicationRepository.findAll();
    }

    public CreditApplication updateCreditApplication(Long applicationId, CreditApplication updatedApplication){
        log.debug("Updating credit application with ID: {}", applicationId);
        CreditApplication existingApplication = creditApplicationRepository.findById(applicationId)
                .orElseThrow(()-> new RuntimeException("CreditApplication not found with id" + applicationId));
        //I create an exception object

        existingApplication.setMonthlyIncome(updatedApplication.getMonthlyIncome());
        existingApplication.setFirstName(updatedApplication.getFirstName());

        existingApplication.setCreditScore(generateCreditScore());
        CreditResult newCreditResult =determineCreditResult(existingApplication);
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
        //I create an exception object
        creditApplicationRepository.delete(existingApplication);
        log.info("Credit Application deleted with ID: {}", applicationId);
    }


    public int generateCreditScore() {
        //static method (nesne oluşturmadan, doğrudan sınıf üzerinden üretir.)
        int score = (int) (Math.random() * 1000) + 1;  // there is int cast here. Because Math random gives double information.
        log.debug("Generated credit score: {}",score);
        return score;
    }

    private CreditResult determineCreditResult(CreditApplication app) {

        int    score     = app.getCreditScore();
        double income    = app.getMonthlyIncome();
        double multiplier= 4.0;

        if (score < 500) {
            app.setCreditLimit(0.0);
            return CreditResult.REJECTION;
        }

        if (score < 1000) {
            app.setCreditLimit(income < 500 ? 10_000.0 : 20_000.0);
            return CreditResult.ACCEPT;
        }

        app.setCreditLimit(income * multiplier);
        return CreditResult.ACCEPT;
    }
}
