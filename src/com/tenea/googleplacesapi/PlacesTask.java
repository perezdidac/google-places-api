/*
Copyright 2013 TENEA TECNOLOGÍAS. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:
 
   1. Redistributions of source code must retain the above copyright notice, this list of
      conditions and the following disclaimer.
 
   2. Redistributions in binary form must reproduce the above copyright notice, this list
      of conditions and the following disclaimer in the documentation and/or other materials
      provided with the distribution.
 
THIS SOFTWARE IS PROVIDED BY TENEA TECNOLOGÍAS ''AS IS'' AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL TENEA TECNOLOGÍAS OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
The views and conclusions contained in the software and documentation are those of the
authors and should not be interpreted as representing official policies, either expressed
or implied, of TENEA TECNOLOGÍAS.
 */

package com.tenea.googleplacesapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.tenea.googleplacesapi.places.Coordinates;
import com.tenea.googleplacesapi.places.Geometry;
import com.tenea.googleplacesapi.places.Place;

public class PlacesTask extends AsyncTask<PlacesQuery, Void, PlacesTaskResult> {

	private static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

	private PlacesTaskListener placerTaskListener;

	public PlacesTask(PlacesTaskListener placerTaskListener) {
		//
		this.placerTaskListener = placerTaskListener;
	}

	protected PlacesTaskResult doInBackground(PlacesQuery... params) {
		PlacesTaskResult result = new PlacesTaskResult();

		PlacesQuery placeQuery = params[0];

		// Build the URL
		String url = buildUrl(placeQuery);

		try {
			// Perform the GET
			String response = get(url);

			// No error during the get operation, so let's analyze
			// the content of the response
			List<Place> places = getPlaces(response);

			// Set the places
			result.setPlaces(places);
		} catch (Exception e) {
			// Some error has occurred
			result.setPlaces(new ArrayList<Place>());
		}

		return result;
	}

	protected void onPostExecute(PlacesTaskResult result) {
		List<Place> places = result.getPlaces();
		placerTaskListener.onPlacesReceived(places);
	}

	private String buildUrl(PlacesQuery placesQuery) {
		double radius = placesQuery.getRadius();
		Coordinates coordinates = placesQuery.getCoordinates();
		PlacesQueryOptions placesQueryOptions = placesQuery.getPlacesQueryOptions();
		String apiKey = placesQueryOptions.getApiKey();
		boolean sensor = placesQueryOptions.isSensor();

		String url = DIRECTIONS_URL;

		// Set location coordinates
		url += "location=";
		url += coordinates.getLatitude();
		url += ',';
		url += coordinates.getLongitude();

		// Set radius
		url += "&radius=";
		url += radius;

		// Set sensor
		url += "&sensor=";
		url += sensor;

		// Set key
		url += "&key=";
		url += apiKey;
		
		// Optional parameters
		String search = placesQueryOptions.getSearch();
		if (search != null) {
			// Set search
			url += "&keyword=";
			url += search;
		}

		return url;
	}

	private List<Place> getPlaces(String response) {
		List<Place> places = new ArrayList<Place>();

		// Build the JSON object
		JSONArray jsonPlaces;
		try {
			JSONObject jsonResponse = new JSONObject(response);
			jsonPlaces = jsonResponse.getJSONArray("results");
		} catch (JSONException e) {
			// Not well formatted
			return places;
		}

		// Loop through the full list of places
		for (int k = 0; k < jsonPlaces.length(); ++k) {
			Place place = new Place();

			// Get the place object
			JSONObject jsonPlace;
			try {
				jsonPlace = jsonPlaces.getJSONObject(k);
			} catch (JSONException e) {
				// No place
				continue;
			}

			// Get geometry
			try {
				Geometry geometry = getGeometry(jsonPlace);
				place.setGeometry(geometry);
			} catch (JSONException e) {
				// Error, but keep walking...
			}

			// Get types
			try {
				List<String> types = getTypes(jsonPlace);
				place.setTypes(types);
			} catch (JSONException e) {
				// Error, but keep walking...
			}

			// Get values
			try {
				String icon = jsonPlace.getString("icon");
				place.setIcon(icon);
			} catch (JSONException e) {
				// Error, but keep walking...
			}

			try {
				String id = jsonPlace.getString("id");
				place.setId(id);
			} catch (JSONException e) {
				// Error, but keep walking...
			}

			try {
				String name = jsonPlace.getString("name");
				place.setName(name);
			} catch (JSONException e) {
				// Error, but keep walking...
			}

			try {
				String reference = jsonPlace.getString("reference");
				place.setReference(reference);
			} catch (JSONException e) {
				// Error, but keep walking...
			}

			try {
				String vicinity = jsonPlace.getString("vicinity");
				place.setVicinity(vicinity);
			} catch (JSONException e) {
				// Error, but keep walking...
			}

			places.add(place);
		}

		return places;
	}

	private List<String> getTypes(JSONObject jsonPlace) throws JSONException {
		List<String> types = new ArrayList<String>();

		JSONArray jsonTypes = jsonPlace.getJSONArray("types");
		for (int k = 0; k < jsonTypes.length(); ++k) {
			String type = jsonTypes.getString(k);
			types.add(type);
		}

		return types;
	}

	private Geometry getGeometry(JSONObject jsonPlace) throws JSONException {
		Geometry geometry = new Geometry();

		// Parse geometry
		JSONObject jsonGeometry = jsonPlace.getJSONObject("geometry");

		// Location
		JSONObject jsonGeometryLocation = jsonGeometry.getJSONObject("location");
		Coordinates location = getCoordinates(jsonGeometryLocation);
		geometry.setLocation(location);

		// Viewport
		try {
			JSONObject jsonGeometryViewport = jsonGeometry.getJSONObject("viewport");
			JSONObject jsonGeometryViewportNortheast = jsonGeometryViewport.getJSONObject("northeast");
			JSONObject jsonGeometryViewportSouthwest = jsonGeometryViewport.getJSONObject("southwest");
			Coordinates northeast = getCoordinates(jsonGeometryViewportNortheast);
			Coordinates southwest = getCoordinates(jsonGeometryViewportSouthwest);
			geometry.setNortheast(northeast);
			geometry.setSouthwest(southwest);
		} catch (JSONException e) {
			// No viewport
		}
		
		return geometry;
	}

	private Coordinates getCoordinates(JSONObject json) throws JSONException {
		double latitude = json.getDouble("lat");
		double longitude = json.getDouble("lng");
		return new Coordinates(latitude, longitude);
	}

	private String get(String url) throws Exception {
		// Prepare return string
		String response;

		// Create the connection object
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		// Set some parameters
		HttpParams httpParams = httpClient.getParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);

		// Execute the call
		HttpResponse httpResponse = httpClient.execute(httpGet);

		// Analyze status response
		StatusLine statusLine = httpResponse.getStatusLine();
		if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
			// Get the response
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
		} else {
			// Unexpected status response
			throw new IOException(statusLine.getReasonPhrase());
		}

		return response;
	}

}