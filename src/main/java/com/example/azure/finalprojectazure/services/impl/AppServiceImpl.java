package com.example.azure.finalprojectazure.services.impl;

import com.example.azure.finalprojectazure.models.UploadFileDto;
import com.example.azure.finalprojectazure.services.IAppService;
import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;


import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;


@Service
public class AppServiceImpl implements IAppService {


    @Override
    public String uploadFileAzure(UploadFileDto uploadFileDto) {
        String resultService = "";
        String storageConnectionAzure = "DefaultEndpointsProtocol=https;AccountName=azurestoragecloud1;AccountKey=XosarRDtZ3j11Oh6iRqDKJsYLwd+L736li6+1L5r1iQLQrCxnxQfdSydPhmv3a+uKfAFL+xXVp9C+AStTqbeyA==;EndpointSuffix=core.windows.net";
        String nameContainer = "files";

        try {
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionAzure);
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            CloudBlobContainer container = serviceClient.getContainerReference(nameContainer);

            CloudBlob blob;
            blob = container.getBlockBlobReference(uploadFileDto.getName());
            byte[] decodedBytes = Base64.getDecoder().decode(uploadFileDto.getFileBase64());
            blob.uploadFromByteArray(decodedBytes, 0, decodedBytes.length);

            // Obțineți URL-ul imaginii după încărcare
            String imageUrl = blob.getUri().toString();

            resultService = imageUrl;
        } catch (Exception e) {
            resultService = e.getMessage();
        }
        return resultService;
    }


    @Override
    public String translateTest(String text) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String key = "9396cc5a3b074b648ba65caa77ab203c";
        String endpoint = "https://api.cognitive.microsofttranslator.com";
        String route = "/translate?api-version=3.0&to=ro";
        String url = endpoint.concat(route);
        String location = "westeurope";
        String requestBody = "[{\"Text\": \"" + text + "\"}]";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Ocp-Apim-Subscription-Key", key)
                .header("Ocp-Apim-Subscription-Region", location)
                .header("Content-type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String startMarker = "\"text\":\"";
        String endMarker = "\",\"to\":\"ro\"";

        int startIndex = response.body().indexOf(startMarker);
        int endIndex = response.body().indexOf(endMarker);
        String translatedText = "";
        if (startIndex != -1 && endIndex != -1) {
            startIndex += startMarker.length();
            translatedText = response.body().substring(startIndex, endIndex);
        }
        return translatedText;
    }

    @Override
    public JsonNode getWeather(JsonNode address) {
        RestTemplate restTemplate = new RestTemplate();
        String query = address.get("lat") + ", " + address.get("lon");
        String url = "https://atlas.microsoft.com/weather/currentConditions/json?api-version=1.0&query=" + query + "&subscription-key=UwC-YUGM77leO589nwXnaesMfJVxea7ib3shhvKJWCM";
        JsonNode information = restTemplate.getForObject(url, JsonNode.class);
        return information;
    }

    @Override
    public JsonNode getLocation(String location) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://atlas.microsoft.com/search/address/json?&subscription-key=UwC-YUGM77leO589nwXnaesMfJVxea7ib3shhvKJWCM&api-version=1.0&language=en-US&query=" + location;
        JsonNode information = restTemplate.getForObject(uri, JsonNode.class);
        return information.get("results").get(0).get("position");
    }

    @Override
    public String generateAudio(String text) {
        HttpClient client = HttpClient.newHttpClient();
        String key = "2e9be7a99a3c450b9df2e558df6c102b";
        String url = "https://westeurope.tts.speech.microsoft.com/cognitiveservices/v1";

        String requestBody = "<speak version='1.0' xml:lang='ro-RO'><voice xml:lang='ro-RO' xml:gender='Male' name='ro-RO-EmilNeural'>" + text + "</voice></speak>";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Ocp-Apim-Subscription-Key", key)
                .header("Content-Type", "application/ssml+xml")
                .header("X-Microsoft-OutputFormat", "audio-24khz-160kbitrate-mono-mp3")
                .build();

        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                byte[] audioBytes = response.body();
                String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
                return base64Audio;
            } else {
                System.out.println("Request failed with status code: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public String getImageUrl(String attraction) {
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=azurestoragecloud1;AccountKey=XosarRDtZ3j11Oh6iRqDKJsYLwd+L736li6+1L5r1iQLQrCxnxQfdSydPhmv3a+uKfAFL+xXVp9C+AStTqbeyA==;EndpointSuffix=core.windows.net";
        String containerName = "files";
        String blobName = attraction;
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        String blobUrl = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName).getBlobUrl();
        return blobUrl;
    }





}
