package competition.view;

import competition.model.Competitor;
import competition.model.CompetitorList;
import competition.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class CompetitionGUI extends JFrame {

    private JTable CompetitionTable; //Name of the table 
    private User UserRole;
    private CompetitorList model;

    //Buttons used for the GUI
    private JButton RegisterCompetitorButton; //Button to register the competitor
    private JButton RemoveCompetitorButton; //Removes the competitor on the list
    private JButton ShortCompetitorDetailsButton; //Short Details Button
    private JButton AddToReportButton; //Adds the details to the report
    private JButton EditCompetitorButton; //Edits the registered competitors details (mostly used to edit the scores)
    private JButton SearchCompetitorButton; //Searches for a competitor amongst the registered table

    private JTextField CompetitorSearchField; //Search Field to input the competitor name
    private JTextArea SearchResultArea; //Used when the user wants to view the full details of the competitor
    private JTextArea DetailsArea; //Used when the user wants to view the short details of the competitor

    
    //Constructor for the CompetitionGUI
    public CompetitionGUI(CompetitorList model, User user) {
        this.model = model;
        this.UserRole = user;

        //Components of the GUI
        CompetitionTable = new JTable(); //Create the GUI table
        RegisterCompetitorButton = new JButton("Register Competitor");
        RemoveCompetitorButton = new JButton("Remove Competitor");
        ShortCompetitorDetailsButton = new JButton("Short Competitor Details");
        AddToReportButton = new JButton("Generate Report");
        EditCompetitorButton = new JButton("Edit Competitor");
        SearchCompetitorButton = new JButton("Search Competitor");

        CompetitorSearchField = new JTextField(10); //Limits the search results to 10
        SearchResultArea = new JTextArea();
        DetailsArea = new JTextArea();


        // Set up layout manager
        setLayout(new BorderLayout());

        // Top panel of buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        topPanel.add(RegisterCompetitorButton);
        topPanel.add(RemoveCompetitorButton);
        topPanel.add(ShortCompetitorDetailsButton);
        topPanel.add(AddToReportButton);
        topPanel.add(EditCompetitorButton);
        topPanel.add(CompetitorSearchField);
        topPanel.add(SearchCompetitorButton);

        // Bottom panel for the text
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        
        bottomPanel.add(SearchResultArea, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(CompetitionTable), BorderLayout.CENTER);
        bottomPanel.add(DetailsArea, BorderLayout.SOUTH);


        // Add the panels to the frame
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        ButtonVisibilityAsPerRole(); //Calls the visibility method

        // Add action listeners
        RegisterCompetitorButton.addActionListener(e -> RegisterCompetitor());
        RemoveCompetitorButton.addActionListener(e -> RemoveCompetitor());
        ShortCompetitorDetailsButton.addActionListener(e -> CompetitorShortDetails());
        AddToReportButton.addActionListener(e -> FinalReport());
        EditCompetitorButton.addActionListener(e -> EditCompetitorDetails());
        SearchCompetitorButton.addActionListener(e -> SearchCompetitor());

        //Window listener to handle the closing of the window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveDetailsToCSV();
            }
        });
        
        // Close the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Updates the competitor table
        updateCompetitorTable();
    }
    
    
    
