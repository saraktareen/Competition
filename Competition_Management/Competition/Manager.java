package competition;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Manager {

    private final CompetitorList competitorList;
    private final String textFilePath;

    public Manager(String filePath, String textFilePath) {
        // Creating a CompetitorList object with file handling
        this.competitorList = new CompetitorList(filePath, textFilePath);
        this.textFilePath = textFilePath;
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

                        // Clear the text file before printing the final report
                        clearTextFile(textFilePath);

                        // Print the final report
                        printFinalReport();
                        break;
                    case 4:
                        System.out.println("Debug: Showing Competitor Details as per the Number Entered");
                        competitorList.displayCompetitorDetailsByNumber();
                        break;
                    case 5:
                        System.out.println("Debug: Exiting...");
                        break;
                    default:
                        System.out.println("Debug: Invalid choice!");
                }

            } while (choice != 6);
        }
    }

    private void clearTextFile(String textFilePath) {
        try (PrintWriter txtWriter = new PrintWriter(new FileWriter(textFilePath, false))) {
            // Open the file in overwrite mode (false) to clear the contents
            txtWriter.print("");
            System.out.println("Text file cleared successfully.");
        } catch (IOException e) {
            e.printStackTrace();
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
        System.out.println("4. Show the competitor details as per the competitor number entered");
        System.out.println("5. Show the competitors short details");
        System.out.println("6. Exit");
    }

    private void registerCompetitor() {
        competitorList.registerCompetitor();
    }

    private void printFinalReport() {
        System.out.println("============ Final Report ============");
        competitorList.printFullDetailsToFile("C:\\Users\\PC\\Desktop\\RunCompetitor.csv", textFilePath);
        System.out.println("Final report printed successfully!");
    }

    public static void main(String[] args) {
        // Predefined CSV file path and text file path
        String filePath = "C:\\Users\\PC\\Desktop\\RunCompetitor.csv";
        String textFilePath = "C:\\Users\\PC\\Desktop\\CompetitorReport.txt";

        Manager manager = new Manager(filePath, textFilePath);
        manager.run();
    }
}
