package api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class LastFmApi implements Api {

	@Override
	public void doCall(String zoekterm) throws ApiException {
		// Similar artists api
		ApiCaller call = new ApiCaller();
		String response;
		try {
			response = call
					.doGet("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist="
							+ URLEncoder.encode(zoekterm, "UTF-8")
							+ "&api_key=745dded8e08b7c4e3c30511010b79d48&format=json");
		} catch (UnsupportedEncodingException e) {
			throw new ApiException("calling the lastfm api has failed");
		} catch (IOException e) {
			throw new ApiException("calling the lastfm api has failed");
		}
		// Stuurt naar similarArtists voor de parse
//		similarArtists(response);
	}

	public ArrayList<String> getTags(String zoekterm) throws ApiException {
		ArrayList<String> lijst = null;
		ApiCaller call = new ApiCaller();
		try {
			String test6 = call
					.doGet("http://ws.audioscrobbler.com/2.0/?method=artist.getTags&artist="
							+ URLEncoder.encode(zoekterm, "UTF-8")
							+ "&user=RJ&api_key=745dded8e08b7c4e3c30511010b79d48&format=json");
			JsonParser jsonParser = new JsonParser();
			JsonElement je = jsonParser.parse(test6);
			JsonArray ja = je.getAsJsonObject().getAsJsonObject("tags")
					.getAsJsonArray("tag");
			for (JsonElement e : ja) {
				lijst.add(e.getAsJsonObject().get("name").toString());
			}
		} catch (IOException e) {
			e.getMessage();
		}
		return lijst;
	}

	public ArrayList<String> getSimilarArtists(String response) {
		ApiCaller call = new ApiCaller();
		ArrayList<String> lijst = new ArrayList<String>();
		try {
			String json = call.doGet("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist="
					+ URLEncoder.encode(response, "UTF-8")
					+ "&api_key=745dded8e08b7c4e3c30511010b79d48&format=json");
			JsonParser jsonParser = new JsonParser();
			JsonElement je = jsonParser.parse(json);
			JsonArray ja = je.getAsJsonObject().getAsJsonObject("artist").getAsJsonObject("similar").getAsJsonArray("artist");
			for (JsonElement e : ja) {
				lijst.add(e.getAsJsonObject().get("name").toString());
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return lijst;
	}

	public HashMap<String, String> getTopAlbums(String zoekterm) {
		ApiCaller call = new ApiCaller();
		HashMap<String, String> lijst = null;
		try {
			String test6 = call
					.doGet("http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&artist="
							+ URLEncoder.encode(zoekterm, "UTF-8")
							+ "&api_key=745dded8e08b7c4e3c30511010b79d48&format=json");
			JsonParser jsonParser = new JsonParser();
			System.out.println(test6);
			JsonElement je = jsonParser.parse(test6);
			JsonArray ja = je.getAsJsonObject().getAsJsonObject()
					.getAsJsonObject("topalbums").getAsJsonArray("album");
			for (JsonElement e : ja) {
				lijst.put(e.getAsJsonObject().get("name").toString(), e
						.getAsJsonObject().get("url").toString());
			}
		} catch (IOException e) {
			e.getMessage();
		}
		return lijst;
	}

	public HashMap<String, String> getEvents(String zoekterm) {
		ApiCaller call = new ApiCaller();
		HashMap<String, String> lijst = new HashMap<String, String>();
		try {
			String test6 = call
					.doGet("http://ws.audioscrobbler.com/2.0/?method=artist.getevents&artist="
							+ URLEncoder.encode(zoekterm, "UTF-8")
							+ "&api_key=745dded8e08b7c4e3c30511010b79d48&format=json");
			JsonParser jsonParser = new JsonParser();
			System.out.println(test6);
			JsonElement je = jsonParser.parse(test6);
			JsonArray ja = je.getAsJsonObject().getAsJsonObject()
					.getAsJsonObject("events").getAsJsonArray("event");
			for (JsonElement e : ja) {
				lijst.put(e.getAsJsonObject().get("name").toString(), e
						.getAsJsonObject().get("url").toString());
			}
		} catch (IOException e) {
			e.getMessage();
		}
		return lijst;
	}

	public ArrayList<String> getTopArtists() {
		ApiCaller call = new ApiCaller();
		ArrayList<String> lijst = new ArrayList<String>();
		try {
			String test6 = call
					.doGet("http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&api_key=745dded8e08b7c4e3c30511010b79d48&format=json");
			JsonParser jsonParser = new JsonParser();
			System.out.println(test6);
			JsonElement je = jsonParser.parse(test6);
			JsonArray ja = je.getAsJsonObject().getAsJsonObject()
					.getAsJsonObject("artists").getAsJsonArray("artist");
			for (JsonElement e : ja) {
				lijst.add(e.getAsJsonObject().get("name").getAsString());
			}
		} catch (IOException e) {
			e.getMessage();
		}
		return lijst;
	}

	public ArrayList<String> getSummary(String zoekterm) {
		ApiCaller call = new ApiCaller();
		ArrayList<String> lijst = null;
		try {
			String test6 = call
					.doGet("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist="
							+ URLEncoder.encode(zoekterm, "UTF-8")
							+ "&api_key=745dded8e08b7c4e3c30511010b79d48&format=json");
			JsonParser jsonParser = new JsonParser();
			System.out.println(test6);
			JsonElement je = jsonParser.parse(test6);
			String result = je.getAsJsonObject().getAsJsonObject("artist")
					.getAsJsonObject("bio").get("summary").toString();
		} catch (IOException e) {
			e.getMessage();
		}
		return lijst;
	}

}
