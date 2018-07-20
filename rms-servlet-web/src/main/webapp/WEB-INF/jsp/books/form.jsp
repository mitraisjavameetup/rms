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
    <rms:link type="stylesheet" href="css/styles.css?v=1.0"/>

    <!--[if lt IE 9]>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.js"></script>
    <![endif]-->
</head>

<body>
    <div class="demo-layout-transparent mdl-layout mdl-js-layout">
        <%@include file="/layout/menu.jsp" %>
        <main class="mdl-layout__content">
            <div class="mdl-card mdl-shadow--6dp">
                <div class="mdl-card__title mdl-color--primary mdl-color-text--white">
                    <h2 class="mdl-card__title-text">
                        <c:if test="${book.get().getBookId() != null}">
                            Update Book <c:out value = "${user.get().getUserName()}"/>
                        </c:if>
                        <c:if test="${book.get().getBookId() == null}">
                            Create New Book
                        </c:if>
                    </h2>
                </div>
                <c:if test="${book.get().getBookId() != null}">
                    <form action="update" method="post" name="book">
                </c:if>
                <c:if test="${book.get().getBookId() == null}">
                    <form action="insert" method="post" name="book">
                </c:if>
                <div class="mdl-card__supporting-text">
                    <input type="hidden" id="id"  name="id" value="${book.get().getBookId()}"/>
                    <div class="mdl-textfield mdl-js-textfield">
                        <input class="mdl-textfield__input" type="text" id="title"  name="title" value="${book.get().getTitle()}"/>
                        <label class="mdl-textfield__label" for="userpass">Title</label>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield">
                        <input class="mdl-textfield__input" type="text" id="author" name="author" value="${book.get().getAuthor()}"/>
                        <label class="mdl-textfield__label" for="username">Author</label>
                    </div>
                    <div class="mdl-textfield mdl-js-textfield">
                        <input class="mdl-textfield__input" type="text" id="price"  name="price" value="${book.get().getPrice()}"/>
                        <label class="mdl-textfield__label" for="userpass">Price</label>
                    </div>
                </div>
                <div class="mdl-card__actions mdl-card--border">
                    <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" type="submit"><i class="material-icons">create</i>Save</button>
                    <a class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" href="/rms-servlet-web/books/list"><i class="material-icons">redo</i>Back</a>
                </div>
                </form>
            </div>
        </main>
    </div>
</body>
</html>
