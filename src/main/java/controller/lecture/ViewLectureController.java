package controller.lecture;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.lecture.Lecture;
import model.domain.lecture.LectureReview;
import model.service.lecture.LectureManager;
import model.service.member.StudentManager;


public class ViewLectureController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 여부 확인
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form"; // 로그인하지 않았다면 로그인 폼으로 리다이렉트
        }

        Long lectureId = Long.parseLong(request.getParameter("lectureId"));
        String stuId = MemberSessionUtils.getLoginMemberId(request.getSession());

       
        LectureManager lectureManager = LectureManager.getInstance();
        StudentManager studentManager = StudentManager.getInstance();
        
        // LectureManager를 통해 강의 정보 조회
        Lecture lecture = lectureManager.findLectureById(lectureId);
        if (lecture == null) { // 삭제됐거나 없는 강의 id → NPE로 500 나지 않게 안내 후 목록으로
            request.getSession().setAttribute("flashError", "존재하지 않는 강의입니다.");
            return "redirect:/registration";
        }
        int enrolledCount = lectureManager.findLectureMembers(lectureId.intValue()).size();
        long availableSeats = lecture.getCapacity() - enrolledCount;
          
        List<LectureReview> lectureReviewList = lectureManager.getReviewsByLectureId(lectureId);
        
        // 학생인지 강사인지 구분
        boolean existStudent = studentManager.existingStudent(stuId);
        request.setAttribute("existStudent", existStudent);
        
       
        boolean isLiked = lectureManager.isLikedByUser(stuId, lectureId); // 인스턴스를 통해 호출
        request.setAttribute("isLiked", isLiked);
        
        boolean isInclude = lectureManager.isEnrolledInLecture(stuId, lectureId); // 인스턴스를 통해 호출
        request.setAttribute("isInclude", isInclude);
        
        //강의 스케줄 중복 조회

        boolean isConflict = lectureManager. isLectureConflict(stuId, lectureId); // 인스턴스를 통해 호출
        request.setAttribute("isConflict", isConflict);
       
        
        // 로그인한 사용자 ID를 request에 저장
        request.setAttribute("userId", MemberSessionUtils.getLoginMemberId(request.getSession()));
        request.setAttribute("lectureId",  lectureId);
        request.setAttribute("lectureName",  lecture.getName());
        request.setAttribute("lectureImg",  lecture.getImg());
        request.setAttribute("teacherName", lecture.getTeacherName());
        request.setAttribute("teacherPhone", lecture.getPhone());
        request.setAttribute("lectureroom", lecture.getLectureRoom());
        request.setAttribute("description", lecture.getDescription());
        request.setAttribute("lectureroom", lecture.getLectureRoom());
        request.setAttribute("reviewList", lectureReviewList);
        request.setAttribute("availableSeats", availableSeats);
        
        
        // 강의 상세 페이지로 이동
        return "/lecture/lecture_overview.jsp";
    }
}
        
        
