<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<jsp:directive.page pageEncoding="UTF-8" />

<!-- Header -->
<nav class="navbar navbar-default">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<a class="navbar-brand" href="<c:url value='/' />">JavaEP</a>
		</div>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li <c:if test="${param.activePage == 'index'}" >class="active"</c:if> >
					<a href="<c:url value='/' />">Home</a>
				</li>
				
				<sec:authorize ifAnyGranted="ROLE_USER">
					<c:if test="${mode != 'exam'}">
						<li <c:if test="${param.activePage == 'setting'}" >class="active"</c:if> >
							<a href="<c:url value='/setting' />">Setting</a>
						</li>
						<li <c:if test="${param.activePage == 'qanda'}" >class="active"</c:if> >
		                   <a href="<c:url value='/qanda' />" target="listQA">Q&amp;A</a>
		                </li>
	                </c:if>
                </sec:authorize>

				<sec:authorize ifAnyGranted="ROLE_ADMIN">
					<li <c:if test="${param.activePage == 'setting'}" >class="active"</c:if> >
						<a href="<c:url value='/setting' />">Setting</a>
					</li>
					<li <c:if test="${param.activePage == 'qanda'}" >class="active"</c:if> >
	                   <a href="<c:url value='/qanda' />" target="listQA">Q&amp;A</a>
	                </li>
					<li <c:if test="${param.activePage == 'admin'}" >class="active"</c:if> >
						<a href="<c:url value='/admin' />">Admin</a>
					</li>
				</sec:authorize>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li>
					<a><span class="glyphicon glyphicon-user"></span> <sec:authentication property="principal.username"/></a>
				</li>
				<li>
					<a href="<c:url value='/logout' />"><span class="glyphicon glyphicon-log-out"></span> Log Out</a>
				</li>
			</ul>
		</div>
	</div>
</nav>