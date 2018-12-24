<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>搜索</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	</head>

	<body>
	<c:if test="${requestScope.pageIndex != 1}">
		<td>
			<a href="${pageContext.request.contextPath}/search/search?pageIndex=${requestScope.pageIndex - 1}&text=${requestScope.text}">上一页</a>
		</td>
	</c:if>
	<c:if test="${requestScope.pageIndex == 1}">
		<td>
			上一页
		</td>
	</c:if>
	<c:if test="${requestScope.pageIndex < requestScope.pageCount}">
		<td colspan="4">
			<a href="${pageContext.request.contextPath}/search/search?pageIndex=${requestScope.pageIndex + 1}&text=${requestScope.text}">下一页</a>
		</td>
	</c:if>
	<c:if test="${requestScope.pageIndex == requestScope.pageCount}">
		<td colspan="4">
			下一页
		</td>

	</c:if>
	总记录数：${requestScope.count }
     <c:forEach items="${requestScope.list }" var="list">
           <p>诗名:${list.title}</p>
           <p>内容:${list.content}</p>
		   <p>作者：${list.poet.name}</p>
     </c:forEach>
	</body>
</html>
