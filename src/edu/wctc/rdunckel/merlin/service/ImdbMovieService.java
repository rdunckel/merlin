package edu.wctc.rdunckel.merlin.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;
import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;

public class ImdbMovieService implements MovieListService {

	private static final String HOST = "www.omdbapi.com";
	private HttpJsonService jsonService;

	public ImdbMovieService() {
		jsonService = new HttpJsonService(HOST);
	}

	@Override
	public List<Movie> getMovies() {
		// TODO Not implemented yet!
		return null;
	}

	@Override
	public Rating getRatingForMovie(String movieId) {
		HttpGet httpGet = new HttpGet("/?i=tt" + movieId);
		String imdbRating = "";

		try {
			String imdbJson = jsonService.getJson(httpGet);
			imdbRating = processImdbJson(imdbJson);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Rating(RatingProvider.IMDB, imdbRating);
	}

	private String processImdbJson(String imdbJson) {
		String rating = "";

		try {
			rating = new JSONObject(imdbJson).getString("imdbRating");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rating;

	}

}
