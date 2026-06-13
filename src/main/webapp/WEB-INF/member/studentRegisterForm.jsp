<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds9">
<link rel=stylesheet href="<c:url value='/css/registerForm.css' />?v=20260613-ds9"
	type="text/css">
<title>EduManager registerForm</title>
</head>
 <script>
 	function check() {
 		if(!document.form.id.value){
 			alert("ID를 입력하세요.");
 			return false;
 		}
 		if(!document.form.pwd.value){
 			alert("비밀번호를 입력하세요.");
 			return false;
 		}
 		  if (document.form.pwd.value !== document.form['password-confirm'].value) {
 		        alert("비밀번호가 일치하지 않습니다.");
 		        return false;
 		    }
 		return true;
 	}
 	
 	function confirmId() {
		var id = document.form.id.value.trim();
		var box = document.getElementById('idCheckResult');
		if (id === "") {
			box.innerHTML = '<span class="dupmsg bad">아이디를 입력하세요.</span>';
			return;
		}
		fetch("<c:url value='/confirmId' />?id=" + encodeURIComponent(id))
			.then(function (r) { return r.text(); })
			.then(function (html) { box.innerHTML = html; })
			.catch(function () { box.innerHTML = '<span class="dupmsg bad">확인 중 오류가 발생했습니다.</span>'; });
	}
 	
 </script>
<body>
	<div class="page">
		<header>
			<nav>
				<a href="<c:url value='/member/login'/>" id="logo-link"> <img
					src="<c:url value='/images/edumanager-logo.png' />" alt="Edu Logo" />
				</a>
			</nav>
	  	  </header>  
          <div id="form-container">
              <div id="sign-up-container">
                  <h3>학생 회원가입</h3>
                  <c:if test="${registerFailed}">
                      <div class="error" style="margin:0 0 14px;padding:10px 14px;border-radius:10px;background:#fef2f2;border:1px solid #f6cccc;color:#dc2626;font-weight:600;">
                          <c:out value="${exception.getMessage()}" />
                      </div>
                  </c:if>
                  <form name="form" method="POST" onSubmit="return check()" enctype="multipart/form-data" action="<c:url value='/student/register1' />">
                      <div class="input-group">
                      	<label for="id">아이디</label>
                			<input type="text" name="id" id="id" placeholder="id" value="<c:out value='${student.id}'/>" required>
                			<button type="button" id="checkDuplicate" onClick="confirmId(this.form)">중복 확인</button>
						<span id="idCheckResult" class="dupmsg-line"></span>
              		  </div>
                      <label for="password">비밀번호</label>
                      <input type="password" name="pwd" id="password" placeholder="비밀번호를 입력하세요" required>
                      <label for="password-confirm">비밀번호 확인</label>
                      <input type="password" name="password-confirm" id="password-confirm" placeholder="비밀번호를 다시 입력하세요" required />
                      <label for="name">이름</label>
                      <input type="text" name="name" id="name" placeholder="Name" value="<c:out value='${student.name}'/>" required>
					  <label for="email">이메일</label>
                      <input type="email" name="email" id="email" placeholder="Email" value="<c:out value='${student.email}'/>" required>
                      <label for="email">전화번호</label>
                      <input type="tel" name="phone" id="phone" placeholder="010-1234-5678" pattern="[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}" value="<c:out value='${student.phone}'/>" required>
                     
                      					<label for="profileImg">프로필 사진</label>
					<label class="filebox"><span class="filebox-btn">📷 사진 선택</span><span class="filebox-name">선택된 파일 없음</span><input type="file" name="profileImg" id="profileImg" accept="image/*" onchange="this.parentNode.querySelector('.filebox-name').textContent=this.files.length?this.files[0].name:'선택된 파일 없음'"></label>
					<div id="form-controls">
                          <button type="submit" id="nextBt">다음</button>
                      </div>
                  </form>
              </div>
          </div>
    </div>
  </body>
  </html>
