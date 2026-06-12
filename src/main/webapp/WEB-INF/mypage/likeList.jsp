<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<c:url value='/css/registration.css' />"
	type="text/css">
<title>EduManager</title>
</head>
<script src="<c:url value='/js/registration.js' />"></script>
<body>
	<div class="page">
		<!-- 네비게이션 포함 -->
		<jsp:include page="../navigation/navigation.jsp" />

		<!-- 제목 -->
		<div class="subTitle">찜한 목록</div>

		<div id="tabBtn-container">
			<div class="tab-container">
				<div class="tabs on" onclick="openTab('Tab1')">
					<div>
						<img src="<c:url value='/images/class.svg' />" id="tab1Icon">
					</div>
					<div>강의</div>
				</div>
				<div class="tabs" onclick="openTab('Tab2')">
					<div>
						<img src="<c:url value='/images/studyIcon.svg' />" id="tab2Icon">
					</div>
					<div>스터디</div>
				</div>
			</div>
		</div>

		<div class="hr-wrapper">
			<hr id="registration_mainHr">
			<hr id="registration_hr1">
			<hr id="registration_hr2">
		</div>

		<div class="tab_wrap">
			<!-- Tab 1 -->
			<div id="Tab1" class="tab on">
				<div class="class">
					<c:forEach var="group" items="${lectureList}">
						<div class="groupGallery">
							<a
								href="<c:url value='/lecture/over-view'> <c:param name='lectureId' value='${group.lectureId}'/>
			 	 </c:url>">
								<img src="<c:url value='/images/white.png' />" alt="Group Image">
								<span class="groupGalleryTitle">${group.name}</span>
								<div
									style="display: flex; justify-content: space-between; width: 100%;">
									<span class="groupGalleryCategory"
										style="background-color: ${group.categoryColor};">${group.categoryName}</span>
									<span class="groupGalleryTeacherName">${group.teacherName}</span>
								</div>
							</a>
						</div>
					</c:forEach>
				</div>
			</div>

			<!-- Tab 2 -->
			<div id="Tab2" class="tab">
				<div class="class">
					<c:forEach var="studyGroup" items="${studyGroupList}">
						<div class="groupGallery">
							<a
								href="<c:url value='/study/over-view'> <c:param name='groupId' value='${studyGroup.studyGroupId}'/>
			 	 </c:url>">
								<img src="<c:url value='/images/white.png' />"> <span
								class="groupGalleryTitle">${studyGroup.name}</span> <span
								class="groupGalleryCategory"
								style="background-color: ${studyGroup.categoryColor};">${studyGroup.categoryName}</span>
							</a>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
