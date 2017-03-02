package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

public class OnGoingModel {

    private String message;

    private String result;

    private ArrayList<Data> data;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getResult ()
    {
        return result;
    }

    public void setResult (String result)
    {
        this.result = result;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", result = "+result+", data = "+data+"]";
    }
}
