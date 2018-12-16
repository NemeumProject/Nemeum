package com.nemeum.project.nemeumproject.Entities;

public class scenarios {
    private int id_scenario  ;
    private int sport_id  ;
    private  double price  ;
    private  boolean isindoor ;
    private int capacity;
    private int company_id ;
    private String date_scenario  ;
    private String description;
    private String title;
    private String image ;
    private String address;

    public scenarios(int id_scenario, int sport_id, double price,
                     boolean isindoor, int capacity, int company_id,
                     String date_scenario, String description, String title,
                     String image, String address) {
        this.id_scenario = id_scenario;
        this.sport_id = sport_id;
        this.price = price;
        this.isindoor = isindoor;
        this.capacity = capacity;
        this.company_id = company_id;
        this.date_scenario = date_scenario;
        this.description = description;
        this.title = title;
        this.image = image;
        this.address = address;
    }

    public int getId_scenario() {
        return id_scenario;
    }

    public void setId_scenario(int id_scenario) {
        this.id_scenario = id_scenario;
    }

    public int getSport_id() {
        return sport_id;
    }

    public void setSport_id(int sport_id) {
        this.sport_id = sport_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isIsindoor() {
        return isindoor;
    }

    public void setIsindoor(boolean isindoor) {
        this.isindoor = isindoor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getDate_scenario() {
        return date_scenario;
    }

    public void setDate_scenario(String date_scenario) {
        this.date_scenario = date_scenario;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
