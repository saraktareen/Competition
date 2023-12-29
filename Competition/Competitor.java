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
        return String.format("Overall Score: %d", Math.round(scores.stream().mapToInt(Integer::intValue).average().orElse(0.0)));
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
