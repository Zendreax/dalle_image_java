package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DALLEImageGenerator {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder().build();

    public void generateImage(String prompt) throws IOException {
        String apiKey = "APIKEY";
        String url = "https://api.openai.com/v1/images/generations";

        String json = "{\"model\":\"image-alpha-001\",\"prompt\":\"" + prompt + "\",\"num_images\":1,\"size\":\"1024x1024\",\"response_format\":\"url\"}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String imageUrl = response.body().string();
            Path desktop = Paths.get(System.getProperty("user.home"),"Desktop");
            Path imagePath = desktop.resolve("ai-image.jpg");
            byte[] imageData = client.newCall(new Request.Builder().url(imageUrl).build()).execute().body().bytes();
            Files.write(imagePath, imageData);
            System.out.println("Image saved to " + imagePath);
        }
    }

    public static void main(String[] args) throws IOException {
        DALLEImageGenerator generator = new DALLEImageGenerator();
        generator.generateImage("A two-story pink house with a white fence and a red front door.");
    }
}