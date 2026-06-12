<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="<c:url value='/css/deleteConfirm.css' />" type="text/css">
    <title>회원 탈퇴 확인</title>
     <script>
        // 탈퇴 성공 시 알림 후 리다이렉트
        function showSuccessAlert() {
            alert("탈퇴되었습니다.");
            window.location.href = "<c:url value='/member/login/form' />";
        }
    </script>
</head>
<body>
    <div class="page">
        <jsp:include page="/WEB-INF/navigation/navigation.jsp" />
        <div id="form-container">
            <div id="sign-up-container">
                <h3>회원 탈퇴</h3>
                <p>탈퇴하시려면 비밀번호를 입력하세요.</p>
                <form action="<c:url value='/mypage/deleteAccount' />" method="post">
                    <label for="password">비밀번호:</label>
                    <input type="password" name="pwd" id="password" placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;" required>
                    <div id="form-controls">
                        <button type="submit" class="confirm-button">탈퇴하기</button>
                        <button type="button" class="cancel" onclick="window.location.href='<c:url value='/mypage/myInfo' />'">취소</button>
                    </div>
                </form>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>
            </div>
        </div>
    </div>
     <!-- 성공 시 JavaScript 실행 -->
    <c:if test="${success}">
        <script>showSuccessAlert();</script>
    </c:if>
</body>
</html>
