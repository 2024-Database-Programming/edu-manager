<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.dao.member.MemberDAO" %>
<% 

  String id = request.getParameter("id");
  MemberDAO dao = new MemberDAO();
  boolean result = dao.existingMember(id);
  
  %>

  <center>
      <br /><br />
      <h4>
          <% if (result) { %>
              이미 사용 중인 ID 입니다.
          <% } else { %>
              입력하신 ID는 사용하실 수 있습니다.
          <% } %>
      </h4>
  </center>