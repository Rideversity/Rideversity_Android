package cab.com.rideversitydriver.Models;




public class CarImage
{
    private String cover;

    public String getCover ()
    {
        return cover;
    }

    public void setCover (String cover)
    {
        this.cover = cover;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [cover = "+cover+"]";
    }
}

