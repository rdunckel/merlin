package edu.wctc.rdunckel.merlin.service;

import java.util.ArrayList;
import java.util.List;

import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;
import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;

public class MockDvdMovieListService implements MovieListService {

	private List<Movie> movies;

	public MockDvdMovieListService() {
		movies = buildDvdMovies();
	}

	private List<Movie> buildDvdMovies() {
		movies = new ArrayList<Movie>();

		Movie movie = new Movie();
		movie.setTitle("Dredd");
		movie.setSynopsis("Dredd follows the story of...");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "78%"));
		movie.addRating(new Rating(RatingProvider.IMDB, "7.1"));
		movies.add(movie);

		movie = new Movie();
		movie.setTitle("The Twilight Saga: Breaking Dawn...");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "48%"));
		movies.add(movie);

		movie = new Movie();
		movie.setTitle("Seven Psychopaths");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "82%"));
		movies.add(movie);

		movie = new Movie();
		movie.setTitle("Taken 2");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "21%"));
		movies.add(movie);

		movie = new Movie();
		movie.setTitle("Paranormal Activity 4");
		movie.addRating(new Rating(RatingProvider.ROTTEN_TOMATOES, "26%"));
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
