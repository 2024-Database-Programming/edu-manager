<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel=stylesheet href="<c:url value='/css/my_study_list.css' />"
	type="text/css">
<title>EduManager</title>
<script src="<c:url value='/js/myStudyGroups.js' />"></script>
</head>
<body>
	<div class="page">
		<!-- 네비게이션 포함 -->
		<jsp:include page="../navigation/navigation.jsp" />

		<div class="subTitle">내 강의</div>

		<div id="Tab1" class="tab on">
			<div class="class">
				<c:forEach var="group" items="${lectureList}">
					<div class="groupGallery">
					<a
							href="<c:url value='/mylecture/view'> <c:param name='lectureId' value='${group.lectureId}'/>
			 	 </c:url>">
						<%-- <a
							href="<c:url value='/lecture/over-view'> <c:param name='lectureId' value='${group.lectureId}'/>
			 	 </c:url>"> --%>
			 	 
							<img id=img src="<c:url value='${group.img}' />" alt="Group Image">
								<span class="groupGalleryTitle">${group.name}</span>
								<div
									style="display: flex; justify-content: space-between; width: 100%;">
									<span class="groupGalleryCategory"  style=" background-color: ${group.categoryColor};" >${group.categoryName}</span> <span
										class="groupGalleryTeacherName">${group.teacherName}</span>
								</div>
							
						</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
</html>