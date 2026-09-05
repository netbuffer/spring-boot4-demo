package cn.netbuffer.spring.boot4.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo/text-block")
public class TextBlockController {

    @GetMapping("/basic")
    public Map<String, Object> basic() {
        String classic = "line1\nline2\nline3\n";
        String textBlock = """
                line1
                line2
                line3
                """;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("classic", classic);
        result.put("textBlock", textBlock);
        result.put("equal", classic.equals(textBlock));
        return result;
    }

    @GetMapping("/html")
    public String html(@RequestParam(defaultValue = "Spring Boot 4") String title) {
        return """
                <html>
                  <body>
                    <h1>%s</h1>
                    <p>Text Blocks demo</p>
                  </body>
                </html>
                """.formatted(title);
    }

    @GetMapping("/json")
    public String json(
            @RequestParam(defaultValue = "didi") String name,
            @RequestParam(defaultValue = "18") int age) {
        return """
                {
                    "user": "%s",
                    "age": %d
                }
                """.formatted(name, age);
    }

    @GetMapping("/sql")
    public String sql(@RequestParam(defaultValue = "spring") String keyword) {
        return """
                SELECT id, name, created_at
                FROM demo_user
                WHERE name LIKE '%%%s%%'
                ORDER BY id DESC
                """.formatted(keyword);
    }

    @GetMapping("/escape")
    public Map<String, String> escape() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("withQuotes", """
                {
                    "msg": "say \\"hello\\""
                }
                """);
        result.put("trailingSpace", """
                keep trailing space:\s
                next line
                """);
        return result;
    }

}
