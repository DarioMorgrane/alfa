package dariomorgrane.alfa;

import dariomorgrane.alfa.model.Operation;
import dariomorgrane.alfa.web.client.feign.CurrencyRatesFeignClient;
import dariomorgrane.alfa.web.client.feign.GifFeignClient;
import dariomorgrane.alfa.web.controller.OperationController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class AlfaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyRatesFeignClient mockRatesClient;

    @MockBean
    private GifFeignClient mockGifClient;

    @Autowired
    private OperationController controller;

    @Value("${base-currency}")
    private String baseCurrency;

    @Value("${name-of-rates-key}")
    private String nameOfRatesKey;

    @Value("${rich-gif-query}")
    String richGifQuery;

    @Value("${broke-gif-query}")
    String brokeGifQuery;

    @Value("${gif-url-key}")
    String urlKey;

    @Value("${gif-data-key}")
    String dataKey;

    private Operation expectedOperation;
    private Operation expectedRichOperation;

    private final String brockGifUrl = "broke gif url";
    private final String richGifUrl = "rich gif url";

    private final double todayUSDRate = 1;
    private final double todayEURRate = 2;
    private final double yesterdayUSDRate = 2;
    private final double yesterdayEURRate = 1;

    private final String USDCurrencyName = "USD";
    private final String EURCurrencyName = "EUR";

    private final String applicationUrl = "/operate?code=";


    Map<String, Object> generateMockRatesClientResponse(double usd, double eur) {
        Map<String, Double> mapOfRates = new HashMap<>();
        mapOfRates.put(USDCurrencyName, usd);
        mapOfRates.put(EURCurrencyName, eur);
        Map<String, Object> mockRatesClientResponse = new HashMap<>();
        mockRatesClientResponse.put(nameOfRatesKey, mapOfRates);
        return mockRatesClientResponse;
    }

    Map<String, Object> generateMockGifClientResponse(String url) {
        Map<String, Object> gifMap = new HashMap<>();
        gifMap.put(urlKey, url);
        ArrayList<Object> dataList = new ArrayList<>();
        dataList.add(gifMap);
        Map<String, Object> mockGifClientResponse = new HashMap<>();
        mockGifClientResponse.put(dataKey, dataList);
        return mockGifClientResponse;
    }



    Operation generateExpectedOperationObject(String operatingCurrencyCode,
                                              double todayOperatingCurrencyRate,
                                              double yesterdayOperatingCurrencyRate,
                                              boolean rateGoneUp,
                                              String gifUrl) {
        expectedOperation = new Operation();
        expectedOperation.setBaseCurrencyCode(baseCurrency);
        expectedOperation.setOperatingCurrencyCode(operatingCurrencyCode);
        expectedOperation.setTodayOperatingCurrencyRate(todayOperatingCurrencyRate);
        expectedOperation.setYesterdayOperatingCurrencyRate(yesterdayOperatingCurrencyRate);
        expectedOperation.setRateGoneUp(rateGoneUp);
        expectedOperation.setGifUrl(gifUrl);
        return expectedOperation;
    }

    @BeforeEach
    void setUpMockObjects() {
        Mockito.when(mockRatesClient.getTodayRates(anyString(), anyString()))
                .thenReturn(generateMockRatesClientResponse(todayUSDRate, todayEURRate));
        Mockito.when(mockRatesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(generateMockRatesClientResponse(yesterdayUSDRate, yesterdayEURRate));
        Mockito.when(mockGifClient.findGif(anyString(), eq(brokeGifQuery), anyInt(), anyInt(), anyString()))
                .thenReturn(generateMockGifClientResponse(brockGifUrl));
        Mockito.when(mockGifClient.findGif(anyString(), eq(richGifQuery), anyInt(), anyInt(), anyString()))
                .thenReturn(generateMockGifClientResponse(richGifUrl));
    }

    @Test
    void requestWithUSDShouldProducesExpectedBrokeOperation() throws Exception {
        mockMvc.perform(get(applicationUrl + USDCurrencyName));
        Operation expectedOperation = generateExpectedOperationObject(
                USDCurrencyName,
                todayUSDRate,
                yesterdayUSDRate,
                false,
                brockGifUrl
        );
        Assertions.assertEquals(expectedOperation, controller.getOperation());
    }

    @Test
    void requestWithEURShouldProducesExpectedRichOperation() throws Exception {
        mockMvc.perform(get(applicationUrl + EURCurrencyName));
        Operation expectedOperation = generateExpectedOperationObject(
                EURCurrencyName,
                todayEURRate,
                yesterdayEURRate,
                true,
                richGifUrl
        );
        Assertions.assertEquals(expectedOperation, controller.getOperation());
    }

}
