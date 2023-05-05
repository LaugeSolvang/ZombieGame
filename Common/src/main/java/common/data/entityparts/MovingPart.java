package common.data.entityparts;

import common.data.Entity;
import common.data.GameData;


public class MovingPart implements EntityPart{
    private float dx, dy;

    private float deceleration, acceleration;
    private float maxSpeed;
    private boolean left, right, up, down;

    public MovingPart(float deceleration, float acceleration, float maxSpeed) {
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
    }

    public float getDeceleration() {
        return deceleration;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setSpeed(float speed) {
        this.acceleration = speed;
        this.maxSpeed = speed;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }
    public void setDown(boolean down) {
        this.down = down;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }


    @Override
    public void process(GameData gameData, Entity entity) {

        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float dt = gameData.getDelta();
        float radians = positionPart.getRadians();


        // update speed based on acceleration
        if (down) {
            dy -= acceleration * dt;
        }

        if (up) {
            dy += acceleration * dt;
        }

        if (left) {
            dx -= acceleration * dt;
        }

        if (right) {
            dx += acceleration * dt;
        }

        // calculate current speed
        float speed = (float) Math.sqrt(dx * dx + dy * dy);

        // limit speed to maximum
        if (speed > maxSpeed) {
            dx = (dx / speed) * maxSpeed;
            dy = (dy / speed) * maxSpeed;
        }

        // update position based on speed and delta time
        x += dx * dt;
        y += dy * dt;

        // apply deceleration if no movement keys are pressed
        if (!up && !down) {
            if (dy > 0) {
                dy -= deceleration * dt;
                if (dy < 0) {
                    dy = 0;
                }
            } else if (dy < 0) {
                dy += deceleration * dt;
                if (dy > 0) {
                    dy = 0;
                }
            }
        }

        if (!left && !right) {
            if (dx > 0) {
                dx -= deceleration * dt;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += deceleration * dt;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        // set position
        if (x > gameData.getDisplayWidth()) {
            x = 0;
        } else if (x < 0) {
            x = gameData.getDisplayWidth();
        }

        if (y > gameData.getDisplayHeight()) {
            y = 0;
        } else if (y < 0) {
            y = gameData.getDisplayHeight();
        }

        positionPart.setX(x);
        positionPart.setY(y);

        // update radians if moving
        if (dx != 0 || dy != 0) {
            radians = (float) Math.atan2(dy, dx);
        }
        positionPart.setRadians(radians);
    }
}
