package common.data.entityparts;

public class HealthPart {
    public static int health = 10000;



    public HealthPart() {
    }

    public static void updateHealth() {
        health--;
    }

    public static int getHealth() {
        return health;
    }
}


