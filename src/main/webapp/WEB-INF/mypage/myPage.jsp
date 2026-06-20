<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260621-toss1">
<link rel=stylesheet href="<c:url value='/css/myPage.css' />?v=20260615-title1"
	type="text/css">
<title>EduManager</title>
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />

		<div class="subTitle">마이 페이지</div>
		<div id="mypage_container">
			<section class="profile-card">
				<div id="profile_img_container">
					<img id="profile_img" src="<c:url value='${member.img}' />">
				</div>
				<div id="mypage_name">
					<p>${curUserId}</p>
					<c:choose>
						<c:when test="${existStudent}">
							<span class="role-badge">학생</span>
						</c:when>
						<c:otherwise>
							<span class="role-badge">강사</span>
						</c:otherwise>
					</c:choose>
				</div>
			</section>

			<div id="mypage_btn_container">
				<button class="mypageBtn"
					onclick="window.location.href='<c:url value='/mypage/myInfo' />'">
					<span class="mp-ico">👤</span>
					<span class="mp-text"><span class="mp-title">내 정보</span><span
						class="mp-sub">프로필 확인 · 수정</span></span>
					<span class="mp-arrow">›</span>
				</button>
				<button class="mypageBtn"
					onclick="window.location.href='<c:url value='/lecture/list' />'">
					<span class="mp-ico">📚</span>
					<span class="mp-text"><span class="mp-title">내 강의</span><span
						class="mp-sub">개설 · 수강 강의</span></span>
					<span class="mp-arrow">›</span>
				</button>
				<c:if test="${existStudent}">
					<button class="mypageBtn"
						onclick="window.location.href='<c:url value='/mypage/like-list' />'">
						<span class="mp-ico">⭐</span>
						<span class="mp-text"><span class="mp-title">찜 목록</span><span
							class="mp-sub">관심 강의 · 스터디</span></span>
						<span class="mp-arrow">›</span>
					</button>
					<button class="mypageBtn"
						onclick="window.location.href='<c:url value='/study/list' />'">
						<span class="mp-ico">👥</span>
						<span class="mp-text"><span class="mp-title">내 스터디그룹</span><span
							class="mp-sub">참여 중인 스터디</span></span>
						<span class="mp-arrow">›</span>
					</button>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>
