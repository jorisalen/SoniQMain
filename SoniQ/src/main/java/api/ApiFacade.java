package api;

import java.util.ArrayList;

import domain.Record;



public class ApiFacade {

	
	private Api apiCaller;
	
	
	public void callApi(String query,String type) throws ApiException{
		apiCaller = ApiFactory.createApi(type);
		apiCaller.doCall(query);
	}
	
	//bandcamp does not support playlists, otherwise i've would have made this a generic method
	 public ArrayList<Record> getPlaylistSoundcloud(String auth_token) throws ApiException {
		 SoundcloudApi api = new SoundcloudApi();
		 return api.getPlaylistSoundcloud(auth_token);
	 }

		
}
