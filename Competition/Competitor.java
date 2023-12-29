package competition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Competitor {

    // Properties of the Competitor class
    private int competitorNumber;
    private String competitorName;
    private String email;
    private String dateOfBirth;
    private String category;
    private String level;
    private String country; // Added country property
    private List<Integer> scores;

    // Default constructor
    public Competitor() {
        // Initializes the scores list
        this.scores = generateRandomScores();
    }

    // Competitor constructor for the details
    public Competitor(int competitorNumber, String competitorName, String email, String dateOfBirth,
                      String category, String level, String country) {
        this.competitorNumber = competitorNumber;
        this.competitorName = competitorName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.category = category;
        this.level = level;
        this.country = country; // Initialize country
        this.scores = generateRandomScores();
    }

    // Getter and setter methods for Competitor properties

    // Getter for the CompetitionNumber
    public int getCompetitorNumber() {
        return competitorNumber;
    }

    // Setter for the CompetitionNumber
    public void setCompetitorNumber(int competitorNumber) {
        this.competitorNumber = competitorNumber;
    }

    // Getter for the CompetitionName
    public String getCompetitorName() {
        return competitorName;
    }

    // Setter for the CompetitionName
    public void setCompetitorName(String competitorName) {
        this.competitorName = competitorName;
    }

    // Getter for the Email
    public String getEmail() {
        return email;
    }

    // Setter for the Email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for the Date of Birth
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    // Setter for the Date of Birth
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getter for the Category
    public String getCategory() {
        return category;
    }

    // Setter for the Category
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter for the Level
    public String getLevel() {
        return level;
    }

    // Setter for the Level
    public void setLevel(String level) {
        this.level = level;
    }

    // Getter for the Country
    public String getCountry() {
        return country;
    }

    // Setter for the Country
    public void setCountry(String country) {
        this.country = country;
    }

    // Getter for the Scores
    public List<Integer> getScores() {
        return scores;
    }

    // Instance variable for the array of Integer scores
    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    // Method to add a score to the scores list
    public void addScore(int score) {
        scores.add(score);
    }

    // Method to calculate the overall score
    public String getOverallScore() {
        double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double weightedAverage = calculateWeightedAverage();
        double averageWithoutMinMax = calculateAverageWithoutMinMax();

        return String.format("Overall Score:%n  Average: %d%n  Weighted Average: %d%n  Average without Min and Max: %d",
                Math.round(average), Math.round(weightedAverage), Math.round(averageWithoutMinMax));
    }

    // Method to create a string representation of the Competitor object
    @Override
    public String toString() {
        return String.format("Competitor number %d, name %s, category %s, country %s.%n%s is a %s aged %s and has an overall of %s.",
                competitorNumber, competitorName, category, country, competitorName, level, calculateAge(), getOverallScore());
    }

    // Method to get full details of the competitor
    public String getFullDetails() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Competitor number %d, name %s, country %s.%n%s is a %s aged %s and has an %s.",
                competitorNumber, competitorName, country, competitorName, level, calculateAge(), getOverallScore()));

        return result.toString();
    }

    // Method to calculate the age based on the date of birth
    private String calculateAge() {
        // Logic to calculate age goes here
        // For simplicity, returning a constant value for demonstration
        return "21";
    }

    // Method to get short details of the competitor
    public String getShortDetails() {
        return String.format("CN %d (%s) has overall score %d.", competitorNumber, getInitials(), Math.round(scores.stream().mapToInt(Integer::intValue).average().orElse(0.0)));
    }

    // Method to get initials from the competitor name
    private String getInitials() {
        String[] names = competitorName.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String name : names) {
            initials.append(name.charAt(0));
        }
        return initials.toString().toUpperCase();
    }

    // Method to generate a random number of scores between 4 and 6, and each score value between 0 and 5
    private List<Integer> generateRandomScores() {
        Random random = new Random();
        int numberOfScores = random.nextInt(3) + 4; // Random number between 4 and 6
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < numberOfScores; i++) {
            scores.add(random.nextInt(6)); // Random score between 0 and 5
        }
        return scores;
    }

    // Method to get the array of integer scores
    public int[] getScoreArray() {
        return scores.stream().mapToInt(Integer::intValue).toArray();
    }

    // Method to calculate the weighted average of scores based on levels
    private double calculateWeightedAverage() {
        double sum = 0;
        double weightSum = 0;

        for (int i = 0; i < scores.size(); i++) {
            int score = scores.get(i);
            int weight = getWeightForScoreAndLevel(score, level);
            sum += score * weight;
            weightSum += weight;
        }

        return weightSum > 0 ? sum / weightSum : 0;
    }

    // Method to calculate the average of scores disregarding the highest and lowest score
    private double calculateAverageWithoutMinMax() {
        if (scores.size() <= 2) {
            return scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        }

        int minScore = scores.stream().min(Integer::compare).orElse(0);
        int maxScore = scores.stream().max(Integer::compare).orElse(0);

        int sum = scores.stream().mapToInt(Integer::intValue).sum();
        return (sum - minScore - maxScore) / (scores.size() - 2.0);
    }

    // Method to get the weight for a score based on level
    private int getWeightForScoreAndLevel(int score, String level) {
        // You can customize the weights based on your requirements
        int baseWeight = 1;

        switch (level.toLowerCase()) {
            case "novice":
                return baseWeight + 1; // Novice has higher weight
            case "intermediate":
                return baseWeight;
            case "advanced":
                return baseWeight - 1; // Advanced has lower weight
            default:
                return baseWeight;
        }
    }

    // Example usage in the main method
    public static void main(String[] args) {
        Competitor competitor = new Competitor(1, "John Doe", "john@example.com", "2000-01-01", "Category A", "Intermediate", "USA");
        // Print the full details of the competitor
        System.out.println(competitor.getFullDetails());

        // Print short details of the competitor
        System.out.println(competitor.getShortDetails());

        // Print the array of integer scores
        int[] scoreArray = competitor.getScoreArray();
        System.out.print("Scores: ");
        for (int score : scoreArray) {
            System.out.print(score + " ");
        }
    }
}
