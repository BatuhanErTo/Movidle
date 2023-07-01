package com.assignment.movie.controller;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageController {
    private static final Pattern IMAGE_PATTERN =
            Pattern.compile("image\":\"(https://m\\.media-amazon\\.com/images/M/.+?\\.jpg)");
    protected String getImageFromURL(String in) {
        try {
            URL url = new URL(in + "/");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        Matcher matcher = IMAGE_PATTERN.matcher(line);
                        if (matcher.find()) {
                            return matcher.group(1);
                        }
                    }
                }
                System.out.println("The URL doesn't contain the desired image pattern");
            } else {
                System.out.println("Couldn't connect RESPONSE CODE: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
