package utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Role;
import model.dao.AuthDAO;
import model.dao.AuthDAOPostgres;

// Classe utilitaire qui sert à extraire le chemin voulu lors de la requête, puis à l'accéder
// De ce fait, on simplifie beaucoup le code en l'utilisant dans les API Rest plutôt que de faire le même traitement à chaque fois
public class RequestContext {
    private final PrintWriter out;
    private final ObjectMapper mapper;
    /** Segments de chemin URL sans le "" initial (ex: "/foo/42" → ["foo", "42"]) */
    private final String[] segments;
    private final HttpServletRequest req;
    private final AuthDAO dao = new AuthDAOPostgres();

    public RequestContext(HttpServletRequest req, HttpServletResponse res) throws IOException {
        this.req = req;
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

    public String getLogin() {
        return (String) req.getAttribute("login");
    }

    public boolean isAuthenticated() {
        return getLogin() != null;
    }

    public Role getUserRole() {
        if (isAuthenticated()) {
            return dao.getRole(getLogin());
        }
        return Role.valueOf("UNKNOWN");
    }

    public boolean hasArguments() {
        return this.segments.length != 0;
    }

    public void printValueAsString(Object value) throws JsonProcessingException {
        this.out.println(this.mapper.writeValueAsString(value));
    }

    public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException {
        return this.mapper.readValue(content, valueType);
    }

    public boolean doesNotHaveExactlyXArguments(int x) {
        return this.segments.length != x;
    }

    public String getArgument(int index) {
        return this.segments[index];
    }

    public ObjectReader readerForUpdating(Object valueToUpdate) {
        return this.mapper.readerForUpdating(valueToUpdate);
    }
    
    public boolean hasNoToken() {
        return getLogin() == null;
    }

    public void print(String... args) {
        for (String arg : args) {
            this.out.print(arg);
        }
    }
}
