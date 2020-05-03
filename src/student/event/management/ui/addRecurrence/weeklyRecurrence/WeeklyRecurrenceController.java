package student.event.management.ui.addRecurrence.weeklyRecurrence;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class WeeklyRecurrenceController implements Initializable {
    @FXML
    private Label weeklylabel;
    @FXML
    private JFXTextField numberOfWeeks;
    @FXML
    private JFXCheckBox mondayCheckbox;
    @FXML
    private JFXCheckBox tuesdayCheckbox;
    @FXML
    private JFXCheckBox wednesdayCheckbox;
    @FXML
    private JFXCheckBox thursdayCheckbox;
    @FXML
    private JFXCheckBox fridayCheckbox;
    @FXML
    private JFXCheckBox saturdayCheckbox;
    @FXML
    private JFXCheckBox sundayCheckbox;


    /**
     * initializes the data in the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberOfWeeks.setText("1");
        saturdayCheckbox.setSelected(true);
    }

    public JFXTextField getNumberOfWeeks() {
        return this.numberOfWeeks;
    }

    public JFXCheckBox getMondayCheckbox() {
        return mondayCheckbox;
    }

    public JFXCheckBox getTuesdayCheckbox() {
        return tuesdayCheckbox;
    }

    public JFXCheckBox getWednesdayCheckbox() {
        return wednesdayCheckbox;
    }

    public JFXCheckBox getThursdayCheckbox() {
        return thursdayCheckbox;
    }

    public JFXCheckBox getFridayCheckbox() {
        return fridayCheckbox;
    }

    public JFXCheckBox getSaturdayCheckbox() {
        return saturdayCheckbox;
    }

    public JFXCheckBox getSundayCheckbox() {
        return sundayCheckbox;
    }
}
