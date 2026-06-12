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

		response.setContentType(img.contentType != null && !img.contentType.isEmpty()
				? img.contentType : "application/octet-stream");
		response.setContentLength(img.data.length);
		// 캐시 허용(같은 URL은 내용이 바뀔 때 id/쿼리로 무력화)
		response.setHeader("Cache-Control", "max-age=300");

		ServletOutputStream out = response.getOutputStream();
		out.write(img.data);
		out.flush();
		return null; // 직접 응답을 썼으므로 forward/redirect 하지 않음
	}
}
