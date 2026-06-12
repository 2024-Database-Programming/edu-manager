<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel=stylesheet href="<c:url value='/css/my_study_list.css' />"
	type="text/css">
<title>EduManager</title>
<script src="<c:url value='/js/myStudyGroups.js' />"></script>
</head>
<body>
	<div class="page">
		<!-- 네비게이션 포함 -->
		<jsp:include page="../navigation/navigation.jsp" />

		<div class="subTitle">내 스터디</div>
		<div class="tab-container">
			<div class="tabs on" onclick="openTab('Tab1')">
				<div>
					<img src="<c:url value='/images/leader.svg' />" id="tabIcon">
				</div>
				<div>팀장</div>
			</div>
			<div class="tabs" onclick="openTab('Tab2')">
				<div>
					<img src="<c:url value='/images/follower.svg' />" id="tabIcon">
				</div>
				<div>팀원</div>
			</div>
		</div>
		<div class="hr-wrapper">
			<hr id="myStudyGroups_mainHr">
			<hr id="myStudyGroups_hr1">
			<hr id="myStudyGroups_hr2">
		</div>
		<div class="tab_wrap">
			<div id="Tab1" class="tab on">

				<div class="class">
					<c:forEach var="studyGroup" items="${studyGroupListByLeader}">
						<div class="leaderRoom">
							<div class="groupGallery">
								<a
									href="<c:url value='/mystudy/view'> <c:param name='groupId' value='${studyGroup.studyGroupId}'/>

			 	 </c:url>">
									<img src="<c:url value='${studyGroup.img}' />"> <span
									class="groupGalleryTitle">${studyGroup.name}</span> <span
									class="groupGalleryCategory"
									style=" background-color: ${studyGroup.categoryColor};">${studyGroup.categoryName}</span>
								</a>
							</div>
							<button class="leaderRoomBtn"
								onclick="window.location.href='<c:url value='/study/requests' />?groupId=${studyGroup.studyGroupId}'">
								가입 요청 확인하기</button>
						</div>
					</c:forEach>
				</div>
			</div>

			<div id="Tab2" class="tab">

				<div class="class">
					<c:forEach var="studyGroup" items="${studyGroupListByMember}">
						<div class="studyRoom">
							<div class="groupGallery">
								<a
									href="<c:url value='/mystudy/view'> <c:param name='groupId' value='${studyGroup.studyGroupId}'/>

			 	 </c:url>">
									<img src="<c:url value='${studyGroup.img}' />"> <span
									class="groupGalleryTitle">${studyGroup.name}</span> <span
									class="groupGalleryCategory"
									style=" background-color: ${studyGroup.categoryColor};">${studyGroup.categoryName}</span>
								</a>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>

		</div>
	</div>

</body>
</html>