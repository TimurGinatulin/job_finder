package ru.geekbrains.job_finder.cor_lib.filters;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.geekbrains.job_finder.cor_lib.interfacces.ITokenService;
import ru.geekbrains.job_finder.cor_lib.models.UserInfo;
import ru.geekbrains.job_finder.cor_lib.services.RedisService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final ITokenService tokenService;
    private final RedisService redisService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeaderIsInvalid(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = createToken(authorizationHeader);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        UserInfo userInfo = tokenService.parseToken(token);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (userInfo.getRole() != null && !userInfo.getRole().isEmpty())
            userInfo.getRole().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
    }

    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
        return authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ") ||
                redisService.checkTokenInRedis(authorizationHeader.replace("Bearer ", ""));
    }
}
