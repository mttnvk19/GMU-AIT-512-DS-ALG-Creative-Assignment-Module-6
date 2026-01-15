/**
 * Module 6 Creative Assignment
 * Charing Network Optimizer
 * @author Matthew Novak
 */
package mb6.dc3a;

import java.util.*;

/**
 * Autonomous Electric Vehicle Charging Station Network Optimizer
 * Uses Prim's Algorithm to find the Minimum Spanning Tree for optimal
 * underground power cable installation between charging stations.
 */
public class ChargingNetworkOptimizer {
	// Graph representation using adjacency matrix
	    private int[][] adjacencyMatrix;
	    private int numStations;
	    private String[] stationNames;
	    
	    /**
	     * Constructor initializes the charging station network
	     * @param numStations Number of charging station locations
	     */
	    public ChargingNetworkOptimizer(int numStations) {
	        this.numStations = numStations;
	        this.adjacencyMatrix = new int[numStations][numStations];
	        this.stationNames = new String[numStations];
	        
	        // Initialize adjacency matrix with "infinity" (no direct connection)
	        for (int i = 0; i < numStations; i++) {
	            for (int j = 0; j < numStations; j++) {
	                adjacencyMatrix[i][j] = (i == j) ? 0 : Integer.MAX_VALUE;
	            }
	        }
	    }
	    
	    /**
	     * Set name for a charging station
	     * @param index Station index
	     * @param name Station name
	     */
	    public void setStationName(int index, String name) {
	        stationNames[index] = name;
	    }
	    
	    /**
	     * Add a bidirectional edge (cable route) between two stations
	     * @param station1 First station index
	     * @param station2 Second station index
	     * @param cost Installation cost in thousands of dollars
	     */
	    public void addCableRoute(int station1, int station2, int cost) {
	        // Undirected graph: add edge in both directions
	        adjacencyMatrix[station1][station2] = cost;
	        adjacencyMatrix[station2][station1] = cost;
	    }
	    
	    /**
	     * Prim's Algorithm implementation to find Minimum Spanning Tree
	     * This finds the minimum cost network to connect all charging stations
	     * @return Total minimum cost of the network
	     */
	    public int primsAlgorithm() {
	        // Array to track which stations are included in MST
	        boolean[] inMST = new boolean[numStations];
	        
	        // Array to store the minimum cost to connect each station
	        int[] key = new int[numStations];
	        
	        // Array to store the parent of each station in MST
	        int[] parent = new int[numStations];
	        
	        // Initialize all keys as infinite
	        Arrays.fill(key, Integer.MAX_VALUE);
	        
	        // Start with station 0 (arbitrary choice)
	        key[0] = 0;
	        parent[0] = -1; // First node is root of MST
	        
	        System.out.println("\n=== PRIM'S ALGORITHM EXECUTION ===");
	        System.out.println("Building Minimum Spanning Tree for Charging Network...\n");
	        
	        // MST will have (numStations - 1) edges
	        for (int count = 0; count < numStations - 1; count++) {
	            
	            // Find minimum key vertex not yet in MST
	            int u = findMinKeyVertex(key, inMST);
	            
	            // Add selected vertex to MST
	            inMST[u] = true;
	            
	            System.out.println("Step " + (count + 1) + ": Added station '" + 
	                             stationNames[u] + "' to MST");
	            
	            // Update key values of adjacent vertices
	            for (int v = 0; v < numStations; v++) {
	                // Update key only if:
	                // 1. There is an edge from u to v
	                // 2. v is not yet in MST
	                // 3. The edge weight is less than current key value
	                if (adjacencyMatrix[u][v] != Integer.MAX_VALUE && 
	                    !inMST[v] && 
	                    adjacencyMatrix[u][v] < key[v]) {
	                    
	                    parent[v] = u;
	                    key[v] = adjacencyMatrix[u][v];
	                }
	            }
	        }
	        
	        // Calculate and display total cost
	        return displayMST(parent);
	    }
	    
	    /**
	     * Helper method to find vertex with minimum key value not in MST
	     * @param key Array of key values
	     * @param inMST Array tracking vertices in MST
	     * @return Index of minimum key vertex
	     */
	    private int findMinKeyVertex(int[] key, boolean[] inMST) {
	        int min = Integer.MAX_VALUE;
	        int minIndex = -1;
	        
	        for (int v = 0; v < numStations; v++) {
	            if (!inMST[v] && key[v] < min) {
	                min = key[v];
	                minIndex = v;
	            }
	        }
	        
	        return minIndex;
	    }
	    
