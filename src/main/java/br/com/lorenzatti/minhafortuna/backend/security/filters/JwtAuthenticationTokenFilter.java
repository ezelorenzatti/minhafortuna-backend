package br.com.lorenzatti.minhafortuna.backend.security.filters;


import br.com.lorenzatti.minhafortuna.backend.security.JwtUserFactory;
import br.com.lorenzatti.minhafortuna.backend.security.utils.JwtTokenUtil;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import br.com.lorenzatti.minhafortuna.backend.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private UserService usuarioService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private List<String> freeUris = Arrays.asList("/auth/signin", "/auth/signup");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!freeUris.contains(request.getRequestURI())) {
            String token = request.getHeader(AUTH_HEADER);
            if (token != null && token.startsWith(BEARER_PREFIX)) {
                token = token.substring(7);
            }
            String email = jwtTokenUtil.getUsernameFromToken(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Optional<User> user = usuarioService.findByEmail(email);
                if (user.isPresent()) {
                    UserDetails userDetails = JwtUserFactory.create(user.get());
                    if (jwtTokenUtil.validateToken(token)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    response.sendError(401);
                    return;
                }
            } else {
                response.sendError(401);
                return;
            }
        }
        chain.doFilter(request, response);
    }

}
