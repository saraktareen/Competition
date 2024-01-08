package competition;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CompetitorList {



    private List<Competitor> competitors;
    private String csvFilePath;
    private String textFilePath;

    
    //Constructors
    public CompetitorList(String csvFilePath, String textFilePath) {
        this.competitors = new ArrayList<>();
        this.csvFilePath = csvFilePath;
        this.textFilePath = textFilePath;
//        initializeCompetitors();
    }
    
    
//  ------------------------------- COMPETITOR MANAGEMENT -------------------------------

    
    //This adds a new competitor to the list of competitors
    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
    }
    
    
//    =============================================== REGISTER COMPETITOR TO THE CSV FILE ===============================================
    
    //This method registers a competitor
    public void registerCompetitor() {
        try (Scanner scanner = new Scanner(System.in)) {
            do {
                int competitorNumber;
                boolean isUnique;

                do {
                    System.out.println("Enter competitor number:");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a valid competitor number:");
                        scanner.next(); // Consume invalid input
                    }
                    competitorNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left by nextInt()

                    isUnique = isCompetitorNumberUniqueInFile(competitorNumber, csvFilePath);

                    if (!isUnique) {
                        System.out.println("Competitor number " + competitorNumber + " is already assigned. Please enter another one.");
                    }
                } while (!isUnique);

                // Prompt for other details
                String competitorName;
                do {
                    System.out.println("Enter competitor name:");
                    competitorName = scanner.nextLine();

                    if (!isValidCompetitorName(competitorName)) {
                        System.out.println("Invalid input! Please enter a valid competitor name:");
                    }
                } while (!isValidCompetitorName(competitorName));

                String email;
                do {
                    System.out.println("Enter email:");
                    email = scanner.nextLine();

                    if (!isValidEmail(email)) {
                        System.out.println("Invalid input! Please enter a valid email address:");
                    }
                } while (!isValidEmail(email));

                String dateOfBirth;
                do {
                    System.out.println("Enter date of birth (YYYY-MM-DD):");
                    dateOfBirth = scanner.nextLine();

                    if (!isValidDateFormat(dateOfBirth)) {
                        System.out.println("Invalid input! Please enter a valid date of birth:");
                    }
                } while (!isValidDateFormat(dateOfBirth));

                int age = calculateAge(dateOfBirth);

                // Automatically determine the level based on age
                String level = determineCompetitorLevel(age);

                String category;
                do {
                    System.out.println("Enter category:");
                    category = scanner.nextLine();

                    if (!isValidCategory(category)) {
                        System.out.println("Invalid input! Please enter a valid category name:");
                    }
                } while (!isValidCategory(category));

                char gender;
                do {
                    System.out.println("Enter gender (M/F):");
                    gender = scanner.next().charAt(0);
                    scanner.nextLine(); // Consume newline left by next()

                    if (!isValidGender(gender)) {
                        System.out.println("Invalid input! Please enter a valid gender (M/F).");
                    }
                } while (!isValidGender(gender));

                String country;
                do {
                    System.out.println("Enter country:");
                    country = scanner.nextLine();

                    if (!isValidCountry(country)) {
                        System.out.println("Invalid input! Please enter a valid country name:");
                    }
                } while (!isValidCountry(country));

                List<Integer> scores = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    int score;
                    do {
                        System.out.println("Enter score " + i + ":");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input! Please enter a valid score:");
                            scanner.next(); // Consume invalid input
                        }
                        score = scanner.nextInt();

                        // Validate the entered score is between 0 and 5 inclusive
                        if (score < 0 || score > 5) {
                            System.out.println("Invalid input! Please enter a score between 0 and 5 inclusive.");
                        }
                    } while (score < 0 || score > 5);
                    scores.add(score);
                }

                Competitor competitor = new Competitor(competitorNumber, competitorName, email, dateOfBirth, category, level, country, gender, scores);

                if (!isCompetitorNumberUniqueInFile(competitorNumber, csvFilePath)) {
                    System.out.println("Competitor number " + competitorNumber + " is already assigned in the file. Please enter another one.");
                    continue;
                }

                addCompetitor(competitor);

                System.out.println("---------------Competitor registered successfully!---------------");

                enterCompetitorDetailsToFile(competitor);

                // Ask if the user wants to register another competitor
                System.out.println("Do you want to register another competitor? (Y/N)");
            } while (scanner.next().equalsIgnoreCase("Y"));
        }
    }

    
