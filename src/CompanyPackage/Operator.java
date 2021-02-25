package CompanyPackage;

public class Operator
{
    private static Operator operator = new Operator();

    private Operator(){}

    public static Operator getOperator() {
        return operator;
    }

    private int id;
    private String first_name;
    private String last_name;
    private int user_id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
