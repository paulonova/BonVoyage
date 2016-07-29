package bonvoyage.objects;


public class Voyage {

    public static final String TABLE = "voyage";
    public static final String _ID = "_id";
    public static final String USER_ID = "user_id";
    public static final String DESTINY = "destiny";
    public static final String ARRIVAL_DATE = "arrival_date";
    public static final String EXIT_DATE = "exit_date";
    public static final String BUDGET = "budget";
    public static final String NUMBER_PEOPLES = "number_peoples";
    public static final String TYPE_VOYAGE = "type_voyage";
    public static final String[] COLUMNS = new String[]{_ID, USER_ID, DESTINY, ARRIVAL_DATE, EXIT_DATE, TYPE_VOYAGE, BUDGET, NUMBER_PEOPLES};


    private Integer id;
    private Integer user_id;
    private String destiny;
    private String typeVoyage;
    private String arrivalDate;
    private String exitDate;
    private Double budget;
    private Integer numberPeoples;
    private Integer actualVoyage;

    //Constructor


    public Voyage(Integer id, Integer user_id, String destiny, String typeVoyage, String arrivalDate, String exitDate, Double budget, Integer numberPeoples, Integer actualVoyage) {
        this.id = id;
        this.user_id = user_id;
        this.destiny = destiny;
        this.typeVoyage = typeVoyage;
        this.arrivalDate = arrivalDate;
        this.exitDate = exitDate;
        this.budget = budget;
        this.numberPeoples = numberPeoples;
        this.actualVoyage = actualVoyage;
    }

    public Voyage(Integer id, Integer user_id, String destiny, String typeVoyage, String arrivalDate, String exitDate, Double budget, Integer numberPeoples) {
        this.id = id;
        this.user_id = user_id;
        this.destiny = destiny;
        this.typeVoyage = typeVoyage;
        this.arrivalDate = arrivalDate;
        this.exitDate = exitDate;
        this.budget = budget;
        this.numberPeoples = numberPeoples;
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

    public String getTypeVoyage() {
        return typeVoyage;
    }

    public void setTypeVoyage(String typeVoyage) {
        this.typeVoyage = typeVoyage;
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

    public Integer getActualVoyage() {
        return actualVoyage;
    }

    public void setActualVoyage(Integer actualVoyage) {
        this.actualVoyage = actualVoyage;
    }
}
