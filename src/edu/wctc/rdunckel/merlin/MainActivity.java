package edu.wctc.rdunckel.merlin;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

	public enum IntentKeys {
		MOVIE_TYPE_KEY, PREFERRED_PROVIDER_KEY, MOVIE_TITLE_KEY, MOVIE_RATING_KEY, MOVIE_KEY, STATE_BUNDLE_KEY
	}

	public enum MovieType {
		IN_THEATERS, ON_DVD
	}

	public enum RatingProvider {
		IMDB, ROTTEN_TOMATOES
	}

	private String theaterChoiceLabel;
	private String dvdChoiceLabel;
	private MovieType movieTypeSelection;
	private RatingProvider preferredProvider;
	private SharedPreferences userPrefs;
	private Editor userPrefsEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		theaterChoiceLabel = getString(R.string.label_in_theaters);
		dvdChoiceLabel = getString(R.string.label_on_dvd);
		userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		userPrefsEditor = userPrefs.edit();

		setContentView(R.layout.activity_main);
		setupActionBar();
		setupMovieTypeSpinner();
		setupPreferredProviderSpinner();
	}

	private void setupMovieTypeSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.movie_type_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout

		String[] movieTypes = { theaterChoiceLabel, dvdChoiceLabel };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, movieTypes);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String typeSelection = (String) parent.getItemAtPosition(pos);

				if (theaterChoiceLabel.equals(typeSelection)) {
					movieTypeSelection = MovieType.IN_THEATERS;
				} else {
					movieTypeSelection = MovieType.ON_DVD;
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void setupPreferredProviderSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.preferred_provider_spinner);

		List<String> providers = new ArrayList<String>();

		for (RatingProvider provider : RatingProvider.values()) {
			providers.add(provider.name());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, providers);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				preferredProvider = RatingProvider.valueOf((String) parent
						.getItemAtPosition(pos));

				// Preferences
				userPrefsEditor.putString(
						IntentKeys.PREFERRED_PROVIDER_KEY.name(),
						preferredProvider.name());

				userPrefsEditor.commit();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void goToListings(View view) {
		Intent intent = new Intent(this, MovieListActivity.class);
		intent.putExtra(IntentKeys.MOVIE_TYPE_KEY.name(),
				movieTypeSelection.name());
		intent.putExtra(IntentKeys.PREFERRED_PROVIDER_KEY.name(),
				preferredProvider.name());
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
	}

}
