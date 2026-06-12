<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel=stylesheet href="<c:url value='/css/myPage.css' />"
	type="text/css">
<title>EduManager</title>
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />

		<div class="subTitle">마이 페이지</div>
		<div id="mypage_container">
			<div id="profile_img_container">
			<img id="profile_img" src="<c:url value='${member.img}' />" >
			</div>
			<div id="mypage_name">
				<c:choose>
					<c:when test="${existStudent}">
						<p>${curUserId} 학생</p>
					</c:when>
					<c:otherwise>
						<p>${curUserId} 강사님</p>
					</c:otherwise>
				</c:choose>
			</div>
			<div id="mypage_btn_container">
				<button class="mypageBtn myinfo"
					onclick="window.location.href='<c:url value='/mypage/myInfo' />'">내
					정보</button>
				<button class="mypageBtn mystudyroom"
					onclick="window.location.href='<c:url value='/lecture/list' />'">내
					강의</button>
				<c:choose>
					<c:when test="${existStudent}">
						<button class="mypageBtn setting"
							onclick="window.location.href='<c:url value='/mypage/like-list' />'">찜
							목록</button>
						<button class="mypageBtn mystudyroom"
							onclick="window.location.href='<c:url value='/study/list' />'">
							내스터디그룹</button>
					</c:when>

				</c:choose>

			</div>
		</div>
	</div>
</body>
</html>
