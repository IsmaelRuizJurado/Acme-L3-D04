<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>


<acme:list>
	<acme:list-column code="company.practicum.list.label.code" path="code" width="20%"/>
	<acme:list-column code="company.practicum.list.label.title" path="title" width="20%"/>
	<acme:list-column code="company.practicum.list.label.estimatedTime" path="estimatedTime" width="20%"/>
	<acme:list-column code="company.practicum.list.label.courseCode" path="courseCode" width="20%"/>
</acme:list>

<acme:button code="company.practicum.list.button.create" action="/company/practicum/create"/>