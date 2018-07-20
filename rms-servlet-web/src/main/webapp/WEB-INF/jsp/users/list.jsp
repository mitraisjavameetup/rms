<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix = "rms" uri = "/WEB-INF/tags/link.tld"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">

        <title>RMS</title>
        <meta name="description" content="Index">
        <meta name="author" content="Mitrais">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
        <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
        <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
        <rms:link type="stylesheet" href="css/styles.css?v=1.0"/>

    <!--[if lt IE 9]>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.js"></script>
    <![endif]-->
</head>

<body>
    <div class="demo-layout-transparent mdl-layout mdl-js-layout">
        <%@include file="/layout/menu.jsp" %>
        <main class="mdl-layout__content">
            <a class="mdl-button mdl-js-button mdl-button--fab mdl-button--colored" href="new"><i class="material-icons">add</i></a>
            <hr>
            <table class="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp">
                <thead>
                    <tr>
                        <th class="mdl-data-table__cell--non-numeric">User Name</th>
                        <th>Password</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items = "${users}" var="user">
                    <tr>
                        <td class="mdl-data-table__cell--non-numeric"><c:out value = "${user.getUserName()}"/></td>
                        <td><c:out value = "${user.getPassword()}"/></td>
                        <td>
                            <a class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" href='edit?id=<c:out value = "${user.getId()}"/>'><i class="material-icons">create</i>Edit</a>
                            <a class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent delete-row" href='delete?id=<c:out value = "${user.getId()}"/>'><i class="material-icons">clear</i>Delete</a>
                        </td> 
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </main>
    </div>

    <script>
        $(document).ready(function () {            
            $(".delete-row").click(function () {
                var r = confirm("Are you sure, want to delete this data!");
                if (r == true) {

                } else {
                    return false;
                }
            });
        });
    </script>
</body>
</html>
