package edu.wctc.rdunckel.merlin;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import edu.wctc.rdunckel.merlin.MainActivity.IntentKeys;
import edu.wctc.rdunckel.merlin.MainActivity.MovieType;
import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;
import edu.wctc.rdunckel.merlin.layout.MovieArrayAdapter;
import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;
import edu.wctc.rdunckel.merlin.service.ImdbMovieService;
import edu.wctc.rdunckel.merlin.service.MovieListService;
import edu.wctc.rdunckel.merlin.service.MovieListServiceFactory;

public class MovieListActivity extends ListActivity {

	private static final String MOVIE_LIST = "MovieList";
	private MovieListService movieService;
	private MovieType movieTypeSelection;
	private Movie[] moviesArray;
	private SharedPreferences userPrefs;
	private Editor userPrefsEditor;
	private MovieArrayAdapter movieAdapter;
	private List<Movie> movies;
	private Bundle stateBundle;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // Always call the superclass first

		Intent intent = getIntent();
		movieTypeSelection = MovieType.valueOf(intent
				.getStringExtra(IntentKeys.MOVIE_TYPE_KEY.name()));

		stateBundle = (savedInstanceState != null) ? savedInstanceState
				: intent.getExtras();

		userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		userPrefsEditor = userPrefs.edit();

		// Check whether we're recreating a previously destroyed instance
		if (stateBundle != null) {
			movies = (List<Movie>) stateBundle.getSerializable(MOVIE_LIST);
		}

		if (movies == null) {

			movieService = MovieListServiceFactory.INSTANCE
					.getMovieListService(movieTypeSelection);

			movies = movieService.getMovies();

			MovieListService imdbService = new ImdbMovieService();

			for (Movie movie : movies) {
				String imdbId = movie.getImdbId();
				Rating imdbRating = imdbService.getRatingForMovie(imdbId);
				movie.addRating(imdbRating);
			}
		}

		ListView listView = getListView();
		moviesArray = movies.toArray(new Movie[movies.size()]);
		movieAdapter = new MovieArrayAdapter(this, moviesArray);
		listView.setAdapter(movieAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Movie selectedMovie = moviesArray[position];

				Toast.makeText(getApplicationContext(),
						selectedMovie.getSynopsis(), Toast.LENGTH_SHORT).show();
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getApplicationContext(),
						MovieDetailActivity.class);

				Movie selectedMovie = moviesArray[position];

				intent.putExtra(IntentKeys.MOVIE_KEY.name(), selectedMovie);

				startActivity(intent);
				return false;
			}
		});

		listView.setLongClickable(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		// Save the user's current game state
		outState.putSerializable(MOVIE_LIST, (Serializable) movies);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movie_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.context_1:
			final CharSequence[] options = new CharSequence[] {
					RatingProvider.ROTTEN_TOMATOES.name(),
					RatingProvider.IMDB.name() };

			Arrays.sort(options);
			int position = Arrays.binarySearch(options, userPrefs.getString(
					IntentKeys.PREFERRED_PROVIDER_KEY.name(),
					RatingProvider.ROTTEN_TOMATOES.name()));

			new AlertDialog.Builder(this)
					.setSingleChoiceItems(options, position, null)
					.setPositiveButton(R.string.ok_button_label,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
									int selectedPosition = ((AlertDialog) dialog)
											.getListView()
											.getCheckedItemPosition();

									userPrefsEditor.putString(
											IntentKeys.PREFERRED_PROVIDER_KEY
													.name(),
											options[selectedPosition]
													.toString());
									userPrefsEditor.commit();

									stateBundle.putSerializable(MOVIE_LIST,
											(Serializable) movies);
									getIntent().putExtras(stateBundle);

									startActivity(getIntent());
								}
							}).show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.movie_list, menu);
	}

}
