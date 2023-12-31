// CompetitorList.java
package competition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CompetitorList {

    // ArrayList to store competitors
    private List<Competitor> competitors;
    private String csvFilePath;


    // Default constructor
    public CompetitorList() {
        this.competitors = new ArrayList<>();
        // Initialize the list with 10 to 15 competitors
        initializeCompetitors();
    }
    
    public CompetitorList(String csvFilePath) {
        this.competitors = new ArrayList<>();  // Initialize the list
        this.csvFilePath = csvFilePath;
    }

    // Method to add a competitor to the list
    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
    }

    // Method to display the full details of all competitors in the list   
    public void displayCompetitorsDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;

            // Print a message indicating the start of reading
            System.out.println("Reading file contents:");

            while ((line = reader.readLine()) != null) {
                // Print each line
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to register a single competitor
    public void registerCompetitor() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter competitor number:");
        int competitorNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline left by nextInt()

        System.out.println("Enter competitor name:");
        String competitorName = scanner.nextLine();

        System.out.println("Enter email:");
        String email = scanner.nextLine();

        System.out.println("Enter date of birth (YYYY-MM-DD):");
        String dateOfBirth = scanner.nextLine();

        // Calculate age from date of birth
        int age = calculateAge(dateOfBirth);

        System.out.println("Enter category:");
        String category = scanner.nextLine();

        System.out.println("Enter level:");
        String level = scanner.nextLine();

        System.out.println("Enter gender:");
        char gender = scanner.next().charAt(0);

        System.out.println("Enter country:");
        scanner.nextLine(); // Consume newline left by next()
        String country = scanner.nextLine();

        System.out.println("Enter scores (comma-separated):");
        String scoresInput = scanner.nextLine();

        // Process scores input and create a Competitor object
        String[] scoresArray = scoresInput.split(",");
        List<Integer> scores = new ArrayList<>();
        for (String score : scoresArray) {
            scores.add(Integer.parseInt(score.trim()));
        }

        Competitor competitor = new Competitor(competitorNumber, competitorName, email, dateOfBirth, category, level, country, gender, scores);
        addCompetitor(competitor);

        System.out.println("Competitor registered successfully!");

        // Save the competitor details to the file
        enterCompetitorDetailsToFile(competitor);

        // Close the scanner
        scanner.close();
    }

 // Method to enter competitor details to a CSV file
    public void enterCompetitorDetailsToFile(Competitor competitor) {
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFilePath, true));
             PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {

        	// Convert the list of integers to a list of strings
            List<String> scoreStrings = new ArrayList<>();
            for (Integer score : competitor.getScores()) {
                scoreStrings.add(String.valueOf(score));
            }
            // Save to CSV file
            String csvData = String.format("%d,%s,%s,%s,%s",
                    competitor.getCompetitorNumber(), competitor.getCompetitorName(),
                    calculateAge(competitor.getDateOfBirth()), competitor.getGender(),
                    String.join(",", scoreStrings));
            csvWriter.write(csvData);
            csvWriter.newLine();
            System.out.println("CSV: Competitor details written to the file successfully.");

            // Save to text file
            String txtData = String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                    competitor.getCompetitorNumber(), competitor.getCompetitorName(),
                    competitor.getEmail(), competitor.getDateOfBirth(),
                    competitor.getGender(), competitor.getCategory(), competitor.getLevel(),
                    String.join(",", scoreStrings));

            txtWriter.println(txtData);
            System.out.println("Text: Full details written to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 // Method to display the full details of all competitors in the list   
    public void printFullDetailsToFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\PC\\Desktop\\CompetitorReport.txt"))) {
            String line;

            // Print a message indicating the start of reading
            System.out.println("Reading file contents:");

            while ((line = reader.readLine()) != null) {
                // Print each line
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to initialize the list with 10 to 15 competitors (modify as needed)
    private void initializeCompetitors() {
        // Implement this method based on your requirements
        // You can use random data or any other logic to initialize competitors
    }

    // Method to calculate age from date of birth
    private int calculateAge(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return 0; // Return 0 if date of birth is not available
        }

        LocalDate birthDate = LocalDate.parse(dateOfBirth);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
    
    
}
