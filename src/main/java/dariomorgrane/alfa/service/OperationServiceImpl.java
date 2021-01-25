package dariomorgrane.alfa.service;

import dariomorgrane.alfa.model.Operation;
import dariomorgrane.alfa.web.client.CurrencyRatesClient;
import dariomorgrane.alfa.web.client.GifClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OperationServiceImpl implements OperationService {

    private final CurrencyRatesClient ratesClient;
    private final GifClient gifClient;

    @Value("${base-currency}")
    private String baseCurrency;

    @Autowired
    public OperationServiceImpl(CurrencyRatesClient ratesClient, GifClient gifClient) {
        this.ratesClient = ratesClient;
        this.gifClient = gifClient;
    }

    @Override
    public Operation setupInitialData(String operatingCurrencyCode) {
        Operation operation = new Operation();
        operation.setBaseCurrencyCode(baseCurrency);
        operation.setOperatingCurrencyCode(operatingCurrencyCode);
        return operation;
    }

    @Override
    public void setupRatesData(Operation operation) {
        Double todayOperatingCurrencyRate = ratesClient.getTodayRatesMap().get(operation.getOperatingCurrencyCode());
        operation.setTodayOperatingCurrencyRate(todayOperatingCurrencyRate);
        Double yesterdayOperatingCurrencyRate = ratesClient.getYesterdayRatesMap().get(operation.getOperatingCurrencyCode());
        operation.setYesterdayOperatingCurrencyRate(yesterdayOperatingCurrencyRate);
        operation.setRateGoneUp(operation.getTodayOperatingCurrencyRate() >= operation.getYesterdayOperatingCurrencyRate());
    }

    @Override
    public void setupGifData(Operation operation) {
        operation.setGifUrl(operation.getRateGoneUp() ? gifClient.getRichGifUrl() : gifClient.getBrokeGifUrl());
    }

}
