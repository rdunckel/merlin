package edu.wctc.rdunckel.merlin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wctc.rdunckel.merlin.MainActivity.MovieType;
import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;
import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;

public class RottenTomatoesMovieService implements MovieListService {

	private static final String API_KEY = "332nudq4ruv2hvzt95v672mz";
	private static final String HOST = "api.rottentomatoes.com";
	private static final String BASE_URI = "/api/public/v1.0";
	private static final String IN_THEATERS_URI = BASE_URI
			+ "/lists/movies/in_theaters.json" + "?apikey=" + API_KEY;
	private static final String ON_DVD_URI = BASE_URI
			+ "/lists/dvds/current_releases.json" + "?apikey=" + API_KEY;

	private HttpJsonService jsonService;
	private MovieType movieType;

	public RottenTomatoesMovieService(MovieType movieType) {
		this.movieType = movieType;
		jsonService = new HttpJsonService(HOST);
	}

	@Override
	public List<Movie> getMovies() {
		List<Movie> movieList = new ArrayList<Movie>();
		switch (movieType) {
		case IN_THEATERS:
			movieList = getInTheatersMovies();
			break;
		case ON_DVD:
			movieList = getOnDvdMovies();
			break;
		default:
			movieList = getInTheatersMovies();
			break;
		}

		return movieList;
	}

	@Override
	public Rating getRatingForMovie(String movieId) {
		// TODO Not implemented yet!
		return null;
	}

	public List<Movie> getInTheatersMovies() {
		return getMovies(new HttpGet(IN_THEATERS_URI));
	}

	public List<Movie> getOnDvdMovies() {
		return getMovies(new HttpGet(ON_DVD_URI));
	}

	private List<Movie> getMovies(HttpGet httpGet) {
		String movieJson = "";
		try {
			movieJson = jsonService.getJson(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return processJson(movieJson);
	}

	@SuppressWarnings("unused")
	private List<Movie> processJson(String result) {

		List<Movie> movieList = new ArrayList<Movie>();

		JSONObject moviesJson;
		int total = 0;
		JSONArray movies;
		try {
			moviesJson = new JSONObject(result);
			total = moviesJson.getInt("total");
			movies = moviesJson.getJSONArray("movies");
		} catch (JSONException e) {
			e.printStackTrace();
			return movieList;
		}

		for (int i = 0; i < movies.length(); i++) {
			try {
				JSONObject movieJson = movies.getJSONObject(i);

				String id = movieJson.getString("id");
				String title = movieJson.getString("title");
				int year = movieJson.getInt("year");
				String mpaaRating = movieJson.getString("mpaa_rating");
				int runtime = movieJson.getInt("runtime");
				// String criticsConsensus =
				// movieJson.getString("critics_consensus");

				JSONObject releaseDates = movieJson
						.getJSONObject("release_dates");
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
					// String charactersArray =
					// castMember.getString("characters");
					// String[] characters = charactersArray != null ?
					// castMember
					// .getString("characters").split(",") : new String[0];

				}

				JSONObject alternateIds = movieJson
						.getJSONObject("alternate_ids");
				String imdbId = alternateIds.getString("imdb");

				JSONObject links = movieJson.getJSONObject("links");
				String selfLink = links.getString("self");
				String alternateLink = links.getString("alternate");
				String castLink = links.getString("cast");
				String clipsLink = links.getString("clips");
				String reviewsLink = links.getString("reviews");
				String similarLink = links.getString("similar");

				Movie movie = new Movie();
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
			} catch (JSONException je) {
				je.printStackTrace();
			}
		}

		return movieList;
	}
}
