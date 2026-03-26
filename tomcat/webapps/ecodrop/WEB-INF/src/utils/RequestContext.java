package utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Classe utilitaire qui sert à extraire le chemin voulu lors de la requête, puis à l'accéder
// De ce fait on simplifie beaucoup le code en l'utilisant dans les API Rest plutôt que de faire le même traitement à chaque fois
public class RequestContext {
    public final PrintWriter out;
    public final ObjectMapper mapper;
    /** Segments de chemin URL sans le "" initial (ex: "/foo/42" → ["foo", "42"]) */
    public final String[] segments;

    public RequestContext(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        this.out = res.getWriter();
        this.mapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            this.segments = new String[0];
        } else {
            String[] parts = info.split("/");
            this.segments = Arrays.copyOfRange(parts, 1, parts.length);
        }
    }
}
