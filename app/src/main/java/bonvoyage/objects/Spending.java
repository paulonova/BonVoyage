package bonvoyage.objects;


public class Spending {

    public static final String TABLE = "spending";
    public static final String _ID = "_id";
    public static final String VOYAGE_ID = "trip_id";
    public static final String CATEGORY = "category";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final String PLACE = "place";
    public static final String[] COLUMNS = new String[]{_ID, VOYAGE_ID, CATEGORY, DATE, DESCRIPTION, VALUE, PLACE};


    private Integer id;
    private String date;
    private String category;
    private String description;
    private Double value;
    private String place;
    private Integer voyageId;

    //Constructor
    public Spending(Integer id, String date, String category, String description, Double value, String place, Integer voyageId) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.description = description;
        this.value = value;
        this.place = place;
        this.voyageId = voyageId;
    }

    //Empty Constructor
    public Spending() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(Integer voyageId) {
        this.voyageId = voyageId;
    }
}
