<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<tiles:useAttribute name="component" />
<tiles:useAttribute name="componentFullName"/>
<tiles:useAttribute name="componentFullNameWithDots"/>
<tiles:useAttribute name="viewName" ignore="true"/>
<tiles:useAttribute name="pluginIdentifier" ignore="true"/>

<tiles:insertTemplate template="formComponent.jsp">
	<tiles:putAttribute name="component" value="${component}" />
	<tiles:putAttribute name="componentType" value="textArea" />
	<tiles:putAttribute name="componentFullName" value="${componentFullName}" />
	<tiles:putAttribute name="componentFullNameWithDots" value="${componentFullNameWithDots}" />
	<tiles:putAttribute name="viewName" value="${viewName}" />
	<tiles:putAttribute name="pluginIdentifier" value="${pluginIdentifier}" />
	<tiles:putAttribute name="componentBody">
<<<<<<< HEAD
		<textarea id="${componentFullName}_input" name="fields[${component.name}]"></textarea>
=======
		<textarea id="${componentFullName}_input" name="fields[${component.name}]" rows="${component.rows}"></textarea>
>>>>>>> 99edbda9d6dfafbb20652b814000f51a7130f540
	</tiles:putAttribute>
</tiles:insertTemplate>