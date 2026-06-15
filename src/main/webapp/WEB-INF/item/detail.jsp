<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>${itemTitle}</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel="stylesheet" href="<c:url value='/css/item_detail.css' />?v=20260616-item3" type="text/css">
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
				<div class="item-actions">
					<a class="list-button secondary" href="${listUrl}">${listLabel}</a>
					<a class="list-button primary" href="${backUrl}">${backLabel}</a>
				</div>
			</article>
		</main>
	</div>
</body>
</html>
