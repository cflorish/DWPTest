package uk.gov.swagger.client.api;

import org.json.JSONArray;

import uk.gov.swagger.client.ApiException;

/**
 * A client interface implemented by classes that provide use of the HeroKu Application.
 * 
 */
public interface HeroKuClientApi {

	/**
	 * Extract all user records for a particular city.
	 * 
	 * @param city The city we want to extract users from.
	 * 
	 * @return Records matching city.
	 * @throws ApiException If city not set or Api call fails.
	 */
	public JSONArray getUsersForCity(String city) throws ApiException;

	
	/**
	 * Extract all user records.
	 * 
	 * @return All users living in or within 50 miles of London, UK.
	 * @throws ApiException If Api call fails.
	 */
	public JSONArray getAllLondonUsers() throws ApiException;

}
	
