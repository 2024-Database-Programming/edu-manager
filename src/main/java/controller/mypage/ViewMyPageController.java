package controller.mypage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import model.dao.member.MemberDAO;
import model.dao.member.StudentDAO;
import model.domain.member.Member;
import model.domain.member.Student;
import controller.member.MemberSessionUtils;

public class ViewMyPageController implements Controller {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 세션에서 로그인한 사용자 ID 가져오기
        String memberId = (String) request.getSession().getAttribute("id");
        System.out.println("Session memberId: " + memberId); // 확인용 로그
        if (memberId == null) {
            return "redirect:/member/login/form"; // 로그인하지 않았다면 로그인 폼으로 이동
        }

        try {
            // MemberDAO, StudentDAO 인스턴스 생성
            MemberDAO memberDAO = new MemberDAO();
            StudentDAO studentDAO = new StudentDAO();

            // 사용자 정보 조회
            Member member = memberDAO.findMember(memberId);
            boolean existStudent = false;

            if (member != null) {
                // 학생 여부 확인
                Student student = studentDAO.findStudent(memberId);
                if (student != null) {
                    existStudent = true; // 학생인 경우 true
                }

                // 사용자 정보를 JSP로 전달
                request.setAttribute("member", member);
                request.setAttribute("existStudent", existStudent);
                request.setAttribute("curUserId", member.getName()); // 사용자 이름 전달

                return "/mypage/myPage.jsp"; // 마이 페이지로 이동
            } else {
                request.setAttribute("error", "사용자 정보를 찾을 수 없습니다.");
                return "/main/main.jsp"; // 에러 발생 시 메인 페이지로 이동
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "마이 페이지를 불러오는 도중 오류가 발생했습니다.");
            return "/main/main.jsp"; // 에러 발생 시 메인 페이지로 이동
        }
    }
}
