package collisionsystem;

import java.util.ArrayList;
import java.util.List;

public class Quadtree<T> {
    private static final int MAX_OBJECTS = 10;
    private static final int MAX_LEVELS = 5;

    private int level;
    private List<QuadtreeObject<T>> objects;
    private Rectangle bounds;
    private Quadtree<T>[] nodes;

    public Quadtree(int level, double x, double y, double width, double height) {
        this.level = level;
        this.objects = new ArrayList<>();
        this.bounds = new Rectangle(x, y, width, height);
        this.nodes = new Quadtree[4];
    }

    public void insert(T object, double x, double y, double width, double height) {
        if (nodes[0] != null) {
            int index = getIndex(x, y, width, height);

            if (index != -1) {
                nodes[index].insert(object, x, y, width, height);
                return;
            }
        }

        objects.add(new QuadtreeObject<>(object, x, y, width, height));

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                QuadtreeObject<T> obj = objects.get(i);
                int index = getIndex(obj.x, obj.y, obj.width, obj.height);
                if (index != -1) {
                    objects.remove(i);
                    nodes[index].insert(obj.object, obj.x, obj.y, obj.width, obj.height);
                } else {
                    i++;
                }
            }
        }
    }

    public List<T> retrieve(T object, double x, double y, double width, double height) {
        List<T> result = new ArrayList<>();
        int index = getIndex(x, y, width, height);
        if (index != -1 && nodes[0] != null) {
            result.addAll(nodes[index].retrieve(object, x, y, width, height));
        }

        for (QuadtreeObject<T> obj : objects) {
            if (obj.object != object) {
                result.add(obj.object);
            }
        }

        return result;
    }

    private void split() {
        double subWidth = bounds.width / 2;
        double subHeight = bounds.height / 2;
        double x = bounds.x;
        double y = bounds.y;

        nodes[0] = new Quadtree<>(level + 1, x + subWidth, y, subWidth, subHeight);
        nodes[1] = new Quadtree<>(level + 1, x, y, subWidth, subHeight);
        nodes[2] = new Quadtree<>(level + 1, x, y + subHeight, subWidth, subHeight);
        nodes[3] = new Quadtree<>(level + 1, x + subWidth, y + subHeight, subWidth, subHeight);
    }

    private int getIndex(double x, double y, double width, double height) {
        int index = -1;
        double verticalMidpoint = bounds.x + (bounds.width / 2);
        double horizontalMidpoint = bounds.y + (bounds.height / 2);

        boolean topQuadrant = (y < horizontalMidpoint && y + height < horizontalMidpoint);
        boolean bottomQuadrant = (y > horizontalMidpoint);

        if (x < verticalMidpoint && x + width < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } else if (x > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    static class QuadtreeObject<T> {
        T object;
        double x, y, width, height;

        public QuadtreeObject(T object, double x, double y, double width, double height) {
            this.object = object;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    static class Rectangle {
        double x, y, width, height;

        public Rectangle(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}

