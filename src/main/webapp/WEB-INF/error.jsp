<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // 컨테이너가 넘겨주는 상태코드만 읽어 사용자 친화적 메시지로 변환한다.
    // 스택트레이스/예외 객체는 의도적으로 노출하지 않는다(보안).
    Object __sc = request.getAttribute("javax.servlet.error.status_code");
    int sc = (__sc instanceof Integer) ? ((Integer) __sc).intValue() : 500;
    String big, title, desc;
    if (sc == 404) {
        big = "404"; title = "페이지를 찾을 수 없습니다";
        desc = "주소가 잘못되었거나 삭제된 페이지일 수 있어요.";
    } else if (sc == 403) {
        big = "403"; title = "접근 권한이 없습니다";
        desc = "이 페이지를 볼 수 있는 권한이 없어요.";
    } else {
        big = String.valueOf(sc); title = "문제가 발생했습니다";
        desc = "잠시 후 다시 시도해 주세요. 문제가 계속되면 관리자에게 문의해 주세요.";
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<title>EduManager · 오류 <%= big %></title>
</head>
<body>
	<div style="min-height:100vh;display:flex;align-items:center;justify-content:center;padding:24px;box-sizing:border-box;">
		<div style="max-width:480px;width:100%;text-align:center;background:var(--surface);border:1px solid var(--line);border-radius:var(--r-lg);box-shadow:var(--shadow);padding:44px 32px;">
			<div style="font-size:64px;font-weight:800;color:var(--brand);line-height:1;letter-spacing:-0.02em;"><%= big %></div>
			<h1 style="margin:18px 0 8px;font-size:20px;font-weight:800;color:var(--ink);"><%= title %></h1>
			<p style="margin:0 0 28px;color:var(--muted);font-size:14px;line-height:1.6;"><%= desc %></p>
			<a href="<c:url value='/main'/>" class="em-btn em-btn--primary" style="text-decoration:none;display:inline-flex;">홈으로 돌아가기</a>
		</div>
	</div>
</body>
</html>
