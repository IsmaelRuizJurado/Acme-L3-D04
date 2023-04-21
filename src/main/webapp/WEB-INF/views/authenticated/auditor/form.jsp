<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.auditor.form.label.professionalId" path="professionalId"/>
	<acme:input-textbox code="authenticated.auditor.form.label.firm" path="firm"/>
	<acme:input-textbox code="authenticated.auditor.form.label.certifications" path="certifications"/>
	<acme:input-textbox code="authenticated.auditor.form.label.link" path="link"/>
	<acme:submit code="authenticated.auditor.form.submit" action="/authenticated/auditor/create"/>
</acme:form>