package controller.studyGroup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.service.StudyGroupManager;

public class ToggleStudyGroupLikeController implements Controller {

    private final StudyGroupManager studyGroupManager;

    public ToggleStudyGroupLikeController() {
        this.studyGroupManager = StudyGroupManager.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form";
        }

        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        Long groupId = Long.parseLong(request.getParameter("groupId"));

        // 좋아요 상태 토글
        studyGroupManager.toggleStudyGroupLike(memberId, groupId);

        // 상태 변경 후 해당 페이지로 리다이렉트
        return "redirect:/study/over-view?groupId=" + groupId; // 페이지 새로고침
    }
}
