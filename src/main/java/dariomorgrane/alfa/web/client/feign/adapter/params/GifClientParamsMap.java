package dariomorgrane.alfa.web.client.feign.adapter.params;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GifClientParamsMap extends HashMap<String, String> {

    @Value("${query-request-param}")
    private String queryRequestParam;

    @Value("${offset-request-param}")
    String offsetRequestParam;

    public GifClientParamsMap(@Value("${gif-api-key-request-param}") String gifApiKeyRequestParam,
                              @Value("${gif-api-key}") String gifApiKey,
                              @Value("${limit-request-param}") String limitRequestParam,
                              @Value("${limit}") String limit,
                              @Value("${language-request-param}") String languageRequestParam,
                              @Value("${language}") String language) {
        super();
        this.put(gifApiKeyRequestParam, gifApiKey);
        this.put(limitRequestParam, limit);
        this.put(languageRequestParam, language);
    }

    public void setQuery(String query) {
        this.put(queryRequestParam, query);
    }

    public void setOffset(String offset) {
        this.put(offsetRequestParam, offset);
    }

}
