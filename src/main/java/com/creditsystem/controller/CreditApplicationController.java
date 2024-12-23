package com.creditsystem.controller;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.service.CreditApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/credit")
public class CreditApplicationController {

    @Autowired
    private CreditApplicationService creditApplicationService;

    @PostMapping("/apply")
    public CreditApplication applyCredit(@Valid @RequestBody CreditApplication creditApplication){
        return creditApplicationService.createCreditApplication(creditApplication);
    }

    @GetMapping("/{nationalId}")
    public CreditApplication applicationSearch(@PathVariable String nationalId){
        return creditApplicationService.findByNationalId(nationalId);
    }
}
