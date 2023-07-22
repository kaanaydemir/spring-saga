package com.kaanaydemir.commonservice.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetail {
    private String name;
    private String cardNumber;
    private String validUntilMonth;
    private String validUntilYear;
    private String cvv;
}
