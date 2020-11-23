package uk.gov.swagger.client.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.json.JSONArray;
import org.junit.Test;

import uk.gov.swagger.client.ApiException;

public class HeroKuClientApiTest {

	private final HeroKuClientApi api = new HeroKUClientApiImpl();
	
	/**
	 * @throws ApiException if the Api call fails
	 */
	@Test
    public void getUsersForCityNotFound() throws ApiException {
	   	String city = "Lwergtyut";
	   	JSONArray cityUsers = api.getUsersForCity(city);
	    
	    // We should have no users returned  
	    assertEquals("[]", cityUsers.toString());
    }

	
	/**
	 * @throws ApiException if the Api call fails
	 */
	@Test
	public void getUsersForCity() throws ApiException {
		String city = "London";
		JSONArray cityUsers = api.getUsersForCity(city);
		
		// We should have some users returned  
		assertNotEquals("[]", cityUsers.toString());
	}
	
	
	/**
	 * @throws ApiException if the Api call fails
	 */
	@Test
	public void getUsersForCityWithSpace() throws ApiException {
		String city = "Lon don";
		JSONArray cityUsers = api.getUsersForCity(city);
		
		// We should have no users returned  
		assertEquals("[]", cityUsers.toString());
	}
	
	
	/**
	 * @throws ApiException if the Api call fails
	 */
	@Test
	public void getAllLondonUsers() throws ApiException {
		JSONArray allLondonUsers = api.getAllLondonUsers();
		
		// We should have some users returned 
		assertNotEquals("[]", allLondonUsers.toString());
	}
	
	
	/**
	 * @throws ApiException if the Api call fails
	 */
	@Test
	public void getUsersForCityWithNoValue() throws ApiException {
		String city = null;
		JSONArray cityUsers = null;
		
		try {
			cityUsers = api.getUsersForCity(city);
		} catch (ApiException e) {
//			e.printStackTrace();
			return;
		}
		
		// We should have null returned  
		assertEquals(null, cityUsers);
	}
   
}
