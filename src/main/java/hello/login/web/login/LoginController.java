package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm from){
        return "login/loginForm";
    }

/*    @PostMapping("/login")*/
    private String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                         HttpServletResponse response){

        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member login = loginService.login(form.getLoginId(), form.getPassword());
        if (login == null){
            bindingResult.reject("login Fail","아이디또는 비번 ㅏㅈㄹ못침");
            return "login/loginForm";
        }


        //쿠키에 만료시간을 지정하지 않으면 -> 세션쿠키로 설정 -> 브라우저 종료시 쿠키 삭제
        Cookie idCookie = new Cookie("memberId", String.valueOf(login.getId()));
        response.addCookie(idCookie);


        return "redirect:/";
    }



    /*@PostMapping("/login")*/
    private String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                         @RequestParam(defaultValue = "/") String redirectURL,
                         HttpServletResponse response){

        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member login = loginService.login(form.getLoginId(), form.getPassword());
        if (login == null){
            bindingResult.reject("login Fail","아이디또는 비번 ㅏㅈㄹ못침");
            return "login/loginForm";
        }

        //세션관리자를 통한 세션 생성
        sessionManager.createSession(login,response);


        return "redirect:"+redirectURL;
    }

/*    @PostMapping("/login")*/
    private String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                           HttpServletRequest request){

        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member login = loginService.login(form.getLoginId(), form.getPassword());
        if (login == null){
            bindingResult.reject("login Fail","아이디또는 비번 ㅏㅈㄹ못침");
            return "login/loginForm";
        }

        //세션이 있으면 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,login);

        return "redirect:/";
    }


    @PostMapping("/login")
    private String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                           @RequestParam(defaultValue = "/") String redirectURL,
                           HttpServletRequest request){

        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member login = loginService.login(form.getLoginId(), form.getPassword());
        if (login == null){
            bindingResult.reject("login Fail","아이디또는 비번 ㅏㅈㄹ못침");
            return "login/loginForm";
        }

        //세션이 있으면 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,login);

        return "redirect:" + redirectURL;
    }

   /* @PostMapping("/logout")*/
    public String logout(HttpServletResponse response){

        expireCookie(response,"memberId");
        return "redirect:/";
    }

/*    @PostMapping("/logout")*/
    public String logoutV2(HttpServletRequest request){

        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request){
        //true로 설정시 없으면 세션을 만들어버리기 때문에 false지정
        HttpSession session = request.getSession(false);

        if (session != null){
            session.invalidate();
        }

        return "redirect:/";
    }



    private static void expireCookie(HttpServletResponse response,String cookieName) {
        Cookie cook = new Cookie(cookieName, null);
        cook.setMaxAge(0);
        response.addCookie(cook);
    }

}
