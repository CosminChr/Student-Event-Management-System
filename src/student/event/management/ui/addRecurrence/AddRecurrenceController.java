package student.event.management.ui.addRecurrence;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import student.event.management.domain.models.Recurrence;
import student.event.management.domain.models.RecurrencePattern;
import student.event.management.domain.models.RecurrenceRange;
import student.event.management.domain.util.DayOfWeekEnum;
import student.event.management.domain.util.MonthEnum;
import student.event.management.domain.util.WeekOrdinalEnum;
import student.event.management.infrastructure.repository.RecurrencePatternRepository;
import student.event.management.infrastructure.repository.RecurrenceRangeRepository;
import student.event.management.infrastructure.repository.RecurrenceRepository;
import student.event.management.ui.addEvent.AddEventController;
import student.event.management.ui.addRecurrence.dailyRecurrence.DailyRecurrenceController;
import student.event.management.ui.addRecurrence.monthlyRecurrence.MonthlyRecurrenceController;
import student.event.management.ui.addRecurrence.weeklyRecurrence.WeeklyRecurrenceController;
import student.event.management.ui.addRecurrence.yearlyRecurrence.YearlyRecurrenceController;
import student.event.management.ui.bookEvent.BookEventController;
import student.event.management.ui.cancelBooking.CancelBookingController;
import student.event.management.ui.deleteEvent.DeleteEventController;
import student.event.management.ui.updateEvent.UpdateEventController;
import student.event.management.ui.updateOrDeleteEvent.UpdateOrDeleteEventController;
import student.event.management.ui.util.RecurrencePatternFrequencyEnum;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddRecurrenceController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTimePicker eventTime;
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXRadioButton dailyRadioButton;
    @FXML
    private JFXRadioButton weeklyRadioButton;
    @FXML
    private JFXRadioButton monthlyRadioButton;
    @FXML
    private JFXRadioButton yearlyRadioButton;
    @FXML
    private AnchorPane recurrenceAnchorPane;
    @FXML
    private JFXRadioButton endByRadioButton;
    @FXML
    private JFXDatePicker stopDate;
    @FXML
    private JFXRadioButton endAfterRadioButton;
    @FXML
    private JFXRadioButton noEndDateRadioButton;
    @FXML
    private JFXTextField numberOfOccurrences;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton removeOccurrenceButton;

    private AnchorPane dailyRecurrenceAnchorPane;

    private AnchorPane weeklyRecurrenceAnchorPane;

    private AnchorPane monthlyRecurrenceAnchorPane;

    private AnchorPane yearlyRecurrenceAnchorPane;

    private AddEventController addEventController;

    private UpdateEventController updateEventController;

    private UpdateOrDeleteEventController updateOrDeleteEventController;

    private DeleteEventController deleteEventController;

    private BookEventController bookEventController;

    private CancelBookingController cancelBookingController;

    private RecurrenceRepository recurrenceRepository;

    private RecurrencePatternRepository recurrencePatternRepository;

    private RecurrenceRangeRepository recurrenceRangeRepository;

    private DailyRecurrenceController dailyRecurrenceController;

    private WeeklyRecurrenceController weeklyRecurrenceController;

    private MonthlyRecurrenceController monthlyRecurrenceController;

    private YearlyRecurrenceController yearlyRecurrenceController;

    public AddRecurrenceController(AddEventController addEventController) {
        this.addEventController = addEventController;
    }

    public AddRecurrenceController(UpdateEventController updateEventController) {
        this.updateEventController = updateEventController;
        this.recurrenceRepository = new RecurrenceRepository();
        this.recurrencePatternRepository = new RecurrencePatternRepository();
        this.recurrenceRangeRepository = new RecurrenceRangeRepository();
    }

    public AddRecurrenceController(UpdateOrDeleteEventController updateOrDeleteEventController) {
        this.updateOrDeleteEventController = updateOrDeleteEventController;
        this.recurrenceRepository = new RecurrenceRepository();
        this.recurrencePatternRepository = new RecurrencePatternRepository();
        this.recurrenceRangeRepository = new RecurrenceRangeRepository();
    }

    /**
     * initializes the data in the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // load the dailyRecurrenceController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/addRecurrence/dailyRecurrence/dailyRecurrence.fxml"));
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        DailyRecurrenceController dailyRecurrenceController = new DailyRecurrenceController();
        loader.setController(dailyRecurrenceController);
        this.dailyRecurrenceController = dailyRecurrenceController;
        try {
            dailyRecurrenceAnchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load the weeklyRecurrenceController
        loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/addRecurrence/weeklyRecurrence/weeklyRecurrence.fxml"));
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        WeeklyRecurrenceController weeklyRecurrenceController = new WeeklyRecurrenceController();
        loader.setController(weeklyRecurrenceController);
        this.weeklyRecurrenceController = weeklyRecurrenceController;
        try {
            weeklyRecurrenceAnchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load the monthlyRecurrenceController
        loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/addRecurrence/monthlyRecurrence/monthlyRecurrence.fxml"));
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        MonthlyRecurrenceController monthlyRecurrenceController = new MonthlyRecurrenceController();
        loader.setController(monthlyRecurrenceController);
        this.monthlyRecurrenceController = monthlyRecurrenceController;
        try {
            monthlyRecurrenceAnchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load the yearlyRecurrenceController
        loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/addRecurrence/yearlyRecurrence/yearlyRecurrence.fxml"));
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        YearlyRecurrenceController yearlyRecurrenceController = new YearlyRecurrenceController();
        loader.setController(yearlyRecurrenceController);
        this.yearlyRecurrenceController = yearlyRecurrenceController;
        try {
            yearlyRecurrenceAnchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set default values
        weeklyRadioButton.setSelected(true);
        this.rootPane.getChildren().add(weeklyRecurrenceAnchorPane);
        this.recurrenceAnchorPane.getChildren().add(weeklyRecurrenceAnchorPane);
        LocalTime now = LocalTime.now().withHour(12).withMinute(0);
        eventTime.setValue(now);
        LocalDate startDate = LocalDate.now();
        this.startDate.setValue(startDate);
        LocalDate endDate = LocalDate.now().plusMonths(5).plusDays(15);
        stopDate.setValue(endDate);
        endByRadioButton.setSelected(true);
        numberOfOccurrences.setText("25");
        numberOfOccurrences.setDisable(true);

        // if a recurrence exists in the addEventController
        //  then load the suitable controller
        // and set the values from the recurrence in it
        if (this.addEventController != null && this.addEventController.getRecurrence() != null) {
            switch (this.addEventController.getRecurrencePattern().getRecurrenceFrequency()) {
                case DAILY:
                    this.weeklyRadioButton.setSelected(false);
                    this.dailyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(dailyRecurrenceAnchorPane);

                    if (this.addEventController.getRecurrencePattern().getEveryXDays() != null
                            && this.addEventController.getRecurrencePattern().getEveryXDays() != 0) {
                        this.dailyRecurrenceController.getNumberOfDays().setText(this.addEventController.getRecurrencePattern().getEveryXDays().toString());
                    }
                    if (this.addEventController.getRecurrencePattern().getEveryWeekday() != null) {
                        this.dailyRecurrenceController.getEveryRadioButton().setSelected(false);
                        this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(true);
                    }
                    break;
                case WEEKLY:
                    this.weeklyRecurrenceController.getNumberOfWeeks().setText(this.addEventController.getRecurrencePattern().getEveryXWeeks().toString());
                    this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(false);
                    if (this.addEventController.getRecurrencePattern().getMonday() != null
                            && this.addEventController.getRecurrencePattern().getMonday() == true) {
                        this.weeklyRecurrenceController.getMondayCheckbox().setSelected(true);
                    }
                    if (this.addEventController.getRecurrencePattern().getTuesday() != null
                            && this.addEventController.getRecurrencePattern().getTuesday() == true) {
                        this.weeklyRecurrenceController.getTuesdayCheckbox().setSelected(true);
                    }
                    if (this.addEventController.getRecurrencePattern().getWednesday() != null
                            && this.addEventController.getRecurrencePattern().getWednesday() == true) {
                        this.weeklyRecurrenceController.getWednesdayCheckbox().setSelected(true);
                    }
                    if (this.addEventController.getRecurrencePattern().getThursday() != null
                            && this.addEventController.getRecurrencePattern().getThursday() == true) {
                        this.weeklyRecurrenceController.getThursdayCheckbox().setSelected(true);
                    }
                    if (this.addEventController.getRecurrencePattern().getFriday() != null
                            && this.addEventController.getRecurrencePattern().getFriday() == true) {
                        this.weeklyRecurrenceController.getFridayCheckbox().setSelected(true);
                    }
                    if (this.addEventController.getRecurrencePattern().getSaturday() != null
                            && this.addEventController.getRecurrencePattern().getSaturday() != false) {
                        this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(true);
                    }
                    if (this.addEventController.getRecurrencePattern().getSunday() != null
                            && this.addEventController.getRecurrencePattern().getSunday() != false) {
                        this.weeklyRecurrenceController.getSundayCheckbox().setSelected(true);
                    }
                    break;
                case MONTHLY:
                    this.weeklyRadioButton.setSelected(false);
                    this.monthlyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(monthlyRecurrenceAnchorPane);
                    if (addEventController.getRecurrencePattern().getXDay() != null &&
                            this.addEventController.getRecurrencePattern().getXDay() != 0) {
                        this.monthlyRecurrenceController.getDayNumber().setText(this.addEventController.getRecurrencePattern().getXDay().toString());
                        this.monthlyRecurrenceController.getMonthsUntil().setText(this.addEventController.getRecurrencePattern().getOfEveryXMonthsFirstOption().toString());
                    }
                    if (this.addEventController.getRecurrencePattern().getTheXMonthly() != null) {
                        this.monthlyRecurrenceController.getDayNumberRadioButton().setSelected(false);
                        this.monthlyRecurrenceController.getDayOfWeekRadioButton().setSelected(true);
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().getSelectionModel().select(this.addEventController.getRecurrencePattern().getTheXMonthly().getOrdinal());
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().setDisable(false);
                        this.monthlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(this.addEventController.getRecurrencePattern().getDayOfWeekMonthly().getLabel());
                        this.monthlyRecurrenceController.getDayOfWeekItem().setDisable(false);
                        this.monthlyRecurrenceController.getNumberOfMonths().setText(this.addEventController.getRecurrencePattern().getOfEveryXMonthsSecondOption().toString());
                        this.monthlyRecurrenceController.getNumberOfMonths().setDisable(false);

                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);
                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);
                    }
                    break;
                case YEARLY:
                    this.weeklyRadioButton.setSelected(false);
                    this.yearlyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(yearlyRecurrenceAnchorPane);
                    this.yearlyRecurrenceController.getNumberOfYears().setText(this.addEventController.getRecurrencePattern().getEveryXYears().toString());
                    if (this.addEventController.getRecurrencePattern().getMonth() != null) {
                        this.yearlyRecurrenceController.getMonthItemFirstOption().getSelectionModel().select(this.addEventController.getRecurrencePattern().getMonth().getMonth());
                        this.yearlyRecurrenceController.getDayOfMonth().setText(this.addEventController.getRecurrencePattern().getX().toString());
                    }
                    if (this.addEventController.getRecurrencePattern().getTheXYearly() != null) {
                        this.yearlyRecurrenceController.getDayRadioButton().setSelected(false);
                        this.yearlyRecurrenceController.getDayReferenceRadioButton().setSelected(true);
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().getSelectionModel().select(this.addEventController.getRecurrencePattern().getTheXYearly().getOrdinal());
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().setDisable(false);
                        this.yearlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(this.addEventController.getRecurrencePattern().getDayOfWeekYearly().getLabel());
                        this.yearlyRecurrenceController.getDayOfWeekItem().setDisable(false);
                        this.yearlyRecurrenceController.getMonthItemSecondOption().getSelectionModel().select(this.addEventController.getRecurrencePattern().getOfX().getMonth());
                        this.yearlyRecurrenceController.getMonthItemSecondOption().setDisable(false);

                        this.yearlyRecurrenceController.getMonthItemFirstOption().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfMonth().setDisable(true);

                    }
                    break;
            }
        }

        // if a recurrence exists in the updateEventController
        //  then load the suitable controller
        // and set the values from the recurrence in it
        if (this.updateEventController != null && this.updateEventController.getRecurrence() != null) {
            switch (this.updateEventController.getRecurrencePattern().getRecurrenceFrequency()) {
                case DAILY:
                    this.weeklyRadioButton.setSelected(false);
                    this.dailyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(dailyRecurrenceAnchorPane);
                    if (this.updateEventController.getRecurrencePattern().getEveryXDays() != null
                            && this.updateEventController.getRecurrencePattern().getEveryXDays() != 0) {
                        this.dailyRecurrenceController.getNumberOfDays().setText(this.updateEventController.getRecurrencePattern().getEveryXDays().toString());
                    }
                    if (this.updateEventController.getRecurrencePattern().getEveryWeekday() != null) {
                        this.dailyRecurrenceController.getEveryRadioButton().setSelected(false);
                        this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(true);
                    }
                    break;
                case WEEKLY:
                    this.weeklyRecurrenceController.getNumberOfWeeks().setText(this.updateEventController.getRecurrencePattern().getEveryXWeeks().toString());
                    this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(false);
                    if (this.updateEventController.getRecurrencePattern().getMonday() != null
                            && this.updateEventController.getRecurrencePattern().getMonday() == true) {
                        this.weeklyRecurrenceController.getMondayCheckbox().setSelected(true);
                    }
                    if (this.updateEventController.getRecurrencePattern().getTuesday() != null
                            && this.updateEventController.getRecurrencePattern().getTuesday() == true) {
                        this.weeklyRecurrenceController.getTuesdayCheckbox().setSelected(true);
                    }
                    if (this.updateEventController.getRecurrencePattern().getWednesday() != null
                            && this.updateEventController.getRecurrencePattern().getWednesday() == true) {
                        this.weeklyRecurrenceController.getWednesdayCheckbox().setSelected(true);
                    }
                    if (this.updateEventController.getRecurrencePattern().getThursday() != null
                            && this.updateEventController.getRecurrencePattern().getThursday() == true) {
                        this.weeklyRecurrenceController.getThursdayCheckbox().setSelected(true);
                    }
                    if (this.updateEventController.getRecurrencePattern().getFriday() != null
                            && this.updateEventController.getRecurrencePattern().getFriday() == true) {
                        this.weeklyRecurrenceController.getFridayCheckbox().setSelected(true);
                    }
                    if (this.updateEventController.getRecurrencePattern().getSaturday() != null
                            && this.updateEventController.getRecurrencePattern().getSaturday() != false) {
                        this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(true);
                    }
                    if (this.updateEventController.getRecurrencePattern().getSunday() != null
                            && this.updateEventController.getRecurrencePattern().getSunday() != false) {
                        this.weeklyRecurrenceController.getSundayCheckbox().setSelected(true);
                    }
                    break;
                case MONTHLY:
                    this.weeklyRadioButton.setSelected(false);
                    this.monthlyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(monthlyRecurrenceAnchorPane);
                    if (updateEventController.getRecurrencePattern().getXDay() != null &&
                            this.updateEventController.getRecurrencePattern().getXDay() != 0) {
                        this.monthlyRecurrenceController.getDayNumber().setText(this.updateEventController.getRecurrencePattern().getXDay().toString());
                        this.monthlyRecurrenceController.getMonthsUntil().setText(this.updateEventController.getRecurrencePattern().getOfEveryXMonthsFirstOption().toString());
                    }
                    if (this.updateEventController.getRecurrencePattern().getTheXMonthly() != null) {
                        this.monthlyRecurrenceController.getDayNumberRadioButton().setSelected(false);
                        this.monthlyRecurrenceController.getDayOfWeekRadioButton().setSelected(true);
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().getSelectionModel().select(this.updateEventController.getRecurrencePattern().getTheXMonthly().getOrdinal());
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().setDisable(false);
                        this.monthlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(this.updateEventController.getRecurrencePattern().getDayOfWeekMonthly().getLabel());
                        this.monthlyRecurrenceController.getDayOfWeekItem().setDisable(false);
                        this.monthlyRecurrenceController.getNumberOfMonths().setText(this.updateEventController.getRecurrencePattern().getOfEveryXMonthsSecondOption().toString());
                        this.monthlyRecurrenceController.getNumberOfMonths().setDisable(false);

                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);
                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);

                    }
                    break;
                case YEARLY:
                    this.weeklyRadioButton.setSelected(false);
                    this.yearlyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(yearlyRecurrenceAnchorPane);
                    this.yearlyRecurrenceController.getNumberOfYears().setText(this.updateEventController.getRecurrencePattern().getEveryXYears().toString());
                    if (this.updateEventController.getRecurrencePattern().getMonth() != null) {
                        this.yearlyRecurrenceController.getMonthItemFirstOption().getSelectionModel().select(this.updateEventController.getRecurrencePattern().getMonth().getMonth());
                        this.yearlyRecurrenceController.getDayOfMonth().setText(this.updateEventController.getRecurrencePattern().getX().toString());
                    }
                    if (this.updateEventController.getRecurrencePattern().getTheXYearly() != null) {
                        this.yearlyRecurrenceController.getDayRadioButton().setSelected(false);
                        this.yearlyRecurrenceController.getDayReferenceRadioButton().setSelected(true);
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().getSelectionModel().select(this.updateEventController.getRecurrencePattern().getTheXYearly().getOrdinal());
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().setDisable(false);
                        this.yearlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(this.updateEventController.getRecurrencePattern().getDayOfWeekYearly().getLabel());
                        this.yearlyRecurrenceController.getDayOfWeekItem().setDisable(false);
                        this.yearlyRecurrenceController.getMonthItemSecondOption().getSelectionModel().select(this.updateEventController.getRecurrencePattern().getOfX().getMonth());
                        this.yearlyRecurrenceController.getMonthItemSecondOption().setDisable(false);

                        this.yearlyRecurrenceController.getMonthItemFirstOption().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfMonth().setDisable(true);

                    }
                    break;
            }
        }

        // if a recurrence exists in the updateOrDeleteController
        // then load the suitable controller
        // and set the values from the recurrence in it
        if (this.updateOrDeleteEventController != null && this.updateOrDeleteEventController.getRecurrence() != null) {
            switch (this.updateOrDeleteEventController.getRecurrencePattern().getRecurrenceFrequency()) {
                case DAILY:
                    this.weeklyRadioButton.setSelected(false);
                    this.dailyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(dailyRecurrenceAnchorPane);
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getEveryXDays() != null
                            && this.updateOrDeleteEventController.getRecurrencePattern().getEveryXDays() != 0) {
                        this.dailyRecurrenceController.getNumberOfDays().setText(this.updateOrDeleteEventController.getRecurrencePattern().getEveryXDays().toString());
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getEveryWeekday() != null) {
                        this.dailyRecurrenceController.getEveryRadioButton().setSelected(false);
                        this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(true);
                    }
                    break;
                case WEEKLY:
                    this.weeklyRecurrenceController.getNumberOfWeeks().setText(this.updateOrDeleteEventController.getRecurrencePattern().getEveryXWeeks().toString());
                    this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(false);
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getMonday() != null
                            && this.updateOrDeleteEventController.getRecurrencePattern().getMonday() == true) {
                        this.weeklyRecurrenceController.getMondayCheckbox().setSelected(true);
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getTuesday() != null
                            && this.updateOrDeleteEventController.getRecurrencePattern().getTuesday() == true) {
                        this.weeklyRecurrenceController.getTuesdayCheckbox().setSelected(true);
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getWednesday() != null
                            && this.updateOrDeleteEventController.getRecurrencePattern().getWednesday() == true) {
                        this.weeklyRecurrenceController.getWednesdayCheckbox().setSelected(true);
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getThursday() != null
                            && this.updateOrDeleteEventController.getRecurrencePattern().getThursday() == true) {
                        this.weeklyRecurrenceController.getThursdayCheckbox().setSelected(true);
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getFriday() != null
                            && this.updateOrDeleteEventController.getRecurrencePattern().getFriday() == true) {
                        this.weeklyRecurrenceController.getFridayCheckbox().setSelected(true);
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getSaturday() != null
                            && this.updateOrDeleteEventController.getRecurrencePattern().getSaturday() != false) {
                        this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(true);
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getSunday() != null
                            && this.updateOrDeleteEventController.getRecurrencePattern().getSunday() != false) {
                        this.weeklyRecurrenceController.getSundayCheckbox().setSelected(true);
                    }
                    break;
                case MONTHLY:
                    this.weeklyRadioButton.setSelected(false);
                    this.monthlyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(monthlyRecurrenceAnchorPane);
                    if (updateOrDeleteEventController.getRecurrencePattern().getXDay() != null &&
                            this.updateOrDeleteEventController.getRecurrencePattern().getXDay() != 0) {
                        this.monthlyRecurrenceController.getDayNumber().setText(this.updateOrDeleteEventController.getRecurrencePattern().getXDay().toString());
                        this.monthlyRecurrenceController.getMonthsUntil().setText(this.updateOrDeleteEventController.getRecurrencePattern().getOfEveryXMonthsFirstOption().toString());
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getTheXMonthly() != null) {
                        this.monthlyRecurrenceController.getDayNumberRadioButton().setSelected(false);
                        this.monthlyRecurrenceController.getDayOfWeekRadioButton().setSelected(true);
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().getSelectionModel().select(this.updateOrDeleteEventController.getRecurrencePattern().getTheXMonthly().getOrdinal());
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().setDisable(false);
                        this.monthlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(this.updateOrDeleteEventController.getRecurrencePattern().getDayOfWeekMonthly().getLabel());
                        this.monthlyRecurrenceController.getDayOfWeekItem().setDisable(false);
                        this.monthlyRecurrenceController.getNumberOfMonths().setText(this.updateOrDeleteEventController.getRecurrencePattern().getOfEveryXMonthsSecondOption().toString());
                        this.monthlyRecurrenceController.getNumberOfMonths().setDisable(false);

                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);
                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);

                    }
                    break;
                case YEARLY:
                    this.weeklyRadioButton.setSelected(false);
                    this.yearlyRadioButton.setSelected(true);
                    this.recurrenceAnchorPane.getChildren().remove(weeklyRecurrenceAnchorPane);
                    this.recurrenceAnchorPane.getChildren().add(yearlyRecurrenceAnchorPane);
                    this.yearlyRecurrenceController.getNumberOfYears().setText(this.updateOrDeleteEventController.getRecurrencePattern().getEveryXYears().toString());
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getMonth() != null) {
                        this.yearlyRecurrenceController.getMonthItemFirstOption().getSelectionModel().select(this.updateOrDeleteEventController.getRecurrencePattern().getMonth().getMonth());
                        this.yearlyRecurrenceController.getDayOfMonth().setText(this.updateOrDeleteEventController.getRecurrencePattern().getX().toString());
                    }
                    if (this.updateOrDeleteEventController.getRecurrencePattern().getTheXYearly() != null) {
                        this.yearlyRecurrenceController.getDayRadioButton().setSelected(false);
                        this.yearlyRecurrenceController.getDayReferenceRadioButton().setSelected(true);
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().getSelectionModel().select(this.updateOrDeleteEventController.getRecurrencePattern().getTheXYearly().getOrdinal());
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().setDisable(false);
                        this.yearlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(this.updateOrDeleteEventController.getRecurrencePattern().getDayOfWeekYearly().getLabel());
                        this.yearlyRecurrenceController.getDayOfWeekItem().setDisable(false);
                        this.yearlyRecurrenceController.getMonthItemSecondOption().getSelectionModel().select(this.updateOrDeleteEventController.getRecurrencePattern().getOfX().getMonth());
                        this.yearlyRecurrenceController.getMonthItemSecondOption().setDisable(false);

                        this.yearlyRecurrenceController.getMonthItemFirstOption().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfMonth().setDisable(true);

                    }
                    break;
            }
        }

        // set regex validation listener that does not permit any character other than numbers
        // and validation that does not permit numbers wih more than 3 digits
        this.dailyRecurrenceController.getNumberOfDays().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    dailyRecurrenceController.getNumberOfDays().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 3) {
                    dailyRecurrenceController.getNumberOfDays().setText(oldValue);
                }
            }
        });

        // set regex validation listener that does not permit any character other than numbers
        // and validation that does not permit numbers wih more than 3 digits
        this.weeklyRecurrenceController.getNumberOfWeeks().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    weeklyRecurrenceController.getNumberOfWeeks().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 3) {
                    weeklyRecurrenceController.getNumberOfWeeks().setText(oldValue);
                }
            }
        });

        // set regex validation listener that does not permit any character other than numbers
        // and validation that does not permit numbers wih more than 2 digits
        this.monthlyRecurrenceController.getDayNumber().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    monthlyRecurrenceController.getDayNumber().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 2) {
                    monthlyRecurrenceController.getDayNumber().setText(oldValue);
                }
            }
        });

        // set regex validation listener that does not permit any character other than numbers
        // and validation that does not permit numbers wih more than 2 digits
        this.monthlyRecurrenceController.getNumberOfMonths().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    monthlyRecurrenceController.getNumberOfMonths().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 2) {
                    monthlyRecurrenceController.getNumberOfMonths().setText(oldValue);
                }
            }
        });

        // set regex validation listener that does not permit any character other than numbers
        // and validation that does not permit numbers wih more than 2 digits
        this.monthlyRecurrenceController.getMonthsUntil().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    monthlyRecurrenceController.getMonthsUntil().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 2) {
                    monthlyRecurrenceController.getMonthsUntil().setText(oldValue);
                }
            }
        });

        // set regex validation listener that does not permit any character other than numbers
        // and validation that does not permit numbers wih more than 2 digits
        this.yearlyRecurrenceController.getNumberOfYears().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    yearlyRecurrenceController.getNumberOfYears().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 2) {
                    yearlyRecurrenceController.getNumberOfYears().setText(oldValue);
                }
            }
        });

        // set regex validation listener that does not permit any character other than numbers
        // and validation that does not permit numbers wih more than 2 digits
        this.yearlyRecurrenceController.getDayOfMonth().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    yearlyRecurrenceController.getDayOfMonth().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 2) {
                    yearlyRecurrenceController.getDayOfMonth().setText(oldValue);
                }
            }
        });

        // set regex validation listener that does not permit any character other than numbers
        // and validation that does not permit numbers wih more than 2 digits
        this.numberOfOccurrences.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numberOfOccurrences.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 3) {
                    numberOfOccurrences.setText(oldValue);
                }
            }
        });
    }

    /**
     * handles the change event for the daily radio button
     *
     * @param actionEvent
     */
    @FXML
    public void onDailyRadioButtonChange(ActionEvent actionEvent) {
        dailyRadioButton.setSelected(true);
        weeklyRadioButton.setSelected(false);
        monthlyRadioButton.setSelected(false);
        yearlyRadioButton.setSelected(false);
        this.recurrenceAnchorPane.getChildren().remove(0);
        this.recurrenceAnchorPane.getChildren().add(dailyRecurrenceAnchorPane);
    }

    @FXML
    /**
     *
     * handles the change event for the weekly radio button
     *
     * @param actionEvent
     */
    public void onWeeklyRadioButtonChange(ActionEvent actionEvent) {
        dailyRadioButton.setSelected(false);
        weeklyRadioButton.setSelected(true);
        monthlyRadioButton.setSelected(false);
        yearlyRadioButton.setSelected(false);
        this.recurrenceAnchorPane.getChildren().remove(0);
        this.recurrenceAnchorPane.getChildren().add(weeklyRecurrenceAnchorPane);
    }

    /**
     * handles the change event for the monthly radio button
     *
     * @param actionEvent
     */
    @FXML
    public void onMonthlyRadioButtonChange(ActionEvent actionEvent) {
        dailyRadioButton.setSelected(false);
        weeklyRadioButton.setSelected(false);
        monthlyRadioButton.setSelected(true);
        yearlyRadioButton.setSelected(false);
        this.recurrenceAnchorPane.getChildren().remove(0);
        this.recurrenceAnchorPane.getChildren().add(monthlyRecurrenceAnchorPane);
    }

    /**
     * handles the change event for the yearly radio button
     *
     * @param actionEvent
     */
    @FXML
    public void onYearlyRadioButtonChange(ActionEvent actionEvent) {
        dailyRadioButton.setSelected(false);
        weeklyRadioButton.setSelected(false);
        monthlyRadioButton.setSelected(false);
        yearlyRadioButton.setSelected(true);
        this.recurrenceAnchorPane.getChildren().remove(0);
        this.recurrenceAnchorPane.getChildren().add(yearlyRecurrenceAnchorPane);
    }

    /**
     * handles the change event for the endBy radio button
     *
     * @param actionEvent
     */
    @FXML
    public void onEndByRadioButtonChange(ActionEvent actionEvent) {
        endByRadioButton.setSelected(true);
        endAfterRadioButton.setSelected(false);
        noEndDateRadioButton.setSelected(false);
        stopDate.setDisable(false);
        numberOfOccurrences.setDisable(true);
    }

    /**
     * handles the change event for the endAfter radio button
     *
     * @param actionEvent
     */
    @FXML
    public void onEndAfterRadioButtonChange(ActionEvent actionEvent) {
        endByRadioButton.setSelected(false);
        endAfterRadioButton.setSelected(true);
        noEndDateRadioButton.setSelected(false);
        stopDate.setDisable(true);
        numberOfOccurrences.setDisable(false);
    }

    /**
     * handles the change event for the noEndDate radio button
     *
     * @param actionEvent
     */
    @FXML
    public void onNoEndDateRadioButtonChange(ActionEvent actionEvent) {
        endByRadioButton.setSelected(false);
        endAfterRadioButton.setSelected(false);
        noEndDateRadioButton.setSelected(true);
        stopDate.setDisable(true);
        numberOfOccurrences.setDisable(true);
    }

    /**
     * saves the recurrence into the database
     * this function is triggered when the user clicks the save button
     *
     * @param actionEvent
     */
    @FXML
    public void saveRecurrence(ActionEvent actionEvent) {

        // get the values from textfields
        LocalTime eventTime = this.eventTime.getValue();
        LocalDate startDate = this.startDate.getValue();

        // validation on the time and date
        if (eventTime == null || startDate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter all required fields");
            alert.showAndWait();
            return;
        }

        // create the reucrrence object
        Recurrence recurrence = Recurrence.builder()
                .setStartTime(eventTime);

        // create the recurrence range object
        RecurrenceRange recurrenceRange = RecurrenceRange.builder()
                .setStartDate(startDate);

        // validate the stopDate for the recurrence
        if (this.endByRadioButton.isSelected()) {
            LocalDate stopDate = this.stopDate.getValue();
            if (stopDate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please  enter the stop date");
                alert.showAndWait();
                return;
            }
            recurrenceRange.setEndByDate(stopDate);
        }
        // validate the numberOfOccurences for the recurrence
        if (this.endAfterRadioButton.isSelected()) {

            if (numberOfOccurrences.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please  enter the number of occurrences");
                alert.showAndWait();
                return;
            }
            Integer numberOfOccurrences = Integer.parseInt(this.numberOfOccurrences.getText());

            recurrenceRange.setEndAfter(numberOfOccurrences);
        }

        // in case the week recurrence pattern is weekly
        // validate that at least one day of the week is checked in the checkboxes
        if (weeklyRadioButton.isSelected()) {
            if (!weeklyRecurrenceController.getMondayCheckbox().isSelected()
                    && !weeklyRecurrenceController.getTuesdayCheckbox().isSelected()
                    && !weeklyRecurrenceController.getTuesdayCheckbox().isSelected()
                    && !weeklyRecurrenceController.getWednesdayCheckbox().isSelected()
                    && !weeklyRecurrenceController.getThursdayCheckbox().isSelected()
                    && !weeklyRecurrenceController.getFridayCheckbox().isSelected()
                    && !weeklyRecurrenceController.getSaturdayCheckbox().isSelected()
                    && !weeklyRecurrenceController.getSundayCheckbox().isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please  select at least one day of the week");
                alert.showAndWait();
                return;
            }
        }

        // set the recurrenceRange noEndDate if there is the case
        if (this.noEndDateRadioButton.isSelected()) {

            recurrenceRange.setNoEndDate(true);
        }

        // handle the case when the user wants to create a daily recurrence pattern
        if (dailyRadioButton.isSelected()) {

            // get the Hbox containing the first radio button
            HBox firstOption = (HBox) dailyRecurrenceAnchorPane.getChildren().get(0);

            // get the radio button from the Hbox
            JFXRadioButton everyRadioButton = (JFXRadioButton) firstOption.getChildren().get(0);

            // handle the case when the first radio button is selected
            if (everyRadioButton.isSelected()) {
                final JFXTextField numberOfDaysField = (JFXTextField) firstOption.getChildren().get(1);

                // validate the numberOfDays
                if (numberOfDaysField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Please  enter the number of days");
                    alert.showAndWait();
                    return;
                } else {
                    Integer numberOfDays = Integer.parseInt(numberOfDaysField.getText());

                    // create the recurrencePattern object
                    RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                            .setRecurrenceFrequency(RecurrencePatternFrequencyEnum.DAILY)
                            .setEveryXDays(numberOfDays);

                    // if the controller is not null
                    // it means that this controller opened the addRecurrenceController
                    // so set the recurrence on it
                    if (addEventController != null) {
                        addEventController.setRecurrence(recurrence);
                        addEventController.setRecurrencePattern(recurrencePattern);
                        addEventController.setRecurrenceRange(recurrenceRange);
                    }
                    if (updateEventController != null) {
                        updateEventController.setRecurrence(recurrence);
                        updateEventController.setRecurrencePattern(recurrencePattern);
                        updateEventController.setRecurrenceRange(recurrenceRange);
                    }
                    if (updateOrDeleteEventController != null) {
                        updateOrDeleteEventController.setRecurrence(recurrence);
                        updateOrDeleteEventController.setRecurrencePattern(recurrencePattern);
                        updateOrDeleteEventController.setRecurrenceRange(recurrenceRange);
                    }

                    // close the current stage
                    Stage stage = (Stage) this.rootPane.getScene().getWindow();
                    stage.close();
                    return;

                }
            }
            // get the Hbox containing the second radio button
            HBox secondOption = (HBox) dailyRecurrenceAnchorPane.getChildren().get(1);

            // get the radio button from the Hbox
            JFXRadioButton everyWeekdayRadioButton = (JFXRadioButton) secondOption.getChildren().get(0);

            // handle the case when the second radio button is selected
            if (everyWeekdayRadioButton.isSelected()) {
                RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                        .setRecurrenceFrequency(RecurrencePatternFrequencyEnum.DAILY)
                        .setEveryWeekday(true);

                // if the controller is not null
                // it means that this controller opened the addRecurrenceController
                // so set the recurrence on it
                if (addEventController != null) {
                    addEventController.setRecurrence(recurrence);
                    addEventController.setRecurrencePattern(recurrencePattern);
                    addEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateEventController != null) {
                    updateEventController.setRecurrence(recurrence);
                    updateEventController.setRecurrencePattern(recurrencePattern);
                    updateEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateOrDeleteEventController != null) {
                    updateOrDeleteEventController.setRecurrence(recurrence);
                    updateOrDeleteEventController.setRecurrencePattern(recurrencePattern);
                    updateOrDeleteEventController.setRecurrenceRange(recurrenceRange);
                }

                // close the current stage
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.close();
                return;
            }
        }
        // handle the case when the user wants to create a weekly recurrence pattern
        if (weeklyRadioButton.isSelected()) {

            // get the Hbox containing the first radio button
            HBox firstOption = (HBox) weeklyRecurrenceAnchorPane.getChildren().get(0);

            JFXTextField numberOfWeeksTextField = (JFXTextField) firstOption.getChildren().get(1);

            // validate the numberOfWeeks field
            if (numberOfWeeksTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please  enter the number of weeks");
                alert.showAndWait();
                return;
            }
            Integer numberOfWeeks = Integer.parseInt(numberOfWeeksTextField.getText());

            // get the Hbox containing the second radio button
            HBox secondOption = (HBox) weeklyRecurrenceAnchorPane.getChildren().get(1);

            // get the values from the checkboxes
            JFXCheckBox monday = (JFXCheckBox) secondOption.getChildren().get(0);
            JFXCheckBox tuesday = (JFXCheckBox) secondOption.getChildren().get(1);
            JFXCheckBox wednesday = (JFXCheckBox) secondOption.getChildren().get(2);
            JFXCheckBox thursday = (JFXCheckBox) secondOption.getChildren().get(3);
            JFXCheckBox friday = (JFXCheckBox) secondOption.getChildren().get(4);

            // get the Hbox containing the third radio button
            HBox thirdOption = (HBox) weeklyRecurrenceAnchorPane.getChildren().get(2);

            // get the values from the checkboxes
            JFXCheckBox saturday = (JFXCheckBox) thirdOption.getChildren().get(0);
            JFXCheckBox sunday = (JFXCheckBox) thirdOption.getChildren().get(1);

            // create the recurrencePattern object
            RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                    .setRecurrenceFrequency(RecurrencePatternFrequencyEnum.WEEKLY)
                    .setEveryXWeeks(numberOfWeeks)
                    .setMonday(monday.isSelected())
                    .setTuesday(tuesday.isSelected())
                    .setWednesday(wednesday.isSelected())
                    .setThursday(thursday.isSelected())
                    .setFriday(friday.isSelected())
                    .setSaturday(saturday.isSelected())
                    .setSunday(sunday.isSelected());

            // if the controller is not null
            // it means that this controller opened the addRecurrenceController
            // so set the recurrence on it
            if (addEventController != null) {
                addEventController.setRecurrence(recurrence);
                addEventController.setRecurrencePattern(recurrencePattern);
                addEventController.setRecurrenceRange(recurrenceRange);
            }
            if (updateEventController != null) {
                updateEventController.setRecurrence(recurrence);
                updateEventController.setRecurrencePattern(recurrencePattern);
                updateEventController.setRecurrenceRange(recurrenceRange);
            }
            if (updateOrDeleteEventController != null) {
                updateOrDeleteEventController.setRecurrence(recurrence);
                updateOrDeleteEventController.setRecurrencePattern(recurrencePattern);
                updateOrDeleteEventController.setRecurrenceRange(recurrenceRange);
            }

            // close the current stage
            Stage stage = (Stage) this.rootPane.getScene().getWindow();
            stage.close();
            return;

        }

        // handle the case when the user wants to create a monthly recurrence pattern
        if (monthlyRadioButton.isSelected()) {

            // get the Hbox containing the first radio button
            HBox firstOption = (HBox) monthlyRecurrenceAnchorPane.getChildren().get(0);

            // get the radio button from the Hbox
            JFXRadioButton dayNumberRadioButton = (JFXRadioButton) firstOption.getChildren().get(0);

            // handle the case when the first radio button is selected
            if (dayNumberRadioButton.isSelected()) {
                JFXTextField dayNumberTextField = (JFXTextField) firstOption.getChildren().get(1);

                // validate the dayNumber value
                if (dayNumberTextField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Please  enter the day number");
                    alert.showAndWait();
                    return;
                }

                JFXTextField monthsUntilTextField = (JFXTextField) firstOption.getChildren().get(3);

                // validate the monthUntil value
                if (monthsUntilTextField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Please  enter the month number");
                    alert.showAndWait();
                    return;
                }

                Integer dayNumber = Integer.parseInt(dayNumberTextField.getText());
                Integer monthsUntil = Integer.parseInt(monthsUntilTextField.getText());

                // validate that the day of the month is not bigger than 31
                if (dayNumber > 31) {
                    dayNumber = 31;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("The day of month is bigger than 31. The last day of month will be considered");
                    alert.showAndWait();
                }

                // create the recurrence pattern object
                RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                        .setRecurrenceFrequency(RecurrencePatternFrequencyEnum.MONTHLY)
                        .setxDay(dayNumber)
                        .setOfEveryXMonthsFirstOption(monthsUntil);

                // if the controller is not null
                // it means that this controller opened the addRecurrenceController
                // so set the recurrence on it
                if (addEventController != null) {
                    addEventController.setRecurrence(recurrence);
                    addEventController.setRecurrencePattern(recurrencePattern);
                    addEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateEventController != null) {
                    updateEventController.setRecurrence(recurrence);
                    updateEventController.setRecurrencePattern(recurrencePattern);
                    updateEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateOrDeleteEventController != null) {
                    updateOrDeleteEventController.setRecurrence(recurrence);
                    updateOrDeleteEventController.setRecurrencePattern(recurrencePattern);
                    updateOrDeleteEventController.setRecurrenceRange(recurrenceRange);
                }

                // close the current stage
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.close();
                return;
            }

            // get the Hbox containing the second radio button
            HBox secondOption = (HBox) monthlyRecurrenceAnchorPane.getChildren().get(1);

            // get the radio button from the Hbox
            JFXRadioButton dayOfWeekRadioButton = (JFXRadioButton) secondOption.getChildren().get(0);

            // handle the case when the first radio button is selected
            if (dayOfWeekRadioButton.isSelected()) {
                // get the values from comboboxes
                JFXComboBox dayOfWeekNumberItemCombobox = (JFXComboBox) secondOption.getChildren().get(1);
                final String weekNumber = (String) dayOfWeekNumberItemCombobox.getSelectionModel().getSelectedItem();
                final WeekOrdinalEnum weekOrdinalEnum = WeekOrdinalEnum.parse(weekNumber);

                JFXComboBox dayOfWeekItemCombobox = (JFXComboBox) secondOption.getChildren().get(2);
                final String dayOfWeek = (String) dayOfWeekItemCombobox.getSelectionModel().getSelectedItem();
                final DayOfWeekEnum dayOfWeekEnum = DayOfWeekEnum.parse(dayOfWeek);

                JFXTextField numberOfMonthsTextField = (JFXTextField) secondOption.getChildren().get(4);

                // validate the numberOfmonths
                if (numberOfMonthsTextField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Please  enter the number of months");
                    alert.showAndWait();
                    return;
                }
                Integer numberOfMonths = Integer.parseInt(numberOfMonthsTextField.getText());

                // create the recurrencePattern object
                RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                        .setRecurrenceFrequency(RecurrencePatternFrequencyEnum.MONTHLY)
                        .setTheXMonthly(weekOrdinalEnum)
                        .setDayOfWeekMonthly(dayOfWeekEnum)
                        .setOfEveryXMonthsSecondOption(numberOfMonths);

                // if the controller is not null
                // it means that this controller opened the addRecurrenceController
                // so set the recurrence on it
                if (addEventController != null) {
                    addEventController.setRecurrence(recurrence);
                    addEventController.setRecurrencePattern(recurrencePattern);
                    addEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateEventController != null) {
                    updateEventController.setRecurrence(recurrence);
                    updateEventController.setRecurrencePattern(recurrencePattern);
                    updateEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateOrDeleteEventController != null) {
                    updateOrDeleteEventController.setRecurrence(recurrence);
                    updateOrDeleteEventController.setRecurrencePattern(recurrencePattern);
                    updateOrDeleteEventController.setRecurrenceRange(recurrenceRange);
                }

                // close the current stage
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.close();
                return;
            }

        }

        // handle the case when the user wants to create a yearly recurrence pattern
        if (yearlyRadioButton.isSelected()) {

            // get the Hbox containing the first radio button
            HBox firstOption = (HBox) yearlyRecurrenceAnchorPane.getChildren().get(0);

            JFXTextField numberOfYearsTextField = (JFXTextField) firstOption.getChildren().get(1);

            // validate the numberOfYears value
            if (numberOfYearsTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please  enter the number of weeks");
                alert.showAndWait();
                return;
            }
            Integer numberOfYears = Integer.parseInt(numberOfYearsTextField.getText());

            // get the Hbox containing the first radio button
            HBox secondOption = (HBox) yearlyRecurrenceAnchorPane.getChildren().get(1);

            // get the radio button from the Hbox
            JFXRadioButton dayRadioButton = (JFXRadioButton) secondOption.getChildren().get(0);

            // handle the case when the second radio button is selected
            if (dayRadioButton.isSelected()) {
                // get the value from the combobox
                JFXComboBox monthItemFirstOptionComboBox = (JFXComboBox) secondOption.getChildren().get(1);
                final String monthItem = (String) monthItemFirstOptionComboBox.getSelectionModel().getSelectedItem();
                final MonthEnum monthEnum = MonthEnum.parse(monthItem);

                JFXTextField dayOfMonthTextField = (JFXTextField) secondOption.getChildren().get(2);

                // validate the dayOfMonthValue
                if (dayOfMonthTextField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Please  enter the day of month");
                    alert.showAndWait();
                    return;
                }
                Integer dayOfMonth = Integer.parseInt(dayOfMonthTextField.getText());

                // validate that the day of the month is not bigger than 31
                if (dayOfMonth > 31) {
                    dayOfMonth = 31;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("The day of month is bigger than 31. The last day of month will be considered");
                    alert.showAndWait();
                }

                // create the recurrencePattern object
                RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                        .setRecurrenceFrequency(RecurrencePatternFrequencyEnum.YEARLY)
                        .setEveryXYears(numberOfYears)
                        .setMonth(monthEnum)
                        .setX(dayOfMonth);

                // if the controller is not null
                // it means that this controller opened the addRecurrenceController
                // so set the recurrence on it
                if (addEventController != null) {
                    addEventController.setRecurrence(recurrence);
                    addEventController.setRecurrencePattern(recurrencePattern);
                    addEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateEventController != null) {
                    updateEventController.setRecurrence(recurrence);
                    updateEventController.setRecurrencePattern(recurrencePattern);
                    updateEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateOrDeleteEventController != null) {
                    updateOrDeleteEventController.setRecurrence(recurrence);
                    updateOrDeleteEventController.setRecurrencePattern(recurrencePattern);
                    updateOrDeleteEventController.setRecurrenceRange(recurrenceRange);
                }

                // close the current stage
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.close();
                return;
            }

            // get the Hbox containing the third radio button
            HBox thirdOption = (HBox) yearlyRecurrenceAnchorPane.getChildren().get(2);

            JFXRadioButton dayReferenceRadioButton = (JFXRadioButton) thirdOption.getChildren().get(0);

            // handle the case when the third radio button is selected
            if (dayReferenceRadioButton.isSelected()) {

                // get the values from comboboxes
                ComboBox weekOrdinalComboBox = (ComboBox) thirdOption.getChildren().get(1);
                final String weekOrdinal = (String) weekOrdinalComboBox.getSelectionModel().getSelectedItem();
                final WeekOrdinalEnum weekOrdinalEnum = WeekOrdinalEnum.parse(weekOrdinal);

                ComboBox dayOfWeekComboBox = (ComboBox) thirdOption.getChildren().get(2);
                final String dayOfWeek = (String) dayOfWeekComboBox.getSelectionModel().getSelectedItem();
                final DayOfWeekEnum dayOfWeekEnum = DayOfWeekEnum.parse(dayOfWeek);

                ComboBox monthComboBox = (ComboBox) thirdOption.getChildren().get(4);
                final String month = (String) monthComboBox.getSelectionModel().getSelectedItem();
                final MonthEnum monthEnum = MonthEnum.parse(month);

                // create the recurrencePattern
                RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                        .setRecurrenceFrequency(RecurrencePatternFrequencyEnum.YEARLY)
                        .setEveryXYears(numberOfYears)
                        .setTheXYearly(weekOrdinalEnum)
                        .setDayOfWeekYearly(dayOfWeekEnum)
                        .setOfX(monthEnum);

                // if the controller is not null
                // it means that this controller opened the addRecurrenceController
                // so set the recurrence on it
                if (addEventController != null) {
                    addEventController.setRecurrence(recurrence);
                    addEventController.setRecurrencePattern(recurrencePattern);
                    addEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateEventController != null) {
                    updateEventController.setRecurrence(recurrence);
                    updateEventController.setRecurrencePattern(recurrencePattern);
                    updateEventController.setRecurrenceRange(recurrenceRange);
                }
                if (updateOrDeleteEventController != null) {
                    updateOrDeleteEventController.setRecurrence(recurrence);
                    updateOrDeleteEventController.setRecurrencePattern(recurrencePattern);
                    updateOrDeleteEventController.setRecurrenceRange(recurrenceRange);
                }

                // close the current stage
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * closes the AddRecurrence stage
     *
     * @param actionEvent
     */
    @FXML
    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * removes the recurrence from the suitable controller
     *
     * @param actionEvent
     */
    @FXML
    public void removeRecurrence(ActionEvent actionEvent) {

        if (addEventController != null) {
            addEventController.setRecurrence(null);
            addEventController.setRecurrencePattern(null);
            addEventController.setRecurrenceRange(null);
        }
        if (updateEventController != null) {
            updateEventController.setRecurrence(null);
            updateEventController.setRecurrencePattern(null);
            updateEventController.setRecurrenceRange(null);
        }
        if (updateOrDeleteEventController != null) {
            updateOrDeleteEventController.setRecurrence(null);
            updateOrDeleteEventController.setRecurrencePattern(null);
            updateOrDeleteEventController.setRecurrenceRange(null);
        }
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }
}
