package zombieaisystem;

import java.util.*;

public class AStar {
    public static class State {
        public int x, y;
        public State(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;
            State state = (State) o;
            return x == state.x && y == state.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class Node {
        public State state;
        public Node parentNode;
        public int depth;
        public int cost;
        public Node(State state, Node parentNode, int depth, int cost) {
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
    }

    private List<int[]> nodePathToIntList(ArrayList<Node> path) {
        List<int[]> result = new ArrayList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            int[] nodeCoords = new int[2];
            nodeCoords[0] = path.get(i).state.x;
            nodeCoords[1] = path.get(i).state.y;
            result.add(nodeCoords);
        }
        return result;
    }

    public List<int[]> treeSearch(String[][] grid, State INITIAL_STATE, State GOAL_STATE) {
        PriorityQueue<Node> fringe = new PriorityQueue<>(
                Comparator.comparingDouble(x -> x.cost + calculateHeuristic(x.state, GOAL_STATE))
        );
        Set<State> visited = new HashSet<>();
        Node initialNode = new Node(INITIAL_STATE, null, 0, 0);
        visited.add(initialNode.state);
        fringe.add(initialNode);
        while (!fringe.isEmpty()) {
            Node node = fringe.poll();
            if (node.state.equals(GOAL_STATE)) {
                return nodePathToIntList(node.path());
            }
            ArrayList<Node> children = expand(node, grid);
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

    private ArrayList<Node> expand(Node node, String[][] grid) {
        ArrayList<Node> successors = new ArrayList<>();
        List<State> children = generateNeighbours(grid, node.state);
        for (State child : children) {
            Node s = new Node(child, node, node.depth + 1, 0);
            successors.add(s);
        }
        return successors;
    }

    private List<State> generateNeighbours(String[][] grid, State state) {
        List<State> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newX = state.x + dir[0];
            int newY = state.y + dir[1];
            if (newX < 0 || newX >= grid.length || newY < 0 || newY >= grid[0].length) {
                continue;
            }
            if (newY < grid[newX].length - 1 &&
                    (Objects.equals(grid[newX][newY], "obstruction") || Objects.equals(grid[newX][newY + 1], "obstruction"))) {
                continue;
            }
            neighbors.add(new State(newX, newY));
        }
        return neighbors;
    }

    private double calculateHeuristic(State current, State goal) {
        return Math.sqrt(Math.pow(goal.x - current.x, 2) + Math.pow(goal.y - current.y, 2));
    }
}
