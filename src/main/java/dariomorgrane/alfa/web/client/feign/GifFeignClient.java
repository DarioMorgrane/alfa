package dariomorgrane.alfa.web.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "GifClient", url = "${gif-url}")
public interface GifFeignClient {

    @GetMapping("/{searchEndpoint}")
    Map<String, Object> findGif(@PathVariable("searchEndpoint") String searchEndpoint,
                                @RequestParam Map<String, String> params);

}
