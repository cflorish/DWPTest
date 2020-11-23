package uk.gov.swagger.client.api;


import uk.gov.swagger.client.Configuration;

import org.json.JSONArray;

import uk.gov.swagger.client.ApiClient;
import uk.gov.swagger.client.ApiException;

public class HeroKUClientApiImpl implements HeroKuClientApi {
    
    private ApiClient apiClient;

    public HeroKUClientApiImpl() {
        this(Configuration.getDefaultApiClient());
    }

    public HeroKUClientApiImpl(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * @param city (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @return string in JSON format
     */
	@Override
    public JSONArray getUsersForCity(String city) throws ApiException {
		
		// verify the required parameter 'city' is set
		if (city == null) {
			throw new ApiException("Missing the required parameter 'city': getUsersForCity(String city)");
		}

        return apiClient.getUsersWithHttpInfo(city);
    }

    
    /**
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @return string in JSON format
     */
	@Override
	public JSONArray getAllLondonUsers() throws ApiException {

        return apiClient.getUsersWithHttpInfo();
	}
}
