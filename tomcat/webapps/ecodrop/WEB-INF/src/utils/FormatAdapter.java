package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import jakarta.servlet.http.HttpServletRequest;

public class FormatAdapter {
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    public static final XmlMapper XML_MAPPER = new XmlMapper();

    private static boolean isXml(HttpServletRequest req) {
        String ct = req.getContentType();
        return ct != null && ct.contains("application/xml");
    }

    public static ObjectMapper mapperFor(HttpServletRequest req) {
        return isXml(req) ? XML_MAPPER : JSON_MAPPER;
    }

    public static String contentTypeFor(HttpServletRequest req) {
        return isXml(req) ? "application/xml;charset=UTF-8" : "application/json;charset=UTF-8";
    }
}