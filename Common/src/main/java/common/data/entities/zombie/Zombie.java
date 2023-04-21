package common.data.entities.zombie;

import common.data.Entity;

public class Zombie extends Entity {
    int[][] pathFinding;

    public int[][] getPathFinding() {
        return pathFinding;
    }

    public void setPathFinding(int[][] pathFinding) {
        this.pathFinding = pathFinding;
    }
}
