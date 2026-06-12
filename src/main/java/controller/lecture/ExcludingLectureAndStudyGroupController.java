package controller.lecture;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.lecture.Lecture;
import model.domain.member.Teacher;
import model.domain.studyGroup.StudyGroup;
import model.service.LectureManager;
import model.service.StudyGroupManager;
import model.service.member.TeacherManager;

public class ExcludingLectureAndStudyGroupController implements Controller {
    
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 여부 확인
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form"; // login form 요청으로 redirect
        }


      String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
      String searchParam = request.getParameter("searchParam");
      
      LectureManager lectureManager = LectureManager.getInstance();
      StudyGroupManager studyGroupManager = StudyGroupManager.getInstance();
      TeacherManager teacherManager = TeacherManager.getInstance();

      
      if (searchParam != null && !searchParam.trim().isEmpty()) {
         
          List<Lecture> lectureSearchList = lectureManager.getLecturesSearch(memberId, searchParam);
          request.setAttribute("lectureList", lectureSearchList);
          
          List<StudyGroup> studyGroupSearchList = studyGroupManager.getStudyGroupsSearch(memberId, searchParam);
          
          request.setAttribute("isTeacher", teacherManager.isTeacher(memberId)); 
          request.setAttribute("studyGroupList", studyGroupSearchList);
          
          request.setAttribute("searchParam",  searchParam);
        
        
      } else {
        
          List<Lecture> lectureList = lectureManager.getLecturesExcludingStudent(memberId);
          List<StudyGroup> studyGroupList = studyGroupManager.getStudyGroupsExcludingStudent(memberId);

          request.setAttribute("isTeacher", teacherManager.isTeacher(memberId)); 
          request.setAttribute("lectureList", lectureList);
          request.setAttribute("studyGroupList", studyGroupList);
      }
    

      return "/registration/registration.jsp";

    }

}

