package zombieaisystem;

import java.util.*;

public class AStar {
    public static void main(String[] args) {
        String[][] map = new String[45][23];
        AStar astar = new AStar();


        long totalDuration = 0;

        for (int i = 0; i < 30; i++) {
            long startTime = System.nanoTime();

            astar.treeSearch(map, "26,10", "28,5");

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);  // in nanoseconds
            totalDuration += duration;
        }

        long averageDuration = totalDuration / 30;
        System.out.println("Average execution time: " + averageDuration + " ns");

        System.out.println(Arrays.deepToString(new List[]{astar.treeSearch(map, "26,10", "28,5")}));




    }

    private static class Node {
        public String state;
        public Node parentNode;
        public int depth;
        public int cost;
        public Node(String state, Node parentNode, int depth, int cost) {
            this.state = state;
            this.parentNode = parentNode;
            this.depth = depth;
            this.cost = cost;
        }

        public ArrayList<Node> path() {
            Node currentNode = this;
            ArrayList<Node> path = new ArrayList<>();
            path.add(this);
            while (currentNode.parentNode != null) {
                currentNode = currentNode.parentNode;
                path.add(currentNode);
            }
            return path;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "STATE='" + state + '\'' +
                    ", DEPTH=" + depth +
                    ", COST=" + cost +
                    '}';
        }
    }

    private static Map<String, List<String>> STATE_SPACE;
    private Map<String, Double> HEURISTICS;

    private List<int[]> nodePathToIntList(ArrayList<Node> path) {
        List<int[]> result = new ArrayList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            String[] splitState = path.get(i).state.split(",");
            int[] nodeCoords = new int[2];
            nodeCoords[0] = Integer.parseInt(splitState[0]);
            nodeCoords[1] = Integer.parseInt(splitState[1]);
            result.add(nodeCoords);
        }
        return result;
    }


    public List<int[]> treeSearch(String[][] grid, String INITIAL_STATE, String GOAL_STATE) {
        if (STATE_SPACE == null) {
            STATE_SPACE = generateStateSpace(grid);
        }
        if (HEURISTICS == null) {
            HEURISTICS = generateHeuristics(grid, GOAL_STATE);
        }

        PriorityQueue<Node> fringe = new PriorityQueue<>(Comparator.comparingDouble(x -> x.cost + HEURISTICS.get(x.state)));
        Node initialNode = new Node(INITIAL_STATE, null, 0, 0);
        Set<String> visited = new HashSet<>();
        visited.add(initialNode.state);
        fringe.add(initialNode);
        while (!fringe.isEmpty()) {
            Node node = fringe.poll();
            if (node.state.equals(GOAL_STATE)) {
                return nodePathToIntList(node.path());
            }
            ArrayList<Node> children = expand(node);
            for (Node child : children) {
                if (!visited.contains(child.state)) {
                    visited.add(child.state);
                    child.cost = node.cost + 1;
                    fringe.add(child);
                }
            }
        }
        return null;
    }

    private ArrayList<Node> expand(Node node) {
        ArrayList<Node> successors = new ArrayList<>();
        List<String> children = successorFunction(node.state);
        if (children != null) {
            for (String child : children) {
                Node s = new Node(child, node, node.depth + 1, 0);
                insert(s, successors);
            }
        }
        return successors;
    }

    private static void insert(Node node, ArrayList<Node> queue) {
        queue.add(node);
    }
    private static List<String> successorFunction(String state) {
        return STATE_SPACE.get(state);
    }

    private static Map<String, List<String>> generateStateSpace(String[][] grid) {
        Map<String, List<String>> stateSpace = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == null) {
                    String state = i + "," + j;
                    stateSpace.put(state, generateNeighbours(grid, state));
                }
            }
        }
        return stateSpace;
    }

    // Method to get a list of a node's neighbors on the grid
    private static List<String> generateNeighbours(String[][] grid, String STATE) {
        List<String> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String[] parts = STATE.split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX < 0 || newX >= grid.length || newY < 0 || newY >= grid[0].length) {
                continue;
            }
            if (newY < grid[newX].length - 1 &&
                    (Objects.equals(grid[newX][newY], "obstruction") || Objects.equals(grid[newX][newY + 1], "obstruction"))) {
                continue;
            }

            neighbors.add(newX + "," + newY);
        }
        return neighbors;
    }

    private static Map<String, Double> generateHeuristics(String[][] grid, String GOAL_STATE) {
        Map<String, Double> heuristics = new HashMap<>();
        String[] goalParts = GOAL_STATE.split(",");
        int dx = Integer.parseInt(goalParts[0]);
        int dy = Integer.parseInt(goalParts[1]);
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                heuristics.put(x + "," + y, Math.sqrt(Math.pow(dx - x, 2) + Math.pow(dy - y, 2)));
            }
        }
        return heuristics;
    }
}
