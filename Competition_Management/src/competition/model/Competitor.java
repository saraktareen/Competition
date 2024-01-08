package competition.model;

import competition.Controller.Controller;
import java.util.Arrays;
import java.util.Date;

//Competitor constructor class
public class Competitor {
    private int competitorNumber;
    private String CompetitorName;
    private Category category;
    private Level level;
    private int DateofBirth;
    private String email;
    private String gender;
    private String country;
    private int[] scores;

    public Competitor(int competitorNumber, String CompetitorName, Category category, Level level, String email, int DateofBirth, String gender, String country, int[] scores) {
        this.competitorNumber = competitorNumber;
        this.CompetitorName = CompetitorName;
        this.category = category;
        this.level = level;
        this.email = email;
        this.DateofBirth = DateofBirth;
        this.gender = gender;
        this.country = country;
        this.scores = scores;
    }

    
    //List of getters
    public int getCompetitorNumber() {
        return competitorNumber;
    }

    public String getCompetitorName() {
        return CompetitorName;
    }

    public Category getCategory() {
        return category;
    }

    public Level getLevel() {
        return level;
    }
    
    public int getDateofBirth() {
      return DateofBirth;
  }
    
    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public int[] getScores() {
        return scores;
    }

    
    //Calculates and returns the overall score of the competitors average score
    //If a competitor does not have any scores, a 0 will be returned
    public double getCompetitorOverallScore() {
        return Arrays.stream(scores).average().orElse(0);
    }
    
  //Provides the user with short details of the user
    //The competitor number along with the overall score to the nearest 2nd decimal place
    public String getCompetitorShortDetails() {
        return String.format("CN %d (%s) has overall score %.2f.%n",
                competitorNumber, getCompetitorInitials(), getCompetitorOverallScore());
    }

 // This provides the user with the full details of any competitor
    public String getCompetitorFullDetails() {
        return String.format("Competitor number %d, name %s, Country %s."
        					+ "\n%s is a %s aged %d and has an overall score of %.2f.\n",
                competitorNumber, CompetitorName, email, getFirstName(), level, DateofBirth, getCompetitorOverallScore());
    }


 // Gets the first name of the competitor
    public String getFirstName() {
        String[] nameParts = CompetitorName.split(" ");
        return nameParts.length >= 1 ? nameParts[0] : "";
    }

    
    //List of setters
    public void setCompetitorName(String newCompetitorName) {
        this.CompetitorName = newCompetitorName;
    }

    public void setCategory(Category newCategory) {
        this.category = newCategory;
    }

    public void setLevel(Level newLevel) {
        this.level = newLevel;
    }

    public void setDateofBirth(int newDateofBirth) {
      this.DateofBirth = newDateofBirth;
  }
    
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setGender(String newGender) {
        this.gender = newGender;
    }

    public void setCountry(String newCountry) {
        this.country = newCountry;
    }

    public void setScores(int[] newScores) {
        this.scores = newScores;
    }
    
    
    
    //The competitors name if written as the first and last name uses library routines to get the competitors initials
    //For competitors with only one name, it outputs the first letter of their name
    private String getCompetitorInitials() {
        String[] nameParts = CompetitorName.split(" ");
        return nameParts.length >= 2 ?
                Character.toString(nameParts[0].charAt(0)) + nameParts[1].charAt(0) :
                (nameParts.length == 1 ? Character.toString(nameParts[0].charAt(0)) : "");
    }

    
    //Enumeration methods to represent groups of constants
    public enum Level {
        Novice, Intermediate, Expert
    }

    public enum Category {
    	Football, Cricket, Hockey
    }
    
    
    
    public void setDateofBirth(String newDateofBirth) {
        try {
            // Attempt to parse the newDateofBirth String into an int
            int parsedDateOfBirth = Integer.parseInt(newDateofBirth);
            
            // Set the DateofBirth field
            this.DateofBirth = parsedDateOfBirth;
        } catch (NumberFormatException e) {
            // Handle the exception if the parsing fails
            System.out.println("Error parsing date of birth: " + e.getMessage());
        }
    }
}

