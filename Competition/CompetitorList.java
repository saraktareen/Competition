package competition;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
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

        List<Integer> scores = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            System.out.println("Enter score " + i + ":");
            int score = scanner.nextInt();
            scores.add(score);
        }

        Competitor competitor = new Competitor(competitorNumber, competitorName, email, dateOfBirth, category, level, country, gender, scores);
        addCompetitor(competitor);

        System.out.println("---------------Competitor registered successfully!---------------");

        enterCompetitorDetailsToFile(competitor);

        scanner.close();
    }

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

    private void addDataToTextFile(String content) {
        try (PrintWriter txtWriter = new PrintWriter(new FileWriter("C:\\Users\\PC\\Desktop\\CompetitorReport.txt", true))) {
            txtWriter.println(content);
            System.out.println("Data written to the file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateAndPrintHighestOverallScore(String highestScorerDetails) {
        String[] parts = highestScorerDetails.split(",");
        int competitorNumber = Integer.parseInt(parts[0]);
        String competitorName = parts[1];
        int overallScore = calculateOverallScore(highestScorerDetails);

        System.out.println("=============== HIGHEST OVERALL SCORE ===============");
        System.out.println("\nCompetitor with the highest overall score:");
        System.out.println("Competitor Number: " + competitorNumber);
        System.out.println("Competitor Name: " + competitorName);
        System.out.println("Scores: " + parts[8] + ", " + parts[9] + ", " + parts[10] + ", " + parts[11] + ", " + parts[12]);
        System.out.println("Overall Score: " + overallScore);

        // Add data to text file
        addDataToTextFile("=============== HIGHEST OVERALL SCORE ===============\n" +
                "Competitor with the highest overall score:\n" +
                "Competitor Number: " + competitorNumber + "\n" +
                "Competitor Name: " + competitorName + "\n" +
                "Scores: " + parts[8] + ", " + parts[9] + ", " + parts[10] + ", " + parts[11] + ", " + parts[12] + "\n" +
                "Overall Score: " + overallScore);
    }

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
                if (parts.length > 1 && !parts[0].isEmpty()) {
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
            } else {
                System.out.println("\nNo competitors found in the file.");
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private int calculateTotalScores(String line) {
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
}
