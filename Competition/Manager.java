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
                System.out.println();
                displayMenu();
                System.out.println();
                System.out.print("Enter your choice: ");

                // Read user input for choice
                choice = readUserChoice(scanner);

                // Process user choice using a switch statement
                switch (choice) {
                    case 1:
                        System.out.println("Debug: Calling registerCompetitor()");
                        registerCompetitor();
                        System.out.println("Competitor registered successfully!");
                        break;
                    case 2:
                        System.out.println("Debug: Displaying Competitors Details");
                        System.out.println("Competitors Details in the CSV File:");
                        competitorList.displayCompetitorsDetails();
                        break;
                    case 3:
                        System.out.println("Debug: Printing Final Report");
                        System.out.println("Competitors Details in the Text File:");
                        printFinalReport();
                        break;
//                    case 4:
//                        System.out.println("Debug: Showing Short Details");
//                        competitorList.showShortDetails();
//                        break;
//                    case 5:
//                        System.out.println("Debug: Showing Overall Highest Score");
//                        competitorList.showOverallHighestScore();
//                        break;
//                    case 6:
//                        System.out.println("Debug: Showing Total Scores");
//                        competitorList.showTotalScores();
//                        break;
//                    case 7:
//                        System.out.println("Debug: Showing Average Scores");
//                        competitorList.showAverageScores();
//                        break;
                    case 4:
                        System.out.println("Debug: Exiting...");
                        break;
                    default:
                        System.out.println("Debug: Invalid choice!");
                }

            } while (choice != 4);
        }
    }

    private int readUserChoice(Scanner scanner) {
        // Read user input
        int choice;
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
        } else {
            System.out.println("Debug: Invalid input. Please enter a number.");
            choice = 0; // Reset choice to loop again
        }
        scanner.nextLine(); // Consume newline left by nextInt()
        return choice;
    }

    private void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Register Competitor");
        System.out.println("2. View Competitors Details");
        System.out.println("3. Print Final Report");
//        System.out.println("4. Show short details");
//        System.out.println("5. Show overall highest score");
//        System.out.println("6. Show total scores");
//        System.out.println("7. Show average scores");
        System.out.println("4. Exit");
    }

    private void registerCompetitor() {
        competitorList.registerCompetitor();
    }

    private void printFinalReport() {
        System.out.println("============ Final Report ============");
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
