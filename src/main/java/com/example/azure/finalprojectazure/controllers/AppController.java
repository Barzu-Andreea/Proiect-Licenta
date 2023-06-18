package com.example.azure.finalprojectazure.controllers;

import com.example.azure.finalprojectazure.models.Atractii;
import com.example.azure.finalprojectazure.models.Postcard;
import com.example.azure.finalprojectazure.models.UploadFileDto;
import com.example.azure.finalprojectazure.repositories.AttractionRepository;
import com.example.azure.finalprojectazure.repositories.PostcardRepository;
import com.example.azure.finalprojectazure.services.IAppService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;
import java.util.List;

@RestController
public class AppController {



    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private PostcardRepository postcardRepository;

    @Autowired
    private IAppService iAppService;

    @GetMapping("/redirect-attractions")
    public RedirectView redirectToAttractions() {
        String redirectUrl = "https://www.tripadvisor.com/Attractions-g304060-Activities-Iasi_Iasi_County_Northeast_Romania.html";

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUrl);

        return redirectView;
    }

    @GetMapping("/redirect-booking")
    public RedirectView redirectToBooking() {
        String redirectUrl = "https://www.tripadvisor.com/Hotels-g304060-Iasi_Iasi_County_Northeast_Romania-Hotels.html";

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUrl);

        return redirectView;
    }

    @GetMapping("/redirect-restaurants")
    public RedirectView redirectToRestaurants() {
        String redirectUrl = "https://www.tripadvisor.com/Restaurants-g304060-Iasi_Iasi_County_Northeast_Romania.html";

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUrl);

        return redirectView;
    }
    @PostMapping("/savePostcard")
    public ModelAndView newPostcard(@RequestParam("file") MultipartFile file,
                                              @RequestParam("name") String name,
                                              @RequestParam("message") String message) {
        try {
            byte[] fileBytes = file.getBytes();
            String fileBase64 = Base64.getEncoder().encodeToString(fileBytes);
            UploadFileDto uploadFileDto = new UploadFileDto();
            uploadFileDto.setName(name);
            uploadFileDto.setFileBase64(fileBase64);
            String imageUrl = iAppService.uploadFileAzure(uploadFileDto);

            Postcard postcard = new Postcard();
            postcard.setName(name);
            postcard.setImageUrl(imageUrl);
            postcard.setMessage(message);
            postcardRepository.save(postcard);

        } catch (Exception e) {

        }
        ModelAndView modelAndView = new ModelAndView("redirect:/postcards");
        return modelAndView;
    }
    @PostMapping("/upload-file")
    public void uploadFileBlobStorage(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("name") String fileName) {
        try {
            byte[] fileBytes = file.getBytes();
            String fileBase64 = Base64.getEncoder().encodeToString(fileBytes);

            UploadFileDto uploadFileDto = new UploadFileDto();
            uploadFileDto.setName(fileName);
            uploadFileDto.setFileBase64(fileBase64);

             iAppService.uploadFileAzure(uploadFileDto);
        } catch (Exception e) {

        }
    }



    @GetMapping("/attractions")
    public List<Atractii> attractions(){
        return attractionRepository.findAll();
    }

    @PostMapping("/attraction")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Atractii attraction){
        attractionRepository.save(attraction);
    }


    @GetMapping("/location")
    public JsonNode location (@RequestBody String location){
        JsonNode information = iAppService.getLocation(location);
        return information;
    }

    @GetMapping("/weather")
    public ModelAndView  weather (){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode address = objectMapper.createObjectNode();
        address.put("lat", 47.1585);
        address.put("lon", 27.6014);

        JsonNode information = iAppService.getWeather(address);
        ModelAndView modelAndView = new ModelAndView("weather");
        modelAndView.addObject("dateTime", information.get("results").get(0).get("dateTime").asText());
        modelAndView.addObject("description", information.get("results").get(0).get("phrase").asText());
        modelAndView.addObject("temperature", information.get("results").get(0).get("temperature").get("value").asDouble());
        modelAndView.addObject("windSpeed", information.get("results").get(0).get("wind").get("speed").get("value").asDouble());
        modelAndView.addObject("relativeHumidity", information.get("results").get(0).get("relativeHumidity").asInt());
        modelAndView.addObject("pressure", information.get("results").get(0).get("pressure").get("value").asDouble());
        modelAndView.addObject("cloudCover", information.get("results").get(0).get("cloudCover").asInt());
        modelAndView.addObject("visibility", information.get("results").get(0).get("visibility").get("value").asDouble());
        modelAndView.addObject("uvIndex", information.get("results").get(0).get("uvIndex").asInt());
        modelAndView.addObject("precipitation", information.get("results").get(0).get("precipitationSummary").get("past24Hours").get("value").asDouble());

        return modelAndView;

    }


    @GetMapping("/image")
    public String image (@RequestParam("attraction") String attraction){
        String information = iAppService.getImageUrl(attraction);
        return information;
    }

    @PostMapping("/checkin")
    public RedirectView processCheckinForm(@RequestParam("checkinDate") String checkinDate, @RequestParam("checkoutDate") String checkoutDate) {

        String redirectUrl = "https://www.airbnb.com/s/Iasi/homes?checkin=" + checkinDate + "&checkout=" + checkoutDate;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUrl);

        return redirectView;
    }



}