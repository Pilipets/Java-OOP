import connection.DBProxy;
import model.WeatherStamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ExamDemo {
    public static void main(String[]args){
        //first test
        System.out.println("Print the weather stats in the given region");
        Scanner in = new Scanner(System.in);
        String region = in.nextLine();
        ArrayList<WeatherStamp> weathersStats = DBProxy.getWeatherStatsInRegion(region);
        for(WeatherStamp w: weathersStats){
            System.out.println(w.toString());
        }

        //second test
        System.out.println("Print the dates in the given region when it was snowing and the temparature was lower that -10");
        region = in.nextLine();
        ArrayList<java.sql.Date> snowDates = DBProxy.getSnowDatesInRegion(region);
        for(Date d: snowDates){
            System.out.println(d);
        }

        //third test
        System.out.println("Print info about the weather within the last week in the regions with ukraininan speaking inhabitants.");
        String lang = in.nextLine();
        weathersStats = DBProxy.getWeatherStatsInRegionsBySpeakingLang(lang);
        for(WeatherStamp w: weathersStats){
            System.out.println(w.toString());
        }

        //fourth test
        System.out.println("Print average temperature within the last week in the regions with the square area > 1000.");
        double temperature = DBProxy.getAverageTemperature();
        System.out.println("Average temperature is : " + temperature);
    }
}
