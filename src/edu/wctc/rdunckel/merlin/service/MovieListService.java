package edu.wctc.rdunckel.merlin.service;

import java.util.List;

import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;

public interface MovieListService {

	List<Movie> getMovies();

	Rating getRatingForMovie(String movieId);
}