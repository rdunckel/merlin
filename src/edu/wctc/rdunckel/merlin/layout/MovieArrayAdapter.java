package edu.wctc.rdunckel.merlin.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import edu.wctc.rdunckel.merlin.MainActivity.IntentKeys;
import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;
import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;

public class MovieArrayAdapter extends TwoLineArrayAdapter<Movie> {

	private SharedPreferences userPrefs;
	private RatingProvider preferredProvider;

	public MovieArrayAdapter(Context context, Movie[] movies) {
		super(context, movies);
		userPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		preferredProvider = RatingProvider.valueOf(userPrefs.getString(
				IntentKeys.PREFERRED_PROVIDER_KEY.name(),
				RatingProvider.ROTTEN_TOMATOES.name()));
	}

	@Override
	public String lineOneText(Movie movie) {
		return movie.getTitle();
	}

	@Override
	public String lineTwoText(Movie movie) {
		Rating rating = getPreferredRating(movie, preferredProvider);

		return rating != null ? rating.getProvider().name() + ": "
				+ rating.getScore() : "";

	}

	private Rating getPreferredRating(Movie movie,
			RatingProvider preferredProvider) {

		List<Rating> newRatings = new ArrayList<Rating>(movie.getRatings());

		Iterator<Rating> ratingsIterator = newRatings.iterator();

		while (ratingsIterator.hasNext()) {
			if (ratingsIterator.next().getProvider() != preferredProvider) {
				ratingsIterator.remove();
			}
		}

		if (newRatings.size() > 0) {
			return newRatings.get(0);
		} else {
			return null;
		}
	}
}