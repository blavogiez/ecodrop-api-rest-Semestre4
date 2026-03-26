package utils;

import java.io.IOException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String auth = req.getHeader("Authorization");

        // par sécurité on enleve tout attribut user antérieur !
        req.removeAttribute("user");

        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                String token = auth.substring(7);
                // ci-dessous la méthode lance une exception si le jwt est incorrect
                Claims claims = JwtManager.decodeJWT(token);

                // c'est valide !
                // on stocke le login (issuer dans JwtManager) dans la requête
                System.out.println("Login valide pour : " + claims.getIssuer());
                req.setAttribute("user", claims.getIssuer());
            } catch (Exception ignored) {
                // Token invalide ou expiré : on ne met pas l'attribut "user"
                // ; donc pour vérifier si qqun n'a pas de token on fait user==null
            }
        }
        chain.doFilter(request, response);
    }
}
