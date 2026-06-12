package controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.lecture.CreateLectureAssignmentController;
import controller.lecture.CreateLectureController;
import controller.lecture.CreateLectureEnrollmentController;
import controller.lecture.CreateLectureNoticeController;
import controller.lecture.CreateLectureReviewController;
import controller.lecture.CreateLectureScheduleController;
import controller.lecture.UpdateLectureController;
import controller.lecture.ViewLectureAssignmentsController;
import controller.lecture.ViewLectureController;
import controller.lecture.ViewLectureNoticeListController;
import controller.lecture.ViewMyLectureController;
import controller.lecture.ViewMyLectureListController;
import controller.lecture.ExcludingLectureAndStudyGroupController;
import controller.lecture.ToggleLectureLikeController;
import controller.main.MainController;
import controller.member.DeleteMemberController;
import controller.member.InterestCategoryController;
import controller.member.ListMemberController;
import controller.member.LoginController;
import controller.member.LogoutController;
import controller.member.RegisterMemberController;
import controller.member.RegisterStudent1Controller;
import controller.member.RegisterStudent2Controller;
import controller.member.RegisterStudent3Controller;
import controller.member.RegisterStudentController;
import controller.member.RegisterTeacherController;
import controller.member.UpdateMemberController;
import controller.member.ViewUserController;
import controller.studyGroup.AcceptedRequestController;
import controller.studyGroup.CreateStudyReviewController;
import controller.studyGroup.DeleteRequestController;
import controller.studyGroup.StudyGroupJoinRequestController;
import controller.studyGroup.ToggleStudyGroupLikeController;
import controller.studyGroup.ViewMyStudyListController;
import controller.studyGroup.ViewStudyAssignmentsController;
import controller.studyGroup.ViewStudyGroupController;
import controller.studyGroup.ViewStudyNoticeListController;
import controller.studyGroup.ViewStudyRequestListController;
import controller.mypage.DeleteAccountController;
import controller.mypage.EditController;
import controller.mypage.ViewLikeListController;
import controller.mypage.ViewMyInfoController;
import controller.mypage.ViewMyPageController;
import controller.mypage.EditMyInfoController;
import controller.study.CreateStudyAssignmentController;
import controller.study.CreateStudyController;
import controller.study.CreateStudyNoticeController;
import controller.study.CreateStudyScheduleController;
import controller.study.UpdateStudyController;
import controller.study.ViewMyStudyController;

//import controller.user.*;
//import controller.comm.*;

public class RequestMapping {



    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    // 각 요청 URI에 대한 controller 객체를 저장할 HashMap 생성
    private Map<String, Controller> mappings = new HashMap<>();

