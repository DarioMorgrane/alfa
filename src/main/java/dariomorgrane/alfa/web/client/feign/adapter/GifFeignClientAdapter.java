package dariomorgrane.alfa.web.client.feign.adapter;

import dariomorgrane.alfa.exception.WebClientLayerException;
import dariomorgrane.alfa.web.client.GifClient;
import dariomorgrane.alfa.web.client.feign.GifFeignClient;
import dariomorgrane.alfa.web.client.feign.adapter.params.GifClientParamsMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@Component
public class GifFeignClientAdapter implements GifClient, ApplicationContextAware {

    private ApplicationContext applicationContext;

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

    @Value("${gif-random-bound}")
    private Integer gifRandomBound;

    private final Random random = new Random();
    private final GifFeignClient client;

    @Autowired
    public GifFeignClientAdapter(GifFeignClient client) {
        this.client = client;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public String getRichGifUrl() {
        return getGifUrl(richGifQuery);
    }

    @Override
    public String getBrokeGifUrl() {
        return getGifUrl(brokeGifQuery);
    }

    private String getGifUrl(String gifQuery) {
        try {
            GifClientParamsMap params = applicationContext.getBean("gifClientParamsMap", GifClientParamsMap.class);
            params.setOffset(Integer.toString(random.nextInt(gifRandomBound)));
            params.setQuery(gifQuery);
            Map<String, Object> fullRespondJsonMap = client.findGif(searchEndpoint, params);
            String gifUrl = extractOriginalGifUrl(fullRespondJsonMap);
            return gifUrl;
        } catch (Exception e) {
            throw new WebClientLayerException(e.getMessage());
        }
    }

    private String extractOriginalGifUrl(Map<String, Object> fullRespondJsonMap) {
        ArrayList<Object> dataList;
        dataList = (ArrayList<Object>) fullRespondJsonMap.get(gifDataKey);
        Map<String, Object> gifMap = (Map<String, Object>) dataList.get(gifJsonPosition);
        return (String) gifMap.get(gifUrlKey);
    }

}
