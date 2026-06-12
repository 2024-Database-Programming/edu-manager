package controller.member;

import controller.Controller;
import model.dao.member.InterestCategoryDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class InterestCategoryController implements Controller {

    private InterestCategoryDAO interestCategoryDAO;

    public InterestCategoryController() {
        interestCategoryDAO = new InterestCategoryDAO(); // DAO 객체 초기화
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // DB에서 관심 분야 목록을 가져옴
        List<Map<String, Object>> categories = interestCategoryDAO.getCategories();

        // 가져온 데이터를 request에 설정
        request.setAttribute("categories", categories);

        return "/member/onboardingCategory.jsp";
    }
}
