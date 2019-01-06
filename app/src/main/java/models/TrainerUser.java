package models;

public class TrainerUser {

    private Integer idTrainerUser;
    private String username;
    private String password;
    private Boolean isPremium;
    private String firstName;
    private String middleSurname;
    private String lastSurname;
    private String ssn;
    private String email;
    private Integer teachedHours;
    private String city;
    private String address;
    private String postalCode;
    private Integer phone;
    private String description;
    private String title;
    private String image;

    public TrainerUser(Integer idTrainerUser, String username, String password, Boolean isPremium, String firstName, String middleSurname, String lastSurname, String ssn, String email, Integer teachedHours, String city, String address, String postalCode, Integer phone, String description, String title, String image) {
        this.idTrainerUser = idTrainerUser;
        this.username = username;
        this.password = password;
        this.isPremium = isPremium;
        this.firstName = firstName;
        this.middleSurname = middleSurname;
        this.lastSurname = lastSurname;
        this.ssn = ssn;
        this.email = email;
        this.teachedHours = teachedHours;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.description = description;
        this.title = title;
        this.image = image;
    }

    public TrainerUser(){
        super();
    }

    public Integer getIdTrainerUser() {
        return idTrainerUser;
    }

    public void setIdTrainerUser(Integer idTrainerUser) {
        this.idTrainerUser = idTrainerUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getPremium() {
        return isPremium;
    }

    public void setPremium(Boolean premium) {
        isPremium = premium;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleSurname() {
        return middleSurname;
    }

    public void setMiddleSurname(String middleSurname) {
        this.middleSurname = middleSurname;
    }

    public String getLastSurname() {
        return lastSurname;
    }

    public void setLastSurname(String lastSurname) {
        this.lastSurname = lastSurname;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTeachedHours() {
        return teachedHours;
    }

    public void setTeachedHours(Integer teachedHours) {
        this.teachedHours = teachedHours;
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
}
