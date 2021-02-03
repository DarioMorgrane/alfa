package dariomorgrane.alfa.web.client.feign.adapter;

import dariomorgrane.alfa.exception.WebClientLayerException;
import dariomorgrane.alfa.web.client.CurrencyRatesClient;
import dariomorgrane.alfa.web.client.feign.CurrencyRatesFeignClient;
import dariomorgrane.alfa.web.client.feign.adapter.params.CurrencyRatesClientParamsMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class CurrencyRatesFeignClientAdapter implements CurrencyRatesClient {

    @Value("${name-of-rates-key}")
    private String nameOfRatesKey;

    @Value("${yesterday-path-suffix}")
    private String yesterdayPathSuffix;

    private final CurrencyRatesClientParamsMap paramsMap;
    private final CurrencyRatesFeignClient client;

    @Autowired
    public CurrencyRatesFeignClientAdapter(CurrencyRatesClientParamsMap paramsMap, CurrencyRatesFeignClient client) {
        this.paramsMap = paramsMap;
        this.client = client;
    }

    @Override
    public Map<String, Double> getTodayRatesMap() {
        try {
            Map<String, Object> respondJson = client.getTodayRates(paramsMap);
            return getRatesDoubleMap(respondJson);
        } catch (Exception e) {
            throw new WebClientLayerException(e.getMessage());
        }
    }

    @Override
    public Map<String, Double> getYesterdayRatesMap() {
        try {
            String yesterdayRatesPath = getYesterdayDateString() + yesterdayPathSuffix;
            Map<String, Object> respondJson = client.getYesterdayRates(yesterdayRatesPath, paramsMap);
            return getRatesDoubleMap(respondJson);
        } catch (Exception e) {
            throw new WebClientLayerException(e.getMessage());
        }
    }

    private Map<String, Double> getRatesDoubleMap(Map<String, Object> respondJson) {
        Map<String, Number> ratesNumberMap = (Map<String, Number>) respondJson.get(nameOfRatesKey);
        Map<String, Double> ratesDoubleMap = new HashMap<>();
        ratesNumberMap.forEach((key, value) -> ratesDoubleMap.put(key, value.doubleValue()));
        return ratesDoubleMap;
    }

    private String getYesterdayDateString() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
