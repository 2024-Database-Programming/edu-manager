<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
 <link rel=stylesheet href="<c:url value='/css/loginForm.css' />?v=20260613-ds4" type="text/css" >
    <title>EduManager LoginForm</title>
  <script>
  function login() {
		if (form.id.value == "") {
			alert("사용자 ID를 입력하십시오.");
			form.id.focus();
			return false;
		} 
		if (form.pwd.value == "") {
			alert("비밀번호를 입력하십시오.");
			form.pwd.focus();
			return false;
		}		
		form.submit();
	}

	function userCreate(targetUri) {
		form.action = targetUri;
		form.method="GET";		// register form 요청
		form.submit();
	}
	function userCreate(targetUri) {
	    form.reset();  // 폼 초기화
	    form.action = targetUri;
	    form.method = "GET";  // register form 요청
	    form.submit();
	}

	// 로그인 오류 메시지: 입력 시작하면 즉시, 그리고 4초 뒤 자동으로 사라짐
	window.addEventListener('DOMContentLoaded', function () {
		var err = document.getElementById('loginError');
		if (!err) return;
		var hide = function () { err.style.display = 'none'; };
		['id', 'pwd'].forEach(function (n) {
			var el = document.getElementById(n);
			if (el) el.addEventListener('input', hide);
		});
		setTimeout(hide, 4000);
	});
  </script>
  </head>
  <body>
    <div class="page" >
    	  <header>
				<nav>
					<a href="<c:url value='/member/login'/>" id="logo-link">
						<img src="<c:url value='/images/edumanager-logo.png' />" alt="Edu Logo" />
					</a>
				</nav>
	  	  </header>
          <div id="form-container">
              <div id="sign-up-container">
                  <h3>로그인 </h3>
                  <!-- 로그인이 실패한 경우 exception 객체에 저장된 오류 메시지를 출력 -->
			      <%-- PRG: 세션에 담긴 로그인 오류를 한 번만 표시하고 즉시 제거 --%>
			      <%
			        Object __le = session.getAttribute("loginError");
			        String loginErrorMsg = (__le != null) ? __le.toString() : null;
			        if (loginErrorMsg != null) { session.removeAttribute("loginError"); }
			      %>
			      <% if (loginErrorMsg != null) { %>
				  	<div id="loginError" class="error"><%= loginErrorMsg %></div>
				  <% } %>
                  <form name="form" method="POST" action="<c:url value='/member/login' />">
                      <div class="input-group">
                       <label for="email">아이디</label>
                 		<input type="text" name="id" id="id" placeholder="아이디를 입력하세요">
                       <label for="password">비밀번호</label>
                       <input type="password" name="pwd" id="pwd" placeholder="비밀번호를 입력하세요">
                      </div>
                      <div id="form-controls">
                      	
                          <button type="button" id="nextBt" onClick="login()">로그인</button>
                       
                      	  <!-- get으로 요청 -->
                          <button type="button" id="nextBt" onClick="userCreate('<c:url value='/member/register/form'/>')">회원가입</button>
                      	
                      </div>
                  </form>
                  
              </div>
          </div>
    </div>
  </body>
  </html>
