package com.example.asyrofiabdusani.earthquakeapp;

/**
 * Created by Asyrofi Abdusani on 17/02/2018.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();



    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractFeatureFromJSON(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        List<Earthquake> earthquakes = new ArrayList<>();

        try {

            // Buat JSONObject dari string respons JSON
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Ekstrak JSONArray yang berhubungan dengan kunci bernama "features",
            // yang mewakili daftar fitur (atau gempa).
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            // Untuk tiap gempa di earthquakeArray, buat objek {@link Earthquake} untuk
            for (int i = 0; i < earthquakeArray.length(); i++) {

                // Buatlah gempa pada posisi i di dalam daftar gempa
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // Untuk gempa yang sudah ada, ekstrak JSONObject yang berhubungan dengan
                // kunci bernama "properties", yang mewakili daftar semua properties
                // untuk gempa tersebut.
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // Ekstrak nilai untuk kunci bernama "mag"
                double magnitude = properties.getDouble("mag");

                // Ekstrak nilai untuk kunci bernama "place"
                String location = properties.getString("place");

                // Ekstrak nilai untuk kunci bernama "time"
                long time = properties.getLong("time");

                // Ekstrak nilai untuk kunci bernama "url"
                String url = properties.getString("url");

                // Buat objek {@link Earthquake} baru dengan magnitudo, lokasi, waktu,
                // dan url dari response JSON.ß
                Earthquake earthquake = new Earthquake(magnitude, location, time, url);

                // Tambahkan {@link Earthquake} baru ke daftar gempa.
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // Jika error dimunculkan saat mengeksekusi pernyataan apapun di dalam blok "try",
            // tangkap perkecualiannya di sini, agar aplikasi tidak crash. Cetak pesan log dengan
            // pesan dari perkecualian.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // kembalikan daftar gempa
        return earthquakes;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {

        // Buat objek URLß
        URL url = createUrl(requestUrl);

        // Buat HTTP request ke URL dan terima kembali respons JSON
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Ekstrak field yang relevan dari respons JSON dan buatlah daftar {@link Earthquake}
        List<Earthquake> earthquakes = extractFeatureFromJSON(jsonResponse);

        // Kembalikan daftar {@link Earthquake}
        return earthquakes;
    }

}
