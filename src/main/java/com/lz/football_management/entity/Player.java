package com.lz.football_management.entity;



import java.math.BigDecimal;

public class Player {
    private int id;
    private int team_id;
    private String name;
    private int number;
    private String position;
    private int age;
    private BigDecimal height;
    private BigDecimal weight;
    private String img;

    private Team team;

    public Player() {
        this.id = id;
        this.team_id = team_id;
        this.name = name;
        this.number = number;
        this.position = position;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.img = img;
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", team_id=" + team_id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", position='" + position + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", img='" + img + '\'' +
                ", team=" + team +
                '}';
    }
}