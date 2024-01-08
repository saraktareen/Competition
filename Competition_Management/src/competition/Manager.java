package competition;

import competition.view.CompetitionGUI;
import competition.model.CompetitorList;
import competition.model.User;

import javax.swing.*;

import competition.Controller.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Manager {
    private JFrame selectionFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Manager().showSelectionPanel());
    }

    private void showSelectionPanel() {
        selectionFrame = new JFrame("Access Level Selection");
        selectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(2, 1));

        //Views of the first GUI screen
        JButton OfficialButton = new JButton("Official");        
        JButton CompetitorButton = new JButton("Competitor");

        
        OfficialButton.addActionListener(e -> openCompetitionGUI("official"));
        CompetitorButton.addActionListener(e -> openCompetitionGUI("competitor"));

        selectionPanel.add(OfficialButton);
        selectionPanel.add(CompetitorButton);

        selectionFrame.add(selectionPanel);
        selectionFrame.setSize(400, 400);
        selectionFrame.setLocationRelativeTo(null);
        selectionFrame.setVisible(true);
    }
    
    
    //Depending on the selection of the role of the user, the views and methods are chosen for the access level
    private void openCompetitionGUI(String role) {
        selectionFrame.dispose();

        //Model
        CompetitorList model = new CompetitorList();

        model.readCompetitorsFromCSV("src/CompetitorList.csv");

        //User based on the selected role
        User user = new User(role);

        //View for staff and competitor
        CompetitionGUI competitionView = new CompetitionGUI(model, user);

        //Controller for staff and competitor
        Controller competitionController = new Controller(competitionView, model);

        //GUI for official and competitor
        competitionView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        competitionView.pack();
        competitionView.setLocationRelativeTo(null);
        competitionView.setVisible(true);
    }
}

