package com.example.azure.finalprojectazure.services;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface ITourService {
    public int distanceBetweenTwoLocations(double lat1, double lon1, double lat2, double lon2);
    public int[][] distanceMatrix(List<JsonNode> pairs);
    public int pathDistanceString(String[] path, int[][] distances, List<JsonNode> locations);
    public String[] findShortestPath(int[][] distances, List<JsonNode> locations);

}


