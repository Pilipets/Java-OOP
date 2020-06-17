package model;

import java.sql.Date;

public class WeatherStamp {

    private int id;
    private int regionId;
    private Date date;
    private int temperature;
    private String precipitation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    @Override
    public String toString() {
        return "{id=" + id + '\n' +
                "regionId=" + regionId + '\n' +
                "date=" + date + '\n' +
                "temperature=" + temperature + '\n' +
                "precipitation=" + precipitation + "}\n";
    }
}
