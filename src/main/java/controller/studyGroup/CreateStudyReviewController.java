package controller.studyGroup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.studyGroup.StudyGroupReview;
import model.service.StudyGroupManager;
import model.service.member.MemberManager;

public class CreateStudyReviewController implements Controller {

    private final StudyGroupManager studyGroupManager;
    private final MemberManager memberManager;

    public CreateStudyReviewController() {
        this.studyGroupManager = StudyGroupManager.getInstance();
        this.memberManager = MemberManager.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form";
        }

        // 요청 파라미터에서 데이터 추출
        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        Long groupId = Long.parseLong(request.getParameter("groupId"));
        String reviewText = request.getParameter("reviewText");

        if (!studyGroupManager.isMemberOfStudyGroup(memberId, groupId)) {
            return "redirect:/study/over-view?groupId=" + groupId;
        }
        
        System.out.println("memberId: " + memberId + " groupId: " + groupId + " reviewText: " + reviewText);

       
        String memberName = memberManager.findName(memberId);
        
        // LectureReview 객체 생성 (생성자 활용)
        StudyGroupReview studyGroupReview = new StudyGroupReview(
            0L,
            reviewText,
            groupId,
            memberId,
            memberName
        );

        // 생성된 LectureReview 객체 출력
        System.out.println("Created LectureReview: ");
        System.out.println("Review Text: " + studyGroupReview.getReviewText());

        // 리뷰 생성 서비스 호출
        studyGroupManager.createStudyReview(studyGroupReview );

        // 상태 변경 후 해당 페이지로 리다이렉트
        return "redirect:/study/over-view?groupId=" + groupId; // 페이지 새로고침
    }
}
