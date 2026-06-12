package controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;

public class RegisterStudent3Controller implements Controller {
	private static final Logger log = LoggerFactory.getLogger(RegisterMemberController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession();

	    // 선택된 카테고리 ID 배열 가져오기
	    String[] selectedCategories = request.getParameterValues("categories");

	    // 선택된 카테고리가 있으면, 각 항목을 int 배열로 변환하여 세션에 저장
	    if (selectedCategories != null && selectedCategories.length > 0) {
	        int[] categoryIds = new int[selectedCategories.length];
	        for (int i = 0; i < selectedCategories.length; i++) {
	            categoryIds[i] = Integer.parseInt(selectedCategories[i]);  // String을 int로 변환
	        }
	        session.setAttribute("interest", categoryIds);  // int[] 배열을 세션에 저장
	    } else {
	        session.setAttribute("interest", new int[0]);  // 선택된 카테고리가 없으면 빈 배열 저장
	    }

	    // 최종 등록 페이지로 리다이렉트
	    return "redirect:/student/register";
	}

}