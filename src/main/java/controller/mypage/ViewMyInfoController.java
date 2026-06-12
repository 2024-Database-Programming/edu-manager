package controller.mypage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import model.dao.member.MemberDAO;
import model.domain.member.Member;
import controller.member.MemberSessionUtils;


public class ViewMyInfoController implements Controller {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String memberId = (String) request.getSession().getAttribute("id");
    	System.out.println("Session memberId: " + memberId); // 확인용 로그
    	if (memberId == null) {
    	    return "redirect:/member/login/form";
    	}

        try {
            // MemberDAO 인스턴스 생성
            MemberDAO memberDAO = new MemberDAO();
            // 사용자 정보 조회
            Member member = memberDAO.findMember(memberId);

            if (member != null) {
                // 사용자 정보를 JSP로 전달
                request.setAttribute("member", member);
                return "/mypage/myInfo.jsp"; // 내 정보 페이지로 이동
            } else {
                request.setAttribute("error", "사용자 정보를 찾을 수 없습니다.");
                return "/main/main.jsp"; // 에러 발생 시 메인 페이지로 이동
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "내 정보를 불러오는 도중 오류가 발생했습니다.");
            return "/main/main.jsp"; // 에러 발생 시 메인 페이지로 이동
        }
    }
}
