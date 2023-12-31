package competition;

import java.util.Scanner;

public class Manager {

    public static void main(String[] args) {
        // Predefined CSV file path
        String filePath = "C:\\Users\\PC\\Desktop\\RunCompetitor.csv";

        // Creating a CompetitorList object with file handling
        CompetitorList competitorList = new CompetitorList(filePath);

        Scanner scanner = new Scanner(System.in);

        // Displaying menu options
        int choice = 0;

        // Use a single Scanner for System.in
            do {
                System.out.println("Menu:");
                System.out.println("1. Register Competitor");
                System.out.println("2. View Competitors Details");
                System.out.println("3. Exit");

                // Handling user input
                System.out.print("Enter your choice: ");

                    choice = scanner.nextInt(); // Get the user's choice
                    scanner.nextLine(); // Consume newline left by nextInt()

                    switch (choice) {
                        case 1:
                            competitorList.registerCompetitor();
                            System.out.println("Competitor registered successfully!");
                            break;
                        case 2:
                            System.out.println("Competitors Details:");
                            competitorList.displayCompetitorsDetails();
                            break;
                        case 3:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }


            } while (choice != 3);
            scanner.close();

        }
}
