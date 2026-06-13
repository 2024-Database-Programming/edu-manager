package controller.studyGroup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.studyGroup.StudyGroupApplication;
import model.service.StudyGroupManager;

public class ViewStudyRequestListController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 여부 확인
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form"; // 로그인하지 않았다면 로그인 폼으로 리다이렉트
        }

        // PathVariable을 사용하여 lectureId를 URL 경로에서 받도록 수정
        Long groupId = Long.parseLong(request.getParameter("groupId"));
        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        // LectureManager를 통해 강의 정보 조회
        StudyGroupManager manager = StudyGroupManager.getInstance();

        if (!AuthorizationUtils.canManageStudy(manager, memberId, groupId)) {
            return "redirect:/study/over-view?groupId=" + groupId;
        }
	      

        List<StudyGroupApplication> groupRequestList = manager.getStudyRequestList(groupId);

        // 로그인한 사용자 ID를 request에 저장     
        request.setAttribute("requestList", groupRequestList);
        // 강의 상세 페이지로 이동
        return "/study/study_request.jsp";
    }
}
        
        
