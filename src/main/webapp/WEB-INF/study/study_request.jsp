<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel="stylesheet" href="<c:url value='/css/study_request.css' />?v=20260615-title1"
	type="text/css">
<title>EduManager</title>
</head>
<body>
	<div class="page">
		<!-- 네비게이션 포함 -->
		<jsp:include page="../navigation/navigation.jsp" />

		<!-- 제목 -->
		<div class="subTitle">스터디 그룹 참여 요청</div>
		<div class="requestListContainer">
		  <c:forEach var="studyGroup" items="${requestList}">
				   <div class="requestContainer">
				<div class="requestProfileContainer">
					<img src="<c:url value='/images/profileImg.svg' />" class="requestProfile" />
					<div class="requestProfileName">${studyGroup.memberName}</div>
				</div>
				
				
					<div class="buttonContainter">
					
					<form action="<c:url value='/studyGroup/accepted-request' />" method="post"
						id="acceptedForm-${studyGroup.studyGroupApplicationId}">

						<input type="hidden" name="studyGroupApplicationId" value="${studyGroup.studyGroupApplicationId}" />
						<input type="hidden" name="groupId" value="${studyGroup.studyGroupId}" />
						<input type="button" id="acceptBtn-${studyGroup.studyGroupApplicationId}" value="수락" onclick="document.getElementById('acceptedForm-${studyGroup.studyGroupApplicationId}').submit();" />

					</form>
					
					<form action="<c:url value='/studyGroup/delete-request' />" method="post"
						id="deleteRequestForm-${studyGroup.studyGroupApplicationId}">
						<input type="hidden" name="studyGroupApplicationId" value="${studyGroup.studyGroupApplicationId}" />
						<input type="hidden" name="groupId" value="${studyGroup.studyGroupId}" />
						<input
							type="button" id="rejectBtn-${studyGroup.studyGroupApplicationId}" value="거절"  onclick="document.getElementById('deleteRequestForm-${studyGroup.studyGroupApplicationId}').submit();"/>
			
				</form>
					 
			</div>
			</div>
        </c:forEach>
			

		</div>

	</div>
</body>
</html>
