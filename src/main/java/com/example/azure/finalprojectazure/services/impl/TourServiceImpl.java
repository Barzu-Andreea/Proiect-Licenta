package com.example.azure.finalprojectazure.services.impl;
import com.example.azure.finalprojectazure.services.ITourService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TourServiceImpl implements ITourService {

    @Override
    public int distanceBetweenTwoLocations(double lat1, double lon1, double lat2, double lon2) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://atlas.microsoft.com/route/directions/json?subscription-key=UwC-YUGM77leO589nwXnaesMfJVxea7ib3shhvKJWCM&api-version=1.0&query="+lat1+","+lon1+":"+lat2+","+lon2;
        JsonNode information = restTemplate.getForObject(url, JsonNode.class);
        return Integer.parseInt(information.get("routes").get(0).get("summary").get("lengthInMeters").toString());
    }
    @Override
    public int[][] distanceMatrix(List<JsonNode> pairs) {
        int[][] distanceMatrix = new int[pairs.size()][pairs.size()];

        for (int i = 0; i < pairs.size(); i++) {
            JsonNode pair = pairs.get(i);
            double latitude = pair.get("lat").asDouble();
            double longitude = pair.get("lon").asDouble();

            for (int j = 0; j < pairs.size(); j++){
                JsonNode pair2 = pairs.get(j);  // Utilizăm indexul j pentru perechea a doua
                double latitude2 = pair2.get("lat").asDouble();
                double longitude2 = pair2.get("lon").asDouble();

                int distance = distanceBetweenTwoLocations(latitude, longitude, latitude2, longitude2);
                distanceMatrix[i][j] = distance;
            }
        }

        return distanceMatrix;
    }



    public static void swap(int[] array, int first, int second) {
        int temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

    private static void allPermutationsHelper(int[] permutation, List<int[]> permutations, int n) {
        // Base case - we found a new permutation, add it and return
        if (n <= 0) {
            permutations.add(permutation.clone());
            return;
        }

        // Recursive case - find more permutations by doing swaps
        int[] tempPermutation = permutation.clone();
        for (int i = 0; i < n; i++) {
            swap(tempPermutation, i, n - 1); // move element at i to end
            // move everything else around, holding the end constant
            allPermutationsHelper(tempPermutation, permutations, n - 1);
            swap(tempPermutation, i, n - 1); // backtrack
        }
    }

    private static List<int[]> permutations(int[] original) {
        List<int[]> permutations = new ArrayList<>();
        allPermutationsHelper(original, permutations, original.length);
        return permutations;
    }


    public int pathDistance(int[] path, int[][] distances) {
        int last = path[0];
        int distance = 0;
        for (int i = 1; i < path.length; i++) {
            int next = path[i];
            distance += distances[last][next];
            // distance to get back from last city to first city
            last = next;
        }
        return distance;
    }

    @Override
    public int pathDistanceString(String[] path, int[][] distances, List<JsonNode> locations) {
        String lastLocation = path[0];
        int distance = 0;
        for (int i = 1; i < path.length; i++) {
            String nextLocation = path[i];
            int lastIndex = getIndexForLocation(lastLocation, locations);
            int nextIndex = getIndexForLocation(nextLocation, locations);
            distance += distances[lastIndex][nextIndex];
            // distance to get back from last city to first city
            lastLocation = nextLocation;
        }
        return distance;
    }

    private int getIndexForLocation(String location, List<JsonNode> locations) {
        for (int i = 0; i < locations.size(); i++) {
            JsonNode locationNode = locations.get(i);
            String locationName = locationNode.get("attraction").asText();
            if (locationName.equals(location)) {
                return i;
            }
        }
        return -1; // sau aruncați o excepție în cazul în care locația nu este găsită
    }


    @Override
    public String[] findShortestPath(int[][] distances, List<JsonNode> locations) {
        int numCities = distances.length;
        int[] cities = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            cities[i] = i;
        }
        List<int[]> paths = permutations(cities);
        int[] shortestPath = null;
        int minDistance = Integer.MAX_VALUE; // arbitrarily high
        for (int[] path : paths) {
            int distance = pathDistance(path, distances);
            // distance from last to first must be added
            distance += distances[path[path.length - 1]][path[0]];
            if (distance < minDistance) {
                minDistance = distance;
                shortestPath = path;
            }
        }
        // add first city on to end and return
        shortestPath = Arrays.copyOf(shortestPath, shortestPath.length + 1);
        shortestPath[shortestPath.length - 1] = shortestPath[0];

        String[] shortestPathWithNames = new String[shortestPath.length];
        for (int i = 0; i < shortestPath.length; i++) {
            JsonNode location = locations.get(shortestPath[i]);
            shortestPathWithNames[i] = location.get("attraction").asText();
        }

        return shortestPathWithNames;
    }



}
