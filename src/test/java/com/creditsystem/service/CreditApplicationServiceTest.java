package com.creditsystem.service;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.repository.CreditApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreditApplicationServiceTest {

    @Mock
    private CreditApplicationRepository creditApplicationRepository;

    @InjectMocks
    private CreditApplicationService creditApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCreditApplication() {

        //Arrange
        CreditApplication application = new CreditApplication();
        application.setNationalId("12345678901");
        application.setMonthlyIncome(5000.0);

        when(creditApplicationRepository.save(any(CreditApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        CreditApplication result = creditApplicationService.createCreditApplication(application);

        //Assert
        assertNotNull(result);
        assertTrue(result.getCreditScore() > 0);
        assertNotNull(result.getCreditResult());
        verify(creditApplicationRepository, times(1)).save(any(CreditApplication.class));

    }


    @Test
    void findByNationalId() {
    }
}