	    /**
	     * Display the MST and calculate total cost
	     * @param parent Array storing parent relationships in MST
	     * @return Total cost of MST
	     */
	    private int displayMST(int[] parent) {
	        int totalCost = 0;
	        
	        System.out.println("\n=== OPTIMAL CHARGING NETWORK CONFIGURATION ===");
	        System.out.println("Cable Route Connections:");
	        System.out.println("----------------------------------------------------");
	        
	        for (int i = 1; i < numStations; i++) {
	            int cost = adjacencyMatrix[i][parent[i]];
	            totalCost += cost;
	            
	            System.out.printf("%-20s <---> %-20s Cost: $%,d K\n",
	                            stationNames[parent[i]], 
	                            stationNames[i], 
	                            cost);
	        }
	        
	        System.out.println("----------------------------------------------------");
	        System.out.printf("TOTAL NETWORK COST: $%,d K\n", totalCost);
	        System.out.println("====================================================\n");
	        
	        return totalCost;
	    }
	    
	    /**
	     * Display the complete network graph
	     */
	    public void displayNetwork() {
	        System.out.println("=== COMPLETE CHARGING STATION NETWORK ===");
	        System.out.println("All Possible Cable Routes and Costs:\n");
	        
	        for (int i = 0; i < numStations; i++) {
	            for (int j = i + 1; j < numStations; j++) {
	                if (adjacencyMatrix[i][j] != Integer.MAX_VALUE) {
	                    System.out.printf("%-20s <---> %-20s Cost: $%,d K\n",
	                                    stationNames[i], 
	                                    stationNames[j], 
	                                    adjacencyMatrix[i][j]);
	                }
	            }
	        }
	        System.out.println();
	    }
	    
	    /**
	     * Main method - demonstrates the charging network optimization
	     */
	    public static void main(String[] args) {
	    	Date date = new Date();
	    	System.out.println("Module 6 - Creative Assignment - Matt Novak");
	    	System.out.println("Executed on: "+date);
	        System.out.println("╔════════════════════════════════════════════════════════╗");
	        System.out.println("║     AUTONOMOUS VEHICLE CHARGING NETWORK OPTIMIZER      ║");
	        System.out.println("║                                                        ║");
	        System.out.println("╚════════════════════════════════════════════════════════╝\n");
	        
	        // Create network with 10 charging stations
	        ChargingNetworkOptimizer optimizer = new ChargingNetworkOptimizer(10);
	        
	        // Define charging station names (strategic city locations)
	        optimizer.setStationName(0, "Downtown Hub");
	        optimizer.setStationName(1, "Airport Terminal");
	        optimizer.setStationName(2, "University Campus");
	        optimizer.setStationName(3, "Medical District");
	        optimizer.setStationName(4, "Tech Park");
	        optimizer.setStationName(5, "Shopping Center");
	        optimizer.setStationName(6, "Stadium Complex");
	        optimizer.setStationName(7, "Harbor District");
	        optimizer.setStationName(8, "Residential North");
	        optimizer.setStationName(9, "Industrial South");
	        
	        // Add cable routes with installation costs (in thousands of dollars)
	        // Costs based on distance, terrain, and permit requirements
	        
	        // Downtown Hub connections
	        optimizer.addCableRoute(0, 1, 45);  // To Airport
	        optimizer.addCableRoute(0, 2, 32);  // To University
	        optimizer.addCableRoute(0, 3, 28);  // To Medical District
	        optimizer.addCableRoute(0, 5, 25);  // To Shopping Center
	        
	        // Airport Terminal connections
	        optimizer.addCableRoute(1, 4, 38);  // To Tech Park
	        optimizer.addCableRoute(1, 9, 52);  // To Industrial South
	        
	        // University Campus connections
	        optimizer.addCableRoute(2, 3, 22);  // To Medical District
	        optimizer.addCableRoute(2, 4, 35);  // To Tech Park
	        optimizer.addCableRoute(2, 8, 40);  // To Residential North
	        
	        // Medical District connections
	        optimizer.addCableRoute(3, 5, 18);  // To Shopping Center
	        optimizer.addCableRoute(3, 6, 30);  // To Stadium
	        
	        // Tech Park connections
	        optimizer.addCableRoute(4, 8, 33);  // To Residential North
	        optimizer.addCableRoute(4, 9, 41);  // To Industrial South
	        
	        // Shopping Center connections
	        optimizer.addCableRoute(5, 6, 27);  // To Stadium
	        optimizer.addCableRoute(5, 7, 36);  // To Harbor
	        
	        // Stadium Complex connections
	        optimizer.addCableRoute(6, 7, 29);  // To Harbor
	        optimizer.addCableRoute(6, 8, 44);  // To Residential North
	        
	        // Harbor District connections
	        optimizer.addCableRoute(7, 9, 48);  // To Industrial South
	        
	        // Residential North connections
	        optimizer.addCableRoute(8, 9, 55);  // To Industrial South
	        
	        // Display complete network
	        optimizer.displayNetwork();
	        
	        // Run Prim's Algorithm to find optimal network
	        int minimumCost = optimizer.primsAlgorithm();
	        
	        // Summary
	        System.out.println("ANALYSIS SUMMARY:");
	        System.out.println("- Number of charging stations: 10");
	        System.out.println("- Number of cables in optimal network: 9");
	        System.out.println("- Total installation cost: $" + minimumCost + ",000");
	        System.out.println("- Network ensures full connectivity with minimum cost");
	    }
	}
