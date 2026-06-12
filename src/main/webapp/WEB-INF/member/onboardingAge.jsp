<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>EduManager</title>
<link rel="stylesheet" href="<c:url value='/css/onboarding.css'/>">
<script src="<c:url value='/js/onboarding/age.js'/>"></script>

</head>
<body>
	<header>
		<nav>
			<a href="<c:url value='/member/login'/>" id="logo-link"> <img
				src="<c:url value='/images/eduLogo.png' />" alt="Edu Logo" />
			</a>
		</nav>
	</header>
	<!-- 연령 선택 -->
	<form id="ageForm" class="container" method="POST"
		action="<c:url value='/student/register2'/>">
		<div style="text-align: center; height: 100px">
			<div class="title">연령대를 선택해주세요</div>
			<div style="color: #A6A9AF; height: 20px; margin-top: 20px;"></div>
		</div>
		<div class="age">
			<label id="teenLabel" class="age-button" onclick="selectAge('teen')">
				<input type="radio"  name="age" value="10" required>10대</label> 
			<label id="twentiesLabel" class="age-button" onclick="selectAge('twenties')">
				<input type="radio" name="age" value="20" required>20대</label> 
			<label id="thirtiesLabel" class="age-button" onclick="selectAge('thirties')">
				<input type="radio" name="age" value="30" required> 30대</label> 
			<label id="fortyPlusLabel" class="age-button" onclick="selectAge('fortyPlus')">
				<input type="radio"	name="age" value="40" required> 40대 이상</label>
		</div>
		
		<!-- 다음 버튼 -->
		<button type="submit" class="styled-button">다음</button>
	</form>
	
</body>
</html>
