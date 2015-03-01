package api;



public class ApiFactory {

	private static Api caller;
	
	public static Api createApi(String type) throws ApiException{
		
		if(type.equals("bandcamp")){
			caller = new BandcampApi();
		} else if(type.equals("soundcloud")){
			caller = new SoundcloudApi();
		} else if(type.equals("jamendo")){
			caller = new JamendoApi();
		}else {
			throw new ApiException("no such api");
		}
		
		return caller;
	}
	
}

