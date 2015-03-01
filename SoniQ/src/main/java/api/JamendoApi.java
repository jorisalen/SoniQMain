package api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.lightcouch.CouchDbClient;
import org.lightcouch.DocumentConflictException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JamendoApi implements Api {

	
	private CouchDbClient dbClient1 = new CouchDbClient("couchdb.properties");

	public void doCall(String query) throws ApiException {
		 ApiCaller call = new ApiCaller();
		 String response;
         try {
			response = call.doGet("https://api.jamendo.com/v3.0/tracks/?client_id=c3295b93&format=json&limit=50&namesearch="+ URLEncoder.encode(query,"UTF-8") +"&include=musicinfo&groupby=artist_id");
		} catch (UnsupportedEncodingException e) {
			throw new ApiException("calling the jamendo api has failed");
		} catch (IOException e) {
			throw new ApiException("calling the jamendo api has failed");
		}
         
         saveResponse(response);
	}

	
	private void saveResponse(String response) {
		
		try{
		JsonParser jsonParser = new JsonParser();
        JsonElement jo = jsonParser.parse(response);
        JsonObject object = jo.getAsJsonObject();
        JsonArray ja = (JsonArray) object.get("results");
        	for (JsonElement e : ja){
                JsonObject json = e.getAsJsonObject();
                json.addProperty("username", json.get("artist_name").getAsString());
                json.addProperty("stream_url", json.get("audio").getAsString());
                json.addProperty("streamable", true);
                //jamendo gives us Strings as id, and we need longs, so i just cut out the letters
	               
                json.addProperty("id", json.get("id").getAsString());
                json.addProperty("duration", json.get("duration").getAsInt() * 1000);
                json.addProperty("type", "ja");
                json.addProperty("title", json.get("name").getAsString());

                dbClient1.save(json);
        	}      
        } catch(DocumentConflictException e){
			
		} finally {
			
		}
        
		
	}
}
