package com.example.android.cinemate.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class NetworkUtils {

    private static URL createUrl (String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest (URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        InputStream is = urlConnection.getInputStream();

        String line = null;

        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("\\A");
        if (scanner.hasNext()) {
            line = scanner.next();
        }
        scanner.close();
        return line;
    }


    public static String getDataFromNetwork(String stringUrl) {
        URL url = createUrl(stringUrl);
        String i = null;
        try {
            i = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }
}
