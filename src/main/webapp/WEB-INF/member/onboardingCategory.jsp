<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>EduManager</title>
<link rel="stylesheet" href="<c:url value='/css/onboarding.css'/>">
<script src="<c:url value='/js/onboarding/category.js'/>"></script>
<%--  <input type="hidden" name="age" value="<%= request.getParameter("age") %>" />
 --%>
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
		<!-- 분야 선택 -->

		<form id="category" id="categoryForm" method="POST"
			action="<c:url value='/student/register4'/>">
			<div style="text-align: center; height: 100px">
				<div class="title">관심있는 분야를 모두 선택해주세요</div>
				<div style="color: #A6A9AF; margin-top: 20px;">
					관심있는 분야를 <span style="color: #616161; font-weight: bold">3가지</span>
					이하로 선택해주세요 <br /> 당신에게 맞는 강의/스터디를 추천해 드릴게요
				</div>
			</div>
			<div class="category">
			<!-- categories 확인 -->
				<c:if test="${empty categories}">
				    <p>카테고리가 없습니다.</p>
				</c:if>
				<c:forEach var="category" items="${categories}">
					<label> 
						<input type="checkbox" name="categories" value="${category.id}" onclick="updateCategoryStyle(this)" />
						${category.name}
					</label> 
				</c:forEach>
			</div>
			<button type="submit" class="styled-button">에듀매니저 시작하기</button>
		</form>
	</div>
</body>
</html>