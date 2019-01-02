package models;

import java.sql.Time;

public class TrainerService {

    private Integer id_training_service_post;
    private Integer id_trainer_user;
    private Integer id_sport_training_type;
    private String training_address;
    private Double training_price;
    private String training_city;
    private Time training_start;
    private Time training_end;
    private String training_desc;

    public TrainerService(){
        super();
    }

    public TrainerService(Integer id_training_service_post, Integer id_trainer_user, Integer id_sport_training_type, String training_address, Double training_price, String training_city, Time training_start, Time training_end, String training_desc) {
        this.id_training_service_post = id_training_service_post;
        this.id_trainer_user = id_trainer_user;
        this.id_sport_training_type = id_sport_training_type;
        this.training_address = training_address;
        this.training_price = training_price;
        this.training_city = training_city;
        this.training_start = training_start;
        this.training_end = training_end;
        this.training_desc = training_desc;
    }

    public Integer getId_training_service_post() {
        return id_training_service_post;
    }

    public void setId_training_service_post(Integer id_training_service_post) {
        this.id_training_service_post = id_training_service_post;
    }

    public Integer getId_trainer_user() {
        return id_trainer_user;
    }

    public void setId_trainer_user(Integer id_trainer_user) {
        this.id_trainer_user = id_trainer_user;
    }

    public Integer getId_sport_training_type() {
        return id_sport_training_type;
    }

    public void setId_sport_training_type(Integer id_sport_training_type) {
        this.id_sport_training_type = id_sport_training_type;
    }

    public String getTraining_address() {
        return training_address;
    }

    public void setTraining_address(String training_address) {
        this.training_address = training_address;
    }

    public Double getTraining_price() {
        return training_price;
    }

    public void setTraining_price(Double training_price) {
        this.training_price = training_price;
    }

    public String getTraining_city() {
        return training_city;
    }

    public void setTraining_city(String training_city) {
        this.training_city = training_city;
    }

    public Time getTraining_start() {
        return training_start;
    }

    public void setTraining_start(Time training_start) {
        this.training_start = training_start;
    }

    public Time getTraining_end() {
        return training_end;
    }

    public void setTraining_end(Time training_end) {
        this.training_end = training_end;
    }

    public String getTraining_desc() {
        return training_desc;
    }

    public void setTraining_desc(String training_desc) {
        this.training_desc = training_desc;
    }
}
