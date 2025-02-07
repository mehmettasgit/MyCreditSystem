package com.creditsystem.controller;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.service.CreditApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/credit")
public class CreditApplicationController {

    @Autowired
    private CreditApplicationService creditApplicationService;

    @Operation(summary = "Apply for Credit", description = "Create a new credit application")
    @PostMapping("/apply")
    public CreditApplication applyCredit(@Valid @RequestBody CreditApplication creditApplication) {
        return creditApplicationService.createCreditApplication(creditApplication);
    }

    @Operation(summary = "Search Credit Application", description = "Find a credit application by National ID")
    @GetMapping("/{nationalId}")
    public CreditApplication applicationSearch(@PathVariable String nationalId) {
        return creditApplicationService.findByNationalId(nationalId);
    }

    @Operation(summary = "List All Credit Applications", description = "Retrieve all credit applications")
    @GetMapping("/allCredits")
    public List<CreditApplication> getAllCreditApplications(){
        return creditApplicationService.findAll();
    }

}

