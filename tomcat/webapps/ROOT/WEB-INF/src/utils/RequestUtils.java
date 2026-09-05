package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestUtils {

    public static String readBody(HttpServletRequest req) throws IOException {
        return new BufferedReader(new InputStreamReader(req.getInputStream()))
                .lines().collect(Collectors.joining());
    }

    public static int parseId(String s, HttpServletResponse res) throws IOException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return -1;
        }
    }
}
