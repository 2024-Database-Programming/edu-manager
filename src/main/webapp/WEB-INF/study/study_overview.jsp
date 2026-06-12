<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<c:url value='/css/study_overview.css' />"
	type="text/css">
<title>EduManager</title>
</head>
<body>
	<div class="page">
		<!-- 네비게이션 포함 -->
		<jsp:include page="../navigation/navigation.jsp" />

		<!-- 제목 -->
		<div class="subTitle">스터디 그룹 가입</div>
		<div class="sectionContainer">
			<div class="section1">
				<div>
					<img src="<c:url value='${groupImg}' />"
						id="studyGroupOverviewImg">
				</div>
				<div class="overViewInfo">
					<div class="overViewInfoText" id="studyGroupPlace">
						<img src="<c:url value='/images/roomIcon.svg' />" class="infoIcon" />${place}
					</div>
					<div class="overViewInfoText" id="studyGroupTime">
						<img src="<c:url value='/images/overViewTime.svg' />"
							class="infoIcon" />매주 ${dayOfWeek}요일
					</div>
					<div class="overViewInfoText">
						모집 ${memberCount}/${capacity}
					</div>
				</div>
			</div>
			<div class="section2">
				<div id="groupOverviewTitle">${groupName}</div>
				<div id="groupOverviewDescription">${description}</div>

				<div class="section3">
					<div class="likeButtonContainer">
							<form action="<c:url value='/studyGroup/like' />" method="post"
								id="likeForm">
								<input type="hidden" name="groupId" value="${groupId}" />
							<c:choose>
								<c:when test="${isLiked}">
									<img src="<c:url value='/images/likeButton.svg' />"
										class="likeButton"
										onclick="document.getElementById('likeForm').submit();"
										style="cursor: pointer;" />
								</c:when>
								<c:otherwise>
									<img src="<c:url value='/images/EmptylikeButton.svg' />"
										class="emptyLikeButton"
										onclick="document.getElementById('likeForm').submit();"
										style="cursor: pointer;" />
								</c:otherwise>
							</c:choose>
						</form>
					</div>
					<div>
							<form action="<c:url value='/studyGroup/join-request' />"
								method="post" id="requestForm">
								<input type="hidden" name="groupId" value="${groupId}" />

							<c:choose>
								<c:when test="${isLeader}">
									<input type="button" class="statusAccepted" value="내 스터디" disabled/>
								</c:when>
								<c:when test="${requestStatus == '진행중'}">
									<input type="button" class="statusInProgress" value="가입 요청중" disabled/>
								</c:when>
								<c:when test="${requestStatus == '수락' || isInclude}">
									<input type="button" class="statusAccepted" value="가입 완료" disabled/>
								</c:when>
								<c:when test="${isFull}">
									<input type="button" class="statusAccepted" value="정원 마감" disabled/>
								</c:when>
								<c:otherwise>
									<input type="button" class="applyButton" value="가입 요청하기"
										onclick="document.getElementById('requestForm').submit();" />
								</c:otherwise>
							</c:choose>
						</form>

					</div>
				</div>
			</div>

		</div>
		<div class="section4">
			<div class="reviewIconContainer">
				<img src="<c:url value='/images/reviewIcon.svg' />"
					class="reviewIcon" />
				<div class="reviewIconText">스터디 후기</div>
			</div>
			<div class="writeReviewContainer">
					<form action="<c:url value='/study/createReview' />" method="post"
						id="studyReviewForm">
						<input type="hidden" name="groupId" value="${groupId}" />

					<!-- 리뷰 내용을 작성할 textarea -->
					<textarea class="reviewTextArea" name="reviewText"
						placeholder="후기를 작성해주세요"></textarea>
					<!-- 리뷰 제출 버튼 -->
					<c:choose>
						<c:when test="${isInclude}">
							<input type="button" class="reviewSubmit" value="작성"
								onclick="document.getElementById('studyReviewForm').submit();" />
						</c:when>
						<c:otherwise>
							<input type="button" class="reviewSubmit" value="작성"
								onclick="alert('스터디원만 후기를 작성할 수 있어요!')" />
						</c:otherwise>
					</c:choose>
				</form>
			</div>
			<div class="reviewListContainer">
				<c:forEach var="group" items="${reviewList}">
					<div class="reviewContainer">

						<img src="<c:url value='/images/profileImg.svg' />"
							class="reviewProfileImg" />
						<div class="reviewTextContainer">
							<div class="reviewUserName">${group.memberName}</div>
							<div class="reviewText">${group.reviewText}</div>
						</div>

					</div>
				</c:forEach>
			</div>
		</div>


	</div>

</body>
</html>
