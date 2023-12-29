package competition;

import java.util.ArrayList;
import java.util.List;

public class CompetitorList {

    // ArrayList to store competitors
    private List<Competitor> competitors;

    // Default constructor
    public CompetitorList() {
        this.competitors = new ArrayList<>();
        // Initialize the list with 10 to 15 competitors
        initializeCompetitors();
    }

    // Method to add a competitor to the list
    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
    }

    // Method to display the full details of all competitors in the list
    public void displayCompetitorsDetails() {
        for (Competitor competitor : competitors) {
            System.out.println(competitor.getFullDetails());
        }
    }

 // Method to initialize the list with 10 to 15 competitors (modify as needed)
    private void initializeCompetitors() {
        int numberOfCompetitors = 10 + (int) (Math.random() * 6); // Generates a random number between 10 and 15

        for (int i = 1; i <= numberOfCompetitors; i++) {
            Competitor competitor = new Competitor(i, "Competitor " + i, "email@example.com", "2000-01-01", "Category A", "Novice", "Country");
            competitors.add(competitor);
        }
    }

}
