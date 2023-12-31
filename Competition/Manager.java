package competition;

import java.util.Scanner;

public class Manager {

    private final CompetitorList competitorList;

    public Manager(String filePath) {
        // Creating a CompetitorList object with file handling
        this.competitorList = new CompetitorList(filePath);
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            int choice;

            do {
                displayMenu();
                System.out.print("Enter your choice: ");

                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left by nextInt()

                    switch (choice) {
                        case 1:
                            registerCompetitor();
                            System.out.println("Competitor registered successfully!");
                            break;
                        case 2:
                            System.out.println("Competitors Details:");
                            competitorList.displayCompetitorsDetails();
                            break;
                        case 3:
                            printFinalReport();
                            break;
                        case 4:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Consume the invalid input
                    choice = 0; // Reset choice to loop again
                }

            } while (choice != 4);
        }
    }

    private void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Register Competitor");
        System.out.println("2. View Competitors Details");
        System.out.println("3. Print Final Report");
        System.out.println("4. Exit");
    }

    private void registerCompetitor() {
        competitorList.registerCompetitor();
    }

    private void printFinalReport() {
        System.out.println("===== Final Report =====");
        competitorList.printFullDetailsToFile("C:\\Users\\PC\\Desktop\\CompetitorReport.txt");
        System.out.println("Final report printed successfully!");
    }

    public static void main(String[] args) {
        // Predefined CSV file path
        String filePath = "C:\\Users\\PC\\Desktop\\RunCompetitor.csv";

        Manager manager = new Manager(filePath);
        manager.run();
    }
}
