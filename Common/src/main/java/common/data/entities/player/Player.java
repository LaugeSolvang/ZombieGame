package common.data.entities.player;


import common.data.Entity;
public class Player extends Entity {
    private final Inventory inventory;

    public Player() {
        inventory = new Inventory();
    }
    public Inventory getInventory() {
        return inventory;
    }}

