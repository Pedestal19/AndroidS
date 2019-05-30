package hosanna_techvibes.revcollectenumerator.adapter;

public class TaxpayerSearch {
    private String name;
    private String t_id;

    private String fname;
    private String mname;
    private String lname;

    public TaxpayerSearch(String name, String t_id) {
        this.name = name;
        this.t_id = t_id;
    }

    public String getName() {
//        name = lname + " , "+fname;
        return name;
    }

    public String getT_id() {
        return t_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getFname() {
        return fname;
    }

    public String getMname() {
        return mname;
    }

    public String getLname() {
        return lname;
    }
}
