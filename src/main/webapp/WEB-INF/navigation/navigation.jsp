<%@page contentType="text/html; charset=utf-8" %>
<%-- <%@page import="java.util.*, model.domain.*" %> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel=stylesheet href="<c:url value='/css/navigation.css' />?v=20260613-ds4" type="text/css">
<title>Insert title here</title>
</head>
<body>
	<header>
	  <a href="<c:url value='/main/main'/>" id="logo-link">
		<img src="<c:url value='/images/edumanager-logo.png' />" alt="EduManager Logo"/>
	  </a>
      <nav>
          <ul>
              <li class="menu"><a href="<c:url value='/main/main'/>">일정캘린더</a></li>
              <li class="menu"><a href="<c:url value='/registration'/>">강의/스터디 신청</a></li>
              <li class="menu"><a href="<c:url value='/mypage'/>">마이 페이지</a></li>
              <li class="menu"><a href="<c:url value='/member/logout'/>">${curUserId}님 로그아웃</a></li>
          </ul>
      </nav>
	</header>
	<%-- 작업 실패 시 1회성 알림(flash): 컨트롤러가 session "flashError"에 담으면 여기서 1번 표시 후 제거 --%>
	<%
	    Object __flash = session.getAttribute("flashError");
	    if (__flash != null) { session.removeAttribute("flashError"); }
	%>
	<% if (__flash != null) { %>
		<div style="max-width:1200px;margin:14px auto 0;padding:12px 16px;border-radius:10px;background:#fef2f2;border:1px solid #f6cccc;color:#dc2626;font-weight:600;text-align:center;">
			<%= __flash %>
		</div>
	<% } %>
</body>
</html>
