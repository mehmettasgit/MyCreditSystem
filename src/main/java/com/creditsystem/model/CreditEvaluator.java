package com.creditsystem.model;

import com.creditsystem.entity.CreditApplication;

@FunctionalInterface
public interface CreditEvaluator {
    CreditResult evaluate(CreditApplication application);
}
