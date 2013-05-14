package edu.wctc.rdunckel.merlin.service;

import java.util.List;

import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;
import edu.wctc.rdunckel.rotten.RottenListConnector;

public class RottenTomatoesMovieService implements MovieListService {

	@Override
	public List<Movie> getMovies() {

		RottenListConnector rottenList = new RottenListConnector();

		List<Movie> rottenMovies = rottenList.getInTheatersMovies();

		// List<Movie> movieList = transformResults(rottenList
		// .getInTheatersMovies());

		// return transformResults(rottenMovies);

		return rottenMovies;
	}

	@Override
	public Rating getRatingForMovie(String movieId) {
		// TODO Auto-generated method stub
		return null;
	}
}
