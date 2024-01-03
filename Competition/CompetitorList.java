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

    public CompetitorList() {
        this.competitors = new ArrayList<>();
        initializeCompetitors();
    }

    public CompetitorList(String csvFilePath) {
        this.competitors = new ArrayList<>();
        this.csvFilePath = csvFilePath;
    }

    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
    }

    
//  =============================================== DISPLAY COMPETITOR DETAILS ===============================================

    public void displayCompetitorsDetails() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;

            System.out.println("===============FILE CONTENTS===============");

            System.out.println("Competitor Number|Competitor Name|Age|Gender|Country|Score 1|Score 2|Score 3|Score 4|Score 5");

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
//  =============================================== REGISTER COMPETITOR ===============================================

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
                    System.out.println("Enter score " + i + ":");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a valid score:");
                        scanner.next(); // Consume invalid input
                    }
                    int score = scanner.nextInt();
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
    
    private boolean isValidCompetitorName(String name) {
        return name.matches("[A-Za-z ]+");
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



//  =============================================== ENTER DETAILS TO TEXT FILE ===============================================

    
    public void enterCompetitorDetailsToFile(Competitor competitor) {
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFilePath, true));
             PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {

            List<String> scoreStrings = new ArrayList<>();
            for (Integer score : competitor.getScores()) {
                scoreStrings.add(String.valueOf(score));
            }

            String csvData = String.format("%d,%s,%s,%s,%s,%s",
                    competitor.getCompetitorNumber(), competitor.getCompetitorName(),
                    calculateAge(competitor.getDateOfBirth()), competitor.getGender(), competitor.getCountry(),
                    String.join(",", scoreStrings));

            csvWriter.write(csvData);
            csvWriter.newLine();
            System.out.println("---------------CSV: Competitor details written to the file successfully.---------------");

            String txtData = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s",
                    competitor.getCompetitorNumber(), competitor.getCompetitorName(),
                    competitor.getEmail(), calculateAge(competitor.getDateOfBirth()),
                    competitor.getGender(), competitor.getCountry(), competitor.getCategory(), competitor.getLevel(),
                    String.join(",", scoreStrings));

            txtWriter.println(txtData);
            System.out.println("---------------Text: Full details written to the file successfully.---------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
//  =============================================== ADDING DATA TO TEXT FILE ===============================================

    private void addDataToTextFile(String content) {
        try (PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {
            txtWriter.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
//    =============================================== HIGHEST OVERALL SCORE ===============================================

    public void calculateAndPrintHighestOverallScore(String highestScorerDetails) {
        String[] parts = highestScorerDetails.split(",");
        int competitorNumber = Integer.parseInt(parts[0]);
        String competitorName = parts[1];
        int overallScore = calculateOverallScore(highestScorerDetails);
        int[] scores = new int[5];  // Assuming there are 5 scores

        // Extract individual scores and update the scores array
        for (int i = 0; i < 5; i++) {
            scores[i] = Integer.parseInt(parts[8 + i]);
        }

        // Find the highest and lowest scores
        int highestScore = Arrays.stream(scores).max().orElse(0);
        int lowestScore = Arrays.stream(scores).min().orElse(0);

        System.out.println("=============== HIGHEST OVERALL SCORE ===============");
        System.out.println("\nCompetitor with the highest overall score:");
        System.out.println("Competitor Number: " + competitorNumber);
        System.out.println("Competitor Name: " + competitorName);
        System.out.println("Scores: " + Arrays.toString(scores));
        System.out.println("Overall Score: " + overallScore);
        System.out.println("Maximum Score: " + highestScore);
        System.out.println("Minimum Score: " + lowestScore);

        // Add data to text file
        addDataToTextFile("=============== HIGHEST OVERALL SCORE ===============\n" +
                "Competitor with the highest overall score:\n" +
                "Competitor Number: " + competitorNumber + "\n" +
                "Competitor Name: " + competitorName + "\n" +
                "Scores: " + Arrays.toString(scores) + "\n" +
                "Overall Score: " + overallScore + "\n" +
                "Maximum Score: " + highestScore + "\n" +
                "Minimum Score: " + lowestScore);
    }


    
    
//  =============================================== PRINT DETAILS TO FILE ===============================================

    public void printFullDetailsToFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            System.out.println("---------------Reading file contents:---------------");

            System.out.println("Competitor Number|Competitor Name|Email|Age|Gender|Country|Category|Level|Score 1|Score 2|Score 3|Score 4|Score 5");

            int maxOverallScore = Integer.MIN_VALUE;
            String highestScorerDetails = null;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                String[] parts = line.split(",");
                if (parts.length >= 13 && !parts[0].isEmpty()) { // Check if the array has enough elements
                    int competitorNumber = Integer.parseInt(parts[0]);
                    String competitorName = parts[1];

                    int overallScore = calculateOverallScore(line);
                    int totalScore = calculateTotalScores(line);

                    if (overallScore > maxOverallScore) {
                        maxOverallScore = overallScore;
                        highestScorerDetails = line;
                    }
                }
            }

            if (highestScorerDetails != null) {
                calculateAndPrintHighestOverallScore(highestScorerDetails);
                printTotalCompetitorScoresToFile(filePath);
                printAverageCompetitorScoresToFile(filePath);
                
                // Call the method to print competitors with the max total score
                printCompetitorsWithMaxIndividualScoreToFile(filePath);
                printCompetitorsWithMinIndividualScoreToFile(filePath);
                displayIndividualScoreFrequencyReportToFile(filePath);
            } else {
                System.out.println("\nNo competitors found in the file.");
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    


    
//  =============================================== MAXIMUM COMPETITOR SCORES ===============================================

    public void printCompetitorsWithMaxIndividualScoreToFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {

            String line;
            int numberOfGames = 5; // Assuming there are 5 games, adjust accordingly
            int[] maxIndividualScores = new int[numberOfGames];

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && !parts[0].isEmpty()) {
                    for (int i = 8; i < parts.length; i++) {
                        int score = Integer.parseInt(parts[i]);
                        // Assuming the scores are for Game 1, Game 2, ..., Game n
                        // Adjust the index accordingly
                        int gameIndex = i - 8;

                        // Update the max score for the game
                        maxIndividualScores[gameIndex] = Math.max(maxIndividualScores[gameIndex], score);
                    }
                }
            }

            // Display and save to text file
            System.out.println("\n=============== MAXIMUM INDIVIDUAL SCORES ===============");
            addDataToTextFile("\n=============== MAXIMUM INDIVIDUAL SCORES ===============");

            for (int i = 0; i < numberOfGames; i++) {
                String game = "Game " + (i + 1); // Assuming the scores are for Game 1, Game 2, ..., Game n
                System.out.println(game + ": " + maxIndividualScores[i]);
                addDataToTextFile(game + ": " + maxIndividualScores[i]);
            }

            System.out.println("Maximum individual scores displayed and saved to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
    
    
//  =============================================== MINIMUM COMPETITOR SCORES ===============================================

    
    public void printCompetitorsWithMinIndividualScoreToFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {

            String line;
            int numberOfGames = 5; // Assuming there are 5 games, adjust accordingly
            int[] minIndividualScores = new int[numberOfGames];
            Arrays.fill(minIndividualScores, Integer.MAX_VALUE); // Initialize with maximum possible value

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
            addDataToTextFile("\n=============== MINIMUM INDIVIDUAL SCORES ===============");

            for (int i = 0; i < numberOfGames; i++) {
                String game = "Game " + (i + 1); // Assuming the scores are for Game 1, Game 2, ..., Game n
                int minScore = (minIndividualScores[i] == Integer.MAX_VALUE) ? 0 : minIndividualScores[i];
                System.out.println(game + ": " + minScore);
                addDataToTextFile(game + ": " + minScore);
            }

            System.out.println("Minimum individual scores displayed and saved to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
    

    
    
//  =============================================== FREQUENCY REPORT OF SCORES ===============================================

    public void displayIndividualScoreFrequencyReportToFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {

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
            addDataToTextFile("\n=============== INDIVIDUAL SCORE FREQUENCY REPORT ===============");

            for (Map.Entry<Integer, Integer> entry : scoreFrequencyMap.entrySet()) {
                int score = entry.getKey();
                int frequency = entry.getValue();

                // Display stars based on frequency
                String stars = "*".repeat(frequency);

                String reportLine = score + " - " + stars + " (" + frequency + ")";
                System.out.println(reportLine);
                addDataToTextFile(reportLine);
            }

            System.out.println("Individual score frequency report displayed and saved to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    

    
//  =============================================== TOTAL COMPETITOR SCORES ===============================================

    public void printTotalCompetitorScoresToFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {

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
                addDataToTextFile(score);
            }

            System.out.println("Total scores written to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    
    private int calculateTotalScores(String line) {
        String[] parts = line.split(",");
        int totalScore = 0;

        for (int i = 8; i < parts.length; i++) {
            totalScore += Integer.parseInt(parts[i]);
        }

        return totalScore;
    }
    
//  =============================================== AVERAGE SCORES ===============================================

    public void printAverageCompetitorScoresToFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {

            String line;
            List<String> averageScores = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && !parts[0].isEmpty()) {
                    String competitorName = parts[1];
                    double averageScore = calculateAverageScores(line);

                    // Add a condition to skip lines with all zero scores
                    if (averageScore > 0) {
                        averageScores.add(competitorName + ": " + averageScore);
                    }
                }
            }

            System.out.println("\n=============== AVERAGE COMPETITOR SCORES ===============");
            addDataToTextFile("=============== AVERAGE COMPETITOR SCORES ===============");
            for (String score : averageScores) {
                System.out.println(score);
                addDataToTextFile(score);
            }

            System.out.println("Average scores written to the file successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public double calculateAverageScores(String line) {
        String[] parts = line.split(",");
        int numberOfScores = parts.length - 8;
        int totalScore = 0;

        for (int i = 8; i < parts.length; i++) {
            totalScore += Integer.parseInt(parts[i]);
        }

        return (double) totalScore / numberOfScores;
    }

    
    
//  =============================================== AGE ===============================================

    private int calculateAge(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return 0;
        }

        LocalDate birthDate = LocalDate.parse(dateOfBirth);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    private int calculateOverallScore(String line) {
        String[] parts = line.split(",");
        int totalScore = 0;

        for (int i = 8; i < parts.length; i++) {
            totalScore += Integer.parseInt(parts[i]);
        }

        return totalScore;
    }

    

    private void initializeCompetitors() {
        // Implement this method based on your requirements
    }

//=============================================== AGE ===============================================

	//Add this method to CompetitorList class
	public void showShortDetailsForAll() {
	 for (Competitor competitor : competitors) {
	     competitor.getShortDetails();
	 }
	}
}

