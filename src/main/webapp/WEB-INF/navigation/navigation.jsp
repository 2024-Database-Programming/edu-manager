<%@page contentType="text/html; charset=utf-8" %>
<%-- <%@page import="java.util.*, model.domain.*" %> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel=stylesheet href="<c:url value='/css/navigation.css' />" type="text/css">
<title>Insert title here</title>
</head>
<body>
	<header>
	  <a href="<c:url value='/main/main'/>" id="logo-link">
   		 <img src="<c:url value='/images/eduLogo.png' />" alt="Edu Logo" style="height:50px"/>
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
</body>
</html>