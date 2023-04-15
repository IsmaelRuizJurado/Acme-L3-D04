
<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="company.practicumSession.list.label.code" path="code" width="25"/>
	<acme:list-column code="company.practicumSession.list.label.title" path="title" width="25"/>	
	<acme:list-column code="company.practicumSession.list.label.startPeriod" path="startPeriod" width="25%"/>
	<acme:list-column code="company.practicumSession.list.label.endPeriod" path="endPeriod" width="25%"/>
</acme:list>
<jstl:if test="${extraAvailable}">
    <acme:button code="company.practicumSession.list.button.create" action="/company/practicum-session/create?masterId=${masterId}"/>
</jstl:if>