package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다";
        }


        //세션 데이터들 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name->log.info("{}={}", name, session.getAttribute(name)));

        log.info("sessionId={}",session.getId());
        log.info("sessionId={}",session.getMaxInactiveInterval());
        log.info("sessionId={}",new Date(session.getCreationTime()));
        log.info("sessionId={}",new Date(session.getLastAccessedTime()));
        log.info("sessionId={}",session.isNew());

        return "세션정보 출력";
    }
}
