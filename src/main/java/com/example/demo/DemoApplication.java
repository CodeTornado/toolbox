package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RequestMapping("index")
    public String index() {
        return "random_pw";
    }

    @RequestMapping("/")
    public String random_pw() {
        return "random_pw";
    }

    @RequestMapping("word_translation")
    public String wordTranslation() {
        return "word_translation";
    }

    @RequestMapping("wordTranslationData")
    @ResponseBody
    public List wordTranslationData(@RequestParam("word") String word) {
        if (word == null || "".equals(word)) {
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://dict.youdao.com/example/mdia/" + word + "/#keyfrom=dict.main.moremedia";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);

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
        return list;
    }
}
