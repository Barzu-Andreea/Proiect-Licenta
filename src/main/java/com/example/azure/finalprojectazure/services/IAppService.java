package com.example.azure.finalprojectazure.services;

import com.example.azure.finalprojectazure.models.UploadFileDto;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;


public interface IAppService {
    public String uploadFileAzure (UploadFileDto uploadFileDto);
    public String translateTest (String text) throws IOException, InterruptedException;
    public JsonNode getWeather(JsonNode address);
    public JsonNode getLocation(String location);
    public String generateAudio(String text);
    public String getImageUrl(String attraction);

}
