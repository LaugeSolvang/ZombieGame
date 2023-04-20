package zombieaisystem;

import java.util.*;

public class AStar {

    class Node {
        public int[] STATE;
        public Node PARENT_NODE;
        public int DEPTH;
        public int COST;

        public Node(int[] state, Node parent, int depth, int cost) {
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

        public void display() {
            System.out.println(this.toString());
        }

        @Override
        public String toString() {
            return "State " + java.util.Arrays.toString(STATE) + " Depth " + DEPTH + " Cost " + COST;
        }
    }

    public class TreeSearch {
        private static Map<int[], List<int[]>> STATE_SPACE = null;
        private int[][] HEURISTICS = null;
        private int[][] COST = null;

        public ArrayList<Node> treeSearch(int[][] grid, int[] INITIAL_STATE, int[] GOAL_STATE) {
            if (STATE_SPACE == null) {
                STATE_SPACE = generateStateSpace(grid);
            }

            ArrayList<Node> fringe = new ArrayList<>();
            Node initialNode = new Node(INITIAL_STATE, null, 0, 0);
            fringe = INSERT(initialNode, fringe);
            while (!fringe.isEmpty()) {
                Node node = REMOVE_FIRST(fringe);
                if (java.util.Arrays.equals(node.STATE, GOAL_STATE)) {
                    return node.path();
                }
                ArrayList<Node> children = EXPAND(node);
                fringe = INSERT_ALL(children, fringe);
                for (Node child : children) {
                    child.COST = node.COST + COST[node.COST][child.COST];
                }
                fringe.sort(Comparator.comparingInt(x -> HEURISTICS[x.STATE[0]][x.STATE[1]] + x.COST));
                System.out.println("fringe: " + fringe);
            }
            return null;
        }

        public ArrayList<Node> EXPAND(Node node) {
            ArrayList<Node> successors = new ArrayList<>();
            List<int[]> children = successor_fn(node.STATE);
            for (int[] child : children) {
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

        public static List<int[]> successor_fn(int[] state) {
            return STATE_SPACE.get(state);
        }

        public static Map<int[], List<int[]>> generateStateSpace(int[][] grid) {
            Map<int[], List<int[]>> stateSpace = new HashMap<>();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    int [] state = new int[]{i, j};
                    stateSpace.put(state, generateNeighbours(grid, state));
                }
            }
            return stateSpace;
        }
        // Method to get a list of a node's neighbors on the grid
        private static List<int[]> generateNeighbours(int[][] grid, int[] STATE) {
            List<int[]> neighbors = new ArrayList<>();
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int x = STATE[0] + dir[0];
                int y = STATE[1] + dir[1];
                if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
                    continue;
                }
                if (grid[x][y] == 1) {
                    continue;
                }
                neighbors.add(STATE);
            }
            return neighbors;
        }
    }
}
