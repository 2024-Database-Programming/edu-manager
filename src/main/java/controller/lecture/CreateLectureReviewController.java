package controller.lecture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.lecture.LectureReview;
import model.service.LectureManager;
import model.service.member.MemberManager;

public class CreateLectureReviewController implements Controller {

    private final LectureManager lectureManager;
    private final MemberManager memberManager;

    public CreateLectureReviewController() {
        this.lectureManager = LectureManager.getInstance();
        this.memberManager = MemberManager.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form";
        }

        // 요청 파라미터에서 데이터 추출
        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        Long lectureId = Long.parseLong(request.getParameter("lectureId"));
        String reviewText = request.getParameter("reviewText");

        if (!lectureManager.isEnrolledInLecture(memberId, lectureId)) {
            return "redirect:/lecture/over-view?lectureId=" + lectureId;
        }
        

       
        String memberName = memberManager.findName(memberId);
        
        // LectureReview 객체 생성 (생성자 활용)
        LectureReview lectureReview = new LectureReview(
            0L,
            reviewText,
            lectureId,
            memberId,
            memberName
            
        );

        // 생성된 LectureReview 객체 출력

        // 리뷰 생성 서비스 호출
        lectureManager.createLectureReview(lectureReview);

        // 상태 변경 후 해당 페이지로 리다이렉트
        return "redirect:/lecture/over-view?lectureId=" + lectureId; // 페이지 새로고침
    }
}
