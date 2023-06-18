package com.example.azure.finalprojectazure.controllers;


import com.example.azure.finalprojectazure.services.ITourService;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;

@RestController
public class ItineraryController {



    List<JsonNode> itinerary = new ArrayList<>();
    @Autowired
    private ITourService iTourService;
    @PostMapping("/itinerary/add")
    public ModelAndView addToItinerary(@RequestParam("attraction") String attraction,
                                       @RequestParam("latitude") double latitude,
                                       @RequestParam("longitude") double longitude) {
        ObjectNode attractionNode = JsonNodeFactory.instance.objectNode();
        attractionNode.put("attraction", attraction);
        attractionNode.put("lat", latitude);
        attractionNode.put("lon", longitude);
        itinerary.add(attractionNode);
        ModelAndView modelAndView = new ModelAndView("redirect:/atractii");
        return modelAndView;
    }

    @GetMapping("/distance")
    public int[][] getMatrix(List<JsonNode> locations){
        int[][] matrix = iTourService.distanceMatrix(locations);
        return matrix;
    }


    @GetMapping("/matrice")
    public int[][] showMatrix(){
        int[][] matrix = iTourService.distanceMatrix(itinerary);
        return matrix;
    }

        @GetMapping("/best")
        public ModelAndView getBestRoute() {
            if (itinerary.isEmpty()) {
                ModelAndView emptyModelAndView = new ModelAndView("planner");
                return emptyModelAndView;
            }
            String[] optimalPath = iTourService.findShortestPath(getMatrix(itinerary), itinerary);
            int[][] matrix = iTourService.distanceMatrix(itinerary);
            int miles = iTourService.pathDistanceString(iTourService.findShortestPath(getMatrix(itinerary), itinerary),
                    iTourService.distanceMatrix(itinerary),itinerary);
            ModelAndView modelAndView = new ModelAndView("planner");
            modelAndView.addObject("itineraryList", itinerary);
            modelAndView.addObject("optimalPath", optimalPath);
            modelAndView.addObject("distantaMatrice", matrix);
            modelAndView.addObject("meters", miles);
            return modelAndView;
        }

    @GetMapping("/twoLocations")
    public int distance(@RequestParam double lat1, @RequestParam double lon1, @RequestParam double lat2, @RequestParam double lon2){
        int distance = iTourService.distanceBetweenTwoLocations(lat1, lon1, lat2, lon2);
        return distance;
    }


        @GetMapping("/miles")
    public int miles() {

        int distance = iTourService.pathDistanceString(iTourService.findShortestPath(getMatrix(itinerary), itinerary),
                iTourService.distanceMatrix(itinerary),itinerary);
        return distance;
    }
    @GetMapping("/clear")
    public void clearItinerary(){
        itinerary.clear();
    }
}
