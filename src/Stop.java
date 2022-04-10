public class Stop {

    int stop_id;
    int stop_code;
    String stop_name;
    String stop_desc;
    double stop_lat;
    double stop_lon;
    String zone_id;


    public Stop(String in) {

        String[] input = in.split(",");

        try {
            this.stop_id = Integer.parseInt(input[0]);
        } catch (Exception e) {
            this.stop_id = -1;
        }

        try {
            this.stop_code = Integer.parseInt(input[1]);
        } catch (Exception e) {
            this.stop_code = -1;
        }

        this.stop_name = rearrange(input[2]);

        this.stop_desc = input[3];

        try {
            this.stop_lat = Double.parseDouble(input[4]);
        } catch (Exception e) {
            this.stop_id = -1;
        }
        try {
            this.stop_lon = Double.parseDouble(input[5]);
        } catch (Exception e) {
            this.stop_id = -1;
        }

        this.zone_id = input[6];

    }

    private String rearrange(String in) {

        if (in.charAt(0) == 'F' && in.charAt(1) == 'L' && in.charAt(2) == 'A' && in.charAt(3) == 'G'
                && in.charAt(4) == 'S' && in.charAt(5) == 'T' && in.charAt(6) == 'O' && in.charAt(7) == 'P'
                && in.charAt(8) == ' ') {
            in = in.substring(9) + " FLAGSTOP";
        }
        if (in.charAt(0) == 'W' && in.charAt(1) == 'B' && in.charAt(2) == ' ') {
            return in.substring(3) + " WB";
        }
        else if (in.charAt(0) == 'N' && in.charAt(1) == 'B' && in.charAt(2) == ' ') {
            return in.substring(3) + " NB";
        }
        else if (in.charAt(0) == 'S' && in.charAt(1) == 'B' && in.charAt(2) == ' ') {
            return in.substring(3) + " SB";
        }
        else if (in.charAt(0) == 'E' && in.charAt(1) == 'B' && in.charAt(2) == ' ') {
            return in.substring(3) + " EB";
        } else
            return in;
    }

    public String getID() {
        if (stop_id == -1) {
            return "NA";
        } else
            return "" + this.stop_id;
    }

    public String getCode() {
        if (stop_code == -1) {
            return "NA";
        } else
            return "" + this.stop_code;
    }

    public String getName() {
        return this.stop_name;
    }

    public String getDesc() {
        return this.stop_desc;
    }

    public String getLat() {
        if (stop_lat == -1) {
            return "NA";
        } else
            return "" + this.stop_lat;
    }

    public String getLon() {
        if (stop_lon == -1) {
            return "NA";
        } else
            return "" + this.stop_lon;
    }

    public String getZone() {
        return this.zone_id;
    }
}
