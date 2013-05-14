package edu.wctc.rdunckel.merlin.model;

import java.io.Serializable;

import edu.wctc.rdunckel.merlin.MainActivity.RatingProvider;

@SuppressWarnings("serial")
public class Rating implements Serializable {

	private RatingProvider provider;
	private String score;

	public Rating(RatingProvider provider, String score) {
		this.provider = provider;
		this.score = score;
	}

	public RatingProvider getProvider() {
		return provider;
	}

	public void setProvider(RatingProvider provider) {
		this.provider = provider;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return provider + ": " + score;
	}

}
