package edu.wctc.rdunckel.merlin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;
import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;

public class ImdbMovieService implements MovieListService {

	private static final String OMDB_API_URI = "http://www.omdbapi.com/";

	@Override
	public List<Movie> getMovies() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rating getRatingForMovie(String movieId) {
		String imdbJson = "";
		String imdbRating = "";

		try {
			imdbJson = getImdbJson(movieId);
			imdbRating = processImdbJson(imdbJson);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Rating(RatingProvider.IMDB, imdbRating);
	}

	private String processImdbJson(String imdbJson) throws JSONException {
		JSONObject imdbInfo = new JSONObject(imdbJson);
		return imdbInfo.getString("imdbRating");

	}

	public static void test() {

	}

	private String getImdbJson(String imdbId) throws ClientProtocolException,
			IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient(
				new BasicHttpParams());

		HttpGet httppost = new HttpGet(OMDB_API_URI + "?i=tt" + imdbId);
		// Depends on your web service
		httppost.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String result = null;
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

		inputStream = entity.getContent();
		// json is UTF-8 by default i beleive
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, "UTF-8"), 8);
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		result = sb.toString();

		return result;
	}
}
