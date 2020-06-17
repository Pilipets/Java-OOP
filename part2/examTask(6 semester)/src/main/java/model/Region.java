package model;

public class Region {
    private int id;
    private String name;
    private int square;
    private int personType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSquare() {
        return square;
    }

    public void setSquare(int square) {
        this.square = square;
    }

    public int getPersonId() {
        return personType;
    }

    public void setPersonId(int personId) {
        this.personType = personId;
    }

    @Override
    public String toString() {
        return "{id=" + id + '\n' +
                "name=" + name + '\n' +
                "area=" + square + '\n' +
                "personType=" + personType + "}\n";
    }
}
