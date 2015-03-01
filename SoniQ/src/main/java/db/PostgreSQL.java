package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import domain.Comment;
import domain.Member;
import domain.PlayList;
import domain.Record;

public class PostgreSQL implements Database {

	Connection dbConnection = null;

	public PostgreSQL() throws DbException {

		try {
			Class.forName("org.postgresql.Driver");
			dbConnection = DriverManager.getConnection(ConnectionData.url,
					ConnectionData.user, ConnectionData.password);
			dbConnection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new DbException(e.getMessage());
		}

	}
	
	/*
	 * 
	 * MEMBER functies
	 * 
	 * 
	 * 
	 * (non-Javadoc)
	 * @see db.Database#addMember(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */

	public boolean doesUserExist(String userId) throws DbException{
		
		
		Member user = null;
		try {
			PreparedStatement getUser = dbConnection
					.prepareStatement("select * from users where userid = ? or idfacebook = ? or idsoundcloud =? or idgoogleplus = ?");
			getUser.setString(1, userId);
			getUser.setString(2, userId);
			getUser.setString(3, userId);
			getUser.setString(4, userId);

			ResultSet result = getUser.executeQuery();

			if (result.next()) {
				user = convertToUser(result);
			} else {
				throw new DbException("geen user gevonden met dit id");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		if(user == null){
			return false;
		}
		return true;	
	}
	
	public void addMember(String userId, String idFacebook, String idSoundcloud,String idGoogleplus,String name,String nickname,String email,String picture) throws DbException {

		try {
			PreparedStatement addMember = dbConnection
					.prepareStatement("insert into users(userId, idFacebook, idSoundcloud, idGoogleplus, name, nickname, email,picture) values(?,?,?,?,?,?,?,?)");
			addMember.setString(1, userId);
			addMember.setString(2, idFacebook);
			addMember.setString(3, idSoundcloud);
			addMember.setString(4, idGoogleplus);
			addMember.setString(5, name);
			addMember.setString(6, nickname);
			addMember.setString(7, email);
			addMember.setString(8, picture);

			if (addMember.executeUpdate() < 1) {
				throw new DbException("failed to add member");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public Member getMember(String id) throws DbException {
		Member user = null;
		try {
			PreparedStatement getUser = dbConnection
					.prepareStatement("select * from users where userid = ? ");
			getUser.setString(1, id);
			ResultSet result = getUser.executeQuery();

			if (result.next()) {
				user = convertToUser(result);
			} else {
				throw new DbException("geen user gevonden met dit id");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		return user;
	}

	private Member convertToUser(ResultSet result) throws DbException {
		// String naam, String vnaam, String pw, String email, Date geb_datum
		Member user = null;
		try {
			user = new Member(result.getString("naam"), "pw",
					result.getString("voornaam"), result.getString("email"),
					null);

		} catch (SQLException e) {
			throw new DbException("user convertion went wrong", e);
		}
		return user;
	}
	
	/*
	 * 
	 * COMMENT function
	 * 
	 * 
	 * (non-Javadoc)
	 * @see db.Database#addComment(java.lang.String, java.lang.String, int)
	 */

	public void addComment(String content, String email, int recordId)
			throws DbException {

		try {
			PreparedStatement addComment = dbConnection
					.prepareStatement("insert into comment(content, email, recordId, timestamp) values(?,?,?,now())");
			addComment.setString(1, content);
			addComment.setString(2, email);
			addComment.setInt(3, recordId);

			if (addComment.executeUpdate() < 1) {
				throw new DbException("failed to add comment");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	private ArrayList<Comment> convertToCommentList(ResultSet result)
			throws DbException {
		ArrayList<Comment> messages = new ArrayList<Comment>();
		try {
			while (result.next()) {
				messages.add(convertToComment(result));
			}
		} catch (SQLException e) {
			throw new DbException("comment to list convertion went wrong", e);
		}
		return messages;
	}
	
	private Comment convertToComment(ResultSet result) throws DbException {
		// String content,String userId,int recordId
		Comment comment = null;
		try {

			comment = new Comment(result.getString("content"),
					result.getString("email"), result.getInt("recordid"));
//			comment.setNaam(result.getString("voornaam") + " " + result.getString("naam"));

		} catch (SQLException e) {
			throw new DbException("comment convertion went wrong", e);
		}
		return comment;
	}

	public Comment getComment(int id) {
		return null;
	}
	

	public ArrayList<Comment> getCommentsForRecord(int recordId)
			throws DbException {
		ArrayList<Comment> comments = null;
		try {
			PreparedStatement getCommentsForRecord = dbConnection
					.prepareStatement(""
			+ "select * from comment where recordid = ?");
			getCommentsForRecord.setInt(1, recordId);
			ResultSet result = getCommentsForRecord.executeQuery();
			comments = convertToCommentList(result);
			// No check needed for users can have an empty messagebox
		} catch (SQLException e) {
			throw new DbException("Error while getting comments for record", e);
		}
		return comments;
	}
	
	
	/*
	 * 
	 * 
	 * Record functions
	 * 
	 * 
	 * (non-Javadoc)
	 * @see db.Database#addRecord(long, java.lang.String, int, java.lang.String)
	 */

	public void addRecord(long recordId, String link, int duration, String title) throws DbException {

		
		try {
			
			if(getRecord(recordId) == null){
				PreparedStatement addRecord = dbConnection
						.prepareStatement("insert into record(recordId, link, duration, title) values(?,?,?,?)");
				addRecord.setInt(1, (int)recordId);
				addRecord.setString(2, link);
				addRecord.setInt(3, duration);
				addRecord.setString(4, title);
	
				
				if (addRecord.executeUpdate() < 1) {
					throw new DbException("failed to add record");
				}
			
			}
		} catch (SQLException e) {
			throw new DbException("failed to add record:" + e.getMessage());
		}
	}

	public Record getRecord(long id) throws DbException {
		Record record = null;
		try {
			PreparedStatement getRecord = dbConnection
					.prepareStatement("select * from record where recordid = ?");
			getRecord.setLong(1, id);
			ResultSet result = getRecord.executeQuery();

			if (result.next()) {
				record = convertToRecord(result);
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		return record;
	}
	
	private Record convertToRecord(ResultSet result) throws DbException {
		// String content,String userId,int recordId
				Record record = null;
				try {

					record = new Record(result.getString("link"));
					record.setDuration(Integer.parseInt(result.getString("duration")));
					record.setTitle(result.getString("title"));
					

				} catch (SQLException e) {
					throw new DbException("record convertion went wrong", e);
				}
				return record;
	}
	
	public void addRecordToPlayList(int recordId, int playlistId) throws DbException {
		  try {
            PreparedStatement addRecord = dbConnection
                            .prepareStatement("insert into recordplaylist(recordid, playlistid) values(?,?)");
            addRecord.setInt(1, recordId);
            addRecord.setInt(2, playlistId);
            if (addRecord.executeUpdate() < 1) {
                    throw new DbException("failed to add record to playlist");
            }
		  } catch (SQLException e) {
            throw new DbException("failed to add record to playlist");
		  }

	}

	public ArrayList<Record> getRecords() {
		return null;
	}
	
	/*
	 * Playlist functions
	 * 
	 * (non-Javadoc)
	 * @see db.Database#getPlayListId(java.lang.String)
	 */

	public int getPlayListId(String naam) throws DbException {

		int playlistId = 0;
		try {
			PreparedStatement getPlaylistId = dbConnection
					.prepareStatement("select playlistid from playlist where naam = ? limit 1");
				getPlaylistId.setString(1, naam);
				ResultSet result = getPlaylistId.executeQuery();
				
				result.next();
			    playlistId = result.getInt(1);
				
		} catch (SQLException e) {
			throw new DbException("failed to get playlistId");
		}
		
		return playlistId;
		
	}

	public void createPlayList(String naam, String email) throws DbException {
		
		try {
			PreparedStatement addPlaylist = dbConnection
					.prepareStatement("insert into playlist(naam) values(?)");
			addPlaylist.setString(1, naam);

			
			if (addPlaylist.executeUpdate() < 1) {
				throw new DbException("failed to create playlist");
			}
			
			PreparedStatement addToUserPlaylists = dbConnection
					.prepareStatement("insert into userplaylist(playlistid, email) values(?,?)");
			addToUserPlaylists.setInt(1, this.getPlayListId(naam));
			addToUserPlaylists.setString(2, email);
			
			
			if (addToUserPlaylists.executeUpdate() < 1) {
				throw new DbException("failed to add playlist to userPlaylist table");
			}
		} catch (SQLException e) {
			throw new DbException("failed to add playlist" + e.getMessage());
		}
		

	}

	public void sharePlayList(String username, int playListId) {

	}
	
	

	public ArrayList<PlayList> getPlayListsForUser(String email) throws DbException {
		ArrayList<PlayList> playlist = null;
		
		try {
			PreparedStatement getPlaylistsForUser = dbConnection
					.prepareStatement("select playlistid, naam from playlist inner join userplaylist using (playlistid) where email = ?");
			getPlaylistsForUser.setString(1, email);
			ResultSet result = getPlaylistsForUser.executeQuery();
			playlist = convertToPlaylists(result);
		} catch (SQLException e) {
			throw new DbException("Error while getting playlist for user", e);
		}
		
		return playlist;
	}
	
	private ArrayList<PlayList> convertToPlaylists(ResultSet result)
			throws DbException {
		ArrayList<PlayList> playlists = new ArrayList<PlayList>();
		try {
			while (result.next()) {
				playlists.add(convertToPlaylist(result));
			}
		} catch (SQLException e) {
			throw new DbException("comment to list convertion went wrong", e);
		}
		return playlists;
	}

	private PlayList convertToPlaylist(ResultSet result) throws DbException {
			PlayList playlist = null;
			try {
//				String userId,String naam
				playlist = new PlayList( result.getString("playlistid"), result.getString("naam"));

			} catch (SQLException e) {
				throw new DbException("playlist convertion went wrong", e);
			}
			return playlist;
	}
	

	/*
	 * 
	 * misc. functions
	 * 
	 * (non-Javadoc)
	 * @see db.Database#login(java.lang.String, java.lang.String)
	 */




	public Member login(String email, String pass) {
		return null;
	}

	public boolean isUserAuthorized(int userId) {
		return false;
	}

	public void upvote(int recordId, String email) throws DbException {
		PreparedStatement upvote;
		try {
			upvote = dbConnection
					.prepareStatement("insert into vote(vote, recordid,email) values('t',?,?)");
			upvote.setInt(1,recordId);
			upvote.setString(2,email);
			if (upvote.executeUpdate() < 1) {
				throw new DbException("failed to upvote , error: ");
			}
		} catch (SQLException e) {
			throw new DbException("failed to upvote");

		}
	}

	public void downvote(int recordId, String email) throws DbException {
		PreparedStatement downvote;
		try {
			downvote = dbConnection
					.prepareStatement("insert into vote(vote, recordid,email) values('f',?,?)");
			downvote.setInt(1,recordId);
			downvote.setString(2,email);
			if (downvote.executeUpdate() < 1) {
				throw new DbException("failed to downvote , error: ");
			}
		} catch (SQLException e) {
			throw new DbException("failed to downvote");

		}
	}

	public int getUpvotes(int recordId) throws DbException {
		// select count(*) from vote where recordid = 166912941 and vote = 't'
		int upvotes = 0;
		try {
			try {
				PreparedStatement getUpvotes = dbConnection
						.prepareStatement("select count(*) from vote where recordid = ? and vote = 't'");
				getUpvotes.setInt(1, recordId);
					ResultSet result = getUpvotes.executeQuery();
					
				result.next();
				upvotes = result.getInt(1);
					
			} catch (SQLException e) {
				throw new DbException("failed to get upvotes : " + e.getMessage());
			}
		} catch (Exception e) {
			throw new DbException("failed to get upvotes : " + e.getMessage());
		}
		
		return upvotes;
	}

	public int getDownvotes(int recordId) throws DbException {
		// select count(*) from vote where recordid = 166912941 and vote = 'f'
		int downvote = 0;
		try {
			try {
				PreparedStatement getDownvotes = dbConnection
						.prepareStatement("select count(*) from vote where recordid = ? and vote = 'f'");
				getDownvotes.setInt(1, recordId);
					ResultSet result = getDownvotes.executeQuery();
					
				result.next();
				downvote = result.getInt(1);
					
			} catch (SQLException e) {
				throw new DbException("failed to get downvotes : " + e.getMessage());
			}
		} catch (Exception e) {
			throw new DbException("failed to get downvotes : " + e.getMessage());
		}
		
		return downvote;
	}

	

	

}
