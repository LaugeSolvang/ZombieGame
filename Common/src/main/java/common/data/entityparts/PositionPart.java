package common.data.entityparts;

import common.data.Entity;
import common.data.GameData;

public class PositionPart implements EntityPart{
    private float x;
    private float y;
    private float radians;
    private float width;
    private float height;

    public PositionPart(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public PositionPart(float x, float y, float radians) {
        this.x = x;
        this.y = y;
        this.radians = radians;
    }
    public PositionPart(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float newX) {
        this.x = newX;
    }

    public void setY(float newY) {
        this.y = newY;
    }

    public void setPosition(float newX, float newY) {
        this.x = newX;
        this.y = newY;
    }
    public void setPosition(float newX, float newY, float radians) {
        this.x = newX;
        this.y = newY;
        this.radians = radians;
    }

    public void setDimension(int[] dimensions) {
        this.width = dimensions[0];
        this.height = dimensions[1];
    }

    public float getRadians() {
        return radians;
    }

    public void setRadians(float radians) {
        this.radians = radians;
    }

    @Override
    public void process(GameData gameData, Entity entity) {

    }
}
