package controller.study;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.study.StudyGroup;
import model.domain.study.StudyGroupApplication;
import model.service.study.StudyGroupManager;

public class DeleteRequestController implements Controller {

    private final StudyGroupManager studyGroupManager;

    public DeleteRequestController() {
        this.studyGroupManager = StudyGroupManager.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form";
        }

        Long applicationId = Long.parseLong(request.getParameter("studyGroupApplicationId"));
        Long groupId = Long.parseLong(request.getParameter("groupId"));
        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());

        StudyGroupApplication application = studyGroupManager.findApplicationById(applicationId);
        StudyGroup studyGroup = studyGroupManager.findStudyGroupById(groupId);
        boolean invalidRequest = application == null
                || application.getStudyGroupId() != groupId
                || studyGroup == null
                || !AuthorizationUtils.canManageStudy(memberId, studyGroup);

        if (invalidRequest) {
            return "redirect:/study/requests?groupId=" + groupId;
        }

        studyGroupManager.deleteApplication(applicationId);


        // 상태 변경 후 해당 페이지로 리다이렉트
        return "redirect:/study/requests?groupId=" + groupId; // 페이지 새로고침
    }
}
