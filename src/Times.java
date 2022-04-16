import com.sun.tools.javac.Main;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class Times {

    public static ArrayList<String> fileToArrayList(String filename) throws FileNotFoundException, IOException {
        ArrayList<String> result = new ArrayList<>();
        try (BufferedReader f = new BufferedReader(new FileReader(filename))) {
            while (f.ready()) {
                result.add(f.readLine());
            }
        }
        return result;
    }

    public static ArrayList<String> tripInfo(String timeOfArrival) throws FileNotFoundException, IOException {

        ArrayList<String> results = new ArrayList<String>();
        ArrayList<String> fullList = fileToArrayList("stop_times.txt");
        for (int i = 1; i < fullList.size(); i++) {
            try {
                String count = fullList.get(i);
                StopTime StopTimeobject = new StopTime(count);
                LocalTime checkTime = StopTimeobject.getArrivalTime();
                int hour = checkTime.getHour();
                if (hour > 24) {
                    fullList.remove(i);
                }
            } catch (Exception e) {
            }
        }

        for (int i = 1; i < fullList.size(); i++) {
            String index = fullList.get(i);
            StopTime StopTimeObject = new StopTime(index);
            String busTime = StopTimeObject.getArrivalTimeString();
            try {
                if (timeOfArrival.equalsIgnoreCase(busTime)) {
                    String depTime = StopTimeObject.getDepartureTimeString();
                    String info = ""
                            + Integer.toString(StopTimeObject.getTripId())
                            + "\n Stop Name: " + main.stopName(Integer.toString(StopTimeObject.getStopId()))
                            + "\n Stop id: " + Integer.toString(StopTimeObject.getStopId())
                            + "\n Departure time: " + depTime
                            + "\n Stop Sequence: " + Integer.toString(StopTimeObject.getStopSequence())
                            + "\n Pick-up type:  " + Integer.toString(StopTimeObject.getPickUpType())
                            + "\n Drop-off type: " + Integer.toString(StopTimeObject.getDropOffType())
                            + "\n Distance: " + Double.toString(StopTimeObject.getDistance());
                    results.add(info);
                }
            } catch (Exception e) {
            }
        }
        Collections.sort(results);
        return results;
    }
}