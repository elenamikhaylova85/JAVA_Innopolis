package ru.dungeon.model;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private int attack;
    private final List<Item> inventory = new ArrayList<>(); //список инвентаря
    //конструктор без списка инвентаря
    public Player(String name, int hp, int attack) {
        super(name, hp);
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return  "Имя=" + super.getName().toString() + ", hp=" + super.getHp() + ", Атака=" + attack;
    }
}
