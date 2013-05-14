package edu.wctc.rdunckel.merlin.service;

import edu.wctc.rdunckel.merlin.MainActivity.MovieType;

public enum MovieListServiceFactory {
	INSTANCE;

	public MovieListService getMovieListService(MovieType type) {
		MovieListService movieService = null;

		switch (type) {
		case IN_THEATERS:
			// movieService = new MockTheaterMovieListService();
			movieService = new RottenTomatoesMovieService();
			break;
		case ON_DVD:
			movieService = new MockDvdMovieListService();
			break;
		default:
			movieService = new TheaterMovieListService();
			break;
		}

		return movieService;
	}
}
