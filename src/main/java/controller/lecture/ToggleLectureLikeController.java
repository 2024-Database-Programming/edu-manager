package controller.lecture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.service.lecture.LectureManager;


public class ToggleLectureLikeController implements Controller {

    private final LectureManager lectureManager;

    public ToggleLectureLikeController() {
        this.lectureManager = LectureManager.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form";
        }

        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        Long lectureId = Long.parseLong(request.getParameter("lectureId"));

        // 좋아요 상태 토글
        lectureManager.toggleLectureLike(memberId, lectureId);

        // 상태 변경 후 해당 페이지로 리다이렉트
        return "redirect:/lecture/over-view?lectureId=" + lectureId; // 페이지 새로고침
    }
}
