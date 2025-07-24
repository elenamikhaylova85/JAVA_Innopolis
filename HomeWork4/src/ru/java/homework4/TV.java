package ru.java.homework4;

public class TV {
    private String model;
    private int diagonal;

   //Конструктор
    public TV (String m,int d) {
        this.model=m;
        this.diagonal =d;
    }
    //методы
    public String getModel() {
        return model;
    }

    public int getDiagonal() {
        return diagonal;
    }


}
