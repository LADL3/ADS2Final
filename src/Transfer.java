public class Transfer {
    private int from_stop_id;
    private int to_stop_id;
    private int transfer_type;
    private int min_transfer_time;

    public Transfer(String in) {
        String[] input = in.split(",");

        try {
            this.from_stop_id = Integer.parseInt(input[0]);
        } catch (Exception e) {
            this.from_stop_id = -1;
        }

        try {
            this.to_stop_id = Integer.parseInt(input[1]);
        } catch (Exception e) {
            this.to_stop_id = -1;
        }

        try {
            this.transfer_type = Integer.parseInt(input[2]);
        } catch (Exception e) {
            this.transfer_type = -1;
        }

        try {
            this.min_transfer_time = Integer.parseInt(input[3]);
        } catch (Exception e) {
            this.min_transfer_time = -1;
        }

    }

    public int getFromStopId() {
        return from_stop_id;
    }

    public int getToStopId() {
        return to_stop_id;
    }

    public int getTransferTime() {
        return transfer_type;
    }

    public int getTransferType() {
        return min_transfer_time;
    }
}
