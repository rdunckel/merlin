package edu.wctc.rdunckel.merlin.service;

import java.util.ArrayList;
import java.util.List;

import edu.wctc.rdunckel.merlin.MainActivity.MovieType;
import edu.wctc.rdunckel.merlin.model.Movie;

public class OldTheaterMovieListService {

	private List<Movie> movies;

	public OldTheaterMovieListService(MovieType type) {
		movies = getMovies(type);
	}

	public List<Movie> getMovies(MovieType type) {
		switch (type) {
		case IN_THEATERS:
			return buildTheaterMovies();
		case ON_DVD:
			return buildDvdMovies();
		default:
			return buildTheaterMovies();
		}
	}

	private List<Movie> buildTheaterMovies() {
		List<Movie> movies = new ArrayList<Movie>();

//		Movie movie = new Movie();
//		movie.setTitle("A Good Day To Die Hard");
//		movie.addRating(new Rating("RT", "16%"));
//		movies.add(movie);
//
//		movie = new Movie();
//		movie.setTitle("Identity Thief");
//		movie.addRating(new Rating("RT", "24%"));
//		movies.add(movie);
//
//		movie = new Movie();
//		movie.setTitle("Safe Haven");
//		movie.addRating(new Rating("RT", "13%"));
//		movies.add(movie);
//
//		movie = new Movie();
//		movie.setTitle("Escape From Planet Earth 3D");
//		movie.addRating(new Rating("RT", "27%"));
//		movies.add(movie);
//
//		movie = new Movie();
//		movie.setTitle("Warm Bodies");
//		movie.addRating(new Rating("RT", "78%"));
//		movies.add(movie);

		return movies;
	}

	private List<Movie> buildDvdMovies() {
		List<Movie> movies = new ArrayList<Movie>();

//		Movie movie = new Movie();
//		movie.setTitle("Dredd");
//		movie.addRating(new Rating("RT", "78%"));
//		movie.addRating(new Rating("IMDb", "7.1"));
//		movies.add(movie);
//
//		movie = new Movie();
//		movie.setTitle("The Twilight Saga: Breaking Dawn...");
//		movie.addRating(new Rating("RT", "48%"));
//		movies.add(movie);
//
//		movie = new Movie();
//		movie.setTitle("Seven Psychopaths");
//		movie.addRating(new Rating("RT", "82%"));
//		movies.add(movie);
//
//		movie = new Movie();
//		movie.setTitle("Taken 2");
//		movie.addRating(new Rating("RT", "21%"));
//		movies.add(movie);
//
//		movie = new Movie();
//		movie.setTitle("Paranormal Activity 4");
//		movie.addRating(new Rating("RT", "26%"));
//		movies.add(movie);

		return movies;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}

}
