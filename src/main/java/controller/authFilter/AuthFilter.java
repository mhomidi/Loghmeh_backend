package controller.authFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import services.Authentication;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName="authFilter", urlPatterns="/*")
public class AuthFilter implements Filter {
    public AuthFilter(){
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("JWT  HTTP Request: ");
        try {
            String uri = ((HttpServletRequest)servletRequest).getRequestURI();
            if(uri.contains("login") || uri.contains("googleLogin") || uri.contains("signup")){
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
            JWTVerifier verifier = JWT.require(Authentication.algorithm)
                    .withIssuer("loghmeh.com")
                    .build();
            String header = ((HttpServletRequest)servletRequest).getHeader("Authorization");
            if(header != null) {
                header = header.substring(7); // - Bearer
                DecodedJWT jwt;
                jwt = verifier.verify(header);
                String id =  jwt.getClaim("id").asString();
                System.out.println("id in jwt filter: "  + id);
                servletRequest.setAttribute("id", id);
                chain.doFilter(servletRequest, servletResponse);
            }
            else
                ((HttpServletResponse)servletResponse).setStatus(HttpStatus.UNAUTHORIZED.value());
        } catch (JWTVerificationException exception){
            ((HttpServletResponse)servletResponse).setStatus(HttpStatus.FORBIDDEN.value());
        }

    }
    public void init(FilterConfig fConfig) throws ServletException {
    }

}