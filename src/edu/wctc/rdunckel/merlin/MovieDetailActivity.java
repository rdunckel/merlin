package edu.wctc.rdunckel.merlin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import edu.wctc.rdunckel.merlin.MainActivity.IntentKeys;
import edu.wctc.rdunckel.merlin.model.Movie;
import edu.wctc.rdunckel.merlin.model.Rating;

public class MovieDetailActivity extends Activity {

	private Movie movie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_detail);

		Intent intent = getIntent();
		movie = (Movie) intent
				.getSerializableExtra(IntentKeys.MOVIE_KEY.name());

		TextView titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(movie.getTitle());

		TextView synopsisText = (TextView) findViewById(R.id.synopsisText);
		synopsisText.setText(movie.getSynopsis());

		ImageView movieImage = (ImageView) findViewById(R.id.moviePoster);

		URL newurl = null;
		Bitmap image = null;
		try {
			newurl = new URL(movie.getImageLink());
			image = BitmapFactory.decodeStream(newurl.openConnection()
					.getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		movieImage.setImageBitmap(image);

		TableLayout table = (TableLayout) findViewById(R.id.ratingsTable);
		buildTableLayout(table);
	}

	private TableLayout buildTableLayout(TableLayout table) {
		List<Rating> ratings = movie.getRatings();

		for (Rating rating : ratings) {
			TableRow ratingRow = new TableRow(this);

			TextView ratingLabel = new TextView(this);
			ratingLabel.setText(rating.getProvider().name() + ":     ");
			TextView ratingText = new TextView(this);
			ratingText.setText(rating.getScore());

			ratingRow.addView(ratingLabel);
			ratingRow.addView(ratingText);

			table.addView(ratingRow, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

		return table;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movie_detail, menu);
		return true;
	}

}