    public void initMapping() {
        // 각 URI에 대응되는 controller 객체를 생성 및 저장
        // 로그인/로그아웃 요청
        mappings.put("/", new ForwardController("index.jsp"));
        mappings.put("/member/login/form", new ForwardController("/member/loginForm.jsp"));
        mappings.put("/member/login", new LoginController());
        mappings.put("/member/logout", new LogoutController());

        // 메인 화면
        mappings.put("/main/main", new MainController());

        // 회원가입 요청
		  mappings.put("/member/register/form", new RegisterMemberController());
		  mappings.put("/teacher/register", new RegisterTeacherController());
		  mappings.put("/student/register1", new RegisterStudent1Controller());
		  mappings.put("/student/register2", new RegisterStudent2Controller());
		  mappings.put("/student/register3", new InterestCategoryController());
		  mappings.put("/student/register4", new RegisterStudent3Controller());
		  mappings.put("/student/register", new RegisterStudentController());

        // 마이페이지 내 정보 보기
        mappings.put("/mypage/myInfo", new ViewMyInfoController());

        // 마이페이지 탈퇴하기
        mappings.put("/mypage/deleteConfirm", new ForwardController("/mypage/deleteConfirm.jsp"));
        mappings.put("/mypage/deleteAccount", new DeleteAccountController());
        
        //마이페이지 내 정보 수정하기
        mappings.put("/mypage/editMyInfo", new EditController());
        mappings.put("/mypage/edit", new EditMyInfoController());

        // 사용자 수정
        mappings.put("/member/list", new ListMemberController());
        mappings.put("/member/view", new ViewUserController());
        mappings.put("/member/update", new UpdateMemberController());
        mappings.put("/member/delete", new DeleteMemberController());


        

        // 강의 등록 : get->page 띄우기 post:등록 요청
        mappings.put("/lecture/create", new CreateLectureController());

        // 강의 수정: get->page 띄우기 post:등록 요청
        mappings.put("/lecture/update", new UpdateLectureController());

        // 스터디 등록
        mappings.put("/study/create", new CreateStudyController());

        // 스터디 수정
        mappings.put("/study/update", new UpdateStudyController());

      		//내 스터디 상세보기
//		mappings.put("/study/selectDate", new ViewMyStudyController());
		mappings.put("/mystudy/view", new ViewMyStudyController());
      
		//스터디 일정(특정),공지,과제 추가
        mappings.put("/study/addSchedule", new CreateStudyScheduleController());
        mappings.put("/study/addNotice", new CreateStudyNoticeController());
        mappings.put("/study/addAssignment", new CreateStudyAssignmentController());

        mappings.put("/study/listNotice", new ForwardController("/study/listNotice.jsp"));
        mappings.put("/study/listAssignment", new ForwardController("/study/listAssignment.jsp"));

        //내 강의 상세보기
		mappings.put("/mylecture/view", new ViewMyLectureController());

		//스터디 일정(특정),공지,과제 추가
        mappings.put("/lecture/addSchedule", new CreateLectureScheduleController());
        mappings.put("/lecture/addNotice", new CreateLectureNoticeController());
        mappings.put("/lecture/addAssignment", new CreateLectureAssignmentController());
		
		
        // 강의 신청 페이지
        mappings.put("/lecture/over-view", new ViewLectureController());
        mappings.put("/lecture/like", new ToggleLectureLikeController());

        // 스터디그룹 요청 페이지
        mappings.put("/study/over-view", new ViewStudyGroupController());
        mappings.put("/studyGroup/like", new ToggleStudyGroupLikeController());

        // 강의,스터디 신청
        mappings.put("/registration", new ExcludingLectureAndStudyGroupController());
        mappings.put("/registration/search", new ExcludingLectureAndStudyGroupController());
        

        // 내 스터디그룹 리스트 보기
        mappings.put("/study/list", new ViewMyStudyListController());
        mappings.put("/lecture/list", new ViewMyLectureListController());
        
        mappings.put("/mypage/like-list", new ViewLikeListController());
        
        //강의 리뷰 생성
        mappings.put("/lecture/createReview", new CreateLectureReviewController());
        mappings.put("/study/createReview", new CreateStudyReviewController());
        
        //스터디 그룹 가입 요청
        mappings.put("/studyGroup/join-request", new StudyGroupJoinRequestController());

        //스터디 그룹 가입 요청 리스트
        mappings.put("/study/requests", new ViewStudyRequestListController());
        
        //스터디 그룹 가입 요청 수락
        mappings.put("/studyGroup/accepted-request", new AcceptedRequestController());
        
        mappings.put("/studyGroup/delete-request", new DeleteRequestController());
        
        //강의 수강 신청
        mappings.put("/lecture/join", new CreateLectureEnrollmentController());
       
       //마이페이지 
        mappings.put("/mypage", new ViewMyPageController());

        
        logger.info("Mappings initialized: {}", mappings.keySet());
        logger.info("Initialized Request Mapping!");

        mappings.put("/study/listNotice", new ViewStudyNoticeListController());
        mappings.put("/study/searchListNotice", new ViewStudyNoticeListController());
        
        mappings.put("/lecture/listNotice", new ViewLectureNoticeListController());
        mappings.put("/lecture/searchListNotice", new ViewLectureNoticeListController());
        mappings.put("/lecture/listAssignment", new ViewLectureAssignmentsController());
        
        
        mappings.put("/study/listAssignment", new ViewStudyAssignmentsController());

        mappings.put("/confirmId", new ForwardController("/member/confirmId.jsp"));

        // DB(BLOB)에 저장된 이미지 서빙: /image?type=member|lecture|study&id=...
        mappings.put("/image", new ViewImageController());
    }

    public Controller findController(String uri) {
        // 주어진 URI에 대응되는 controller 객체를 찾아 반환
        return mappings.get(uri);
    }


}
