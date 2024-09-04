package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void SessionTest(){


        //가짜 리스폰스를 생성해주는 mock
        MockHttpServletResponse response=new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member,response);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);


        sessionManager.expire(request);
        Object expire = sessionManager.getSession(request);
        Assertions.assertThat(expire).isNull();


    }
}
