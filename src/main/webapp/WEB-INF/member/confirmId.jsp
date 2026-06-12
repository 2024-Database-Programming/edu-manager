<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.dao.member.MemberDAO" %>
<% 

  String id = request.getParameter("id");
  MemberDAO dao = new MemberDAO();
  boolean result = dao.existingMember(id);
  
  %>
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">

  <div style="display:flex;justify-content:center;padding:8px 0;">
      <% if (result) { %>
          <span class="em-badge em-badge--danger" style="height:auto;padding:9px 14px;font-size:13px;">이미 사용 중인 ID 입니다.</span>
      <% } else { %>
          <span class="em-badge em-badge--success" style="height:auto;padding:9px 14px;font-size:13px;">입력하신 ID는 사용하실 수 있습니다.</span>
      <% } %>
  </div>