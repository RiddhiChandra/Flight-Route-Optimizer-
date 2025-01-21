import java.util.*;

class AStarAlgorithm {

    // Inner class to represent edges (neighbor nodes and the price and time)
    static class Edge {
        String destination;
        int price; // Price of the flight
        int time;  // Time of the flight

        Edge(String destination, int price, int time) {
            this.destination = destination;
            this.price = price;
            this.time = time;
        }
    }

    // Coordinates for airports (latitude, longitude)
    static Map<String, double[]> coordinates = new HashMap<>();

    static {
        coordinates.put("DEL", new double[]{28.6139, 77.2090}); // Delhi
        coordinates.put("CCU", new double[]{22.5726, 88.3639}); // Kolkata
        coordinates.put("MP", new double[]{23.2599, 77.4126});  // Madhya Pradesh
        coordinates.put("BLR", new double[]{12.9716, 77.5946}); // Bangalore
        coordinates.put("HYD", new double[]{17.3850, 78.4867}); // Hyderabad
        coordinates.put("MAA", new double[]{13.0827, 80.2707}); // Chennai
    }

    // Heuristic function using Euclidean Distance
    public static int heuristic(String current, String goal) {
        double[] currentCoords = coordinates.get(current);
        double[] goalCoords = coordinates.get(goal);
        if (currentCoords == null || goalCoords == null) return Integer.MAX_VALUE; // If no coordinates, return a large heuristic

        // Calculate Euclidean distance between current and goal
        double distance = euclideanDistance(currentCoords[0], currentCoords[1], goalCoords[0], goalCoords[1]);

        // Estimate time as proportional to Euclidean distance
        return (int) distance; // Use distance as heuristic
    }

    // Euclidean distance formula
    public static double euclideanDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow((lat2 - lat1), 2) + Math.pow((lon2 - lon1), 2));
    }

    // A* Algorithm to find the best path
    public static List<String> findBestPath(Map<String, List<Edge>> graph, String start, String goal, boolean byTime) {
        Map<String, Integer> gScores = new HashMap<>(); // Stores the cost from start to each node
        Map<String, String> cameFrom = new HashMap<>(); // Stores the path
        PriorityQueue<String> openList = new PriorityQueue<>(
                Comparator.comparingInt(city -> gScores.getOrDefault(city, Integer.MAX_VALUE) + heuristic(city, goal))
        );

        gScores.put(start, 0);
        openList.add(start);

        while (!openList.isEmpty()) {
            String current = openList.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            // Check if the current node has neighbors
            List<Edge> neighbors = graph.get(current);
            if (neighbors == null) continue; // Skip if no neighbors

            for (Edge neighbor : neighbors) {
                int tentativeGScore = gScores.get(current) + (byTime ? neighbor.time : neighbor.price); // Use time or price

                if (!gScores.containsKey(neighbor.destination) || tentativeGScore < gScores.get(neighbor.destination)) {
                    cameFrom.put(neighbor.destination, current);
                    gScores.put(neighbor.destination, tentativeGScore);
                    openList.add(neighbor.destination);
                }
            }
        }

        return new ArrayList<>(); // If no path found, return an empty list
    }

    public static List<String> reconstructPath(Map<String, String> cameFrom, String current) {
        List<String> totalPath = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            totalPath.add(current);
            current = cameFrom.get(current);
        }
        totalPath.add(current);
        Collections.reverse(totalPath);
        return totalPath;
    }

    public static void main(String[] args) {
        Map<String, List<Edge>> graph = new HashMap<>();

        // Updated adjacency matrix
        graph.put("DEL", Arrays.asList(
                new Edge("BLR", 7000, 180), // DEL to BLR
                new Edge("MP", 3000, 75),  // DEL to MP
                new Edge("CCU", 6000, 125)  // DEL to CCU
        ));

        graph.put("BLR", Arrays.asList(
                new Edge("DEL", 8000, 180), // BLR to DEL
                new Edge("MP", 7500, 125),  // BLR to MP
                new Edge("MAA", 3000, 60)   // BLR to MAA
        ));

        graph.put("MP", Arrays.asList(
                new Edge("DEL", 5000, 90), // MP to DEL
                //new Edge("BLR", 6000, 240), // MP to BLR
                new Edge("CCU", 8000, 120), // MP to CCU
                new Edge("MAA", 5500, 125)  // MP to MAA
        ));

        graph.put("CCU", Arrays.asList(
                new Edge("DEL", 6000, 150), // CCU to DEL
                new Edge("MP", 4000, 160),  // CCU to MP
                new Edge("MAA", 4600, 130)  // CCU to MAA
        ));

        graph.put("MAA", Arrays.asList(
                new Edge("BLR", 2500, 60),  // MAA to BLR
                new Edge("MP", 8000, 140),  // MAA to MP
                new Edge("CCU", 10000, 130)  // MAA to CCU
        ));

        Scanner scanner = new Scanner(System.in);

        // Prompt user for source and destination
        System.out.println("Select a source airport:");
        System.out.println("1. DEL (Delhi)");
        System.out.println("2. BLR (Bangalore)");
        System.out.println("3. CCU (Kolkata)");
        System.out.println("4. MP (Madhya Pradesh)");
        System.out.println("5. MAA (Chennai)");
        int sourceChoice = scanner.nextInt();
        String source = switch (sourceChoice) {
            case 1 -> "DEL";
            case 2 -> "BLR";
            case 3 -> "CCU";
            case 4 -> "MP";
            case 5 -> "MAA";
            default -> "DEL"; // Default to Delhi if invalid choice
        };

        System.out.println("Select a destination airport:");
        int destinationChoice = scanner.nextInt();
        String destination = switch (destinationChoice) {
            case 1 -> "DEL";
            case 2 -> "BLR";
            case 3 -> "CCU";
            case 4 -> "MP";
            case 5 -> "MAA";
            default -> "MAA"; // Default to Chennai if invalid choice
        };

        // Finding the best path based on time
        List<String> bestPathByTime = findBestPath(graph, source, destination, true);
        System.out.println("Best path from " + source + " to " + destination + " (by time): " + bestPathByTime);

        // Calculating total time for the best path by time
        int totalTimeByTime = 0;
        for (int i = 0; i < bestPathByTime.size() - 1; i++) {
            String current = bestPathByTime.get(i);
            String next = bestPathByTime.get(i + 1);
            totalTimeByTime += graph.get(current).stream()
                    .filter(edge -> edge.destination.equals(next))
                    .findFirst()
                    .map(edge -> edge.time)
                    .orElse(0);
        }
        System.out.println("Total Time (by time): " + totalTimeByTime + " minutes");

        // Finding the best path based on price
        List<String> bestPathByPrice = findBestPath(graph, source, destination, false);
        System.out.println("Best path from " + source + " to " + destination + " (by price): " + bestPathByPrice);

        // Calculating total price for the best path by price
        int totalPriceByPrice = 0;
        for (int i = 0; i < bestPathByPrice.size() - 1; i++) {
            String current = bestPathByPrice.get(i);
            String next = bestPathByPrice.get(i + 1);
            totalPriceByPrice += graph.get(current).stream()
                    .filter(edge -> edge.destination.equals(next))
                    .findFirst()
                    .map(edge -> edge.price)
                    .orElse(0);
        }
        System.out.println("Total Price (by price): " + totalPriceByPrice + " rupees");
    }
}
