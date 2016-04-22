package bonvoyage.objects;


public class Voyage {

    public static final String TABLE = "trip";
    public static final String _ID = "_id";
    public static final String USER_ID = "user_id";
    public static final String DESTINY = "destiny";
    public static final String ARRIVAL_DATE = "arrival_date";
    public static final String EXIT_DATE = "exit_date";
    public static final String BUDGET = "budget";
    public static final String NUMBER_PEOPLES = "number_peoples";
    public static final String TYPE_TRIP = "type_trip";
    public static final String[] COLUMNS = new String[]{_ID, USER_ID, DESTINY, ARRIVAL_DATE, EXIT_DATE, TYPE_TRIP, BUDGET, NUMBER_PEOPLES};


    private Integer id;
    private Integer user_id;
    private String destiny;
    private String typeTrip;
    private String arrivalDate;
    private String exitDate;
    private Double budget;
    private Integer numberPeoples;
    private Integer actualTrip;

    //Constructor


    public Voyage(Integer id, Integer user_id, String destiny, String typeTrip, String arrivalDate, String exitDate, Double budget, Integer numberPeoples, Integer actualTrip) {
        this.id = id;
        this.user_id = user_id;
        this.destiny = destiny;
        this.typeTrip = typeTrip;
        this.arrivalDate = arrivalDate;
        this.exitDate = exitDate;
        this.budget = budget;
        this.numberPeoples = numberPeoples;
        this.actualTrip = actualTrip;
    }

    //Empty Constructor
    public Voyage() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public String getTypeTrip() {
        return typeTrip;
    }

    public void setTypeTrip(String typeTrip) {
        this.typeTrip = typeTrip;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getExitDate() {
        return exitDate;
    }

    public void setExitDate(String exitDate) {
        this.exitDate = exitDate;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Integer getNumberPeoples() {
        return numberPeoples;
    }

    public void setNumberPeoples(Integer numberPeoples) {
        this.numberPeoples = numberPeoples;
    }

    public Integer getActualTrip() {
        return actualTrip;
    }

    public void setActualTrip(Integer actualTrip) {
        this.actualTrip = actualTrip;
    }
}
