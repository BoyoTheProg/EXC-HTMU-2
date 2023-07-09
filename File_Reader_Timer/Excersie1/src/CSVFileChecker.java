import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.*;
import java.util.*;

public class CSVFileChecker {

    static String sourceFolderPath = "C:\\Users\\Boyan\\Documents\\File_Reader_Timer";
    static String targetFolderPath = "C:\\Users\\Boyan\\Documents\\File_Reader_Timer\\Result";
    static String csvFileName = "filename.csv";
    public static void main(String[] args) {

        String sourceFilePath = Paths.get(sourceFolderPath, csvFileName).toString();
        File sourceFile = new File(sourceFilePath);


        if (!sourceFile.exists()) {

            int duration = 10 * 60 * 1000;
            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime <= duration) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (sourceFile.exists()) {
                    break;
                }
            }
            System.out.println("File wasn't found!");
        }



        if (sourceFile.exists()) {
            List<List<Double>> columns = calculateColumnStatistics(sourceFile);


            StringBuilder resultBuilder = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());


            String result = "";
            resultBuilder.append("File Name: ").append(csvFileName).append("\n");
            resultBuilder.append("Date and Time: ").append(dateTime).append("\n");

            for (int i =0 ; i < columns.size(); i++) {
                List<Double> column = columns.get(i);
                double min = Collections.min(column);
                double max = Collections.max(column);
                double sum = 0;
                for (Double value : column) {
                    sum += value;
                }
                int count = column.size();
                String average = String.format("%.2f", sum / count);


                resultBuilder.append("Column ").append(i + 1).append(": ");
                resultBuilder.append("Min: ").append(min).append(", ");
                resultBuilder.append("Max: ").append(max).append(", ");
                resultBuilder.append("Average: ").append(average).append(", ");
                resultBuilder.append("Count: ").append(count).append("\n");

                result = resultBuilder.toString();
            }



            // Save result to a text file in the target folder
            String targetFilePath = Paths.get(targetFolderPath, "result.txt").toString();
            try {
                FileWriter writer = new FileWriter(targetFilePath);
                writer.write(result);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Delete the original CSV file
            sourceFile.delete();

            // Exit the program
            System.exit(0);
        }
    }



    private static List<List<Double>> calculateColumnStatistics(File csvFile) {
        List<List<Double>> columns = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(csvFile.toPath(), StandardCharsets.UTF_8);

            if (!lines.isEmpty()) {
                String[] headers = lines.get(0).split(","); // Assuming the first row contains column headers

                int numColumns = headers.length;

                // Initialize lists for each column
                for (int i = 0; i < numColumns; i++) {
                    columns.add(new ArrayList<>());
                }

                // Parse values in each column
                for (int i = 0; i < lines.size(); i++) { // Start from index 1 to skip the header row
                    String[] values = lines.get(i).split(",");

                    for (int j = 0; j < numColumns; j++) {
                        if (values.length > j) {
                            String valueStr = values[j].trim();
                            if (!valueStr.isEmpty()) {
                                double value = Double.parseDouble(valueStr);
                                columns.get(j).add(value);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return columns;
    }

}
