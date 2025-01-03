package com.creditsystem.service;

import com.creditsystem.entity.CreditApplication;
import com.creditsystem.exception.ResourceNotFoundException;
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
    void findByNationalId_WhenExists_ShouldReturnCreditApplication() {
        //Arrange
        CreditApplication application = new CreditApplication();
        application.setNationalId("12345678901");
        application.setMonthlyIncome(5000.0);

        when(creditApplicationRepository.findByNationalId("12345678901"))
                .thenReturn(java.util.Optional.of(application));

        //Act
        CreditApplication result = creditApplicationService.findByNationalId("12345678901");

        //Assert
        assertNotNull(result);
        assertEquals("12345678901", result.getNationalId());
        verify(creditApplicationRepository, times(1)).findByNationalId("12345678901");

    }

    @Test
    void findByNationalId_WhenNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(creditApplicationRepository.findByNationalId("12345678901"))
                .thenReturn(java.util.Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            creditApplicationService.findByNationalId("12345678901");
        });

        assertEquals("Credit application with National ID 12345678901 not found.", exception.getMessage());
        verify(creditApplicationRepository, times(1)).findByNationalId("12345678901");
    }

    @Test
    void  createCreditApplication_WhenCreditScoreBelow500_ShouldSetRejection(){

        //Arrange
        CreditApplication application = new CreditApplication();
        application.setNationalId("12345678901");
        application.setMonthlyIncome(1000.0);

        when(creditApplicationRepository.save(any(CreditApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        CreditApplication result = creditApplicationService.createCreditApplication(application);

        //Assert
        assertNotNull(result);
        assertEquals("Rejection!", result.getCreditResult());
        assertEquals(0.0, result.getCreditLimit());
    }

}

