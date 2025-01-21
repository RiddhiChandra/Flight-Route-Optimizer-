# Flight Route Optimizer

The Flight Route Optimizer is designed to efficiently determine the best flight route between selected cities based on **minimum travel time** or **lowest ticket price**. This project showcases the use of the **A* algorithm** for pathfinding in a simplified travel network of seven cities.

## Features
- **Optimal Route Recommendation**: Calculate routes based on either travel time or ticket price.
- **Interactive GUI**: Users can select departure and destination cities, optimization criteria, and view recommended routes.

---

## Table of Contents
- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Data Structures](#data-structures)
- [Methodology](#methodology)
- [Results](#results)

---

## Introduction
Efficient travel planning requires balancing multiple factors like cost and time. This project addresses this challenge by:
- Utilizing the **A* algorithm** with a heuristic function based on **Euclidean distance**.
- Designing a **user-friendly GUI** to simplify interaction with the underlying system.

---

## Technologies Used
- **Java**: Core programming language.
- **Swing**: GUI framework for Java.
- **IntelliJ IDEA**: Integrated Development Environment (IDE).
- **Data Structures**: 
  - HashMap for graph representation.
  - ArrayList for adjacency lists.
  - PriorityQueue for A* pathfinding.

---

## Data Structures
1. **HashMap**: Stores airport coordinates and adjacency lists for efficient lookups.
2. **ArrayList**: Implements adjacency lists for dynamic graph representation.
3. **PriorityQueue**: Selects the node with the lowest f(n) during the A* search.

---

## Methodology
### Graph Representation
- Cities: Nodes.
- Flights: Edges with weights for travel time and ticket price.

### Algorithm: A*
1. **Heuristic Function**: Estimates remaining cost using Euclidean distance.
2. **Pathfinding**:
   - Priority queue to explore nodes based on `f(n) = g(n) + h(n)`.
   - Updates g-scores to track the minimum cost path.

---

## Results
- Demonstrated optimal paths for selected routes (e.g., MAA to DEL, HYD to MP) based on time and cost.
- Visualization through graphs and adjacency matrices for both criteria.

---
