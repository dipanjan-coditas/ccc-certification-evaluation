package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CryptocurrencyTracker {
    private static final String API_BASE_URL = "DEMO_URL";

    private final HttpClient httpClient;

    private final Gson gson;

    /**
     * Constructs a new CryptocurrencyTracker object.
     * Initializes the HttpClient and Gson instances.
     */
    public CryptocurrencyTracker() {
        httpClient = HttpClientBuilder.create().build();
        gson = new Gson();
    }

    /**
     * Retrieves the price of a cryptocurrency identified by its ID.
     *
     * @param cryptocurrencyId The ID of the cryptocurrency.
     * @return The price of the cryptocurrency as a BigDecimal.
     * @throws CryptocurrencyDataException If an error occurs while fetching the price data.
     */
    public BigDecimal getPrice(String cryptocurrencyId) throws CryptocurrencyDataException {
        try {
            String apiUrl = API_BASE_URL + "/simple/price?ids=" + cryptocurrencyId + "&vs_currencies=usd";
            JsonObject response = sendGetRequest(apiUrl);
            return response.getAsJsonObject(cryptocurrencyId).get("usd").getAsBigDecimal();
        } catch (IOException e) {
            throw new CryptocurrencyDataException("Error occurred while fetching price data for cryptocurrency: " + cryptocurrencyId);
        }
    }

    /**
     * Retrieves the market capitalization of a cryptocurrency identified by its ID.
     *
     * @param cryptocurrencyId The ID of the cryptocurrency.
     * @return The market capitalization of the cryptocurrency as a BigDecimal.
     * @throws CryptocurrencyDataException If an error occurs while fetching the market cap data.
     */
    public BigDecimal getMarketCap(String cryptocurrencyId) throws CryptocurrencyDataException {
        try {
            String apiUrl = API_BASE_URL + "/coins/" + cryptocurrencyId;
            JsonObject response = sendGetRequest(apiUrl);
            return response.get("market_data").getAsJsonObject().get("market_cap").getAsBigDecimal();
        } catch (IOException e) {
            throw new CryptocurrencyDataException("Error occurred while fetching market cap data for cryptocurrency: " + cryptocurrencyId);
        }
    }

    /**
     * Retrieves the volume of a cryptocurrency identified by its ID.
     *
     * @param cryptocurrencyId The ID of the cryptocurrency.
     * @return The volume of the cryptocurrency as a BigDecimal.
     * @throws CryptocurrencyDataException If an error occurs while fetching the volume data.
     */
    public BigDecimal getVolume(String cryptocurrencyId) throws CryptocurrencyDataException {
        try {
            String apiUrl = API_BASE_URL + "/coins/" + cryptocurrencyId;
            JsonObject response = sendGetRequest(apiUrl);
            return response.get("market_data").getAsJsonObject().get("total_volume").getAsBigDecimal();
        } catch (IOException e) {
            throw new CryptocurrencyDataException("Error occurred while fetching volume data for cryptocurrency: " + cryptocurrencyId);
        }
    }

    /**
     * Retrieves the price change of a cryptocurrency identified by its ID for a specific time period.
     *
     * @param cryptocurrencyId The ID of the cryptocurrency.
     * @param timePeriod       The time period for which to retrieve the price change data.
     * @return The price change of the cryptocurrency as a BigDecimal.
     * @throws CryptocurrencyDataException If an error occurs while fetching the price change data.
     */
    public BigDecimal getPriceChange(String cryptocurrencyId, String timePeriod) throws CryptocurrencyDataException {
        try {
            String apiUrl = API_BASE_URL + "/coins/" + cryptocurrencyId + "/market_chart?vs_currency=usd&days=" + timePeriod;
            JsonObject response = sendGetRequest(apiUrl);
            BigDecimal[] priceData = gson.fromJson(response, BigDecimal[].class);
            return calculatePriceChange(priceData);
        } catch (IOException e) {
            throw new CryptocurrencyDataException("Error occurred while fetching price change data for cryptocurrency: " + cryptocurrencyId);
        }
    }

    /**
     * Calculates the price change percentage given an array of price data.
     *
     * @param priceData An array of BigDecimal representing the price data.
     * @return The price change percentage as a BigDecimal.
     */
    private BigDecimal calculatePriceChange(BigDecimal[] priceData) {
        BigDecimal startPrice = priceData[0];
        BigDecimal endPrice = priceData[priceData.length - 1];
        return endPrice.subtract(startPrice).divide(startPrice, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    /**
     * Sends a GET request to the specified API URL and returns the response as a JsonObject.
     *
     * @param apiUrl The URL of the API endpoint.
     * @return The response from the API as a JsonObject.
     * @throws IOException If an error occurs during the HTTP request.
     */
    private JsonObject sendGetRequest(String apiUrl) throws IOException {
        HttpGet request = new HttpGet(apiUrl);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        return gson.fromJson(responseBody, JsonObject.class);
    }

    /**
     * Displays the details of a currency using a CurrencyDisplayDTO object.
     *
     * @param currencyDisplayDTO The CurrencyDisplayDTO object containing the currency details.
     */
    private void displayCurrentCurrencyDetails(CurrencyDisplayDTO currencyDisplayDTO) {
        System.out.println("Cryptocurrency: " + currencyDisplayDTO.getCurrencyId());
        System.out.println("Price: $" + currencyDisplayDTO.getBtcPrice());
        System.out.println("Market Cap: $" + currencyDisplayDTO.getBtcMarketCap());
        System.out.println("Volume: $" + currencyDisplayDTO.getBtcVolume());
        System.out.println("Price Change (7d): " + currencyDisplayDTO.getBtcPriceChange() + "%");
    }

    /**
     * The entry point of the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        CryptocurrencyTracker tracker = new CryptocurrencyTracker();
        String cryptocurrencyId = "bitcoin";
        try {
            CurrencyDisplayDTO currencyDisplayDTO = new CurrencyDisplayDTO();
            currencyDisplayDTO.setCurrencyId(cryptocurrencyId);
            currencyDisplayDTO.setBtcPrice(tracker.getPrice(cryptocurrencyId));
            currencyDisplayDTO.setBtcMarketCap(tracker.getMarketCap(cryptocurrencyId));
            currencyDisplayDTO.setBtcVolume(tracker.getVolume(cryptocurrencyId));
            currencyDisplayDTO.setBtcPriceChange(tracker.getPriceChange(cryptocurrencyId, "7"));

            tracker.displayCurrentCurrencyDetails(currencyDisplayDTO);

        } catch (CryptocurrencyDataException e) {
            System.out.println("Error occurred while fetching cryptocurrency data: " + e.getMessage());
        }
    }
}