<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script src="https://cdn.auth0.com/w2/auth0-widget-5.js"></script>





<div class="navbar navbar-default navbar-fixed-top">
	<div id="container">
		<div id="mainNav" class="navbar-header">
			<a class="navbar-brand" href="Controller?action=home"> SoniQ</a>
			<audio preload></audio>
			<div id=right>
				<div class="dropdown dropdown-menu-right">
					<button class="btn btn-default dropdown-toggle" type="button"
						id="menu1" data-toggle="dropdown">
						Settings <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu" aria-labelledby="menu1">

						<li><c:if test="${empty user}">
								<a onclick="widget.signin({state: '${state}'})">Login</a>
							</c:if> <c:if test="${not empty user}">
								<p class="user">Logged in as ${user.getEmail()}
								<p>
									<a href="Controller?action=logout">Logout</a>
							</c:if></li>








						<li role="presentation"><a role="menuitem" tabindex="-1"
							href="#">Profile <span class="tab"></span><img
								src="${picture}";></a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1"
							href="#">FB-likes</a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1"
							href="#">Soundcloud playlist</a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1"
							href="#">YouTube playlist</a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1"
							href="#">Google+ access</a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1"
							onclick="widget.signin({state: ''})">Log in</a></li>
						<!-- Indien user ingelogd is, zou het veldje log in moeten veranderen naar "Log out" (actie hiervoor dient ook te wijzigen) -->
					</ul>

				</div>

			</div>
		</div>

	</div>
</div>












<%-- <div class="navbar navbar-default navbar-fixed-top">
	<div class="long-art"></div>
	<div class="container">
		<div class="navbar-header">
			<button class="navbar-toggle" type="button" data-toggle="collapse"
				data-target="#navbar-main">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="logo 	navbar-brand" href="Controller?action=home">
				SoniQ</a>
		</div>
		<center>
			<div class="navbar-collapse collapse" id="navbar-main">
				<ul class="nav navbar-nav">
					<li></li>
				</ul>
				<span class="now-playing"></span>

				<audio preload></audio>
				<c:if test="${empty user}">
					<button class="btn btn-primary pull-right"
						onclick="widget.signin({state: '${state}'})">Login</button>
				</c:if>
				<c:if test="${not empty user}">
					<p class="user">Logged in as ${user.getEmail()}
					<p>
						<a class="pull-right btn btn-primary"
							href="Controller?action=logout">Logout</a>
				</c:if>
			</div>
		</center>
	</div>
</div> --%>
