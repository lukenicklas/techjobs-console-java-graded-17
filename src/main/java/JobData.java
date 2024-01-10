import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     * <p>
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Column that should be searched.
     * @param value  Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        // TODO - implement this method
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) { //loops through each row
            boolean isDuplicate = false; //used to check if the row is a duplicate

            for (HashMap<String, String> job : jobs) {
                if (job.equals(row)) {
                    isDuplicate = true;
                    break; //breaks out of the loop if the row is a duplicate
                }
            }

            if (!isDuplicate) {
                for (Map.Entry<String, String> entry : row.entrySet()) {
                    String aValue = entry.getValue();

                    if (aValue.toLowerCase().contains(value.toLowerCase())) {
                        jobs.add(row);
                        break; //breaks out of the loop if the row is a duplicate
                    }
                }
            }
        }
        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE); //opening the file
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in); //preparing to read the data
            List<CSVRecord> records = parser.getRecords(); //this is reading the entire list of data
            Integer numberOfColumns = records.get(0).size(); //this is looking at the number of columns
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);// this is looking at the headers

            allJobs = new ArrayList<>(); //creating an ArrayList

            // Put the records into a more friendly format
            for (CSVRecord record : records) { //this is going through each record
                HashMap<String, String> newJob = new HashMap<>(); //creating a new HashMap

                for (String headerLabel : headers) { //this is going through each header
                    newJob.put(headerLabel, record.get(headerLabel)); //adding the data to the HashMap
                }

                allJobs.add(newJob); //adding the HashMap to the ArrayList
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
