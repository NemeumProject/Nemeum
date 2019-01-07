package models;

import java.util.Date;

public class Booking {

    private Integer idBooking;
    private Integer idScenario;
    private Integer idService;
    private String title;
    private Date timeStartScheduled;
    private Date timeEndScheduled;
    private Date dateSchedule;
    private String address;
    private String city;
    private Double price;

    public Booking(Integer idBooking, Integer idScenario, Integer idService, String title, Date timeStartScheduled, Date timeEndScheduled, Date dateSchedule, String address, String city, Double price) {
        this.idBooking = idBooking;
        this.idScenario = idScenario;
        this.idService = idService;
        this.title = title;
        this.timeStartScheduled = timeStartScheduled;
        this.timeEndScheduled = timeEndScheduled;
        this.dateSchedule = dateSchedule;
        this.address = address;
        this.city = city;
        this.price = price;
    }

    public Booking() {
        super();
    }

    public Integer getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Integer idBooking) {
        this.idBooking = idBooking;
    }

    public Integer getIdScenario() {
        return idScenario;
    }

    public void setIdScenario(Integer idScenario) {
        this.idScenario = idScenario;
    }

    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimeStartScheduled() {
        return timeStartScheduled;
    }

    public void setTimeStartScheduled(Date timeStartScheduled) {
        this.timeStartScheduled = timeStartScheduled;
    }

    public Date getTimeEndScheduled() {
        return timeEndScheduled;
    }

    public void setTimeEndScheduled(Date timeEndScheduled) {
        this.timeEndScheduled = timeEndScheduled;
    }

    public Date getDateSchedule() {
        return dateSchedule;
    }

    public void setDateSchedule(Date dateSchedule) {
        this.dateSchedule = dateSchedule;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
