package dariomorgrane.alfa.web.client.feign.adapter;

import dariomorgrane.alfa.exception.ClientLayerException;
import dariomorgrane.alfa.web.client.GifClient;
import dariomorgrane.alfa.web.client.feign.GifFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@Component
public class GifFeignClientAdapter implements GifClient {

    @Value("${gif-api-key}")
    private String apiKey;

    @Value("${rich-gif-query}")
    private String richGifQuery;

    @Value("${broke-gif-query}")
    private String brokeGifQuery;

    @Value("${gif-lang}")
    private String gifLang;

    @Value("${gif-data-key}")
    private String gifDataKey;

    @Value("${gif-json-position}")
    private Integer gifJsonPosition;

    @Value("${gif-url-key}")
    private String gifUrlKey;

    @Value("${gif-random-bound}")
    private Integer gifRandomBound;

    private final int limit = 1;
    private final Random random = new Random();
    private final GifFeignClient client;

    @Autowired
    public GifFeignClientAdapter(GifFeignClient client) {
        this.client = client;
    }

    @Override
    public String getRichGifUrl() {
        return getGifUrl(richGifQuery);
    }

    @Override
    public String getBrokeGifUrl() {
        return getGifUrl(brokeGifQuery);
    }

    private String getGifUrl(String brokeGifQuery) {
        try {
            int offset = random.nextInt(gifRandomBound);
            Map<String, Object> fullRespondJsonMap = client.findGif(apiKey, brokeGifQuery, limit, offset, gifLang);
            String gifUrl = extractOriginalGifUrl(fullRespondJsonMap);
            return gifUrl;
        } catch (Exception e) {
            throw new ClientLayerException(e.getMessage());
        }
    }

    private String extractOriginalGifUrl(Map<String, Object> fullRespondJsonMap) {
        ArrayList<Object> dataList;
        dataList = (ArrayList<Object>) fullRespondJsonMap.get(gifDataKey);
        Map<String, Object> gifMap = (Map<String, Object>) dataList.get(gifJsonPosition);
        return (String) gifMap.get(gifUrlKey);
    }

}
