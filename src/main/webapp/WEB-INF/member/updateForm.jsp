<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>EduManager</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navigation.css" />
<style>
.member-page {
	width: min(640px, calc(100% - 80px));
	margin: 48px auto;
}
.member-title {
	font-size: 26px;
	font-weight: 700;
	margin-bottom: 24px;
}
.member-form {
	background: #fff;
	border: 1px solid #e5e7eb;
	border-radius: 8px;
	padding: 28px;
}
.member-field {
	margin-bottom: 18px;
}
.member-field label {
	display: block;
	margin-bottom: 8px;
	color: #6b7280;
	font-weight: 700;
}
.member-field input {
	width: 100%;
	height: 42px;
	border: 1px solid #d1d5db;
	border-radius: 6px;
	padding: 0 12px;
}
.member-actions {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
}
.member-button {
	min-width: 92px;
	height: 40px;
	border-radius: 6px;
	border: 0;
	background: #1E2A7C;
	color: #fff;
	font-weight: 700;
	text-decoration: none;
	cursor: pointer;
}
.member-button.secondary {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	background: #E5E5EA;
	color: #28292a;
}
</style>
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<main class="member-page">
			<h1 class="member-title">회원 정보 수정</h1>
			<form class="member-form" method="post" action="${pageContext.request.contextPath}/member/update">
				<input type="hidden" name="id" value="${member.id}" />
				<div class="member-field">
					<label>아이디</label>
					<input type="text" value="${member.id}" readonly />
				</div>
				<div class="member-field">
					<label>이름</label>
					<input type="text" value="${member.name}" readonly />
				</div>
				<div class="member-field">
					<label>비밀번호</label>
					<input type="password" name="pwd" value="${member.pwd}" required />
				</div>
				<div class="member-field">
					<label>이메일</label>
					<input type="email" name="email" value="${member.email}" required />
				</div>
				<div class="member-field">
					<label>전화번호</label>
					<input type="text" name="phone" value="${member.phone}" required />
				</div>
				<div class="member-actions">
					<a class="member-button secondary" href="<c:url value='/member/view'><c:param name='id' value='${member.id}' /></c:url>">취소</a>
					<button class="member-button" type="submit">저장</button>
				</div>
			</form>
		</main>
	</div>
</body>
</html>
