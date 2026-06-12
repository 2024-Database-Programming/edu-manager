<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>EduManager</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/onboarding.css">
<script src="${pageContext.request.contextPath}/js/onboarding/role.js"
	defer></script>
</head>
<body>
	<header>
		<nav>
			<a href="<c:url value='/member/login'/>" id="logo-link"> <img
				src="<c:url value='/images/eduLogo.png' />" alt="Edu Logo" />
			</a>
		</nav>
	</header>
	<div class="container">
		<div style="text-align: center;">
			<div class="title">역할을 선택해주세요</div>
			<div style="color: #A6A9AF; height: 20px; margin-top: 20px;">
				역할에 맞는 페이지를 제공합니다.</div>
		</div>
		<!-- 역 선택 -->
		<form action="<c:url value='/member/register/form'/>" method="POST">
			<div class="role">
				<label id="teacherLabel" class="role-button"
					onclick="selectRole('TEACHER')"> <input type="radio"
					name="role" value="TEACHER" required> 강사
				</label> 
				<label id="studentLabel" class="role-button"
					onclick="selectRole('STUDENT')"> <input type="radio"
					name="role" value="STUDENT" required> 학생
				</label>
			</div>
			<button type="submit" class="styled-button">다음</button>
		</form>

		<!-- 하단 바 -->
		<%-- 		<img src="<c:url value='/images/slidebar1.png'/>" alt="bar" style="margin-top: 30px; width: 50px">
 --%>
	</div>
</body>
</html>