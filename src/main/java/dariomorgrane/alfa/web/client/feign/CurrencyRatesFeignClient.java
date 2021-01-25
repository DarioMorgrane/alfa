package dariomorgrane.alfa.web.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "CurrencyRatesClient", url = "${currency-rates-url}")
public interface CurrencyRatesFeignClient {

    @GetMapping(value = "/${today-rates-path}")
    Map<String, Object> getTodayRates(@RequestParam("app_id") String appId,
                                      @RequestParam("base") String base);

    @GetMapping(value = "/${historical-rates-path}" + "/{yesterdayRatesPath}")
    Map<String, Object> getYesterdayRates(@PathVariable("yesterdayRatesPath") String yesterdayRatesPath,
                                          @RequestParam("app_id") String app_id,
                                          @RequestParam("base") String base);


}
