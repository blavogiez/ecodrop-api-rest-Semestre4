package utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Classe utilitaire qui sert à extraire le chemin voulu lors de la requête, puis à l'accéder
// De ce fait, on simplifie beaucoup le code en l'utilisant dans les API Rest plutôt que de faire le même traitement à chaque fois
public class RequestContext {
    private final PrintWriter out;
    private final ObjectMapper mapper;
    /** Segments de chemin URL sans le "" initial (ex: "/foo/42" → ["foo", "42"]) */
    private final String[] segments;

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

    public boolean hasArguments(){
        return this.segments.length != 0;
    }

    public void printValueAsString(Object value) throws JsonProcessingException {
        this.out.println(this.mapper.writeValueAsString(value));
    }

    public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException {
        return this.mapper.readValue(content, valueType);
    }

    public boolean doesNotHaveExactlyXArguments(int x){
        return this.segments.length != x;
    }

    public String getArgument(int index){
        return this.segments[index];
    }

    public ObjectReader readerForUpdating(Object valueToUpdate){
        return this.mapper.readerForUpdating(valueToUpdate);
    }
}
