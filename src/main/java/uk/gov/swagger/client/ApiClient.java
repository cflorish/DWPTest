package uk.gov.swagger.client;

import com.squareup.okhttp.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

public class ApiClient {

	private static final String BASE_PATH = "https://bpdts-test-app.herokuapp.com";
	private static final double LONDON_UK_LATITUDE = 51.5074;
	private static final double LONDON_UK_LONGITUDE = -0.1278;
	private static final double AVERAGE_RADIUS_OF_EARTH_MILES = 3959;

    private OkHttpClient httpClient;

    /*
     * Constructor for ApiClient
     */
    public ApiClient() {
        httpClient = new OkHttpClient();
    }

    /**
     * 
     * 
     * @param city  (required)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public JSONArray getUsersWithHttpInfo(String city) throws ApiException {
        
        Call call = getUsersCall(city);
        String cityUsers = execute(call, String.class);
	    JSONArray listedLondon = new JSONArray(cityUsers);  
	      
	    StringBuilder sb =  new StringBuilder();
	    sb.append("Users with city set to " + city + " are:\n");
	    
	    for(int i=0; i < listedLondon.length(); i++) {  
		    JSONObject object = listedLondon.getJSONObject(i);  
		    String formattedString = convertJsonToString(object, false);
		    sb.append(formattedString + "\n");
		}
	    System.out.println(sb.toString());

		return listedLondon;
    }

	public JSONArray getUsersWithHttpInfo() throws ApiException {
        
        Call call = getUsersCall();
        
        String allUsers = execute(call, String.class);
        
	    JSONArray array = new JSONArray(allUsers);  
	    JSONArray londoners = new JSONArray();  
	      
	    StringBuilder sb =  new StringBuilder();
	    sb.append("\nAll users living in or within 50 miles of London, UK are :\n");
	    
	    for(int i=0; i < array.length(); i++) {  
		    JSONObject object = array.getJSONObject(i);  
		    String formattedString = convertJsonToString(object, true);
		    
		    if (!formattedString.equals("NA")) {
		    	londoners.put(object);
			    sb.append(formattedString + "\n");
		    }
		}
	    System.out.println(sb.toString());

	    return londoners;
	}

    /**
     * Build call for getUsers and city
     * @param city  (required)
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call getUsersCall(String city) throws ApiException {

        // create path and map variables
        String localVarPath = "/city/{city}/users"
            .replaceAll("\\{" + "city" + "\\}", escapeString(city.toString()));

        final String url = buildUrl(localVarPath);
        final Request.Builder reqBuilder = new Request.Builder().url(url);

        RequestBody reqBody = null;
        Request request = null;
        request = reqBuilder.method("GET", reqBody).build();

        return httpClient.newCall(request);
    }

    /**
     * Build call for all users
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call getUsersCall() throws ApiException {

        // create path and map variables
        String localVarPath = "/users";

        final String url = buildUrl(localVarPath);
        final Request.Builder reqBuilder = new Request.Builder().url(url);

        RequestBody reqBody = null;
        Request request = null;
        request = reqBuilder.method("GET", reqBody).build();

        return httpClient.newCall(request);
    }

    /**
     * Build full URL by concatenating base path, the given sub path and query parameters.
     *
     * @param path The sub path
     * @return The full URL
     */
    public String buildUrl(String path) {
        final StringBuilder url = new StringBuilder();
        url.append(BASE_PATH);
        url.append(path);

        return url.toString();
    }

