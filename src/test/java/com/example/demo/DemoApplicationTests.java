package com.example.demo;

import com.example.demo.dao.CommentDto;
import com.example.demo.dao.FormatDictDto;
import com.example.demo.dao.RelationDto;
import com.example.demo.dao.TextDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.FormatDict;
import com.example.demo.entity.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    //@Test
    void restTemplateOpenPage() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://blog.csdn.net/cd826_dong/article/details/76652015";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        System.out.println(response.getBody());
    }

    //@Test
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

    @Autowired
    CommentDto commentDto;

    //        @Test
    void jdbcTest() {
        Comment comment = new Comment();
        comment.setTextMainId(2);
//        System.out.println("commentDto.AddComment(comment) = " + commentDto.addComment(comment));
        commentDto.selectByTextMainId(2);
        int a = 1;
    }

    @Autowired
    FormatDictDto formatDto;

    //    @Test
    void jdbcSelectTest() {
//        formatDto.selectAll();
        FormatDict formatDict = new FormatDict();
        formatDict.setId(1);
        List<FormatDict> formatDicts = formatDto.selectById(formatDict);
        for (FormatDict o :
                formatDicts) {
            System.out.println("o.getName() = " + o.getName());
        }
    }

    @Autowired
    TextDto textDto;
    @Autowired
    RelationDto relationDto;

//        @Test
    void jdbcSelectRelationDtoTest() {
        List<Text> texts = textDto.selectByIds("1,3");

        for (int i = 0; i < texts.size(); i++) {
            System.out.println("texts.get(" + i + ") = " + texts.get(i));
        }
    }
}