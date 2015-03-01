<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<c:set var="title" value="index" scope="request" />
<jsp:include page="partials/heading.jsp" />

<body>
	<script src="https://cdn.auth0.com/w2/auth0-widget-5.js"></script>
	<jsp:include page="partials/auth0.jsp" />



	<jsp:include page="partials/header.jsp" />

	<div class='container'>
		<p class="logoMain">SoniQ</p>
		<div class='row'>
			<form method="POST" action="Controller">
				<div class='input-append' style="margin-top: 30%; margin-left: 10%">
					<input name="query" class="form-control" id="main_search"
						type="text">
				</div>

			</form>
			<div class="woordenwolk">
				<h2>Top search results for today</h2>
				<c:forEach var="artist" items="${artists}">
				<a href="Controller?action=search&query=${artist}">${artist}</a>
			</c:forEach>
			</div>
		</div>
	</div>

	<jsp:include page="partials/footer.jsp" />

</body>
</html>
