<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.dao.member.MemberDAO" %>
<%
  String id = request.getParameter("id");
  MemberDAO dao = new MemberDAO();
  boolean result = dao.existingMember(id);
%><% if (result) { %><span class="dupmsg bad">이미 사용 중인 ID입니다.</span><% } else { %><span class="dupmsg ok">사용 가능한 ID입니다.</span><% } %>
