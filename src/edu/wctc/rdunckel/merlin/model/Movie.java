package edu.wctc.rdunckel.merlin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Movie implements Serializable {

	private String title;
	private String synopsis;
	private List<Rating> ratings;
	private String imageLink;
	private String imdbId;

	public Movie() {
		this("");
	}

	public Movie(String title) {
		this.title = title;
		ratings = new ArrayList<Rating>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void addRating(Rating rating) {
		ratings.add(rating);
	}

	@Override
	public String toString() {
		return title + ratings;
	}

}
