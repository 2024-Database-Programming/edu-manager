package controller.mypage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.lecture.Lecture;
import model.domain.studyGroup.StudyGroup;
import model.service.LectureManager;
import model.service.StudyGroupManager;

public class ViewLikeListController implements Controller {
    
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 여부 확인
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form"; // login form 요청으로 redirect
        }

      String stuId = MemberSessionUtils.getLoginMemberId(request.getSession());
        
      LectureManager lectureManager = LectureManager.getInstance();
      StudyGroupManager studyGroupManager = StudyGroupManager.getInstance();
      
      List<Lecture> lectureList = lectureManager.LectureLikeList(stuId);;
      List<StudyGroup> studyGroupList = studyGroupManager. StudyGroupLikeList(stuId);


      System.out.println("강의 목록:");
      for (Lecture lecture : lectureList) {
          System.out.println("강의 ID: " + lecture.getLectureId() +
                             ", 강의 이름: " + lecture.getName() +
                             ", 카테고리: " + lecture.getCategory());
      }
      
      System.out.println("강의 목록:");
      for (StudyGroup studyGroup : studyGroupList) {
          System.out.println("강의 ID: " + studyGroup.getStudyGroupId() +
                             ", 강의 이름: " + studyGroup.getName() +
                             ", 카테고리: " + studyGroup.getCategory());
      }
      

      request.setAttribute("lectureList", lectureList);
      request.setAttribute("studyGroupList", studyGroupList);

      return "/mypage/likeList.jsp";
        
        

        
        
    }
   }
