package competition;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    public static void main(String[] args) {
        // Creating a CompetitorList object
        CompetitorList competitorList = new CompetitorList();

        // Adding competitors to the list
        competitorList.addCompetitor(new Competitor(1, "Keith John Talbot", "keith@example.com", "2001-01-01", "Category A", "Novice", "UK"));
        // Add more competitors as needed...

        // Displaying the full details of each competitor in the list
        competitorList.displayCompetitorsDetails();
    }
}
