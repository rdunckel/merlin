package edu.wctc.rdunckel.merlin;

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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
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

	private MovieListService movieService;
	private MovieType movieTypeSelection;
	private Movie[] moviesArray;
	private SharedPreferences userPrefs;
	private Editor userPrefsEditor;
	private MovieArrayAdapter movieAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		userPrefsEditor = userPrefs.edit();

		Intent intent = getIntent();
		movieTypeSelection = MovieType.valueOf(intent
				.getStringExtra(IntentKeys.MOVIE_TYPE_KEY.name()));

		movieService = MovieListServiceFactory.INSTANCE
				.getMovieListService(movieTypeSelection);

		List<Movie> movies = movieService.getMovies();

		MovieListService imdbService = new ImdbMovieService();

		for (Movie movie : movies) {
			String imdbId = movie.getImdbId();
			Rating imdbRating = imdbService.getRatingForMovie(imdbId);
			movie.addRating(imdbRating);
		}

		// setListAdapter(new ArrayAdapter<Movie>(this,
		// R.layout.activity_movie_list, movies));

		// setListAdapter(new ArrayAdapter<Movie>(this,
		// android.R.layout.simple_expandable_list_item_2,
		// android.R.id.text1, movies));

		// ListView listView = getListView();
		// listView.setTextFilterEnabled(true);

		ListView listView = getListView();
		moviesArray = movies.toArray(new Movie[movies.size()]);
		movieAdapter = new MovieArrayAdapter(this, moviesArray);
		listView.setAdapter(movieAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView lineOne = (TextView) view
						.findViewById(android.R.id.text1);

				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), lineOne.getText(),
						Toast.LENGTH_SHORT).show();
			}
		});

		// registerForContextMenu(listView);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			// AlertDialog.Builder dialogBuilder = new
			// AlertDialog.Builder(this);
			// dialogBuilder
			// .setTitle("Preferred Ratings Provider")
			// .setMultiChoiceItems(
			// new CharSequence[] {
			// RatingProvider.ROTTEN_TOMATOES.name(),
			// RatingProvider.IMDB.name() }, null,
			// new OnMultiChoiceClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog,
			// int position, boolean selected) {
			// Log.d("Test", "Test");
			// }
			//
			// }).show();

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

									// movieAdapter.notifyDataSetChanged();
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		@SuppressWarnings("unused")
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.context_1:
			Log.d("MenuSelection", String.valueOf(item.getItemId()));
			return true;
		case R.id.context_2:
			Log.d("MenuSelection", String.valueOf(item.getItemId()));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

}
