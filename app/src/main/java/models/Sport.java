package models;

public class Sport {

    private Integer idSport;
    private String name;

    public Sport(Integer idSport, String name) {
        this.idSport = idSport;
        this.name = name;
    }

    public Sport(){
        super();
    }

    public Integer getIdSport() {
        return idSport;
    }

    public void setIdSport(Integer idSport) {
        this.idSport = idSport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
