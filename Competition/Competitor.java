package competition;

import java.util.ArrayList;
import java.util.List;

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
        this.scores = new ArrayList<>();
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
        this.scores = new ArrayList<>();
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

    // Setter for the Scores
    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    // Method to add a score to the scores list
    public void addScore(int score) {
        scores.add(score);
    }

    // Method to calculate the overall score
    public double getOverallScore() {
        return scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    // Method to create a string representation of the Competitor object
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%-2d %-2s %-2s", competitorNumber, competitorName, level));

        // Append the overall score to the result
        result.append(String.format(" Overall: %.1f", getOverallScore()));

        return result.toString();
    }

    // Method to get full details of the competitor
    public String getFullDetails() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Competitor number %d, name %s.%n", competitorNumber, competitorName));
        result.append(String.format("%s is a %s from %s and received these scores: ", competitorName, level, country));

        for (int score : scores) {
            result.append(score).append(",");
        }

        result.setLength(result.length() - 1);

        result.append(String.format("%nThis gives him an overall score of %.0f.", getOverallScore()));

        return result.toString();
    }

    // Example usage in the main method
    public static void main(String[] args) {
        Competitor competitor = new Competitor(1, "John Doe", "john@example.com", "2000-01-01", "Category A", "Intermediate", "USA");
        competitor.addScore(85);
        competitor.addScore(92);
        competitor.addScore(78);

        // Print the Competitor details, including the overall score
        System.out.println(competitor.toString());

        // Print the full details of the competitor
        System.out.println(competitor.getFullDetails());
    }
}
