package edu.wctc.rdunckel.merlin.service;

import edu.wctc.rdunckel.merlin.MainActivity.MovieType;

public enum MovieListServiceFactory {
	INSTANCE;

	public MovieListService getMovieListService(MovieType type) {
		return new RottenTomatoesMovieService(type);
	}
}
