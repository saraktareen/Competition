package competition.Controller;

import competition.view.CompetitionGUI;
import competition.model.CompetitorList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private CompetitionGUI view;
    private CompetitorList model;

    //Constructor for the controller class
    public Controller(CompetitionGUI view, CompetitorList model) {
        this.view = view;
        this.model = model;

        // Action listeners
        view.getAddCompetitorButton().addActionListener(new AddCompetitorButtonListener());
        view.getSearchButton().addActionListener(new SearchButtonListener());
        view.getShortButton().addActionListener(new ShortButtonListener());
        view.getEditButton().addActionListener(new EditButtonListener());
        view.getRemoveButton().addActionListener(new RemoveButtonListener());
    }

    
    //Buttons that implement certain methods based on the click of a button
    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.EditCompetitorDetails();
        }
    }

    private class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.RemoveCompetitor();
        }
    }

    private class ReportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.generateReport();
        }
    }

    private class ShortButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.shortReport();
        }
    }

    private class AddCompetitorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.RegisterCompetitor();
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.SearchCompetitor();
        }
    }
}
