package com.omdasoft.simcemovil.service.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.omdasoft.simcemovil.model.QuestionModel;

public class QuestionModelFunctions {
	public static List<QuestionModel> getPlaylists(JSONArray jsonArrayReviews) throws JSONException {
		int n = jsonArrayReviews.length();
		List<QuestionModel> playlists = new ArrayList<QuestionModel>();
		QuestionModelBuilder playlistBuilder = new QuestionModelBuilder();
		
		for(int i=0; i < n; i++){
			playlists.add(playlistBuilder.build(jsonArrayReviews.getJSONObject(i)));
		}
		
		return playlists;
	}
}
