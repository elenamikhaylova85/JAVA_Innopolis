package ru.dungeon.model;
//монстр
public class Monster extends Entity {
    private int level; //уровень
    //конструктор
    public Monster(String name, int level, int hp) {
        super(name, hp);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
