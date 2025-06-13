package com.creditsystem.controller;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.exception.ResourceNotFoundException;
import com.creditsystem.model.CreditResult;
import com.creditsystem.service.CreditApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> applyCredit(@Valid @RequestBody CreditApplication creditApplication) {
     log.info("Kredi başvurusu isteği alındı. Başvuru detayı: {}", creditApplication);

     try {
         CreditApplication creditApplication1 = creditApplicationService.createCreditApplication(creditApplication);
         return ResponseEntity.ok(creditApplication1);

     }catch (Exception e){
         log.error("Kredi başvurusunda hata oluştur", e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Kredi başvurusu başarısız oldu.");
     }
    }

    @Operation(summary = "Search Credit Application", description = "Find a credit application by National ID")
    @GetMapping("/{nationalId}")
    public ResponseEntity<?> applicationSearch(@PathVariable String nationalId) {

        try{
            CreditApplication app = creditApplicationService.findByNationalId(nationalId);
            return ResponseEntity.ok(app);
        }catch (ResourceNotFoundException e){
            log.warn("Kredi başvurusu bulunmadı. TC: {}", nationalId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            log.error("Kredi sorgularken beklenmeyen hata.", e);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bir hata oluştu");
        }
    }

    @Operation(summary = "List All Credit Applications", description = "Retrieve all credit applications")
    @GetMapping("/allCredits")
    public List<CreditApplication> getAllCreditApplications() {
        log.info("Tüm kredi başvuruları listeleniyor.");
        List<CreditApplication> applications = creditApplicationService.findAll();
        log.info("Toplam başvuru sayısı: {}", applications.size());
        applications.forEach(app -> {
            StringJoiner joiner = new StringJoiner("|");
            joiner.add("ID: " + app.getId())
                    .add("TC: " + app.getNationalId())
                    .add("Sonuç: " + app.getCreditResult())
                    .add("Limit: " + app.getCreditLimit());
            log.info("Başvuru Özeti: {}", joiner.toString());
        });
        return applications;
    }

    @Operation(summary = "List Accepted Credit Applications", description = "Retrieve only accepted credit applications")
    @GetMapping("/allAccepted")
    public List<CreditApplication> getAcceptedCreditApplications(){
        log.info("Sadece kabul edilen kredi başvuruları listeleniyor.");
        List<CreditApplication> acceptedApplications = creditApplicationService.findAll().stream()
                .filter(app -> app.getCreditResult() == CreditResult.ACCEPT)
                .collect(Collectors.toList());

        acceptedApplications.forEach(app -> {
            StringJoiner joiner = new StringJoiner(" | ");
            joiner.add("ID: " + app.getId())
                    .add("TC: " + app.getNationalId())
                    .add("Sonuç: " + app.getCreditResult())
                    .add("Limit: " + app.getCreditLimit());
            log.info("Kabul Edilen Başvuru Özeti: {}", joiner.toString());
        });

        return acceptedApplications;
    }


    @PutMapping("/{applicationId}")
    public CreditApplication updateCredit(@PathVariable Long applicationId,
                                          @RequestBody CreditApplication updateApplication) {
        log.info("Kredi başvurusu güncelleme isteği alındı. ID: {}, Güncellenecek Değerler: {}",
                applicationId, updateApplication);
        CreditApplication updatedCreditApp = creditApplicationService.updateCreditApplication(applicationId, updateApplication);
        log.info("Kredi başvurusu başarıyla güncellendi. ID: {}", updatedCreditApp.getId());
        return updatedCreditApp;
        // return creditApplicationService.updateCreditApplication(applicationId, updateApplication);
    }

    @DeleteMapping("/{applicationId}")
    public void deleteCredit(@PathVariable Long applicationId) {
        log.info("Kredi başvurusu silme isteği alındı. ID: {}", applicationId);
        creditApplicationService.deleteCreditApplication(applicationId);
        log.info("Kredi başvurusu başarıyla silindi. ID: {}", applicationId);
    }

}

