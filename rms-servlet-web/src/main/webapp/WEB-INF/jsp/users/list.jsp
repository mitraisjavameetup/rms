<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="rms" uri="/WEB-INF/tags/implicit.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">

<title>RMS</title>
<meta name="description" content="Index">
<meta name="author" content="Mitrais">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet"
	href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
<rms:link type="stylesheet" href="css/styles.css?v=1.0" />

<!--[if lt IE 9]>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.js"></script>
  <![endif]-->
</head>

<body>
	<div class="mdl-layout mdl-js-layout mdl-color--grey-100 box-center">
		<c:if test="${statusSession eq 'fail'}">
			<p style="color: red; margin: 5px; font-size: 12px;">opps!
				fail to delete, please contact admin for further support.</p>
			<c:remove var="statusSession" scope="session" />
		</c:if>
		<!-- <c:remove var="message" scope="session" /> -->

		<main class="mdl-layout__content">
		<table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp">
			<thead>
				<tr>
					<th class="mdl-data-table__cell--non-numeric">User Name</th>
					<th>Password</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${users}" var="user">
					<tr>
						<td class="mdl-data-table__cell--non-numeric"><c:out
								value="${user.getUserName()}" /></td>
						<td class="mdl-data-table__cell--non-numeric"><c:out
								value="${user.getPassword()}" /></td>
						<td class="mdl-data-table__cell--non-numeric"><a
							href="/rms-servlet-web/users/form/<c:out value = '${user.getId()}'/>">Edit</a> | <a
							href="/rms-servlet-web/users/delete/<c:out value = '${user.getId()}'/>"
							onclick="return confirmDelete('${user.getUserName()}')">Delete</a>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan=3 class="mdl-data-table__cell--non-numeric"><a
						href="form"> &oplus; Add new </a></td>
				</tr>
				<tr>
					<td colspan=3 class="mdl-data-table__cell--non-numeric"><a
						href="/rms-servlet-web/users/bulkadd" onclick="return confirmBulkAdd()"> &oplus; Add 10 sample users </a></td>
				</tr>
			</tbody>
		</table>
		</main>
	</div>
	<script type="text/javascript">
		function confirmDelete(username) {
			return confirm("are you sure want to delete username " + username
					+ "?");
		}
		
		function confirmBulkAdd() {
			return confirm("are you sure want to add 10 sample users ?");
		}
	</script>
</body>
</html>
