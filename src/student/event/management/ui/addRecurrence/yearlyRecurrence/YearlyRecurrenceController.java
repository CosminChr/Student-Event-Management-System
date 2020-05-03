package student.event.management.ui.addRecurrence.yearlyRecurrence;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import student.event.management.domain.util.DayOfWeekEnum;
import student.event.management.domain.util.MonthEnum;
import student.event.management.domain.util.WeekOrdinalEnum;

import java.net.URL;
import java.util.ResourceBundle;

public class YearlyRecurrenceController implements Initializable {
    @FXML
    private JFXTextField numberOfYears;
    @FXML
    private JFXRadioButton dayRadioButton;
    @FXML
    private JFXComboBox monthItemFirstOption;
    @FXML
    private JFXTextField dayOfMonth;
    @FXML
    private JFXRadioButton dayReferenceRadioButton;
    @FXML
    private JFXComboBox dayOfWeekItemNumber;
    @FXML
    private JFXComboBox dayOfWeekItem;
    @FXML
    private JFXComboBox monthItemSecondOption;

    /**
     * initializes the data in the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // populate comboboxes
        monthItemFirstOption.getItems().removeAll(monthItemFirstOption.getItems());
        monthItemFirstOption.getItems().addAll(MonthEnum.JANUARY.getMonth(), MonthEnum.FEBRUARY.getMonth(), MonthEnum.MARCH.getMonth(), MonthEnum.APRIL.getMonth(), MonthEnum.MAY.getMonth(), MonthEnum.JUNE.getMonth(), MonthEnum.JULY.getMonth(), MonthEnum.AUGUST.getMonth(), MonthEnum.SEPTEMBER.getMonth(), MonthEnum.OCTOBER.getMonth(), MonthEnum.NOVEMBER.getMonth(), MonthEnum.DECEMBER.getMonth());
        dayOfWeekItemNumber.getItems().removeAll(dayOfWeekItemNumber.getItems());
        dayOfWeekItemNumber.getItems().addAll(WeekOrdinalEnum.FIRST.getOrdinal(), WeekOrdinalEnum.SECOND.getOrdinal(), WeekOrdinalEnum.THIRD.getOrdinal(), WeekOrdinalEnum.FOURTH.getOrdinal(), WeekOrdinalEnum.LAST.getOrdinal());
        dayOfWeekItem.getItems().removeAll(dayOfWeekItem.getItems());
        dayOfWeekItem.getItems().addAll(DayOfWeekEnum.MONDAY.getLabel(), DayOfWeekEnum.TUESDAY.getLabel(), DayOfWeekEnum.WEDNESDAY.getLabel(), DayOfWeekEnum.THURSDAY.getLabel(), DayOfWeekEnum.FRIDAY.getLabel(), DayOfWeekEnum.SATURDAY.getLabel(), DayOfWeekEnum.SUNDAY.getLabel());
        monthItemSecondOption.getItems().removeAll(monthItemFirstOption.getItems());
        monthItemSecondOption.getItems().addAll(MonthEnum.JANUARY.getMonth(), MonthEnum.FEBRUARY.getMonth(), MonthEnum.MARCH.getMonth(), MonthEnum.APRIL.getMonth(), MonthEnum.MAY.getMonth(), MonthEnum.JUNE.getMonth(), MonthEnum.JULY.getMonth(), MonthEnum.AUGUST.getMonth(), MonthEnum.SEPTEMBER.getMonth(), MonthEnum.OCTOBER.getMonth(), MonthEnum.NOVEMBER.getMonth(), MonthEnum.DECEMBER.getMonth());

        // set default values
        dayRadioButton.setSelected(true);
        numberOfYears.setText("1");
        dayOfWeekItemNumber.setDisable(true);
        dayOfWeekItem.setDisable(true);
        monthItemSecondOption.setDisable(true);
        monthItemFirstOption.getSelectionModel().select(3);
        dayOfMonth.setText("25");
        dayOfWeekItemNumber.getSelectionModel().select(3);
        dayOfWeekItem.getSelectionModel().select(5);
        monthItemSecondOption.getSelectionModel().select(3);

    }

    /**
     * handles the change event for the day radio button change
     *
     * @param actionEvent
     */
    @FXML
    private void onDayRadioButtonChange(ActionEvent actionEvent) {
        dayRadioButton.setSelected(true);
        dayReferenceRadioButton.setSelected(false);
        dayOfWeekItemNumber.setDisable(true);
        dayOfWeekItem.setDisable(true);
        monthItemSecondOption.setDisable(true);
        monthItemFirstOption.setDisable(false);
        dayOfMonth.setDisable(false);

    }

    /**
     * handles the change event for the day reference radio button change
     *
     * @param actionEvent
     */
    @FXML
    private void onDayReferenceRadioButonChange(ActionEvent actionEvent) {
        dayRadioButton.setSelected(false);
        dayReferenceRadioButton.setSelected(true);
        monthItemFirstOption.setDisable(true);
        dayOfMonth.setDisable(true);
        dayOfWeekItemNumber.setDisable(false);
        dayOfWeekItem.setDisable(false);
        monthItemSecondOption.setDisable(false);
    }

    public JFXTextField getNumberOfYears() {
        return numberOfYears;
    }

    public JFXRadioButton getDayRadioButton() {
        return dayRadioButton;
    }

    public JFXComboBox getMonthItemFirstOption() {
        return monthItemFirstOption;
    }

    public JFXTextField getDayOfMonth() {
        return dayOfMonth;
    }

    public JFXRadioButton getDayReferenceRadioButton() {
        return dayReferenceRadioButton;
    }

    public JFXComboBox getDayOfWeekItemNumber() {
        return dayOfWeekItemNumber;
    }

    public JFXComboBox getDayOfWeekItem() {
        return dayOfWeekItem;
    }

    public JFXComboBox getMonthItemSecondOption() {
        return monthItemSecondOption;
    }
}
