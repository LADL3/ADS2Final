import java.time.LocalTime;

public class StopTime {
    private int trip_id;
    private LocalTime arrival_time;
    private LocalTime departure_time;
    private String arrivalTime;
    private String departureTime;
    private int stop_id;
    private int stop_sequence;
    private int pickup_type;
    private int drop_off_type;
    private double shape_dist_traveled;


    public StopTime(String inputLine) {
        String[] inputArray = inputLine.split(",");

        try {
            this.trip_id = Integer.parseInt(inputArray[0]);
        } catch (Exception e) {
            this.trip_id = -1;
        }

        try {
            this.arrivalTime = inputArray[1];
        } catch (Exception e) {

        }

        try {
            this.departureTime = inputArray[2];
        } catch (Exception e) {

        }

        try {
            this.arrival_time = LocalTime.parse(inputArray[1]);
        } catch (Exception e) {
            this.arrival_time = null;
        }

        try {
            this.departure_time = LocalTime.parse(inputArray[2]);
        } catch (Exception e) {
            this.departure_time = null;
        }

        try {
            this.stop_id = Integer.parseInt(inputArray[3]);
        } catch (Exception e) {
            this.stop_id = -1;
        }

        try {
            this.stop_sequence = Integer.parseInt(inputArray[4]);
        } catch (Exception e) {
            this.stop_sequence = -1;
        }

        try {
            this.pickup_type = Integer.parseInt(inputArray[6]);
        } catch (Exception e) {
            this.pickup_type = -1;
        }

        try {
            this.drop_off_type = Integer.parseInt(inputArray[7]);
        } catch (Exception e) {
            this.drop_off_type = -1;
        }

        try {
            this.shape_dist_traveled = Double.parseDouble(inputArray[8]);
        } catch (Exception e) {
            this.shape_dist_traveled = -1;
        }

    }

    public int getTripId() {
        return trip_id;
    }

    public LocalTime getArrivalTime() {
        return arrival_time;
    }

    public LocalTime getDepartureTime() {
        return departure_time;
    }

    public int getStopId() {
        return stop_id;
    }

    public int getStopSequence() {
        return stop_sequence;
    }

    public int getPickUpType() {
        return pickup_type;
    }

    public int getDropOffType() {
        return drop_off_type;
    }

    public double getDistance() {
        return shape_dist_traveled;
    }

    public String getArrivalTimeString() {
        return arrivalTime;
    }

    public String getDepartureTimeString() {
        return departureTime;
    }
}
