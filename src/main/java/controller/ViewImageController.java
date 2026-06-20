package controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.ImageDAO;

/**
 * DB(IMAGE 테이블)에 저장된 BLOB 이미지를 스트리밍으로 서빙한다.
 *   /image?type=member|lecture|study&id=...
 * 이미지가 없으면 404 → 화면의 onerror 폴백(기본 커버)이 동작한다.
 */
public class ViewImageController implements Controller {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!controller.member.MemberSessionUtils.hasLogined(request.getSession())) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 비로그인 무단 열람(IDOR) 차단
			return null;
		}
		String type = request.getParameter("type");
		String id = request.getParameter("id");

		if (type == null || id == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		ImageDAO.ImageData img = new ImageDAO().get(type, id);
		if (img == null || img.data == null || img.data.length == 0) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null; // DispatcherServlet은 null이면 forward 없이 종료
		}

		// 저장된 contentType을 그대로 신뢰하지 않음: 이미지 화이트리스트만 인라인, 그 외엔 다운로드 처리(SVG/HTML 인라인 렌더 차단)
		String ct = img.contentType == null ? "" : img.contentType.toLowerCase();
		boolean isImage = ct.equals("image/png") || ct.equals("image/jpeg") || ct.equals("image/jpg")
				|| ct.equals("image/gif") || ct.equals("image/webp");
		response.setHeader("X-Content-Type-Options", "nosniff"); // MIME 스니핑 차단
		if (isImage) {
			response.setContentType(ct);
		} else {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment");
		}
		response.setContentLength(img.data.length);
		// 캐시 허용(같은 URL은 내용이 바뀔 때 id/쿼리로 무력화)
		response.setHeader("Cache-Control", "max-age=300");

		ServletOutputStream out = response.getOutputStream();
		out.write(img.data);
		out.flush();
		return null; // 직접 응답을 썼으므로 forward/redirect 하지 않음
	}
}
