<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <link rel=stylesheet href="<c:url value='/css/loginForm.css' />" type="text/css" >
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
  </script>
  </head>
  <body>
    <div class="page" >
    	  <header>
	    	  <nav>
		          <a href="<c:url value='/member/login'/>" id="logo-link">
		   		 	<img src="<c:url value='/images/eduLogo.png' />" alt="Edu Logo" />
			  	  </a>
		  	  </nav>
	  	  </header>
          <div id="form-container">
              <div id="sign-up-container">
                  <h3>로그인 </h3>
                  <!-- 로그인이 실패한 경우 exception 객체에 저장된 오류 메시지를 출력 -->
			      <c:if test="${loginFailed}">
				  	<br><font color="red"><c:out value="${exception.getMessage()}" /></font><br>
				  </c:if>
                  <form name="form" method="POST" action="<c:url value='/member/login' />">
                      <div class="input-group">
                       <label for="email">아이디</label>
                 		<input type="text" name="id" id="id" placeholder="id">
                       <label for="password">비밀번호</label>
                       <input type="password" name="pwd" id="pwd" placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;">
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