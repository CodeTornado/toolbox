package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void restTemplateOpenPage() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://blog.csdn.net/cd826_dong/article/details/76652015";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        System.out.println(response.getBody());
    }

    @Test
    void jsoupPageParsing() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://dict.youdao.com/example/mdia/hello/#keyfrom=dict.main.moremedia";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        System.out.println("---------------------华丽的分割线A-------");
        System.out.println(response.getBody());
        System.out.println("---------------------华丽的分割线B-------");
        Document document = Jsoup.parse(response.getBody());


        Elements pElements = document.getElementById("originalSound").select("li p:eq(0)");

        List list = new ArrayList<Map>();

        for (Element pEle : pElements) {

            Map map = new HashMap<String, String>();
            Elements aEles = pEle.getElementsByTag("a");
            if (aEles != null && aEles.size() > 0) {
                Element aEle = aEles.get(0);
                String voiceUrlStr = aEle.attr("data-rel");

                map.put("voice", voiceUrlStr);
                aEles.remove();
            }
            String exampleSentenceStr = pEle.text();

            map.put("example_sentence", exampleSentenceStr);
            list.add(map);
        }

        System.out.println("list.toString() = " + list.toString());
    }
}