package dariomorgrane.alfa.web.client;

import java.util.Map;

public interface CurrencyRatesClient {

    Map<String, Double> getTodayRatesMap();

    Map<String, Double> getYesterdayRatesMap();

}
