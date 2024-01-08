package competition.model;

import competition.Controller.Controller;
import competition.model.Competitor.Category;
import competition.model.Competitor.Level;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompetitorList {

    private List<Competitor> competitors;

    public CompetitorList() {
        competitors = new ArrayList<>();
    }

    public void addCompetitor(int competitorNumber, String CompetitorName, Competitor.Category category, Competitor.Level level, 
    		String gender, int DateofBirth, String country, String email, int[] scores) {

//    	String dateOfBirth = Integer.toString(DateofBirth);  // Convert int to String
//        int age = calculateAge(dateOfBirth);
			
			Competitor competitor = new Competitor(competitorNumber, CompetitorName, category, level, email, DateofBirth, gender, country, scores);
			competitors.add(competitor);
			}


// // Calculate age based on Date of Birth
//    private static int calculateAge(String dateOfBirth) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date birthDate = dateFormat.parse(dateOfBirth);
//            Calendar dob = Calendar.getInstance();
//            dob.setTime(birthDate);
//
//            Calendar currentDate = Calendar.getInstance();
//
//            int age = currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//
//            // Adjust age if the birthday hasn't occurred yet this year
//            if (currentDate.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
//                age--;
//            }
//
//            return age;
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return 0; // Return default age or handle the error as needed
//        }
//      }
    
    public Competitor getCompetitorByNumber(int competitorNumber) {
        for (Competitor competitor : competitors) {
            if (competitor.getCompetitorNumber() == competitorNumber) {
                return competitor;
            }
        }
        return null;
    }

    public void removeCompetitor(int competitorNumber) {
        Competitor competitorToRemove = null;
        for (Competitor competitor : competitors) {
            if (competitor.getCompetitorNumber() == competitorNumber) {
                competitorToRemove = competitor;
                break;
            }
        }
        if (competitorToRemove != null) {
            competitors.remove(competitorToRemove);
        }
    }

    public List<Competitor> getAllCompetitors() {
        return competitors;
    }

    public Competitor getWinner() {
        return competitors.stream()
                .max((c1, c2) -> Double.compare(c1.getCompetitorOverallScore(), c2.getCompetitorOverallScore()))
                .orElse(null);
    }

 // Modify the order of fields in the writeCompetitorsToCSV method
    public void writeCompetitorsToCSV(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Competitor competitor : competitors) {
                String line = String.format("%d,%s,%s,%s,%s,%d,%s,%s,%s",
                        competitor.getCompetitorNumber(),
                        competitor.getCompetitorName(),
                        competitor.getCategory(),
                        competitor.getLevel(),
                        competitor.getEmail(),
                        competitor.getDateofBirth(),
                        competitor.getGender(),
                        competitor.getCountry(),
                        String.join(",", Arrays.stream(competitor.getScores()).mapToObj(String::valueOf).toArray(String[]::new)));
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    // Method to read competitors from CSV file
//    public void readCompetitorsFromCSV(String filePath) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//                if (data.length < 9) {
//                    // Skip incomplete or invalid data
//                    System.err.println("Skipping invalid data: " + Arrays.toString(data));
//                    continue;
//                }
//                try {
//                    int competitorNumber = Integer.parseInt(data[0].trim());
//                    String name = data[1].trim();
//                    String categoryStr = data[2].trim();
//                    String levelStr = data[3].trim();
//                    String email = data[4].trim();  // Update this line
//
//                    // Check if the input for age is not empty before parsing
//                    int age;
//                    if (!data[5].trim().isEmpty()) {
//                        age = Integer.parseInt(data[5].trim());
//                    } else {
//                        // Handle the case where age is empty (provide a default value or skip the competitor)
//                        continue; // Skip this competitor
//                    }
//
//                    String dateOfBirth = data[6].trim();
//                    String gender = data[7].trim();
//                    String country = data[8].trim();
//
//                    // Convert the category string and level string to the enum constants
//                    Competitor.Category category = Competitor.Category.valueOf(categoryStr);
//                    Competitor.Level level = Competitor.Level.valueOf(levelStr);
//
//                    // Extract scores from the remaining elements
//                    int[] scores = Arrays.stream(data, 9, data.length)
//                            .mapToInt(Integer::parseInt)
//                            .toArray();
//
//                    Competitor competitor = new Competitor(competitorNumber, name, category, level, email, age, gender, country, scores);
//                    competitors.add(competitor);
//                } catch (NumberFormatException e) {
//                    // Handle the case where a number cannot be parsed or enum constant is invalid
//                    System.err.println("Error parsing data: " + Arrays.toString(data));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Error reading data from CSV file");
//        }
//    }

    
    
    
 // Method to read competitors from CSV file
    public void readCompetitorsFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 9) {
                    // Skip incomplete or invalid data
                    System.err.println("Skipping invalid data: " + Arrays.toString(data));
                    continue;
                }
                try {
                    int competitorNumber = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String categoryStr = data[2].trim();
                    String levelStr = data[3].trim();

                    // Check if the input for age is not empty before parsing
                    int age;
                    if (!data[5].trim().isEmpty()) {
                        age = Integer.parseInt(data[5].trim());
                    } else {
                        // Handle the case where age is empty (provide a default value or skip the competitor)
                        continue; // Skip this competitor
                    }

                    String gender = data[6].trim();
                    String country = data[7].trim();
                    String email = data[4].trim();

                    // Convert the category string and level string to the enum constants
                    Competitor.Category category = Competitor.Category.valueOf(categoryStr);
                    Competitor.Level level = Competitor.Level.valueOf(levelStr);

                    // Extract scores from the remaining elements
                    int[] scores = Arrays.stream(data, 8, data.length) // Include all elements from index 8 to the end
                            .mapToInt(Integer::parseInt)
                            .toArray();

                    Competitor competitor = new Competitor(competitorNumber, name, category, level, email, age, gender, country, scores);
                    competitors.add(competitor);
                } catch (NumberFormatException e) {
                    // Handle the case where a number cannot be parsed or enum constant is invalid
                    System.err.println("Error parsing data: " + Arrays.toString(data));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading data from CSV file");
        }
    }

    
    
    // Method to produce the final report
    public void produceFinalReport(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Table of competitors with full details
            writer.write("Competitors:\n");
            writer.write("Competitor Number | Name | Category | Level | Age | Email | Gender | Country |Scores\n");
            for (Competitor competitor : competitors) {
                writer.write(String.format("%d | %s | %s | %s  | %d | %s | %s | %s | %s%n",
                        competitor.getCompetitorNumber(),
                        competitor.getCompetitorName(),
                        competitor.getCategory(),
                        competitor.getLevel(),
                        competitor.getDateofBirth(),
                        competitor.getGender(),
                        competitor.getCountry(),
                        competitor.getEmail(),
                        Arrays.toString(competitor.getScores())));
            }
            writer.write("\n");

            // Details of the competitor with the highest overall score
            Competitor winner = getWinner();
            if (winner != null) {
                writer.write("Competitor with the highest overall score:\n");
                writer.write(String.format("Competitor Number: %d, Name: %s, Overall Score: %.2f%n",
                        winner.getCompetitorNumber(), winner.getCompetitorName(), winner.getCompetitorOverallScore()));
                writer.write("\n");
            }

            // Other summary statistics
            writer.write("Summary Statistics:\n");
            writer.write("Total Competitors: " + competitors.size() + "\n");
            writer.write("Average Overall Score: " + calculateAverageOverallScore() + "\n");
            writer.write("Maximum Overall Score: " + calculateMaxOverallScore() + "\n");
            writer.write("Minimum Overall Score: " + calculateMinOverallScore() + "\n");

            // Frequency report
            writer.write("Frequency Report:\n");
            Map<Integer, Integer> scoreFrequency = calculateScoreFrequency();

            for (int i = 0; i <= 5; i++) { // Assuming scores range from 0 to 5
                int frequency = scoreFrequency.getOrDefault(i, 0);

                // Generate a string of asterisks based on the frequency
                String asterisks = "*".repeat(frequency);

                writer.write(i + " - (" + frequency + ") " + asterisks + "\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to check the validity of a competitor number
    public boolean isValidCompetitorNumber(int competitorNumber) {
        return getCompetitorByNumber(competitorNumber) != null;
    }

    // Additional methods for calculating statistics
    private double calculateAverageOverallScore() {
        return competitors.stream().mapToDouble(Competitor::getCompetitorOverallScore).average().orElse(0);
    }

    private double calculateMaxOverallScore() {
        return competitors.stream().mapToDouble(Competitor::getCompetitorOverallScore).max().orElse(0);
    }

    private double calculateMinOverallScore() {
        return competitors.stream().mapToDouble(Competitor::getCompetitorOverallScore).min().orElse(0);
    }

    private Map<Integer, Integer> calculateScoreFrequency() {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (Competitor competitor : competitors) {
            for (int score : competitor.getScores()) {
                frequencyMap.put(score, frequencyMap.getOrDefault(score, 0) + 1);
            }
        }
        return frequencyMap;
    }
}
   

