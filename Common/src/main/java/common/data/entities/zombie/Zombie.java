package common.data.entities.zombie;

import common.data.Entity;

import java.util.List;

public class Zombie extends Entity {
    List<int[]> pathFinding;

    public List<int[]> getPathFinding() {
        return pathFinding;
    }

    public void setPathFinding(List<int[]> pathFinding) {
        this.pathFinding = pathFinding;
    }
}
