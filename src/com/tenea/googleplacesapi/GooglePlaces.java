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

import java.util.List;

import com.tenea.googleplacesapi.places.Place;

public class GooglePlaces implements PlacesTaskListener {

	private PlacesListener placesListener;
	private PlacesTask placesTask;

	public GooglePlaces(PlacesListener placesListener) {
		this.placesListener = placesListener;
	}

	public void query(PlacesQuery placesQuery) {
		placesTask = new PlacesTask(this);

		placesTask.execute(placesQuery);
	}

	@Override
	public void onPlacesReceived(List<Place> places) {
		if (!places.isEmpty()) {
			placesListener.onPlacesReceived(places);
		} else {
			placesListener.onPlacesError();
		}
	}

	//
	// private AbstractSet<String> supportedPlaces;
	//
	// public GooglePlaces(String apiKey) {
	// this.apiKey = apiKey;
	// loadSupportedPlaces();
	// }
	//
	// public PlacesResult query(List<String> types, String keyword, int radius,
	// double lat, double lon)
	// throws JSONException, ClientProtocolException, IOException {
	// NearbySearchQuery query = new NearbySearchQuery(lat, lon);
	//
	// query.setRadius(radius);
	//
	// if (types != null) {
	// for (String type : types) {
	// query.addType(type);
	// }
	// }
	//
	// if (keyword != null && keyword != "") {
	// query.setKeyword(keyword);
	// }
	//
	// PlacesResult result = getPlaces(query);
	//
	// return result;
	// }
	//
	// public PlacesResult getPlaces(List<String> types, int radius, double lat,
	// double lon)
	// throws ClientProtocolException, JSONException, IOException {
	// return query(types, null, radius, lat, lon);
	// }
	//
	// public PlacesResult getPlaces(String type, String keyword, int radius,
	// double lat, double lon)
	// throws ClientProtocolException, JSONException, IOException {
	// List<String> types = Arrays.asList(type);
	// return query(types, keyword, radius, lat, lon);
	// }
	//
	// public PlacesResult getPlaces(String type, int radius, double lat, double
	// lon) throws ClientProtocolException,
	// JSONException, IOException {
	// return getPlaces(type, null, radius, lat, lon);
	// }
	//
	// public PlacesResult getPlaces(String searchText, double lat, double lon)
	// throws ClientProtocolException,
	// JSONException, IOException {
	// TextSearchQuery query = new TextSearchQuery(searchText);
	// query.setLocation(lat, lon);
	// PlacesResult result = getPlaces(query);
	//
	// return result;
	// }
	//
	// public PlacesResult getPlaces(String searchText) throws
	// ClientProtocolException, JSONException, IOException {
	// TextSearchQuery query = new TextSearchQuery(searchText);
	// PlacesResult result = getPlaces(query);
	//
	// return result;
	// }
	//
	// public PlacesResult getPlaces(Query query) throws JSONException,
	// ClientProtocolException, IOException {
	// JSONObject response = executeRequest(query.toString());
	// PlacesResult result = new PlacesResult(response);
	//
	// return result;
	// }
	//
	// public DetailsResult getPlaceDetails(String reference) throws
	// JSONException, ClientProtocolException, IOException {
	// DetailsQuery query = new DetailsQuery(reference);
	// DetailsResult result = getPlaceDetails(query);
	//
	// return result;
	// }
	//
	// public DetailsResult getPlaceDetails(Query query) throws JSONException,
	// ClientProtocolException, IOException {
	// JSONObject response = executeRequest(query.toString());
	// DetailsResult result = new DetailsResult(response);
	//
	// return result;
	// }
	//
	// public boolean isSupportedPlace(String placeType) {
	// return (supportedPlaces.contains(placeType));
	// }
	//
	// private JSONObject executeRequest(String query) throws
	// ClientProtocolException, IOException, JSONException {
	// query += "key=" + apiKey;
	//
	// HttpClient client = new DefaultHttpClient();
	// HttpGet request = new HttpGet(query);
	//
	// ResponseHandler<String> handler = new BasicResponseHandler();
	// String response = client.execute(request, handler);
	// JSONObject jsonResponse = new JSONObject(response);
	//
	// return jsonResponse;
	// }
	//
	// private void loadSupportedPlaces() {
	// supportedPlaces = new HashSet<String>();
	//
	// supportedPlaces.add("accounting");
	// supportedPlaces.add("airport");
	// supportedPlaces.add("amusement park");
	// supportedPlaces.add("aquarium");
	// supportedPlaces.add("art gallery");
	// supportedPlaces.add("atm");
	// supportedPlaces.add("bakery");
	// supportedPlaces.add("bank");
	// supportedPlaces.add("bar");
	// supportedPlaces.add("beauty salon");
	// supportedPlaces.add("bicycle store");
	// supportedPlaces.add("book store");
	// supportedPlaces.add("bowling alley");
	// supportedPlaces.add("bus station");
	// supportedPlaces.add("cafe");
	// supportedPlaces.add("campground");
	// supportedPlaces.add("car dealer");
	// supportedPlaces.add("car rental");
	// supportedPlaces.add("car repair");
	// supportedPlaces.add("car wash");
	// supportedPlaces.add("casino");
	// supportedPlaces.add("cemetery");
	// supportedPlaces.add("church");
	// supportedPlaces.add("city hall");
	// supportedPlaces.add("clothing store");
	// supportedPlaces.add("convenience store");
	// supportedPlaces.add("courthouse");
	// supportedPlaces.add("dentist");
	// supportedPlaces.add("department store");
	// supportedPlaces.add("doctor");
	// supportedPlaces.add("electrician");
	// supportedPlaces.add("electronics store");
	// supportedPlaces.add("embassy");
	// supportedPlaces.add("establishment");
	// supportedPlaces.add("finance");
	// supportedPlaces.add("fire station");
	// supportedPlaces.add("florist");
	// supportedPlaces.add("food");
	// supportedPlaces.add("funeral home");
	// supportedPlaces.add("furniture store");
	// supportedPlaces.add("gas station");
	// supportedPlaces.add("general contractor");
	// supportedPlaces.add("grocery or supermarket");
	// supportedPlaces.add("gym");
	// supportedPlaces.add("hair care");
	// supportedPlaces.add("hardware store");
	// supportedPlaces.add("health");
	// supportedPlaces.add("hindu temple");
	// supportedPlaces.add("home goods store");
	// supportedPlaces.add("hospital");
	// supportedPlaces.add("insurance agency");
	// supportedPlaces.add("jewelry store");
	// supportedPlaces.add("laundry");
	// supportedPlaces.add("lawyer");
	// supportedPlaces.add("library");
	// supportedPlaces.add("liquor store");
	// supportedPlaces.add("local government office");
	// supportedPlaces.add("locksmith");
	// supportedPlaces.add("lodging");
	// supportedPlaces.add("meal delivery");
	// supportedPlaces.add("meal takeaway");
	// supportedPlaces.add("mosque");
	// supportedPlaces.add("movie rental");
	// supportedPlaces.add("movie theater");
	// supportedPlaces.add("moving company");
	// supportedPlaces.add("museum");
	// supportedPlaces.add("night club");
	// supportedPlaces.add("painter");
	// supportedPlaces.add("park");
	// supportedPlaces.add("parking");
	// supportedPlaces.add("pet store");
	// supportedPlaces.add("pharmacy");
	// supportedPlaces.add("physiotherapist");
	// supportedPlaces.add("place of worship");
	// supportedPlaces.add("plumber");
	// supportedPlaces.add("police");
	// supportedPlaces.add("post office");
	// supportedPlaces.add("real estate agency");
	// supportedPlaces.add("restaurant");
	// supportedPlaces.add("roofing contractor");
	// supportedPlaces.add("rv park");
	// supportedPlaces.add("school");
	// supportedPlaces.add("shoe store");
	// supportedPlaces.add("shopping mall");
	// supportedPlaces.add("spa");
	// supportedPlaces.add("stadium");
	// supportedPlaces.add("storage");
	// supportedPlaces.add("store");
	// supportedPlaces.add("subway station");
	// supportedPlaces.add("synagogue");
	// supportedPlaces.add("taxi stand");
	// supportedPlaces.add("train station");
	// supportedPlaces.add("travel agency");
	// supportedPlaces.add("university");
	// supportedPlaces.add("veterinary care");
	// supportedPlaces.add("zoo");
	// }
}
