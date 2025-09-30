package ru.dungeon.model;
//игровая сущность
public abstract class Entity {
    private String name;//имя
    private int hp;//количество опыта
    //конструктор
    public Entity(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

}
