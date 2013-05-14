package edu.wctc.rdunckel.rotten;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;
import edu.wctc.rdunckel.merlin.model.Rating;

public class RottenListConnector {

	private static final String API_KEY = "332nudq4ruv2hvzt95v672mz";
	private static final String IN_THEATERS_URI = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json";
	private static final String HOST = "api.rottentomatoes.com";
	private static final String HTTP = "http://".intern();

	private static final String TAG = RottenListConnector.class.getSimpleName();

	private HttpHost mTarget;
	private DefaultHttpClient mClient;

	public RottenListConnector() {
		mTarget = new HttpHost(HOST);
		mClient = new DefaultHttpClient();
	}

	public List<edu.wctc.rdunckel.merlin.model.Movie> getInTheatersMovies() {
		List<edu.wctc.rdunckel.merlin.model.Movie> movies = getMovies();

		return movies;
	}

	private List<edu.wctc.rdunckel.merlin.model.Movie> getMovies() {
		String movieJson = "";
		try {
			movieJson = getJson();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<edu.wctc.rdunckel.merlin.model.Movie> movies = new ArrayList<edu.wctc.rdunckel.merlin.model.Movie>();
		try {
			movies = processJson(movieJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return movies;
	}

	//
	//
	// /**
	// * Execute a GET request.
	// *
	// * @param query
	// * The path of the request.
	// * @return a String containing the request result.
	// */
	// public String getHttp(String query) {
	// Log.d(TAG, "GET " + query);
	//
	// String hostname = mTarget.getHostName();
	//
	// Log.d(TAG, hostname);
	//
	// HttpGet get = new HttpGet(HTTP + hostname + query);
	// HttpEntity results = null;
	// try {
	// Log.d(TAG, "Executing GET");
	//
	// HttpResponse response = mClient.execute(mTarget, get);
	// results = response.getEntity();
	//
	// return EntityUtils.toString(results);
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.d(TAG, e.getMessage());
	// throw new ConnectivityFailure("Web Service Failure");
	// } finally {
	// if (results != null)
	// try {
	// results.consumeContent();
	// } catch (IOException e) {
	// }
	// }
	// }

	private Bitmap getImage(String imageUrl) throws IOException {

		URL newurl = new URL(imageUrl);
		Bitmap image = BitmapFactory.decodeStream(newurl.openConnection()
				.getInputStream());

		return image;
	}

	private String getJson() throws ClientProtocolException, IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient(
				new BasicHttpParams());
		// HttpPost httppost = new HttpPost(IN_THEATERS_URI + "?apikey=" +
		// API_KEY);
		HttpGet httppost = new HttpGet(IN_THEATERS_URI + "?apikey=" + API_KEY);
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

	private List<edu.wctc.rdunckel.merlin.model.Movie> processJson(String result)
			throws JSONException {

		List<edu.wctc.rdunckel.merlin.model.Movie> movieList = new ArrayList<edu.wctc.rdunckel.merlin.model.Movie>();

		JSONObject moviesJson = new JSONObject(result);

		int total = moviesJson.getInt("total");

		JSONArray movies = moviesJson.getJSONArray("movies");

		for (int i = 0; i < movies.length(); i++) {
			JSONObject movieJson = movies.getJSONObject(i);

			String id = movieJson.getString("id");
			String title = movieJson.getString("title");
			int year = movieJson.getInt("year");
			String mpaaRating = movieJson.getString("mpaa_rating");
			int runtime = movieJson.getInt("runtime");
			String criticsConsensus = movieJson.getString("critics_consensus");

			JSONObject releaseDates = movieJson.getJSONObject("release_dates");
			String theaterReleaseDate = releaseDates.getString("theater");

			JSONObject ratings = movieJson.getJSONObject("ratings");
			String criticsRating = ratings.getString("critics_rating");
			String criticsScore = ratings.getString("critics_score");
			String audienceRating = ratings.getString("audience_rating");
			String audienceScore = ratings.getString("audience_score");

			String synopsis = movieJson.getString("synopsis");

			JSONObject posterLinks = movieJson.getJSONObject("posters");
			String thumbnailPosterLink = posterLinks.getString("thumbnail");
			String profilePosterLink = posterLinks.getString("profile");
			String detailedPosterLink = posterLinks.getString("detailed");
			String originalPosterLink = posterLinks.getString("original");

			JSONArray cast = movieJson.getJSONArray("abridged_cast");
			for (int j = 0; j < cast.length(); j++) {
				JSONObject castMember = cast.getJSONObject(j);

				String name = castMember.getString("name");
				String castId = castMember.getString("id");
				// String charactersArray = castMember.getString("characters");
				// String[] characters = charactersArray != null ? castMember
				// .getString("characters").split(",") : new String[0];

			}

			JSONObject alternateIds = movieJson.getJSONObject("alternate_ids");
			String imdbId = alternateIds.getString("imdb");

			JSONObject links = movieJson.getJSONObject("links");
			String selfLink = links.getString("self");
			String alternateLink = links.getString("alternate");
			String castLink = links.getString("cast");
			String clipsLink = links.getString("clips");
			String reviewsLink = links.getString("reviews");
			String similarLink = links.getString("similar");

			edu.wctc.rdunckel.merlin.model.Movie movie = new edu.wctc.rdunckel.merlin.model.Movie();
			movie.setTitle(title);
			movie.setSynopsis(synopsis);
			movie.setImageLink(thumbnailPosterLink);
			movie.setImageLink(originalPosterLink);
			movie.setImageLink(profilePosterLink);
			movie.setImageLink(detailedPosterLink);
			movie.setImdbId(imdbId);
			movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES,
					criticsScore));

			movieList.add(movie);
		}

		return movieList;
	}
}
