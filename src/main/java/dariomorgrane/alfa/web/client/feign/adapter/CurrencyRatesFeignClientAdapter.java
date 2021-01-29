package dariomorgrane.alfa.web.client.feign.adapter;

import dariomorgrane.alfa.exception.WebClientLayerException;
import dariomorgrane.alfa.web.client.CurrencyRatesClient;
import dariomorgrane.alfa.web.client.feign.CurrencyRatesFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class CurrencyRatesFeignClientAdapter implements CurrencyRatesClient {

    @Value("${base-currency}")
    private String baseCurrency;

    @Value("${rates-app-id}")
    private String appId;

    @Value("${name-of-rates-key}")
    private String nameOfRatesKey;

    @Value("${yesterday-path-suffix}")
    private String yesterdayPathSuffix;

    private final CurrencyRatesFeignClient client;

    @Autowired
    public CurrencyRatesFeignClientAdapter(CurrencyRatesFeignClient client) {
        this.client = client;
    }

    @Override
    public Map<String, Double> getTodayRatesMap() {
        try {
            Map<String, Object> respondJson = client.getTodayRates(appId, baseCurrency);
            return (Map<String, Double>) respondJson.get(nameOfRatesKey);
        } catch (Exception e) {
            throw new WebClientLayerException(e.getMessage());
        }
    }

    @Override
    public Map<String, Double> getYesterdayRatesMap() {
        try {
            String yesterdayRatesPath = getYesterdayDateString() + yesterdayPathSuffix;
            Map<String, Object> respondJson = client.getYesterdayRates(yesterdayRatesPath, appId, baseCurrency);
            return (Map<String, Double>) respondJson.get(nameOfRatesKey);
        } catch (Exception e) {
            throw new WebClientLayerException(e.getMessage());
        }
    }

    private String getYesterdayDateString() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
