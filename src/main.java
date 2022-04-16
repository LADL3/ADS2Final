import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    public static ArrayList<Stop> stops = new ArrayList<Stop>();
    public static ArrayList<String> listOfResults = new ArrayList<String>();
    public static ArrayList<Transfer> transfers = new ArrayList<Transfer>();
    public static ArrayList<StopTime> stopTimes = new ArrayList<StopTime>();
    public static TST tst = new TST();
    public static boolean invalid = false;


    public static void main(String[] args) throws IOException {
        init_stops();
        initStopTimes();
        initTransfer();
        make_TST();

        if (!invalid) {
            Scanner inputScanner = new Scanner(System.in);
            boolean finish = false;
            while (!finish) {
                System.out.println("\n\nSelect Function: (1-4)");
                System.out.println(" 1. Shortest Path between 2 Stops ");
                System.out.println(" 2. Search for a Bus Stop ");
                System.out.println(" 3. Search for All Trips ");
                System.out.println(" 4. Exit");
                System.out.print("\nAnswer: ");

                if (inputScanner.hasNextLine()) {
                    String input = inputScanner.nextLine();

                    if (input.equals("4")) {
                        finish = true;
                        System.out.println("Exiting...");
                        break;
                    } else if (input.equals("1")){
                        String stop1 = "";
                        String stop2 = "";
                        try {
                            System.out.println("Enter first stop ID, then press ENTER, then enter second stopID");
                            String start = inputScanner.nextLine();
                            String stop = inputScanner.nextLine();
                            if (!isValidStopID(start)) {
                                stop1 = start;
                            }
                            if (!isValidStopID(stop)) {
                                stop1 = stop;
                            }
                            if (isValidStopID(start) && isValidStopID(stop)) {
                                int startInt = Integer.parseInt(start);
                                int stopInt = Integer.parseInt(stop);
                                ShortestPath shortestPath = new ShortestPath();
                                Stop startStop = null;
                                Stop endStop = null;
                                boolean error = false;

                                for (int i = 0; i < stops.size(); i++) {
                                    if (stops.get(i).stop_id == startInt) {
                                        startStop = stops.get(i);
                                    }
                                    if (stops.get(i).stop_id == stopInt) {
                                        endStop = stops.get(i);
                                    }
                                }

                                Graph bus = new Graph(stops, transfers, stopTimes);
                                if (!bus.invalid) {
                                    shortestPath = bus.getShortestPath(startStop, endStop);
                                    if (shortestPath.getWeight() < 0) {
                                        error = true;
                                    }
                                    if (shortestPath.getWeight() == -1 && !error) {
                                        System.out.println("No Shortest Path");
                                    } else {
                                        System.out.println("Shortest Path : " + shortestPath.getWeight());
                                        System.out.println("\nList of shortest path starting at: \n");
                                        String[] output = new String[7];
                                        for (int j = shortestPath.stops.size() - 1; j >= 0; j--) {
                                            output[0] = "Name: " + capitalize(shortestPath.stops.get(j).stop_name);
                                            output[1] = " Description: " + capitalize(shortestPath.stops.get(j).stop_desc);
                                            output[2] = " Stop ID: " + shortestPath.stops.get(j).stop_id;
                                            output[3] = " Stop Code: " + shortestPath.stops.get(j).stop_code;
                                            output[4] = " Latitude: " + shortestPath.stops.get(j).stop_lat;
                                            output[5] = " Longitude: " + shortestPath.stops.get(j).stop_lon;
                                            output[6] = " Zone ID: " + shortestPath.stops.get(j).zone_id;

                                            for (int k = 0; k < output.length; k++) {
                                                System.out.println(output[k]);
                                            }
                                            System.out.println("\n   |\n   V\n");
                                        }
                                        System.out.println("Destination:");

                                        output[0] = "Name: " + capitalize(endStop.stop_name);
                                        output[1] = " Description: " + capitalize(endStop.stop_desc);
                                        output[2] = " Stop ID: " + endStop.stop_id;
                                        output[3] = " Stop Code: " + endStop.stop_code;
                                        output[4] = " Latitude: " + endStop.stop_lat;
                                        output[5] = " Longitude: " + endStop.stop_lon;
                                        output[6] = " Zone ID: " + endStop.zone_id;

                                        for (int k = 0; k < output.length; k++) {
                                            System.out.println(output[k]);
                                        }

                                    }
                                }
                            }
                        } catch (java.lang.NumberFormatException e) {
                            System.out.println("Error occurred due to incorrect Input.");
                        } catch (java.lang.NullPointerException e) {
                            System.out.println("No paths could be found.");
                        }
                        if (stop1 != "") {
                            System.out.print("Error! Stop: " + stop1 + " does not exist.");
                        }
                        if (stop2 != "") {
                            System.out.print("Error! Stop: " + stop2 + " does not exist.");
                        }
                    } else if (input.equals("2"))
                    {
                        System.out.print("Enter the stop to search for: ");
                        String searchKey = inputScanner.nextLine();
                        System.out.println("\nSearching for: " + searchKey);
                        listOfResults = tst.autocomplete(searchKey.toUpperCase());
                        if (searchKey.length() < 1) {
                            listOfResults = null;
                        }
                        if (listOfResults != null) {
                            System.out.println("Found: " + listOfResults.size() + " Results\n");
                        } else {
                            System.out.println("No Results found.");
                        }
                        String[] currentData;
                        if (listOfResults != null) {
                            for (int i = 0; i < listOfResults.size(); i++) {
                                System.out.println("\n\nResult " + (i + 1) + ".) ");
                                currentData = stopInfo(listOfResults.get(i));
                                currentData = outputData(currentData);
                                for (int k = 0; k < currentData.length; k++) {
                                    System.out.println(currentData[k]);
                                }
                            }
                        }
                    } else if (input.equals("3"))
                    {
                        try {
                            System.out.println("Input time in the format: 'hh:mm:ss'   e.g.: 7:31:26");
                            String inputTime = inputScanner.nextLine();
                            if (inputTime.length() > 3) {
                                if (inputTime.charAt(1) == ':') {
                                    inputTime = " " + inputTime;
                                }
                            }

                            if (validTime(inputTime) == true) {
                                System.out.println("Searching for all trips with the time: " + inputTime);
                                ArrayList<String> arrivalTimesList = new ArrayList<String>();
                                arrivalTimesList = Times.tripInfo(inputTime);
                                System.out.println("Trips found: " + arrivalTimesList.size());
                                for (int i = 0; i < arrivalTimesList.size(); i++) {
                                    System.out.print(i+". Trip id: ");
                                    System.out.println(arrivalTimesList.get(i));
                                }
                            }
                        } catch (java.time.format.DateTimeParseException e) {
                            System.out.println("Incorrect Input");
                        }
                    }

                }
            }
            inputScanner.close();
            System.out.println("Exited");
        } else if (invalid) {
            System.out.print("\nProgram failed because of input files.");
        }
    }


    public static void init_stops() throws FileNotFoundException {
        try {
            File stopsFile = new File("stops.txt");
            Scanner scanner = new Scanner(stopsFile);
            // Skipping the first line, since it's a header and has no data
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                stops.add(new Stop(data));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: 'stops.txt' not found.");
            invalid = true;
        }
    }

    public static void initTransfer() {
        try {
            File transferFile = new File("transfers.txt");
            Scanner fileScanner = new Scanner(transferFile);
            // skip the first line
            fileScanner.nextLine();
            while (fileScanner.hasNextLine()) {
                String newLine = fileScanner.nextLine();
                transfers.add(new Transfer(newLine));
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: 'transfers.txt' not found.");
            invalid = true;
        }
    }

    public static void initStopTimes() {
        try {
            File timesFile = new File("stop_times.txt");
            Scanner fileScanner = new Scanner(timesFile);
            fileScanner.nextLine();
            while (fileScanner.hasNextLine()) {
                String newLine = fileScanner.nextLine();
                StopTime newStopTime = new StopTime(newLine);
                stopTimes.add(newStopTime);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: 'stop_times.txt' not found.");
            invalid = true;
        }
    }

    public static void make_TST() {
        for (int i = 0; i < stops.size(); i++) {
            tst.insert(stops.get(i).getName());
        }
    }

    public static String[] stopInfo(String name) {
        String[] outputData = new String[7];

        for (int i = 0; i < stops.size(); i++) {
            if (name.equalsIgnoreCase(stops.get(i).getName())) {
                outputData[0] = stops.get(i).getID();
                outputData[1] = stops.get(i).getCode();
                outputData[2] = stops.get(i).getName();
                outputData[3] = stops.get(i).getDesc();
                outputData[4] = stops.get(i).getLat();
                outputData[5] = stops.get(i).getLon();
                outputData[6] = stops.get(i).getZone();
            }
        }
        return outputData;
    }


    public static String stopName(String ID) {
        String output = "";
        for (int i = 0; i < stops.size(); i++) {
            if (ID.equals(stops.get(i).getID())) {
                output = stops.get(i).getName();
            }
        }
        return capitalize(output);
    }

    public static String capitalize(String name) {
        if (name != null) {
            name = name.toLowerCase();
            char[] charArray = name.toCharArray();
            boolean foundSpace = true;

            for (int i = 0; i < charArray.length; i++) {
                if (Character.isLetter(charArray[i])) {
                    if (foundSpace) {
                        charArray[i] = Character.toUpperCase(charArray[i]);
                        foundSpace = false;
                    }
                } else {
                    foundSpace = true;
                }
            }
            name = String.valueOf(charArray);
            return name;
        } else {
            return null;
        }
    }

    public static String[] outputData(String[] stopData) {
        String[] output = new String[7];

        output[0] = "Stop ID: " + stopData[0];
        output[1] = "Stop Code: " + stopData[1];
        output[2] = "Name: " + capitalize(stopData[2]);
        output[3] = "Description: " + capitalize(stopData[3]);
        output[4] = "Latitude: " + stopData[4];
        output[5] = "Longitude: " + stopData[5];
        output[6] = "Zone ID: " + stopData[6];
        return output;
    }

    public static boolean validTime(String input) {
        boolean result = true;
        if (input.length() < 1) {
            System.out.println("Invalid input, input is empty");
            return false;
        }
        for (int i = 0; i < input.length(); i++) {
            if (Character.isLetter(input.charAt(i)) == true) {
                System.out.print("Invalid input, input should not contain any letters");
                return false;
            }
        }
        if (input.length() < 8) {
            System.out.println("Invalid input, invalid input length");
            return false;
        }

        if (input.length() == 8) {
            if (input.charAt(2) != ':' || input.charAt(5) != ':') {
                System.out.println("Invalid input, incorrect format");
                return false;
            }
        }
        String[] splitInput = null;
        if (input.charAt(0) == ' ') {
            input = input.substring(1);
        }
        splitInput = input.split(":");
        if (Integer.parseInt(splitInput[0]) > 23) {
            System.out.println("Invalid time, hours are greater than 23");
            result = false;
        }
        if (Integer.parseInt(splitInput[1]) > 59) {
            System.out.println("Invalid time, minutes are greater than 59");
            result = false;
        }
        if (Integer.parseInt(splitInput[2]) > 59) {
            System.out.println("Invalid time, seconds are greater than 59");
            result = false;
        }

        return result;
    }

    public static boolean isValidStopID(String ID) {
        for (int i = 0; i < stops.size(); i++) {
            if (ID.equals(stops.get(i).getID())) {
                return true;
            }
        }
        return false;
    }

}