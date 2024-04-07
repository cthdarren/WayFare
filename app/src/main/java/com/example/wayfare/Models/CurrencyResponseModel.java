package com.example.wayfare.Models;

public class CurrencyResponseModel {
    public final String result;
    public final ConversionRatesModel conversion_rates;

    public CurrencyResponseModel(String result, ConversionRatesModel conversionRates) {
        this.result = result;
        conversion_rates = conversionRates;
    }

}

