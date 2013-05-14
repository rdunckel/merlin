package edu.wctc.rdunckel.merlin.service;

import java.util.ArrayList;
import java.util.List;

import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;
import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;

public class MockTheaterMovieListService implements MovieListService {

	private List<Movie> movies;

	public MockTheaterMovieListService() {
		movies = buildTheaterMovies();
	}

	private List<Movie> buildTheaterMovies() {
		List<Movie> movies = new ArrayList<Movie>();

		Movie movie = new Movie();
		movie.setTitle("A Good Day To Die Hard");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "16%"));
		movies.add(movie);

		movie = new Movie();
		movie.setTitle("Identity Thief");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "24%"));
		movies.add(movie);

		movie = new Movie();
		movie.setTitle("Safe Haven");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "13%"));
		movies.add(movie);

		movie = new Movie();
		movie.setTitle("Escape From Planet Earth 3D");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "27%"));
		movies.add(movie);

		movie = new Movie();
		movie.setTitle("Warm Bodies");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "78%"));
		movies.add(movie);

		return movies;
	}

	@Override
	public List<Movie> getMovies() {
		return movies;
	}

	@Override
	public Rating getRatingForMovie(String movieId) {
		// TODO Auto-generated method stub
		return null;
	}

}
