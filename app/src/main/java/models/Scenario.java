package models;

import java.util.Date;

public class Scenario {

    private Integer idScenario;
    private Integer idSport;
    private Double price;
    private Boolean isIndoor;
    private Integer capacity;
    private Integer idCompany;
    private Date dateScenario;
    private String description;
    private String title;
    private String image;
    private String address;
    private String city;

    public Scenario(Integer idScenario, Integer idSport, Double price, Boolean isIndoor, Integer capacity, Integer idCompany, Date dateScenario, String description, String title, String image, String address, String city) {
        this.idScenario = idScenario;
        this.idSport = idSport;
        this.price = price;
        this.isIndoor = isIndoor;
        this.capacity = capacity;
        this.idCompany = idCompany;
        this.dateScenario = dateScenario;
        this.description = description;
        this.title = title;
        this.image = image;
        this.address = address;
        this.city = city;
    }

    public Scenario(){
        super();
    }

    public Integer getIdScenario() {
        return idScenario;
    }

    public void setIdScenario(Integer idScenario) {
        this.idScenario = idScenario;
    }

    public Integer getIdSport() {
        return idSport;
    }

    public void setIdSport(Integer idSport) {
        this.idSport = idSport;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Integer getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }

    public Date getDateScenario() {
        return dateScenario;
    }

    public void setDateScenario(Date dateScenario) {
        this.dateScenario = dateScenario;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
