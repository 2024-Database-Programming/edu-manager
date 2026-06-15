<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>${itemTitle}</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel="stylesheet" href="<c:url value='/css/item_detail.css' />?v=20260616-submit1" type="text/css">
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<main class="item-detail-page">
			<div class="item-toolbar">
				<a class="back-link" href="${backUrl}">${ownerType} 상세보기</a>
				<a class="list-button" href="${listUrl}">${listLabel}</a>
			</div>
			<article class="item-panel">
				<c:if test="${not empty sessionScope.flashError}">
					<div class="item-alert item-alert--error">${sessionScope.flashError}</div>
					<c:remove var="flashError" scope="session" />
				</c:if>
				<c:if test="${not empty sessionScope.flashMessage}">
					<div class="item-alert item-alert--success">${sessionScope.flashMessage}</div>
					<c:remove var="flashMessage" scope="session" />
				</c:if>
				<div class="item-header">
					<div class="item-kicker">
						<span>${ownerType}</span>
						<span>${itemLabel}</span>
					</div>
					<h1><c:out value="${itemTitle}" /></h1>
					<p><c:out value="${ownerTitle}" /></p>
				</div>
				<div class="item-info-table">
					<div class="item-info-row">
						<div class="item-info-label">분류</div>
						<div class="item-info-value">${ownerType} / ${itemLabel}</div>
					</div>
					<c:if test="${not empty itemMeta}">
						<div class="item-info-row">
							<div class="item-info-label">일시</div>
							<div class="item-info-value"><c:out value="${itemMeta}" /></div>
						</div>
					</c:if>
					<div class="item-info-row">
						<div class="item-info-label">소속</div>
						<div class="item-info-value"><c:out value="${ownerTitle}" /></div>
					</div>
				</div>
				<section class="item-section">
					<h2>상세 내용</h2>
					<div class="item-description">
						<c:out value="${itemDescription}" />
					</div>
				</section>
				<c:if test="${not empty itemAttachment}">
					<section class="item-section">
						<h2>첨부파일</h2>
						<div class="item-attachment">
							<strong><c:out value="${itemAttachment}" /></strong>
						</div>
					</section>
				</c:if>
				<c:if test="${not empty itemAttachments}">
					<section class="item-section">
						<h2>첨부파일 / 이미지</h2>
						<div class="attachment-list">
							<c:forEach var="attachment" items="${itemAttachments}">
								<c:url var="attachmentUrl" value="/attachment/download">
									<c:param name="id" value="${attachment.id}" />
								</c:url>
								<c:url var="previewUrl" value="/attachment/download">
									<c:param name="id" value="${attachment.id}" />
									<c:param name="preview" value="true" />
								</c:url>
								<div class="attachment-row">
									<c:if test="${attachment.image}">
										<img class="attachment-preview" src="${previewUrl}" alt="" />
									</c:if>
									<div class="attachment-meta">
										<strong><c:out value="${attachment.fileName}" /></strong>
										<span><c:out value="${attachment.contentType}" /></span>
									</div>
									<a class="attachment-download" href="${attachmentUrl}">다운로드</a>
								</div>
							</c:forEach>
						</div>
					</section>
				</c:if>
				<c:if test="${itemKind == 'assignment'}">
					<c:choose>
						<c:when test="${canManageItem}">
							<section class="item-section">
								<h2>제출 현황</h2>
								<c:choose>
									<c:when test="${empty assignmentSubmissions}">
										<div class="empty-note">아직 제출한 학생이 없습니다.</div>
									</c:when>
									<c:otherwise>
										<div class="submission-list">
											<c:forEach var="submission" items="${assignmentSubmissions}">
												<div class="submission-row">
													<div>
														<strong><c:out value="${submission.studentId}" /></strong>
														<span><c:out value="${submission.submittedAt}" /></span>
														<p><c:out value="${submission.description}" /></p>
													</div>
													<c:if test="${submission.hasFile}">
														<c:url var="submissionFileUrl" value="/submission/download">
															<c:param name="id" value="${submission.id}" />
														</c:url>
														<a class="attachment-download" href="${submissionFileUrl}">제출 파일</a>
													</c:if>
												</div>
											</c:forEach>
										</div>
									</c:otherwise>
								</c:choose>
							</section>
						</c:when>
						<c:when test="${canSubmitAssignment}">
							<section class="item-section">
								<h2>과제 제출</h2>
								<c:if test="${not empty mySubmission}">
									<div class="submission-current">
										<strong>제출 완료</strong>
										<span><c:out value="${mySubmission.submittedAt}" /></span>
										<c:if test="${not empty mySubmission.description}">
											<p><c:out value="${mySubmission.description}" /></p>
										</c:if>
										<c:if test="${mySubmission.hasFile}">
											<c:url var="mySubmissionFileUrl" value="/submission/download">
												<c:param name="id" value="${mySubmission.id}" />
											</c:url>
											<a class="attachment-download" href="${mySubmissionFileUrl}">내 제출 파일</a>
										</c:if>
									</div>
								</c:if>
								<form class="submission-form" action="${submitUrl}" method="POST" enctype="multipart/form-data">
									<input type="hidden" name="assignmentId" value="${itemId}" />
									<input type="hidden" name="selectedDate" value="${selectedDate}" />
									<label for="submissionDescription">제출 내용</label>
									<textarea id="submissionDescription" name="submissionDescription"
										placeholder="제출 설명을 입력하세요."></textarea>
									<label for="submissionFile">제출 파일</label>
									<input type="file" id="submissionFile" name="submissionFile" class="submission-file" />
									<button type="submit">제출하기</button>
								</form>
							</section>
						</c:when>
					</c:choose>
				</c:if>
				<div class="item-actions">
					<c:if test="${canManageItem}">
						<form class="delete-form" action="${deleteUrl}" method="POST"
							onsubmit="return confirm('이 항목을 삭제할까요?');">
							<input type="hidden" name="type" value="${itemKind}" />
							<input type="hidden" name="id" value="${itemId}" />
							<input type="hidden" name="selectedDate" value="${selectedDate}" />
							<button type="submit" class="delete-button">삭제</button>
						</form>
					</c:if>
					<a class="list-button secondary" href="${listUrl}">${listLabel}</a>
					<a class="list-button primary" href="${backUrl}">${backLabel}</a>
				</div>
			</article>
		</main>
	</div>
</body>
</html>