//    ----------------------------------------------------------------------------------------------------------
    
    //Save details to CSV file
    private void saveDetailsToCSV() {
        String filePath = "src/CompetitorList.csv";
        model.writeCompetitorsToCSV(filePath);
    }
    
    public void RegisterCompetitor() {
    	//Different fields shown
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField dateOfBirthField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField countryField = new JTextField();
        JTextField scoresField = new JTextField();

        JComboBox<Competitor.Level> levelComboBox = new JComboBox<>(Competitor.Level.values());
        JComboBox<Competitor.Category> categoryComboBox = new JComboBox<>(Competitor.Category.values());

        //The GUI for the text boxes and text labels for the user to enter the details
        JPanel formPanel = new JPanel(new GridLayout(9, 2));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryComboBox);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        formPanel.add(dateOfBirthField);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);
        formPanel.add(new JLabel("Country:"));
        formPanel.add(countryField);
        formPanel.add(new JLabel("Scores (e.g n,n,n,n,n):"));
        formPanel.add(scoresField);

        int result = JOptionPane.showConfirmDialog(
                this, formPanel, "Register Competitor", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Extract values from the form fields and uses them to display them in the table
            String name = nameField.getText();
            String dateOfBirth = dateOfBirthField.getText();
            String email = emailField.getText();
            String gender = genderField.getText();
            String country = countryField.getText();
            String scoresText = scoresField.getText();

            // Parse date of birth and calculate the age
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(dateOfBirth);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Date of Birth must be in the format YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int age = calculateAge(dateOfBirth);

            // Validate name and country entry
            if (!isNonEmptyString(name)) {
                JOptionPane.showMessageDialog(this, "Name must be a non-empty string.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isNonEmptyString(country)) {
                JOptionPane.showMessageDialog(this, "Country must be a non-empty string.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse scores to split them from commas
            int[] scores = Arrays.stream(scoresText.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            // Levels based on their ages
            Competitor.Level level;
            if (age <= 18) {
                level = Competitor.Level.Novice;
            } else if (age <= 30) {
                level = Competitor.Level.Intermediate;
            } else {
                level = Competitor.Level.Expert;
            }

            // Get the selected category
            Competitor.Category selectedCategory = (Competitor.Category) categoryComboBox.getSelectedItem();
            
            // Generate a unique competitor number
            int competitorNumber = model.getAllCompetitors().size() + 1;

            // Check if the competitor already exists
            if (competitorAlreadyExists(selectedCategory, gender)) {
                JOptionPane.showMessageDialog(this, "A competitor with the same email and category already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Add the competitor to the model
            model.addCompetitor(competitorNumber, name, selectedCategory, level, email, age, gender, country, scores);

            // Update the competitor table
            updateCompetitorTable();
        }
    }
    
    

    
    
    //Removes the competitor from the list
    //Access denied to the competitor
    public void RemoveCompetitor() {

        if ("competitor".equals(UserRole.getRole())) {
            JOptionPane.showMessageDialog(this, "Competitors are not allowed to remove competitors.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = CompetitionTable.getSelectedRow();
        if (selectedRow != -1) {
            int competitorNumber = (int) CompetitionTable.getValueAt(selectedRow, 0);
            model.removeCompetitor(competitorNumber);
            updateCompetitorTable();
        }
    }

    
    public void CompetitorShortDetails() {
        try {

            if ("competitor".equals(UserRole.getRole())) {
                JOptionPane.showMessageDialog(this, "Competitors are not allowed to add competitors.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int competitorNumberToDisplay = Integer.parseInt(JOptionPane.showInputDialog("Enter competitor number:"));
            if (model.isValidCompetitorNumber(competitorNumberToDisplay)) {
                Competitor competitor = model.getCompetitorByNumber(competitorNumberToDisplay);
                JOptionPane.showMessageDialog(this, "Competitor Details:\n" + competitor.getCompetitorShortDetails(), "Competitor Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid competitor number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid competitor number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    private void FinalReport() {
        if ("competitor".equals(UserRole.getRole())) {
           JOptionPane.showMessageDialog(this, "Competitors are not allowed to add competitors.", "Access Denied", JOptionPane.ERROR_MESSAGE);
           return;
       }

       String filePath = "src/final_report.txt";  // Change this path
       model.produceFinalReport(filePath);
       JOptionPane.showMessageDialog(this, "Final report generated successfully!", "Report Generated", JOptionPane.INFORMATION_MESSAGE);
   }
    
    
    
    
 // Used to edit the details of the already registered competitors
 // This access is not given to competitors
    public void EditCompetitorDetails() {
        if ("competitor".equals(UserRole.getRole())) {
            JOptionPane.showMessageDialog(this, "Competitors are not allowed to edit details.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = CompetitionTable.getSelectedRow();
        if (selectedRow != -1) {
            int competitorNumber = (int) CompetitionTable.getValueAt(selectedRow, 0);
            Competitor competitor = model.getCompetitorByNumber(competitorNumber);
            if (competitor != null) {
                JTextField nameField = new JTextField(competitor.getCompetitorName());
                JComboBox<Competitor.Level> levelComboBox = new JComboBox<>(Competitor.Level.values());
                levelComboBox.setSelectedItem(competitor.getLevel());
                JComboBox<Competitor.Category> categoryComboBox = new JComboBox<>(Competitor.Category.values());
                categoryComboBox.setSelectedItem(competitor.getCategory());

                Object[] message = {
                        "Name:", nameField,
                        "Category:", categoryComboBox,
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Edit Competitor Details", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String newName = nameField.getText();
                        Competitor.Category newCategory = (Competitor.Category) categoryComboBox.getSelectedItem();

                        // Update only name and category
                        competitor.setCompetitorName(newName);
                        competitor.setCategory(newCategory);

                        // Prompt the user for new scores
                        int[] newScores = new int[competitor.getScores().length];
                        for (int i = 0; i < newScores.length; i++) {
                            String scoreStr = JOptionPane.showInputDialog("Enter new score for round " + (i + 1) + ":", competitor.getScores()[i]);
                            newScores[i] = Integer.parseInt(scoreStr);
                        }

                        // Update scores
                        competitor.setScores(newScores);

                        updateCompetitorTable();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error occurred while editing competitor details.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }



    
    
  //THis searches through the array to find the competitor for the search
    public void SearchCompetitor() {
        try {
            int competitorNumberToSearch = Integer.parseInt(CompetitorSearchField.getText());
            Competitor foundCompetitor = model.getCompetitorByNumber(competitorNumberToSearch);
            if (foundCompetitor != null) {
            	SearchResultArea.setText("Full Details of the Competitor:\n" + foundCompetitor.getCompetitorFullDetails());
            } else {
            	SearchResultArea.setText("The Competitor was not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Competitor Number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
  //The headings for the competitor table
    public void updateCompetitorTable() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Competitor Number");
        tableModel.addColumn("Competitior Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Level");
        tableModel.addColumn("Email");
        tableModel.addColumn("Age");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Country");
        tableModel.addColumn("List of Scores");

        //Reads the competitors from the CSV file and adds them here
        for (Competitor competitor : model.getAllCompetitors()) {
            Object[] rowData = {
                    competitor.getCompetitorNumber(),
                    competitor.getCompetitorName(),
                    competitor.getCategory(),
                    competitor.getLevel(),
                    competitor.getGender(),
                    competitor.getDateofBirth(),
                    competitor.getCountry(),
                    competitor.getEmail(),
                    Arrays.toString(competitor.getScores())
            };
            tableModel.addRow(rowData);
        }

        CompetitionTable.setModel(tableModel);
        String filePath = "src/CompetitorList.csv"; 
        if ("staff".equals(UserRole.getRole())) {
            model.writeCompetitorsToCSV(filePath);
        }
    }
    
    
  //Generated the final report
    //Contains the Average scores, max,min and frequency
    public void generateReport() {
        if ("competitor".equals(UserRole.getRole())) {
        	DetailsArea.setText("Access Denied: Competitors are not allowed to generate reports.");
            return;
        }

        int selectedRow = CompetitionTable.getSelectedRow();
        if (selectedRow != -1) {
            int competitorNumber = (int) CompetitionTable.getValueAt(selectedRow, 0);
            Competitor competitor = model.getCompetitorByNumber(competitorNumber);
            if (competitor != null) {
            	DetailsArea.setText(competitor.getCompetitorFullDetails());
            }
        }
    }
    
    
  //Produces the short details report of the competitor
    public void shortReport() {
        StringBuilder sreport = new StringBuilder();
        Competitor winner = model.getWinner();
        if (winner != null) {
            sreport.append("Competitor with the highest overall score:\n");
            sreport.append(String.format("Competitor Number: %d, Name: %s, Overall Score: %.2f%n",
                    winner.getCompetitorNumber(), winner.getCompetitorName(), winner.getCompetitorOverallScore()));
            sreport.append("\n");
        }
        DetailsArea.setText(sreport.toString());
    }
    
//    -------------------------------------------------------------------------------------------------------------------
    
  //The visibility for the roles (staff & competitor)
    public void ButtonVisibilityAsPerRole() {
        if ("staff".equals(UserRole.getRole())) {
        	EditCompetitorButton.setEnabled(true);
            RemoveCompetitorButton.setEnabled(true);
            ShortCompetitorDetailsButton.setEnabled(true);
            RegisterCompetitorButton.setEnabled(true);
            AddToReportButton.setEnabled(true);
            SearchCompetitorButton.setEnabled(true);
        } else if ("competitor".equals(UserRole.getRole())) {
        	EditCompetitorButton.setEnabled(false);
            RemoveCompetitorButton.setEnabled(false);
            ShortCompetitorDetailsButton.setEnabled(true);
            RegisterCompetitorButton.setEnabled(false);
            AddToReportButton.setEnabled(false);
            SearchCompetitorButton.setEnabled(true);
        }
    }
    
    
  //This method converts the Date of birth input to the competitors age
    private static int calculateAge(String dateOfBirth) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      try {
          Date birthDate = dateFormat.parse(dateOfBirth);
          Calendar dob = Calendar.getInstance();
          dob.setTime(birthDate);

          Calendar currentDate = Calendar.getInstance();

          int age = currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

          // Adjust age if the birthday hasn't occurred yet this year
          if (currentDate.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
              age--;
          }

          return age;
      } catch (ParseException e) {
          e.printStackTrace();
          return 0; // Return default age or handle the error as needed
      }
    }
    
//------------------------------------------------------------------------------------------------------------
    

    private boolean competitorAlreadyExists(Competitor.Category category, String gender) {
        for (Competitor competitor : model.getAllCompetitors()) {
            if (competitor.getCategory() == category && competitor.getEmail().equals(gender)) {
                return true;
            }
        }
        return false;
    }

    
    private boolean isNonEmptyString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    
    // Helper method to check if a string is a non-empty integer
    private boolean isNonEmptyInteger(String str) {
        return !str.isEmpty() && isInteger(str);
    }

    // Helper method to check if a string contains only letters
    private boolean isAlpha(String str) {
        return str.matches("^[a-zA-Z]+$");
    }

    // Helper method to check if a string is a non-empty string containing only letters
    private boolean isNonEmptyStringWithLetters(String str) {
        return !str.isEmpty() && isAlpha(str);
    }

    // Helper method to check if a string is an integer
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    
      public JButton getEditButton() {
     return EditCompetitorButton;
    }
      
      public JButton getRemoveButton() {
     return RemoveCompetitorButton;
    }
     
      public JButton getShortButton() {
     return ShortCompetitorDetailsButton;
    }
      
    public JButton getAddCompetitorButton() {
     return RegisterCompetitorButton;
    }
    
    public JButton getSearchButton() {
     return SearchCompetitorButton;
    }

}