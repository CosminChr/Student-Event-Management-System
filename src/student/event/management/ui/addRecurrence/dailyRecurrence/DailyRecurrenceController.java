package student.event.management.ui.addRecurrence.dailyRecurrence;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DailyRecurrenceController implements Initializable {
    @FXML
    private JFXRadioButton everyRadioButton;
    @FXML
    private JFXTextField numberOfDays;
    @FXML
    private Label dailyLabel;
    @FXML
    private JFXRadioButton everyWeekdayRadioButton;


    /**
     * initializes the data in the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numberOfDays.setText("1");
        everyRadioButton.setSelected(true);
    }

    /**
     * handles the change event for the every radio button
     *
     * @param actionEvent
     */
    @FXML
    public void everyRadioButtonChange(ActionEvent actionEvent) {
        everyRadioButton.setSelected(true);
        everyWeekdayRadioButton.setSelected(false);
        numberOfDays.setDisable(false);
    }

    /**
     * handles the change event for the everyWeekday radio button
     *
     * @param actionEvent
     */
    @FXML
    public void everyWeekDayRadioButtonChange(ActionEvent actionEvent) {
        everyRadioButton.setSelected(false);
        everyWeekdayRadioButton.setSelected(true);
        numberOfDays.setDisable(true);
    }

    public JFXRadioButton getEveryRadioButton() {
        return this.everyRadioButton;
    }

    public JFXTextField getNumberOfDays() {
        return this.numberOfDays;
    }

    public JFXRadioButton getEveryWeekdayRadioButton() {
        return this.everyWeekdayRadioButton;
    }

}
