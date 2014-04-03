package com.omdasoft.simcemovil.service.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.omdasoft.simcemovil.model.CodeModel;

public class CodeModelFunctions {
	public static List<CodeModel> getPlaylists(JSONArray jsonArrayReviews) throws JSONException {
		int n = jsonArrayReviews.length();
		List<CodeModel> playlists = new ArrayList<CodeModel>();
		CodeModelBuilder playlistBuilder = new CodeModelBuilder();
		
		for(int i=0; i < n; i++){
			playlists.add(playlistBuilder.build(jsonArrayReviews.getJSONObject(i)));
		}
		
		return playlists;
	}
}
