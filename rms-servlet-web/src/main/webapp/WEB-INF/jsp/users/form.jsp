<%@ page import="com.mitrais.rms.model.User"%>
<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="rms" uri="/WEB-INF/tags/implicit.tld"%>
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
	<div class="mdl-layout mdl-js-layout mdl-color--grey-100">
		<main class="mdl-layout__content">
		<div class="mdl-card mdl-shadow--6dp">
			<div class="mdl-card__title mdl-color--primary mdl-color-text--white">
				<h2 class="mdl-card__title-text">Acme Co.</h2>
			</div>
			<form action="/rms-servlet-web/users/form"
				onsubmit="return checkNewPassword()" method="post">

				<c:if test="${requestScope.status eq 'fail' }">
					<p style="color: red; margin: 5px; font-size: 12px;">opps!
						something's not right, please contact admin for further support.</p>
				</c:if>
				
				<div class="mdl-card__supporting-text">
					<label>Username:</label>
					<div class="mdl-textfield mdl-js-textfield">
						<input class="mdl-textfield__input" type="text" id="username"
							name="username" value="${requestScope.user.getUserName()}" /> <label
							class="mdl-textfield__label" for="username">Username</label>
					</div>

					<label>New Password:</label> <br>
					<c:if test="${requestScope.user.getId() ne 0 }">
						<label style="font-size: x-small; color: red;">Leave it
							empty if you want to keep the old password.</label>
					</c:if>
					<div class="mdl-textfield mdl-js-textfield">
						<input class="mdl-textfield__input" type="password" id="password"
							name="password" /> <label class="mdl-textfield__label"
							for="password">Password</label>
					</div>

					<label>Confirm New Password:</label>
					<div class="mdl-textfield mdl-js-textfield">
						<input class="mdl-textfield__input" type="password"
							id="confirmuserpass" id="confirmuserpass" name="confirmuserpass" />
						<label class="mdl-textfield__label" for="userpass">Confirm
							Password</label>
					</div>
					<input type="hidden" id="id" name="id"
						value="${requestScope.user.getId()}">

				</div>
				<div class="mdl-card__actions mdl-card--border">
					<input type="submit" value="Save"
						class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
				</div>
			</form>
		</div>
		</main>
	</div>

	<script type="text/javascript">
		function checkNewPassword() {
			var password = document.getElementById("password");
			var confirmPassword = document.getElementById("confirmuserpass");

			if (password.value != "" && password.value != confirmPassword.value) {
				alert("Confirm password is not matched");
				return false;
			}

			return true;
		}
	</script>

</body>
</html>
