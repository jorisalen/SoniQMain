<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<c:set var="title" value="search" scope="request" />

<jsp:include page="partials/heading.jsp" />

<body>


	<jsp:include page="partials/header.jsp" />
	<jsp:include page="partials/auth0.jsp" />

	<div id="contentSearch">
		<div id="result">

			<p class="searchQuery">You've searched for: ${query}</p>
			<form method="POST" action="Controller?action=search">
				<input name="query" class="form-control" placeholder="search">
			</form>






			<table class="table-striped">
				<tbody>
					<c:forEach var="record" items="${records}">
						<tr>
							<td><img src="./img/${record.type}.png" width="20px"></td>
							<td class="player"><a href="#" data-src=${record.link}>${record.title}</a></td>

							<c:if test="${not empty user}">
								<td><a
									href="Controller?action=upvote&id=${record.recordId}&query=${query}"
									class="upvote"> ${record.upvotes} </a></td>
								<td><a
									href="Controller?action=downvote&id=${record.recordId}&query=${query}"
									class="downvote "> ${record.downvotes} </a></td>
								<td><a
									href="Controller?action=comment&id=${record.recordId}"
									class="btn">comments</a></td>
								<td><a data-id="${record.recordId}"
									data-link="${record.link}" data-title="${record.title}"
									data-duration="${record.duration}" class="btn open-add"
									data-toggle="modal" data-target="#addToPlaylist">+</a></td>
							</c:if>

						</tr>

					</c:forEach>
				</tbody>
			</table>

		</div>
		<div id="playlist">
			<h1>My playlists</h1>
			<table>
				<tbody>

					<c:if test="${empty user}">
						<p>You must be logged in to create and use playlists! So sign
							up now and be a real hipster!
					</c:if>

					<c:if test="${not empty user}">
						<c:forEach var="playlist" items="${playlist}">
							<tr>
								<td class="playListName">${playlist.naam}</td>
								<td><a data-id="${playlist.playListId}"
									class="btn btn-primary open-share" data-toggle="modal"
									data-target="#sharePlaylist">Share</a></td>
								<td><a href="#" class="btn btn-success">Play</a></td>
							</tr>

						</c:forEach>
					</c:if>


				</tbody>
			</table>
		</div>
		<div id="artistinfo">
			<table>
				<tbody>
					<tr><h4>Artist info</h4></tr>
					<tr><div class="eigenschap">Info</div> ${summary}</tr>


				</tbody>
			</table>
		</div>
	</div>



	<div class="modal fade" id="sharePlaylist" tabindex="-1" role="dialog"
		aria-labelledby="registreer" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="registreer">Share your playlists
						with other users</h4>
				</div>
				<div class="modal-body">
					<form class="form-signin" method="POST"
						action="Controller?action=addToPlayList" autocomplete="on">
						<input type="hidden" id="query" name="query" class="form-control"
							placeholder="${query}" value="${query}"> <input
							type="hidden" id="playListId" name="playListId"
							class="form-control"> <input type="text" id="email"
							name="email" class="form-control" placeholder="email" required
							autofocus> <span class="help-block"></span>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						aria-hidden="true">Close</button>
					<button type="submit" class="btn btn-primary">Share</button>
				</div>
				</form>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>





	<div class="modal fade" id="addToPlaylist" tabindex="-1" role="dialog"
		aria-labelledby="addToPlaylist" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">Add this link to one of your playlists
						or create a new one</h4>
				</div>
				<div class="modal-body">
					<form action="Controller?action=addToPlaylist" method="POST">
						<select class="form-control" name="playlistSelect">
							<c:forEach var="playlist" items="${playlist}">
								<option value="${playlist.playListId}">${playlist.naam}</option>
							</c:forEach>
						</select> <input type="hidden" id="recordId" name="recordId">

						<button type="submit" class="btn btn-primary">Add</button>
					</form>





					<form action="Controller?action=createPlaylist" method="POST">

						<input type="hidden" id="recordId" name="recordId"> <input
							type="hidden" id="link" name="link"> <input type="hidden"
							id="duration" name="duration"> <input type="hidden"
							id="title" name="title"> <input type="hidden" id="query"
							name="query" value="${query}"> <input type="text"
							id="playlistName" name="playlistName" class="form-control"
							placeholder="name of your new playlist">
				</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-default" data-dismiss="modal"
						aria-hidden="true">Close</button>
					<button type="submit" class="btn btn-primary">Add</button>

					</form>
				</div>

			</div>

			</form>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->


	<jsp:include page="partials/footer.jsp"></jsp:include>

	<script>
		$(document).on("click", ".open-share", function() {
			var playListId = $(this).data('id');
			$(".modal-body #playListId").val(playListId);
		});

		$(document).on("click", ".open-add", function() {
			var recordId = $(this).data('id');
			$(".modal-body #recordId").val(recordId);

			var link = $(this).data('link');
			$(".modal-body #link").val(link);

			var title = $(this).data('title');
			$(".modal-body #title").val(title);

			var duration = $(this).data('duration');
			$(".modal-body #duration").val(duration);
		});
	</script>


	<script>
		$(function() {
			// Setup the player to autoplay the next track
			var a = audiojs.createAll({
				trackEnded : function() {
					var next = $('.player .playing').next();
					if (!next.length)
						next = $('.player').first();
					next.addClass('playing').siblings().removeClass('playing');
					audio.load($('a', next).attr('data-src'));
					audio.play();
				}
			});

			// Load in the first track
			var audio = a[0];
			first = $('.player').attr('data-src');
			$('.player').first().addClass('playing');
			audio.load(first);

			// Load in a track on click
			$('.player').click(function(e) {
				e.preventDefault();
				$(this).addClass('playing').siblings().removeClass('playing');
				audio.load($('a', this).attr('data-src'));
				audio.play();
			});
			// Keyboard shortcuts
			$(document).keydown(function(e) {
				var unicode = e.charCode ? e.charCode : e.keyCode;
				// right arrow
				if (unicode == 39) {
					var next = $('.player .playing').next();
					if (!next.length)
						next = $('.player').first();
					next.click();
					// back arrow
				} else if (unicode == 37) {
					var prev = $('.player.playing').prev();
					if (!prev.length)
						prev = $('.player').last();
					prev.click();
					// spacebar
				} else if (unicode == 32) {
					audio.playPause();
				}
			})
		});
	</script>

</body>
</html>