//  =============================================== ENTER THE COMPETITOR DETAILS TO CSV FILE ===============================================

    //This method enters the Competitor Details entered during registration into the CSV file
    public void enterCompetitorDetailsToFile(Competitor competitor) {
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFilePath, true))) {

            List<String> scoreStrings = new ArrayList<>();
            for (Integer score : competitor.getScores()) {
                scoreStrings.add(String.valueOf(score));
            }

            String csvData = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s",
                    competitor.getCompetitorNumber(), competitor.getCompetitorName(), competitor.getEmail(),
                    calculateAge(competitor.getDateOfBirth()), competitor.getGender(), competitor.getCountry(), competitor.getCategory(),
                    competitor.getLevel(), String.join(",", scoreStrings));

            csvWriter.write(csvData);
            csvWriter.newLine();
            System.out.println("---------------CSV: Competitor details written to the csv file successfully.---------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
//  =============================================== DISPLAY COMPETITOR DETAILS FROM THE CSV FILE ===============================================

    //This method displays the competitor details from the CSV file
    public void displayCompetitorsDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;

            System.out.println("===============COMPETITION REPORT===============");

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
    
    
    
//  =============================================== ADDING DATA TO TEXT FILE ===============================================

    //This method displays the competitors details corresponding to the competitor number
    public void displayCompetitorDetailsByNumber() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter competitor number to display details:");
            int competitorNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline left by nextInt()

            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && !parts[0].isEmpty()) {
                        int currentCompetitorNumber = Integer.parseInt(parts[0]);

                        if (currentCompetitorNumber == competitorNumber) {
                            found = true;
                            System.out.println("---------------Competitor Details:---------------");
                            System.out.println("Competitor Number: " + currentCompetitorNumber);
                            System.out.println("Competitor Name: " + parts[1]);
                            System.out.println("Email: " + parts[2]);
                            System.out.println("Date of Birth: " + parts[3]);
                            System.out.println("Gender: " + parts[4]);
                            System.out.println("Country: " + parts[5]);
                            System.out.println("Category: " + parts[6]);
                            System.out.println("Level: " + parts[7]);
                            System.out.println("Scores: " + Arrays.toString(Arrays.copyOfRange(parts, 8, parts.length)));
                            break;
                        }
                    }
                }

                if (!found) {
                    System.out.println("Competitor with number " + competitorNumber + " not found.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
//  ------------------------------- VALIDATION METHODS -------------------------------

    private boolean isCompetitorNumberUniqueInFile(int competitorNumber, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].isEmpty()) {
                    int existingCompetitorNumber = Integer.parseInt(parts[0]);
                    if (existingCompetitorNumber == competitorNumber) {
                        return false; // Competitor number is not unique in the file
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; // Competitor number is unique in the file
    } 
    
    //These are all the Validation methods for when the competitor is being registered
    private boolean isValidCompetitorName(String name) {
        return name.matches("[A-Za-z ]+");
    }
    private boolean isValidDateFormat(String dateOfBirth) {
        // Validate that the date of birth follows the format YYYY-MM-DD
        return dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidCountry(String country) {
        // Validate that the country does not contain integers or symbols
        return country.matches("[a-zA-Z\\s]+");
    }

    private boolean isValidGender(char gender) {
        // Validate that the gender is either 'M' or 'F'
        return gender == 'M' || gender == 'F';
    }

    private boolean isValidCategory(String category) {
        // Validate that the category does not contain integers or symbols
        return category.matches("[a-zA-Z ]+");
    }

    private boolean isValidEmail(String email) {
        // Simple email validation using a regular expression
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }
    
    // New method to determine the level based on age
    private String determineCompetitorLevel(int age) {
        if (age <= 18) {
            return "Novice";
        } else if (age <= 30) {
            return "Intermediate";
        } else {
            return "Expert";
        }
    }

    
    
    
    
    
    
    
    
    
    
//  ------------------------------- PRINTING AND REPORTING METHODS -------------------------------

    
//  =============================================== PRINT DETAILS TO THE TEXT FILE ===============================================

    public void printFullDetailsToFile(String csvFilePath, String textFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
             PrintWriter txtWriter = new PrintWriter(new FileWriter(textFilePath, false))) { // Set append to false

            String line;

            // Print headings
            txtWriter.println("---------------Reading file contents:---------------");
            txtWriter.printf("%-18s%-18s%-25s%-4s%-6s%-15s%-15s%-15s%-8s%-8s%-8s%-8s%-8s\n",
                    "Competitor Number", "Competitor Name", "Email", "Age", "Gender", "Country",
                    "Category", "Level", "Score 1", "Score 2", "Score 3", "Score 4", "Score 5");

            int maxOverallScore = Integer.MIN_VALUE;
            String highestScorerDetails = null;

            // Read the first line separately (headings) and print it
            if ((line = reader.readLine()) != null) {
                // Extract and format headings
                String[] headings = line.split(",");
                txtWriter.printf("%-18s%-18s%-25s%-4s%-6s%-15s%-15s%-15s%-8s%-8s%-8s%-8s%-8s\n",
                        headings[0], headings[1], headings[2], headings[3], headings[4], headings[5],
                        headings[6], headings[7], headings[8], headings[9], headings[10], headings[11], headings[12]);

                System.out.println(line);  // Display headings on the console
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 13 && !parts[0].isEmpty()) {
                    // Extract relevant details
                    int competitorNumber = Integer.parseInt(parts[0]);
                    String competitorName = parts[1];

                    // Extract scores
                    List<Integer> scores = Arrays.stream(parts, 8, parts.length)
                            .map(Integer::parseInt)
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                    // Calculate overall score as the sum of the individual scores
                    int overallScore = scores.stream().mapToInt(Integer::intValue).sum();

                    // Format and print competitor details
                    txtWriter.printf("%-18d%-18s%-25s%-4s%-6s%-15s%-15s%-15s%-8d%-8d%-8d%-8d%-8d\n",
                            competitorNumber, competitorName, parts[2], parts[3], parts[4], parts[5],
                            parts[6], parts[7], scores.get(0), scores.get(1), scores.get(2), scores.get(3), scores.get(4));
                    System.out.printf("%-18d%-18s%-25s%-4s%-6s%-15s%-15s%-15s%-8d%-8d%-8d%-8d%-8d\n",
                            competitorNumber, competitorName, parts[2], parts[3], parts[4], parts[5],
                            parts[6], parts[7], scores.get(0), scores.get(1), scores.get(2), scores.get(3), scores.get(4));

                    // Update highest scorer details if needed
                    if (overallScore > maxOverallScore) {
                        maxOverallScore = overallScore;
                        highestScorerDetails = String.format("Competitor Number: %d\nCompetitor Name: %s\nMaximum Score: %d\nMinimum Score: %d\nScores: %s\n",
                                competitorNumber, competitorName, scores.stream().max(Integer::compare).orElse(0),
                                scores.stream().min(Integer::compare).orElse(0), scores);
                    }
                }
            }

            if (highestScorerDetails != null) {
                // Print highest scorer details
                txtWriter.println("---------------Highest Scorer Details:---------------");
                txtWriter.println(highestScorerDetails);
                System.out.println("---------------Highest Scorer Details:---------------");
                System.out.println(highestScorerDetails);

                printCompetitorsWithMaxIndividualScoreToFile(csvFilePath, txtWriter);
                printCompetitorsWithMinIndividualScoreToFile(csvFilePath, txtWriter);
                displayIndividualScoreFrequencyReportToFile(csvFilePath, txtWriter);
                printTotalCompetitorScoresToFile(csvFilePath, txtWriter);
                printAverageCompetitorScoresToFile(csvFilePath, txtWriter);

            } else {
                txtWriter.println("\nNo competitors found in the file.");
                System.out.println("\nNo competitors found in the file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  
    
    
//  =============================================== MAXIMUM COMPETITOR SCORES ===============================================

    private void printCompetitorsWithMaxIndividualScoreToFile(String filePath, PrintWriter txtWriter) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int numberOfGames = 5;
            int[] maxIndividualScores = new int[numberOfGames];

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && !parts[0].isEmpty()) {
                    for (int i = 8; i < parts.length; i++) {
                        int score = Integer.parseInt(parts[i]);
                        int gameIndex = i - 8;
                        maxIndividualScores[gameIndex] = Math.max(maxIndividualScores[gameIndex], score);
                    }
                }
            }

            System.out.println("\n=============== MAXIMUM INDIVIDUAL SCORES ===============");
            addDataToTextFile("\n=============== MAXIMUM INDIVIDUAL SCORES ===============");

            for (int i = 0; i < numberOfGames; i++) {
                String game = "Game " + (i + 1);
                System.out.println(game + ": " + maxIndividualScores[i]);
                addDataToTextFile(game + ": " + maxIndividualScores[i]);
            }

            System.out.println("--------------- Maximum individual scores printed to the file. ---------------");
            addDataToTextFile("--------------- Maximum individual scores printed to the file. ---------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    
//  =============================================== MINIMUM COMPETITOR SCORES ===============================================

    
    public void printCompetitorsWithMinIndividualScoreToFile(String csvFilePath, PrintWriter txtWriter) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            int numberOfGames = 5; // Assuming there are 5 games, adjust accordingly
            int[] minIndividualScores = new int[numberOfGames];
            Arrays.fill(minIndividualScores, Integer.MAX_VALUE); // Initialize with the maximum possible value

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && !parts[0].isEmpty()) {
                    for (int i = 8; i < parts.length; i++) {
                        int score = Integer.parseInt(parts[i]);
                        // Assuming the scores are for Game 1, Game 2, ..., Game n
                        // Adjust the index accordingly
                        int gameIndex = i - 8;

                        // Update the min score for the game
                        minIndividualScores[gameIndex] = Math.min(minIndividualScores[gameIndex], score);
                    }
                }
            }

            // Display and save to text file
            System.out.println("\n=============== MINIMUM INDIVIDUAL SCORES ===============");
            txtWriter.println("\n=============== MINIMUM INDIVIDUAL SCORES ===============");

            for (int i = 0; i < numberOfGames; i++) {
                String game = "Game " + (i + 1); // Assuming the scores are for Game 1, Game 2, ..., Game n
                int minScore = (minIndividualScores[i] == Integer.MAX_VALUE) ? 0 : minIndividualScores[i];
                System.out.println(game + ": " + minScore);
                txtWriter.println(game + ": " + minScore);
            }

            System.out.println("Minimum individual scores displayed and saved to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    
//  =============================================== FREQUENCY REPORT OF SCORES ===============================================

    public void displayIndividualScoreFrequencyReportToFile(String csvFilePath, PrintWriter txtWriter) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            Map<Integer, Integer> scoreFrequencyMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && !parts[0].isEmpty()) {
                    for (int i = 8; i < parts.length; i++) {
                        int score = Integer.parseInt(parts[i]);

                        // Update the frequency map
                        scoreFrequencyMap.put(score, scoreFrequencyMap.getOrDefault(score, 0) + 1);
                    }
                }
            }

            // Display and save to text file
            System.out.println("\n=============== INDIVIDUAL SCORE FREQUENCY REPORT ===============");
            txtWriter.println("\n=============== INDIVIDUAL SCORE FREQUENCY REPORT ===============");

            for (Map.Entry<Integer, Integer> entry : scoreFrequencyMap.entrySet()) {
                int score = entry.getKey();
                int frequency = entry.getValue();

                // Display stars based on frequency
                String stars = "*".repeat(frequency);

                String reportLine = score + " - " + stars + " (" + frequency + ")";
                System.out.println(reportLine);
                txtWriter.println(reportLine);
            }

            System.out.println("Individual score frequency report displayed and saved to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  
    
    
//  =============================================== TOTAL COMPETITOR SCORES ===============================================

    private void printTotalCompetitorScoresToFile(String filePath, PrintWriter txtWriter) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter textWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {

            String line;
            List<String> totalScores = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && !parts[0].isEmpty()) {
                    String competitorName = parts[1];
                    int totalScore = calculateTotalScores(line);

                    // Add a condition to skip lines with a total score of 0
                    if (totalScore > 0) {
                        totalScores.add(competitorName + ": " + totalScore);
                    }
                }
            }

            System.out.println("\n=============== TOTAL COMPETITOR SCORES ===============");
            addDataToTextFile("=============== TOTAL COMPETITOR SCORES ===============");
            for (String score : totalScores) {
                System.out.println(score);
                textWriter.println(score);
            }

            System.out.println("Total scores written to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//  =============================================== AVERAGE SCORES ===============================================

    public void printAverageCompetitorScoresToFile(String csvFilePath, PrintWriter txtWriter) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            List<String> averageScores = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && !parts[0].isEmpty()) {
                    String competitorName = parts[1];

                    int numberOfGames = 5; // Assuming there are 5 games, adjust accordingly

                    // Extract scores
                    int[] scores = Arrays.stream(parts, 8, Math.min(parts.length, 8 + numberOfGames))
                            .mapToInt(Integer::parseInt)
                            .toArray();


                    // Check if all scores are zero
                    boolean allZeroScores = Arrays.stream(scores).allMatch(score -> score == 0);

                    // Add a condition to skip lines with all zero scores
                    if (!allZeroScores) {
                        double averageScore = Arrays.stream(scores).average().orElse(0);
                        averageScores.add(competitorName + ": " + averageScore);
                    }
                }
            }

            System.out.println("\n=============== AVERAGE COMPETITOR SCORES ===============");
            txtWriter.println("=============== AVERAGE COMPETITOR SCORES ===============");
            for (String score : averageScores) {
                System.out.println(score);
                txtWriter.println(score);
            }

            System.out.println("Average scores written to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    
    
//  =============================================== CALCULATES TOTAL SCORES ===============================================
    
    private int calculateTotalScores(String line) {
        String[] parts = line.split(",");
        int totalScore = 0;

        for (int i = 8; i < parts.length; i++) {
            totalScore += Integer.parseInt(parts[i]);
        }

        return totalScore;
    }
    
    
//  =============================================== CALCULATES AVERAGE SCORES ===============================================

    public double calculateAverageScores(String line) {
        String[] parts = line.split(",");
        int numberOfScores = parts.length - 8;
        int totalScore = 0;

        for (int i = 8; i < parts.length; i++) {
            totalScore += Integer.parseInt(parts[i]);
        }

        return (double) totalScore / numberOfScores;
    }

    
 

//  ---------------------------------------- UTILITY METHODS ----------------------------------------

//  =============================================== AGE ===============================================

    private int calculateAge(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return 0;
        }

        LocalDate birthDate = LocalDate.parse(dateOfBirth);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

  
    
//  =============================================== ADDING DATA TO TEXT FILE ===============================================

    private void addDataToTextFile(String content) {
        try (PrintWriter txtWriter = new PrintWriter(new FileWriter(textFilePath, true))) {
            txtWriter.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}

