package zombieaisystem;

import java.util.*;
import java.util.stream.Collectors;

public class AStar {
    public static void main(String[] args) {
        int[][] map = new int[30][15];

        //Spawn obstructions around the perimeter of the game
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 0 || j == 0 || i == map.length - 1 || j == map[0].length - 1) {
                    map[i][j] = 1;
                } else {
                    map[i][j] = 0;
                }
            }
        }
        AStar astar = new AStar();

        long totalDuration = 0;

        for (int i = 0; i < 30; i++) {
            long startTime = System.nanoTime();

            astar.treeSearch(map, "1,1", "10,10");

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);  // in nanoseconds
            totalDuration += duration;
        }

        long averageDuration = totalDuration / 30;
        System.out.println("Average execution time: " + averageDuration + " ns");

        System.out.println(Arrays.deepToString(astar.treeSearch(map, "1,1", "10,10")));

    }

    class Node {
        public String STATE;
        public Node PARENT_NODE;
        public int DEPTH;
        public int COST;

        public Node(String state, Node parent, int depth, int cost) {
            STATE = state;
            PARENT_NODE = parent;
            DEPTH = depth;
            COST = cost;
        }

        public ArrayList<Node> path() {
            Node currentNode = this;
            ArrayList<Node> path = new ArrayList<>();
            path.add(this);
            while (currentNode.PARENT_NODE != null) {
                currentNode = currentNode.PARENT_NODE;
                path.add(currentNode);
            }
            return path;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "STATE='" + STATE + '\'' +
                    ", DEPTH=" + DEPTH +
                    ", COST=" + COST +
                    '}';
        }
    }

    private static Map<String, List<String>> STATE_SPACE;
    private Map<String, Double> HEURISTICS;

    private int[][] nodePathToIntArray(ArrayList<Node> path) {
        int[][] result = new int[path.size()][2];
        int j = 0;
        for (int i = path.size() - 1; i >= 0; i--) {
            String[] splitState = path.get(i).STATE.split(",");
            result[j][0] = Integer.parseInt(splitState[0]);
            result[j][1] = Integer.parseInt(splitState[1]);
            j++;
        }
        return result;
    }


    public int[][] treeSearch(int[][] grid, String INITIAL_STATE, String GOAL_STATE) {
        if (STATE_SPACE == null) {
            STATE_SPACE = generateStateSpace(grid);
        }
        //printStateSpace(STATE_SPACE);
        if (HEURISTICS == null) {
            HEURISTICS = generateHeuristics(grid, GOAL_STATE);
        }

        //System.out.println(HEURISTICS);

        ArrayList<Node> fringe = new ArrayList<>();
        Node initialNode = new Node(INITIAL_STATE, null, 0, 0);
        Set<String> visited = new HashSet<>();
        visited.add(initialNode.STATE);
        fringe = INSERT(initialNode, fringe);
        while (!fringe.isEmpty()) {
            Node node = REMOVE_FIRST(fringe);
            if (node.STATE.equals(GOAL_STATE)) {
                return nodePathToIntArray(node.path());
            }
            ArrayList<Node> children = EXPAND(node);
            for (Node child : children) {
                if (!visited.contains(child.STATE)) {
                    visited.add(child.STATE);
                    child.COST = node.COST + 1;
                    fringe = INSERT(child, fringe);
                }

            }
            fringe.sort(Comparator.comparingDouble(x -> HEURISTICS.get(x.STATE)));

            //System.out.println("fringe: " + fringe);
        }
        return null;
    }

    public ArrayList<Node> EXPAND(Node node) {
        ArrayList<Node> successors = new ArrayList<>();
        List<String> children = successor_fn(node.STATE);
        for (String child : children) {
            Node s = new Node(child, node, node.DEPTH + 1, 0);
            successors = INSERT(s, successors);
        }
        return successors;
    }

    public static ArrayList<Node> INSERT(Node node, ArrayList<Node> queue) {
        queue.add(node);
        return queue;
    }

    public static ArrayList<Node> INSERT_ALL(ArrayList<Node> list, ArrayList<Node> queue) {
        queue.addAll(list);
        return queue;
    }

    public static Node REMOVE_FIRST(ArrayList<Node> queue) {
        return queue.remove(0);
    }

    public static List<String> successor_fn(String state) {
        return STATE_SPACE.get(state);
    }

    public static Map<String, List<String>> generateStateSpace(int[][] grid) {
        Map<String, List<String>> stateSpace = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != 1) {
                    String state = i + "," + j;
                    stateSpace.put(state, generateNeighbours(grid, state));
                }
            }
        }
        return stateSpace;
    }

    // Method to get a list of a node's neighbors on the grid
    private static List<String> generateNeighbours(int[][] grid, String STATE) {
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
            if (grid[newX][newY] == 1) {
                continue;
            }
            neighbors.add(newX + "," + newY);
        }
        return neighbors;
    }

    public static Map<String, Double> generateHeuristics(int[][] grid, String GOAL_STATE) {
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

    public static void printStateSpace(Map<String, List<String>> stateSpace) {
        TreeSet<String> sortedStates = new TreeSet<>(Comparator.comparing((String o) -> o));
        sortedStates.addAll(stateSpace.keySet());

        for (String state : sortedStates) {
            System.out.print("State: [" + state + "] Neighbours: ");
            List<String> neighbours = stateSpace.get(state);
            for (String neighbour : neighbours) {
                System.out.print("["+neighbour + "] ");
            }
            System.out.println();
        }
    }}
