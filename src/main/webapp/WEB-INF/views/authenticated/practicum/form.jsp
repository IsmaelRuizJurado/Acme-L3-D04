<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form readonly = "true">
	<acme:input-textbox code="authenticated.practicum.form.label.code" path="code"/>
	<acme:input-textbox code="authenticated.practicum.form.label.title" path="title"/>
	<acme:input-textbox code="authenticated.practicum.form.label.abstractt" path="abstractt"/>
	<acme:input-textbox code="authenticated.practicum.form.label.goals" path="goals"/>
	<acme:input-textbox code="authenticated.practicum.form.label.estimatedTime" path="estimatedTime"/>
	<acme:input-textbox code="authenticated.practicum.form.label.company" path="company"/>
	<acme:input-textbox code="authenticated.practicum.form.label.course" path="course"/>
</acme:form>