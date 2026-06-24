package controller.study;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.study.StudyGroup;
import model.service.study.StudyGroupManager;

public class ViewMyStudyListController implements Controller{

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 여부 확인
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form"; // login form 요청으로 redirect
        }

      String stuId = MemberSessionUtils.getLoginMemberId(request.getSession());
        
      StudyGroupManager studyGroupManager = StudyGroupManager.getInstance();
      
      List<StudyGroup> studyGroupListByLeader = studyGroupManager.StudyGroupListByLeader(stuId);

      List<StudyGroup> studyGroupListByMember = studyGroupManager.StudyGroupListByMember(stuId);

  
      for (StudyGroup studyGroup : studyGroupListByLeader) {
      }
      
      for (StudyGroup studyGroup : studyGroupListByMember) {
      }
      
      // 스터디 목록을 request 객체에 저장

      request.setAttribute("studyGroupListByLeader", studyGroupListByLeader);
      request.setAttribute("studyGroupListByMember", studyGroupListByMember);

      return "/study/my_study_list.jsp";
        


}
}
