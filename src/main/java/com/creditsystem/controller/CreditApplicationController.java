package com.creditsystem.controller;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.service.CreditApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController  //HTTP
@RequestMapping("/api/credit")
@Slf4j
public class CreditApplicationController {

   // createdApplication1 -> Yerel değişken

    //üye değişken
    @Autowired
    private CreditApplicationService creditApplicationService;

    @Operation(summary = "Apply for Credit", description = "Create a new credit application")
    @PostMapping("/apply")
    public CreditApplication applyCredit(@Valid @RequestBody CreditApplication creditApplication) {
        log.info("Kredi başvurusu isteği alındı. Başvuru detayı: {}", creditApplication);

        CreditApplication createdApplication1 = creditApplicationService.createCreditApplication(creditApplication);

       // return creditApplicationService.createCreditApplication(creditApplication);
        return createdApplication1;
    }

    @Operation(summary = "Search Credit Application", description = "Find a credit application by National ID")
    @GetMapping("/{nationalId}")
    public CreditApplication applicationSearch(@PathVariable String nationalId) {
        log.info("National ID ile kredi sorgusu yapılıyor: {}", nationalId);
        CreditApplication creditApplication = creditApplicationService.findByNationalId(nationalId);
        log.info("Kredi başvurusu bulundu. ID: {}", creditApplication.getId());
        return creditApplication;
    }

    @Operation(summary = "List All Credit Applications", description = "Retrieve all credit applications")
    @GetMapping("/allCredits")
    public List<CreditApplication> getAllCreditApplications(){
        log.info("Tüm kredi başvuruları listeleniyor.");
        List<CreditApplication> applications = creditApplicationService.findAll();
        log.info("Toplam başvuru sayısı: {}", applications.size());
        applications.forEach(app-> log.info("Başvuru Detayı - ID: {}, TC: {}, Sonuç: {}",
                app.getId(), app.getNationalId(), app.getCreditResult()));
        return applications;
    }

    @PutMapping("/{applicationId}")
    public CreditApplication updateCredit(@PathVariable Long applicationId,
                                          @RequestBody CreditApplication updateApplication){
        log.info("Kredi başvurusu güncelleme isteği alındı. ID: {}, Güncellenecek Değerler: {}",
                applicationId, updateApplication);
        CreditApplication updatedCreditApp = creditApplicationService.updateCreditApplication(applicationId, updateApplication);
        log.info("Kredi başvurusu başarıyla güncellendi. ID: {}", updatedCreditApp.getId());
        return updatedCreditApp;
       // return creditApplicationService.updateCreditApplication(applicationId, updateApplication);
    }

    @DeleteMapping("/{applicationId}")
    public void deleteCredit(@PathVariable Long applicationId){
        log.info("Kredi başvurusu silme isteği alındı. ID: {}", applicationId);
        creditApplicationService.deleteCreditApplication(applicationId);
        log.info("Kredi başvurusu başarıyla silindi. ID: {}", applicationId);
    }

}

