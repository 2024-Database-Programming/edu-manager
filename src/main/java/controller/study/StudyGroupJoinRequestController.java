package controller.study;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.study.StudyGroup;
import model.service.study.StudyManager;
import model.service.study.StudyGroupManager;

public class StudyGroupJoinRequestController implements Controller {

    private final StudyGroupManager studyGroupManager;

    public StudyGroupJoinRequestController() {
        this.studyGroupManager = StudyGroupManager.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form";
        }

        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        Long groupId = Long.parseLong(request.getParameter("groupId"));
        StudyGroup studyGroup = studyGroupManager.findStudyGroupById(groupId);
        String requestStatus = studyGroupManager.getStatusByMemberIdAndGroupId(memberId, groupId);

        if (studyGroup == null
                || AuthorizationUtils.canViewStudy(studyGroupManager, memberId, groupId)
                || requestStatus != null
                || StudyManager.getInstance().findStudyMembers(groupId.intValue()).size() + 1 >= studyGroup.getCapacity()) {
            return "redirect:/study/over-view?groupId=" + groupId;
        }

        //가입 요청 생성
        studyGroupManager.createApplication(memberId, groupId);

        // 상태 변경 후 해당 페이지로 리다이렉트
        return "redirect:/study/over-view?groupId=" + groupId; // 페이지 새로고침
    }
}