    /**
     * Execute HTTP call and deserialize the HTTP response body into the given return type.
     *
     * @param call Call
     * @param returnType The return type used to deserialize HTTP response body
     * @throws ApiException If fail to execute the call
     */
   public String execute(Call call, Type returnType) throws ApiException {
        try {
            Response response = call.execute();
            String data = handleResponse(response, returnType);
            return data;
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    /**
     * Handle the given response, return the deserialized object when the response is successful.
     *
     * @param <T> Type
     * @param response Response
     * @param returnType Return type
     * @throws ApiException If the response has a unsuccessful status code or
     *   fail to deserialize the response body
     * @throws IOException If the response body can't be stringified 
     * @return Type
     */
    public String handleResponse(Response response, Type returnType) throws ApiException, IOException {
        if (response.isSuccessful()) {
            if (returnType == null || response.code() == 204) {
                // returning null if the returnType is not defined,
                // or the status code is 204 (No Content)
                if (response.body() != null) {
                    try {
                        response.body().close();
                    } catch (IOException e) {
                        throw new ApiException(response.message(), e, response.code(), response.headers().toMultimap());
                    }
                }
                return null;
            } else {
                if (returnType.equals(String.class)) {
                    // Expecting string, return the raw response body.
                    return response.body().string();
                } else {
                    throw new ApiException(
                            "Expecting String but found unsupported type: " + returnType,
                            response.code(),
                            response.headers().toMultimap(),
                            response.body().string());
                }
            }
        } else {
            String respBody = null;
            if (response.body() != null) {
                try {
                    respBody = response.body().string();
                } catch (IOException e) {
                    throw new ApiException(response.message(), e, response.code(), response.headers().toMultimap());
                }
            }
            throw new ApiException(response.message(), response.code(), response.headers().toMultimap(), respBody);
        }
    }

    /**
     * Get HTTP client
     *
     * @return An instance of OkHttpClient
     */
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Set HTTP client
     *
     * @param httpClient An instance of OkHttpClient
     * @return Api Client
     */
    public ApiClient setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * Escape the given string to be used as URL query value.
     *
     * @param str String to be escaped
     * @return Escaped string
     */
    public String escapeString(String str) {
        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }
    
    
    /**
     * Convert the return JSONObject to a string.
     *
     * @param JSONObject for conversion to string
     * @return concatenated string
     */
	public String convertJsonToString(JSONObject object, boolean calculateDistance) {
		String s = null;
		double inputLatitude = 0.0;
		double inputLongitude = 0.0;

		StringBuilder sb = new StringBuilder();
		sb.append("Id: " + object.getInt("id") + " - ");
		sb.append("Name: " + object.getString("first_name"));
		sb.append(" " + object.getString("last_name") + " - ");
		sb.append("Email: " + object.getString("email") + " - ");
		sb.append("IP Address: " + object.getString("ip_address") + " - ");
		try {
			inputLatitude = object.getDouble("latitude");
			sb.append("Latitude: " + inputLatitude + " - ");
		} catch (ClassCastException e) {
			s = object.getString("latitude");
			inputLatitude = Double.parseDouble(s);
			sb.append("Latitude: " + inputLatitude + " - ");
		}
		try {
			inputLongitude = object.getDouble("longitude");
			sb.append("Longitude: " + inputLongitude);
		} catch (ClassCastException e) {
			s = object.getString("longitude");
			inputLongitude = Double.parseDouble(s);
			sb.append("Longitude: " + inputLongitude);
		}
		if (calculateDistance) {
			int distance = calculateDistanceInMiles(LONDON_UK_LATITUDE, LONDON_UK_LONGITUDE, 
					inputLatitude, inputLongitude);
			if (distance  > 50) {
				String str = "NA";
				return str;
			}
		}
		return sb.toString();
	}
	
    
    /**
     * Calculate approximate distance between two points using Haversine formula.
     *
     * @param userLat from latitude
     * @param fromLng from longitude
     * @param userLat from latitude
     * @param fromLng from longitude
     * @return int calculated distance
     */
	public int calculateDistanceInMiles(double fromLat, double fromLng, 
			double toLat, double toLng) {

	    double latDistance = Math.toRadians(fromLat - toLat);
	    double lngDistance = Math.toRadians(fromLng - toLng);

	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	      + Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat))
	      * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_MILES * c));
	}

}
