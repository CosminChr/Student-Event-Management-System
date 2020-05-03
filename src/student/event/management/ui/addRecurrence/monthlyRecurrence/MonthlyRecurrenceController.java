package student.event.management.ui.addRecurrence.monthlyRecurrence;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import student.event.management.domain.util.DayOfWeekEnum;
import student.event.management.domain.util.WeekOrdinalEnum;

import java.net.URL;
import java.util.ResourceBundle;

public class MonthlyRecurrenceController implements Initializable {
    @FXML
    private JFXRadioButton dayNumberRadioButton;
    @FXML
    private JFXTextField dayNumber;
    @FXML
    private JFXTextField monthsUntil;
    @FXML
    private JFXRadioButton dayOfWeekRadioButton;
    @FXML
    private JFXComboBox dayOfWeekNumberItem;
    @FXML
    private JFXComboBox dayOfWeekItem;
    @FXML
    private JFXTextField numberOfMonths;

    /**
     * initializes the data in the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // populate comboboxes
        dayOfWeekNumberItem.getItems().removeAll(dayOfWeekNumberItem.getItems());
        dayOfWeekNumberItem.getItems().addAll(WeekOrdinalEnum.FIRST.getOrdinal(), WeekOrdinalEnum.SECOND.getOrdinal(), WeekOrdinalEnum.THIRD.getOrdinal(), WeekOrdinalEnum.FOURTH.getOrdinal(), WeekOrdinalEnum.LAST.getOrdinal());
        dayOfWeekItem.getItems().removeAll(dayOfWeekItem.getItems());
        dayOfWeekItem.getItems().addAll(DayOfWeekEnum.MONDAY.getLabel(), DayOfWeekEnum.TUESDAY.getLabel(), DayOfWeekEnum.WEDNESDAY.getLabel(), DayOfWeekEnum.THURSDAY.getLabel(), DayOfWeekEnum.FRIDAY.getLabel(), DayOfWeekEnum.SATURDAY.getLabel(), DayOfWeekEnum.SUNDAY.getLabel());

        //set default values
        dayNumberRadioButton.setSelected(true);
        dayOfWeekNumberItem.setDisable(true);
        dayNumber.setText("25");
        monthsUntil.setText("1");
        dayOfWeekItem.setDisable(true);
        numberOfMonths.setDisable(true);
        numberOfMonths.setText("1");
        dayOfWeekNumberItem.getSelectionModel().select(3);
        dayOfWeekItem.getSelectionModel().select(5);
    }


    /**
     * handles the change event for the dayNumber radio button
     *
     * @param actionEvent
     */
    @FXML
    private void onDayNumberRadioButtonChange(ActionEvent actionEvent) {
        dayNumberRadioButton.setSelected(true);
        dayOfWeekRadioButton.setSelected(false);
        dayOfWeekNumberItem.setDisable(true);
        dayOfWeekItem.setDisable(true);
        numberOfMonths.setDisable(true);
        dayNumber.setDisable(false);
        monthsUntil.setDisable(false);
    }

    /**
     * handles the change event for the onDayOfWeek radio button
     *
     * @param actionEvent
     */
    @FXML
    private void onDayOfWeekRadioButtonChange(ActionEvent actionEvent) {
        dayNumberRadioButton.setSelected(false);
        dayOfWeekRadioButton.setSelected(true);
        dayNumber.setDisable(true);
        monthsUntil.setDisable(true);
        dayOfWeekNumberItem.setDisable(false);
        dayOfWeekItem.setDisable(false);
        numberOfMonths.setDisable(false);
    }

    public JFXRadioButton getDayNumberRadioButton() {
        return dayNumberRadioButton;
    }

    public JFXTextField getDayNumber() {
        return dayNumber;
    }

    public JFXTextField getMonthsUntil() {
        return monthsUntil;
    }

    public JFXRadioButton getDayOfWeekRadioButton() {
        return dayOfWeekRadioButton;
    }

    public JFXComboBox getDayOfWeekNumberItem() {
        return dayOfWeekNumberItem;
    }

    public JFXComboBox getDayOfWeekItem() {
        return dayOfWeekItem;
    }

    public JFXTextField getNumberOfMonths() {
        return numberOfMonths;
    }
}
