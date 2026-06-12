<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>공지사항 리스트</title>
<link rel=stylesheet href="<c:url value='/css/listAssignmentAndNotice.css' />"
	type="text/css">
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<div class="subTitle">공지사항 리스트</div>

		<!-- 검색창 -->
		 
         <div class="search">
            <form action="<c:url value='/lecture/searchListNotice' />"
               method="post" id="searchLectureNoticeForm">
               <input type="hidden" name="groupId" value="${groupId}" />
               <c:choose>
                  <c:when test="${searchParam == null}">
                     <input type="text" name="searchParam" placeholder="검색어를 입력하세요">
                  </c:when>
                  <c:otherwise>
                     <input type="text" name="searchParam" placeholder="${searchParam}">
                  </c:otherwise>
               </c:choose>
               
               <img src="<c:url value='/images/searchIcon.svg' />"
                  onclick="document.getElementById('searchLectureNoticeForm').submit();">
            </form>

         </div>
      

		<!-- 공지사항 리스트 -->
		<table class="table">
			<thead>
				<tr>
					<th class="table-header">제목</th>
					<th class="table-header">내용</th>
					<th class="table-header">등록일</th>
				</tr>
			</thead>
			<tbody>
				<!-- JSTL 반복문으로 리스트 출력 -->
				<c:forEach var="notice" items="${lectureNoticeList}">
					<tr class="notice-row">
						<td class="title">${notice.title}</td>
						<td class="description">${notice.description}</td>
						<td class="createat">${notice.createat}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>
