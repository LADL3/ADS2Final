public class Stop {

    int stop_id;
    int stop_code;
    String stop_name;
    String stop_desc;
    double stop_lat;
    double stop_lon;
    String zone_id;


    public Stop(String in) {

        String[] inputValues = in.split(",");

        try {
            this.stop_id = Integer.parseInt(inputValues[0]);
        } catch (Exception e) {
            this.stop_id = -1;
        }

        try {
            this.stop_code = Integer.parseInt(inputValues[1]);
        } catch (Exception e) {
            this.stop_code = -1;
        }

        this.stop_name = rearrange(inputValues[2]);

        this.stop_desc = inputValues[3];

        try {
            this.stop_lat = Double.parseDouble(inputValues[4]);
        } catch (Exception e) {
            this.stop_id = -1;
        }
        try {
            this.stop_lon = Double.parseDouble(inputValues[5]);
        } catch (Exception e) {
            this.stop_id = -1;
        }

        this.zone_id = inputValues[6];

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

}
