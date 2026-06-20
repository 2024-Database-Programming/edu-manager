<%@page contentType="text/html; charset=utf-8" %>
<%-- <%@page import="java.util.*, model.domain.*" %> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260621-toss1">
<link rel=stylesheet href="<c:url value='/css/navigation.css' />?v=20260621-toss1" type="text/css">
<title>Insert title here</title>
</head>
<body>
	<header>
	  <a href="<c:url value='/main'/>" id="logo-link">
		<img src="<c:url value='/images/edumanager-logo.png' />" alt="EduManager Logo"/>
	  </a>
      <nav>
          <ul>
              <li class="menu"><a href="<c:url value='/main'/>">일정캘린더</a></li>
              <li class="menu"><a href="<c:url value='/registration'/>">강의/스터디 신청</a></li>
              <li class="menu"><a href="<c:url value='/mypage'/>">마이 페이지</a></li>
              <li class="menu"><a href="<c:url value='/member/logout'/>">${curUserId}님 로그아웃</a></li>
          </ul>
      </nav>
	</header>
	<%-- 작업 결과 1회성 알림(flash): 컨트롤러가 session flashError/flashMessage 에 담으면 여기서 1번 표시 후 제거 (c:out으로 이스케이프) --%>
	<c:if test="${not empty sessionScope.flashError}">
		<div style="max-width:1200px;margin:14px auto 0;padding:12px 16px;border-radius:10px;background:#fef2f2;border:1px solid #f6cccc;color:#dc2626;font-weight:600;text-align:center;">
			<c:out value="${sessionScope.flashError}" />
		</div>
		<c:remove var="flashError" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.flashMessage}">
		<div style="max-width:1200px;margin:14px auto 0;padding:12px 16px;border-radius:10px;background:#ecfdf3;border:1px solid #bbf7d0;color:#047857;font-weight:600;text-align:center;">
			<c:out value="${sessionScope.flashMessage}" />
		</div>
		<c:remove var="flashMessage" scope="session" />
	</c:if>
</body>
</html>
