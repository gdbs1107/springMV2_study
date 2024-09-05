package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {


    private static final String[] whitelist = {"/","/members/add","/login","/css/*"};
    //인터페이스 메서드에 default가 들어가면 필수 구현이 아님

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest= (HttpServletRequest) request;
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            log.info("인증체크 필터 시작 {}", requestURI);

            if (isLoginCheckPath(requestURI)){
                log.info("인증체크 로직 실행{}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){

                    //로그인이 안된 사용자가 접근 후 로그인이 완료되면 접근했던 페이지로 리다이렉트
                    log.info("미인증 사용자 요청 {}", requestURI);
                    httpResponse.sendRedirect("/login?redirectURL="+requestURI);
                    return; //여기가 중요. 미인증 이용자에게 서블릿 호출을 제한
                }
            }
            filterChain.doFilter(request,response);
        }catch (Exception e){
            throw e;
        }finally {
            log.info("인증필터 체크 종료 {}", requestURI);
        }
    }



    private Boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist,requestURI);
    }
}
