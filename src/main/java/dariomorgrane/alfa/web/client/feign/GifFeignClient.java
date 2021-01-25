package dariomorgrane.alfa.web.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "GifClient", url = "${gif-url}")
public interface GifFeignClient {

    @GetMapping
    Map<String, Object> findGif(@RequestParam("api_key") String apiKey,
                                @RequestParam("q") String query,
                                @RequestParam("limit") Integer limit,
                                @RequestParam("offset") Integer offset,
                                @RequestParam("lang") String lang);

}
