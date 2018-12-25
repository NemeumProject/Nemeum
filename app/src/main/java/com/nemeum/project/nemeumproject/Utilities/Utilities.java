package com.nemeum.project.nemeumproject.Utilities;

public class Utilities {

    //constants on table scenarios
    public static final String SCENARIO_TABLE="scenarios";
    public static final String field_IDscenario="id_scenario";
    public static final String field_SportID="sport_id";
    public static final String field_Price="price";
    public static final String field_Isindoor="isindoor";
    public static final String field_Capacity="capacity";
    public static final String field_CompanyID="company_id";
    public static final String field_DateScenario="date_scenario";
    public static final String field_Description="description";
    public static final String field_Title="title";
    public static final String field_Image="image";
    public static final String field_address="address";

   public static final String CREATE_SCENARIOS_TABLE="CREATE TABLE "+SCENARIO_TABLE+" " +
           "( "+field_IDscenario+" INTEGER,\n" +
            "  "+field_SportID+" INTEGER,\n" +
            "  "+field_Price+" REAL,\n" +
            "  "+field_Isindoor+" REAL,\n" +
            "  "+field_Capacity+" INTEGER,\n" +
            "  "+field_CompanyID+" INTEGER,\n" +
            "  "+field_DateScenario+" NUMERIC,\n" +
            "  "+field_Description+" TEXT,\n" +
            "  "+field_Title+" TEXT,\n" +
            "  "+field_Image+" TEXT,\n" +
            "  "+field_address+" TEXT)";
   // public static final String DELETE_SCENARIOS_TABLE="DELETE FROM "+SCENARIO_TABLE+" " ;
}
