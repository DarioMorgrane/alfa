package dariomorgrane.alfa.web.client.feign.adapter.params;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CurrencyRatesClientParamsMap extends HashMap<String, String> {

    public CurrencyRatesClientParamsMap(@Value("${rates-app-id-request-param}") String ratesAppIdRequestParam,
                                        @Value("${rates-app-id}") String ratesAppId,
                                        @Value("${base-currency-request-param}") String baseCurrencyRequestParam,
                                        @Value("${base-currency}") String baseCurrency) {
        super();
        this.put(ratesAppIdRequestParam, ratesAppId);
        this.put(baseCurrencyRequestParam, baseCurrency);
    }
}
