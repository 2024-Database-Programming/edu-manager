<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet"
	href="<c:url value='/css/lecture_overview.css' />" type="text/css">
<title>EduManager</title>
</head>
<body>
	<div class="page">
		<!-- 네비게이션 포함 -->
		<jsp:include page="../navigation/navigation.jsp" />

		<!-- 제목 -->
		<div class="subTitle">강의 수강신청</div>
		<div class="sectionContainer">
			<div class="section1">
				<div>
				<img id=lectureOverviewImg src="<c:url value='${lectureImg}' />" alt="Group Image">
				
				</div>
				<div class="overViewInfo">
					<div class="overViewInfoText" id="lectureTeacherName">
						${teacherName} 강사님</div>
					<div class="overViewInfoText" id="lectureTeacherPhone">
						<img src="<c:url value='/images/phoneIcon.svg' />"
							class="infoIcon" />${teacherPhone}
					</div>
					<div class="overViewInfoText" id="lectureTeacherRoom">
						<img src="<c:url value='/images/roomIcon.svg' />"
							class="infoIcon" />${lectureroom}호
					</div>
				</div>
			</div>
			<div class="section2">
				<div id="lectureOverviewTitle">${lectureName}</div>
				<div id="lectureOverviewDescription">${description}</div>
				<div class="section3">
					<c:choose>
						<c:when test="${existStudent}">
							<div class="likeButtonContainer">
									<form action="<c:url value='/lecture/like' />" method="post"
										id="likeForm">
										<input type="hidden" name="lectureId" value="${lectureId}" />
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
						</c:when>
					</c:choose>
					<div>
							<form action="<c:url value='/lecture/join' />" method="post"
								id="lectureRequestForm">
								<input type="hidden" name="lectureId" value="${lectureId}" />
								<c:choose>
								<c:when test="${existStudent}">
									<c:choose>
										<c:when test="${!isInclude}">
											<c:choose>
												<c:when test="${!isConflict}">
													<c:choose>
														<c:when test="${availableSeats > 0}">
															<input type="button" class="applyButton" value="수강신청하기"
																onclick="document.getElementById('lectureRequestForm').submit();" />
														</c:when>
														<c:otherwise>
															<input type="button" class="applyButton" value="수강신청하기"
																onclick="alert('정원을 초과했습니다!');" />
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<input type="button" class="applyButton" value="수강신청하기"
														onclick="alert('수강중인 강의와 강의 시간이 겹칩니다!');" />
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<input type="button" class="rejectButton" value="수강중"
												disabled />
										</c:otherwise>
									</c:choose>
								</c:when>
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
				<div class="reviewIconText">수강 후기</div>
			</div>
			<div class="writeReviewContainer">
					<form action="<c:url value='/lecture/createReview' />" method="post"
						id="reviewForm">
						<input type="hidden" name="lectureId" value="${lectureId}" />

					<!-- 리뷰 내용을 작성할 textarea -->
					<textarea class="reviewTextArea" name="reviewText"
						placeholder="후기를 작성해주세요"></textarea>

					<!-- 리뷰 제출 버튼 -->
					<c:choose>
						<c:when test="${isInclude}">
							<input type="button" class="reviewSubmit" value="작성"
								onclick="document.getElementById('reviewForm').submit();" />
						</c:when>
						<c:otherwise>
							<input type="button" class="reviewSubmit" value="작성"
								onclick="alert('수강생만 후기를 작성할 수 있어요!')" />
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
