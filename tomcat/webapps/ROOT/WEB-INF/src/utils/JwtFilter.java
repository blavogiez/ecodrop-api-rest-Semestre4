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

// Middleware ; toutes les requêtes passent par ce filtre pour déterminer le login de l'utilisateur en déchiffrant son JWT
@WebFilter("/*")
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String auth = req.getHeader("Authorization");

        // par sécurité on enleve tout attribut login antérieur !
        req.removeAttribute("login");

        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                String token = auth.substring(7);
                // ci-dessous la méthode lance une exception si le jwt est incorrect
                Claims claims = JwtManager.decodeJWT(token);

                // c'est valide !
                // on stocke le login (issuer dans JwtManager) dans la requête
                System.out.println("Login valide pour : " + claims.getIssuer());
                req.setAttribute("login", claims.getIssuer());
            } catch (Exception ignored) {
                // Token invalide ou expiré : on ne met pas l'attribut "login"
                // ; donc pour vérifier si qqun n'a pas de token on fait login==null
                // ; puisque par sécurité on l'a effacé avant, si quelqu'un a un login par la
                // suite de la requête, il est certain que c'est le bon (vérifié par serveur)
            }
        }
        chain.doFilter(request, response);
    }
}
