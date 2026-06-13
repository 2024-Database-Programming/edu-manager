package controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Controller;
import model.service.member.MemberManager;
import controller.member.MemberSessionUtils;

public class LoginController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");

		try {
			// 모델에 로그인 처리를 위임
			MemberManager manager = MemberManager.getInstance();
			manager.login(id, pwd);

			// 세션에 사용자 아이디 저장
			HttpSession session = request.getSession();
			session.setAttribute(MemberSessionUtils.USER_SESSION_KEY, id);
			// 현재 로그인 한 사용자
			request.setAttribute("curUserId", manager.findName(id));
			session.setAttribute("curUserId", manager.findName(id));

			return "redirect:/mypage";
		} catch (Exception e) {
			// PRG: 실패 메시지를 세션에 담고 redirect → 새로고침 시 재전송/재노출 방지
			request.getSession().setAttribute("loginError", e.getMessage());
			return "redirect:/member/login/form";
		}
	}
}
