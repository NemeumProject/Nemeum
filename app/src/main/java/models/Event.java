package models;

import java.util.Date;

public class Event {

    private Integer idEvent;
    private Integer idSport;
    private Integer idCompany;
    private Integer idUser;
    private Integer idTrainer;
    private Boolean isIndoor;
    private Integer capacity;
    private Double price;
    private String city;
    private String address;
    private String postalCode;
    private Integer phone;
    private Date dateEvent;
    private String description;
    private String image;
    private String title;

    public Event(Integer idEvent, Integer idSport, Integer idCompany, Integer idUser, Integer idTrainer, Boolean isIndoor, Integer capacity, Double price, String city, String address, String postalCode, Integer phone, Date dateEvent, String description, String image, String title) {
        this.idEvent = idEvent;
        this.idSport = idSport;
        this.idCompany = idCompany;
        this.idUser = idUser;
        this.idTrainer = idTrainer;
        this.isIndoor = isIndoor;
        this.capacity = capacity;
        this.price = price;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.dateEvent = dateEvent;
        this.description = description;
        this.image = image;
        this.title = title;
    }

    public Event(){
        super();
    }

    public Integer getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
    }

    public Integer getIdSport() {
        return idSport;
    }

    public void setIdSport(Integer idSport) {
        this.idSport = idSport;
    }

    public Integer getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdTrainer() {
        return idTrainer;
    }

    public void setIdTrainer(Integer idTrainer) {
        this.idTrainer = idTrainer;
    }

    public Boolean getIndoor() {
        return isIndoor;
    }

    public void setIndoor(Boolean indoor) {
        isIndoor = indoor;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
