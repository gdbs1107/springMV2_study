package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("log filter doFilter");

        //일반적인 Servlet은 Http를 사용하기에 부족함
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        //로그 짝 맞춰서 보여주기
        String requestURI = httpRequest.getRequestURI();
        String uuid= UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}] URI [{}]", uuid, requestURI);
            filterChain.doFilter(request,response);

        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}] URI [{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
