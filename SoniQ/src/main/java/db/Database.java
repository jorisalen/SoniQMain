package db;

import java.util.ArrayList;
import java.util.Date;

import domain.Comment;
import domain.Member;
import domain.PlayList;
import domain.Record;

public interface Database {

	
	public void addMember(String userId, String idFacebook, String idSoundcloud,String idGoogleplus,String name,String nickname,String email,String picture) throws DbException;
	public void addComment(String content, String email, int recordId) throws DbException;
	public void addRecord(long recordId, String link, int duration, String title) throws DbException;
	
	
	public Member getMember(String id) throws DbException;
	public Comment getComment(int id) throws DbException;
	public Record getRecord(long id) throws DbException;
	public boolean doesUserExist(String userId) throws DbException;
	
	public void createPlayList(String naam ,String userId) throws DbException;
	public void sharePlayList(String username, int playListId) throws DbException;
	public ArrayList<PlayList> getPlayListsForUser(String email) throws DbException;
	public int getPlayListId(String naam)throws DbException;
	public void addRecordToPlayList(int recordId, int playlistId) throws DbException;

	public ArrayList<Comment> getCommentsForRecord(int recordId) throws DbException;
	public ArrayList<Record> getRecords() throws DbException;
	public Member login(String email, String pass) throws DbException;
	public boolean isUserAuthorized(int userId) throws DbException;
	
	
	public void upvote(int recordId, String userid) throws DbException;
	public void downvote(int recordId, String userid) throws DbException;
	public int getUpvotes(int recordId) throws DbException;
	public int getDownvotes(int recordId) throws DbException;
		
}
