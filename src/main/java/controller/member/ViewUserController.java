package controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import model.domain.member.Member;
import model.service.member.MemberManager;
import model.service.member.MemberNotFoundException;


public class ViewUserController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 로그인 여부 확인
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}

		MemberManager manager = MemberManager.getInstance();
		String userId = request.getParameter("id");
		if (userId == null || userId.isBlank()) {
			userId = MemberSessionUtils.getLoginMemberId(request.getSession());
		}

		// 본인 또는 관리자만 타인 상세 조회 (IDOR 방지)
		String loginId = MemberSessionUtils.getLoginMemberId(request.getSession());
		if (!userId.equals(loginId) && !AuthorizationUtils.isAdmin(loginId)) {
			request.getSession().setAttribute("flashError", "본인 정보만 조회할 수 있습니다.");
			return "redirect:/mypage";
		}

		Member member = null;
		try {
			member = manager.findMember(userId); // 사용자 정보 검색
		} catch (MemberNotFoundException e) {
			return "redirect:/member/list";
		}

		request.setAttribute("member", member); // 사용자 정보 저장
		request.setAttribute("curUserId", MemberSessionUtils.getLoginMemberId(request.getSession()));
		return "/member/view.jsp"; // 사용자 보기 화면으로 이동
	}
}
