package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import jakarta.servlet.http.HttpServletRequest;

public class FormatAdapter {

    // Mapper dynamique selon le format de la requête (défaut JSON)
    public static ObjectMapper mapperFor(HttpServletRequest req) {
      String ct = req.getContentType();
      if (ct != null && ct.contains("application/xml")) {
          return new XmlMapper();
      }
      return new ObjectMapper(); // défaut JSON
  }
}
