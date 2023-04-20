package zombieaisystem;

import java.util.*;

public class AStar {
    public static void main(String[] args) {
        int[][] map = new int[30][15];

        //Spawn obstructions around the perimeter of the game
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 0 || j == 0 || i == map.length-1 || j == map[0].length-1) {
                    map[i][j] = 1;
                } else {
                    map[i][j] = 0;
                }
            }
        }
        AStar aStar = new AStar();
        System.out.println(aStar.treeSearch(map, new int[]{2,2},new int[]{10,10}));
        //STATE_SPACE = generateStateSpace(map);
        //printStateSpace(STATE_SPACE);
    }
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
    }


        private static Map<int[], List<int[]>> STATE_SPACE;
        private Map<int[], Integer> HEURISTICS;

        public ArrayList<Node> treeSearch(int[][] grid, int[] INITIAL_STATE, int[] GOAL_STATE) {
            if (STATE_SPACE == null) {
                STATE_SPACE = generateStateSpace(grid);
            }
            if (HEURISTICS == null) {
                HEURISTICS = generateHeuristics(grid, GOAL_STATE);
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
                    child.COST = node.COST + 1;
                }
                fringe.sort(Comparator.comparingInt(x -> HEURISTICS.get(x.STATE) + x.COST));
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
            System.out.println(java.util.Arrays.toString(state));
            System.out.println(STATE_SPACE.get(state));
            return STATE_SPACE.get(state);
        }

        public static Map<int[], List<int[]>> generateStateSpace(int[][] grid) {
            Map<int[], List<int[]>> stateSpace = new HashMap<>();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] != 1) {
                        int[] state = new int[]{i, j};
                        stateSpace.put(state, generateNeighbours(grid, state));
                    }
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
                neighbors.add(new int[]{x,y});
            }
            return neighbors;
        }

        public Map<int[], Integer> generateHeuristics(int[][] grid, int[] GOAL_STATE) {
            Map<int[], Integer> heuristics = new HashMap<>();
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[x].length; y++) {
                    int dx = Math.abs(x - GOAL_STATE[0]);
                    int dy = Math.abs(y - GOAL_STATE[1]);
                    heuristics.put(new int[]{x, y}, dx + dy);
                }
            }
            return heuristics;
        }

    public static void printStateSpace(Map<int[], List<int[]>> stateSpace) {
        TreeSet<int[]> sortedStates = new TreeSet<>(Comparator.comparingInt((int[] o) -> o[0]).thenComparingInt(o -> o[1]));
        sortedStates.addAll(stateSpace.keySet());

        for (int[] state : sortedStates) {
            System.out.print("State: " + Arrays.toString(state) + " Neighbours: ");
            List<int[]> neighbours = stateSpace.get(state);
            for (int[] neighbour : neighbours) {
                System.out.print(Arrays.toString(neighbour) + " ");
            }
            System.out.println();
        }
    }
}
