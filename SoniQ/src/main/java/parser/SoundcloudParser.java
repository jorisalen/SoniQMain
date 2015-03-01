package parser;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import domain.Record;

public class SoundcloudParser implements Parser {


	public ArrayList<Record> parse(String json) {

		
		ArrayList<Record> records = new ArrayList<Record>();

		JsonParser parser = new JsonParser();
		JsonElement jo = parser.parse(json);
		JsonObject object = jo.getAsJsonObject();

		JsonObject ja = object.getAsJsonObject("hits");

		JsonArray array = ja.getAsJsonArray("hits");

		for (JsonElement e : array) {

			JsonObject ob = e.getAsJsonObject();

			JsonObject o = ob.get("_source").getAsJsonObject();
			
			String streamable = o.get("streamable").getAsString();

			if(streamable == "true"){
				String link = "";
				if(o != null){
					link =o.get("stream_url").getAsString();
				}
				
				String type = o.get("type").getAsString();
				
	      	  	String id = o.get("id").getAsString();
	      	  	
				if(type.equals("sc")){
					link += "?client_id=6371e86ce0b447f34f8ed56e5c2a7832";
				}
				
				// remove all character after the dot from the duration
				int duration = Integer.valueOf(o.get("duration").toString().replaceAll("\\..*", ""));
	
				Record r = new Record(link);
				
				r.setTitle((o.get("title").getAsString()));
				r.setRecordId(Long.valueOf(id));
				r.setDuration(duration);
				r.setType(type);
				
				records.add(r);
			}
		}

		return records;
	}
	
public ArrayList<Record> parsePlaylist(String json) {
		
		JsonParser parser = new JsonParser();
	    JsonElement jo = parser.parse(json);

        JsonArray array = jo.getAsJsonArray();
	    System.out.println(array);

        ArrayList<Record> records = new ArrayList<Record>();
        
        for (JsonElement e: array){
       	 JsonObject ob =  e.getAsJsonObject();

       	 String o =  ob.get("stream_url").toString();
       	 System.out.println(o);
       	 String link = ob.get("stream_url").toString();
 
       	 Record r = new Record(link);
       	 r.setRecordId(Long.parseLong(ob.get("id").toString()));
       	 r.setDuration((int)Double.parseDouble(ob.get("duration").toString()));
       	 records.add(r);
        }
        
		return records;
	}
	

public ArrayList<Record> parse(JsonObject json) {

	JsonElement lijst = json.get("soundcloud");
	JsonArray array = lijst.getAsJsonArray();

	ArrayList<Record> records = new ArrayList<Record>();

	for (int i = 0; i < array.size(); i++) {
		JsonObject object = array.get(i).getAsJsonObject();
		if (object.get("stream_url") != null) {

			String link = object.get("stream_url").toString();

			String duration = object.get("duration").toString();
			double du = Double.parseDouble(duration);
			String title = object.get("title").toString();

			link = link.substring(1, link.length() - 1);

			Record r = new Record(link);

			r.setTitle(title);
			r.setRecordId(Long.parseLong(object.get("id").toString()));
			r.setDuration((int) du * 1000);

			records.add(r);

		}
	}
	return records;
}

}


