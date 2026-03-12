package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import jakarta.servlet.http.HttpServletRequest;

public class FormatAdapter {
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    public static final XmlMapper XML_MAPPER = new XmlMapper();

    public static ObjectMapper mapperFor(HttpServletRequest req) {
        String ct = req.getContentType();
        if (ct != null && ct.contains("application/xml")) {
            return XML_MAPPER;
        }
        return JSON_MAPPER;
    }

    public static String wrapper(Format format, String... toWrap){
        StringBuilder sb = new StringBuilder();
        String open = format.open;
        String close = format.close;
        sb.append(open);
        for (String element : toWrap){
            sb.append(element);
        }
        sb.append(close);
        return sb.toString();
    }

    // public static String toUniversalJSON(String input) throws Exception {
    // if (input == null || input.isBlank())
    // return input;

    // String trimmed = input.stripLeading();

    // if (trimmed.startsWith("<")) {
    // JsonNode node = XML_MAPPER.readTree(trimmed);
    // return JSON_MAPPER.writeValueAsString(node);
    // }

    // if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
    // JSON_MAPPER.readTree(trimmed);
    // return input;
    // }

    // throw new IllegalArgumentException(
    // "Format non reconnu (ni JSON ni XML) : " + trimmed.substring(0, Math.min(20,
    // trimmed.length())));
    // }
}