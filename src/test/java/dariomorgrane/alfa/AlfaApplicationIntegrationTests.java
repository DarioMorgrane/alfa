package dariomorgrane.alfa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {"currency-rates-url=http://localhost:${wiremock.server.port}",
        "gif-url=http://localhost:${wiremock.server.port}"})
class AlfaApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Value("${today-rates-path}")
    private String todayRatesPath;

    @Value("${historical-rates-path}")
    private String historicalRatesPath;

    @Value("${base-currency}")
    private String baseCurrency;

    @Value("${rates-app-id-request-param}")
    private String appIdRequestParam;

    @Value("${rates-app-id}")
    private String appId;

    @Value("${base-currency-request-param}")
    private String baseCurrencyRequestParam;

    @Value("${yesterday-path-suffix}")
    private String yesterdayPathSuffix;

    @Value("${gif-api-key-request-param}")
    private String gifApiKeyRequestParam;

    @Value("${gif-api-key}")
    private String gifApiKey;

    @Value("${limit-request-param}")
    private String limitRequestParam;

    @Value("${limit}")
    private String limit;

    @Value("${language-request-param}")
    private String languageRequestParam;

    @Value("${language}")
    private String language;

    @Value("${query-request-param}")
    private String queryRequestParam;

    @Value("${offset-request-param}")
    private String offsetRequestParam;

    @Value("${rich-gif-query}")
    private String richGifQuery;

    @Value("${broke-gif-query}")
    private String brokeGifQuery;

    @Value("${search-endpoint}")
    private String searchEndpoint;

    @Value("${gif-data-key}")
    private String gifDataKey;

    @Value("${gif-json-position}")
    private Integer gifJsonPosition;

    @Value("${gif-url-key}")
    private String gifUrlKey;

    private final String applicationUrl = "/operate?code=";
    private String brokeGifUrl;
    private String richGifUrl;

    @BeforeEach
    void setUpMockRatesAPI() {
        stubFor(WireMock.get(urlPathEqualTo("/" + todayRatesPath))
                .withQueryParam(appIdRequestParam, equalTo(appId))
                .withQueryParam(baseCurrencyRequestParam, equalTo(baseCurrency))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("todayCurrencyResponse.json")));

        stubFor(WireMock.get(urlPathMatching("/" + historicalRatesPath +
                "/" + "(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)" + yesterdayPathSuffix))
                .withQueryParam(appIdRequestParam, equalTo(appId))
                .withQueryParam(baseCurrencyRequestParam, equalTo(baseCurrency))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("yesterdayCurrencyResponse.json")));
    }

    @BeforeEach
    void setUpMockGifAPI() {
        stubFor(WireMock.get(urlPathEqualTo("/" + searchEndpoint))
                .withQueryParam(gifApiKeyRequestParam, equalTo(gifApiKey))
                .withQueryParam(limitRequestParam, equalTo(limit))
                .withQueryParam(languageRequestParam, equalTo(language))
                .withQueryParam(queryRequestParam, equalTo(richGifQuery))
                .withQueryParam(offsetRequestParam, matching("\\d+"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("richGifResponse.json")));

        stubFor(WireMock.get(urlPathEqualTo("/" + searchEndpoint))
                .withQueryParam(gifApiKeyRequestParam, equalTo(gifApiKey))
                .withQueryParam(limitRequestParam, equalTo(limit))
                .withQueryParam(languageRequestParam, equalTo(language))
                .withQueryParam(queryRequestParam, equalTo(brokeGifQuery))
                .withQueryParam(offsetRequestParam, matching("\\d+"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("brokeGifResponse.json")));

    }

    @BeforeEach
    void setProperlyBrokeGifUrl() throws IOException {
        brokeGifUrl = getProperlyGifUrl("src/test/resources/__files/brokeGifResponse.json");
    }

    @BeforeEach
    void setProperlyRichGifUrl() throws IOException {
        richGifUrl = getProperlyGifUrl("src/test/resources/__files/richGifResponse.json");
    }

    String getProperlyGifUrl(String jsonFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File brokeGifResponseFile = new File(jsonFilePath);
        Map<String, Object> brokeGifResponse = mapper.readValue(brokeGifResponseFile,  Map.class);
        ArrayList<Object> data = (ArrayList<Object>) brokeGifResponse.get(gifDataKey);
        Map<String, String> gifInfo = (Map<String, String>) data.get(gifJsonPosition);
        return gifInfo.get(gifUrlKey);
    }

    @Test
    void requestWithUSDShouldReturnBrokeGifUrl() throws Exception {
        String USDCurrencyName = "USD";
        mockMvc.perform(get(applicationUrl + USDCurrencyName)).andExpect(status().isFound())
                .andExpect(header().string("Location", brokeGifUrl));
    }

    @Test
    void requestWithEURShouldReturnRichGifUrl() throws Exception {
        String EURCurrencyName = "EUR";
        mockMvc.perform(get(applicationUrl + EURCurrencyName)).andExpect(status().isFound())
                .andExpect(header().string("Location", richGifUrl));
    }

}
