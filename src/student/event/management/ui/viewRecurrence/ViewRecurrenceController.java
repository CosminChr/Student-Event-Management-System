package student.event.management.ui.viewRecurrence;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import student.event.management.domain.models.Recurrence;
import student.event.management.domain.models.RecurrencePattern;
import student.event.management.domain.models.RecurrenceRange;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewRecurrenceController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTimePicker eventTime;
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
    private JFXDatePicker startDate;
    @FXML
    private JFXRadioButton endByRadioButton;
    @FXML
    private JFXDatePicker stopDate;
    @FXML
    private JFXRadioButton endAfterRadioButton;
    @FXML
    private JFXTextField numberOfOccurrences;
    @FXML
    private JFXRadioButton noEndDateRadioButton;

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


    public ViewRecurrenceController(DeleteEventController deleteEventController) {
        this.deleteEventController = deleteEventController;
        this.recurrenceRepository = new RecurrenceRepository();
        this.recurrencePatternRepository = new RecurrencePatternRepository();
        this.recurrenceRangeRepository = new RecurrenceRangeRepository();
    }

    public ViewRecurrenceController(BookEventController bookEventController) {
        this.bookEventController = bookEventController;
        this.recurrenceRepository = new RecurrenceRepository();
        this.recurrencePatternRepository = new RecurrencePatternRepository();
        this.recurrenceRangeRepository = new RecurrenceRangeRepository();
    }

    public ViewRecurrenceController(CancelBookingController cancelBookingController) {
        this.cancelBookingController = cancelBookingController;
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

        // load the dailyRecurrence controller
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

        // load the weeklyRecurrence controller
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

        // load the monthlyRecurrence controller
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

        // load the yearlyRecurrence controller
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

        try {
            // if the focus comes from the deleteEventController
            if (deleteEventController != null) {
                final Optional<RecurrenceRange> recurrenceRangeByEventTitleOptional = recurrenceRangeRepository.findByEventTitle(deleteEventController.getEvent().getTitle());
                final Optional<RecurrencePattern> reccurencePatternByEventTitleOptional = recurrencePatternRepository.findByEventTitle(deleteEventController.getEvent().getTitle());
                final Optional<Recurrence> recurrenceByEventTitleOptional = recurrenceRepository.findByEventTitle(deleteEventController.getEvent().getTitle());

                RecurrenceRange recurrenceRange = recurrenceRangeByEventTitleOptional.get();
                RecurrencePattern recurrencePattern = reccurencePatternByEventTitleOptional.get();
                Recurrence recurrence = recurrenceByEventTitleOptional.get();

                // set initial values in the controller depending on the recurrence data and disable the controls
                switch (reccurencePatternByEventTitleOptional.get().getRecurrenceFrequency()) {
                    case DAILY:
                        dailyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(dailyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(dailyRecurrenceAnchorPane);
                        this.dailyRecurrenceController.getEveryRadioButton().setDisable(true);
                        this.dailyRecurrenceController.getEveryWeekdayRadioButton().setDisable(true);
                        this.dailyRecurrenceController.getNumberOfDays().setDisable(true);
                        if (recurrencePattern.getEveryXDays() != 0) {
                            this.dailyRecurrenceController.getNumberOfDays().setText(recurrencePattern.getEveryXDays().toString());
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(false);
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setDisable(true);
                        } else {
                            this.dailyRecurrenceController.getEveryRadioButton().setSelected(false);
                            this.dailyRecurrenceController.getNumberOfDays().setText("");
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(true);
                        }
                        break;
                    case WEEKLY:
                        weeklyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(weeklyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(weeklyRecurrenceAnchorPane);
                        this.weeklyRecurrenceController.getNumberOfWeeks().setText(recurrencePattern.getEveryXWeeks().toString());
                        this.weeklyRecurrenceController.getNumberOfWeeks().setDisable(true);
                        this.weeklyRecurrenceController.getMondayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getTuesdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getWednesdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getThursdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getFridayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getSaturdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getSundayCheckbox().setDisable(true);
                        if (recurrencePattern.getMonday()) {
                            this.weeklyRecurrenceController.getMondayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getTuesday()) {
                            this.weeklyRecurrenceController.getTuesdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getWednesday()) {
                            this.weeklyRecurrenceController.getWednesdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getThursday()) {
                            this.weeklyRecurrenceController.getThursdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getFriday()) {
                            this.weeklyRecurrenceController.getFridayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getSaturday()) {
                            this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(true);
                        } else {
                            this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(false);
                        }
                        if (recurrencePattern.getSunday()) {
                            this.weeklyRecurrenceController.getSundayCheckbox().setSelected(true);
                        }
                        break;
                    case MONTHLY:
                        monthlyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);
                        this.monthlyRecurrenceController.getMonthsUntil().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekItem().setDisable(true);
                        this.monthlyRecurrenceController.getNumberOfMonths().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekRadioButton().setDisable(true);
                        this.monthlyRecurrenceController.getDayNumberRadioButton().setDisable(true);
                        this.rootPane.getChildren().add(monthlyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(monthlyRecurrenceAnchorPane);
                        if (recurrencePattern.getXDay() != 0) {
                            this.monthlyRecurrenceController.getDayNumber().setText(recurrencePattern.getXDay().toString());
                            this.monthlyRecurrenceController.getMonthsUntil().setText(recurrencePattern.getOfEveryXMonthsFirstOption().toString());
                        }
                        if (recurrencePattern.getTheXMonthly() != null) {
                            this.monthlyRecurrenceController.getDayOfWeekNumberItem().getSelectionModel().select(recurrencePattern.getTheXMonthly().getOrdinal());
                            this.monthlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(recurrencePattern.getDayOfWeekMonthly().getLabel());
                            this.monthlyRecurrenceController.getNumberOfMonths().setText(recurrencePattern.getOfEveryXMonthsSecondOption().toString());
                            this.monthlyRecurrenceController.getDayNumberRadioButton().setSelected(false);
                            this.monthlyRecurrenceController.getDayOfWeekRadioButton().setSelected(true);
                        }
                        break;
                    case YEARLY:
                        yearlyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(yearlyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(yearlyRecurrenceAnchorPane);
                        this.yearlyRecurrenceController.getNumberOfYears().setText(recurrencePattern.getEveryXYears().toString());
                        this.yearlyRecurrenceController.getNumberOfYears().setDisable(true);
                        this.yearlyRecurrenceController.getDayRadioButton().setDisable(true);
                        this.yearlyRecurrenceController.getDayReferenceRadioButton().setDisable(true);
                        this.yearlyRecurrenceController.getMonthItemFirstOption().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfMonth().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfWeekItem().setDisable(true);
                        this.yearlyRecurrenceController.getMonthItemSecondOption().setDisable(true);

                        if (recurrencePattern.getMonth() != null) {
                            this.yearlyRecurrenceController.getMonthItemFirstOption().getSelectionModel().select(recurrencePattern.getMonth().getMonth());
                            this.yearlyRecurrenceController.getDayOfMonth().setText(recurrencePattern.getX().toString());
                        }
                        if (recurrencePattern.getTheXYearly() != null) {
                            this.yearlyRecurrenceController.getDayRadioButton().setSelected(false);
                            this.yearlyRecurrenceController.getDayReferenceRadioButton().setSelected(true);
                            this.yearlyRecurrenceController.getDayOfWeekItemNumber().getSelectionModel().select(recurrencePattern.getTheXYearly().getOrdinal());
                            this.yearlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(recurrencePattern.getDayOfWeekYearly().getLabel());
                            this.yearlyRecurrenceController.getMonthItemSecondOption().getSelectionModel().select(recurrencePattern.getOfX().getMonth());
                        }
                        break;
                    default:
                        break;
                }

                this.eventTime.setValue(recurrence.getStartTime());
                this.eventTime.setDisable(true);

                this.startDate.setValue(recurrenceRange.getStartDate());
                this.startDate.setDisable(true);

                this.endByRadioButton.setDisable(true);
                this.stopDate.setDisable(true);

                this.endAfterRadioButton.setDisable(true);
                this.numberOfOccurrences.setDisable(true);

                this.noEndDateRadioButton.setDisable(true);

                if (recurrenceRange.getEndByDate() != null) {
                    this.endByRadioButton.setSelected(true);
                    this.stopDate.setValue(recurrenceRange.getEndByDate());
                }

                if (recurrenceRange.getEndAfter() != 0) {
                    this.endAfterRadioButton.setSelected(true);
                    this.numberOfOccurrences.setText(recurrenceRange.getEndAfter().toString());
                }

                if (recurrenceRange.getNoEndDate() == true) {
                    this.noEndDateRadioButton.setSelected(true);
                }


            }
            // if the focus comes from the cancelBookingController
            if (cancelBookingController != null) {
                final Optional<RecurrenceRange> recurrenceRangeByEventTitleOptional = recurrenceRangeRepository.findByEventTitle(cancelBookingController.getEvent().getTitle());
                final Optional<RecurrencePattern> reccurencePatternByEventTitleOptional = recurrencePatternRepository.findByEventTitle(cancelBookingController.getEvent().getTitle());
                final Optional<Recurrence> recurrenceByEventTitleOptional = recurrenceRepository.findByEventTitle(cancelBookingController.getEvent().getTitle());

                RecurrenceRange recurrenceRange = recurrenceRangeByEventTitleOptional.get();
                RecurrencePattern recurrencePattern = reccurencePatternByEventTitleOptional.get();
                Recurrence recurrence = recurrenceByEventTitleOptional.get();

                // set initial values in the controller depending on the recurrence data and disable the controls
                switch (reccurencePatternByEventTitleOptional.get().getRecurrenceFrequency()) {
                    case DAILY:
                        dailyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(dailyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(dailyRecurrenceAnchorPane);
                        this.dailyRecurrenceController.getEveryRadioButton().setDisable(true);
                        this.dailyRecurrenceController.getEveryWeekdayRadioButton().setDisable(true);
                        this.dailyRecurrenceController.getNumberOfDays().setDisable(true);
                        if (recurrencePattern.getEveryXDays() != 0) {
                            this.dailyRecurrenceController.getNumberOfDays().setText(recurrencePattern.getEveryXDays().toString());
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(false);
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setDisable(true);
                        } else {
                            this.dailyRecurrenceController.getEveryRadioButton().setSelected(false);
                            this.dailyRecurrenceController.getNumberOfDays().setText("");
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(true);
                        }
                        break;
                    case WEEKLY:
                        weeklyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(weeklyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(weeklyRecurrenceAnchorPane);
                        this.weeklyRecurrenceController.getNumberOfWeeks().setText(recurrencePattern.getEveryXWeeks().toString());
                        this.weeklyRecurrenceController.getNumberOfWeeks().setDisable(true);
                        this.weeklyRecurrenceController.getMondayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getTuesdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getWednesdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getThursdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getFridayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getSaturdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getSundayCheckbox().setDisable(true);
                        if (recurrencePattern.getMonday()) {
                            this.weeklyRecurrenceController.getMondayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getTuesday()) {
                            this.weeklyRecurrenceController.getTuesdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getWednesday()) {
                            this.weeklyRecurrenceController.getWednesdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getThursday()) {
                            this.weeklyRecurrenceController.getThursdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getFriday()) {
                            this.weeklyRecurrenceController.getFridayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getSaturday()) {
                            this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(true);
                        } else {
                            this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(false);
                        }
                        if (recurrencePattern.getSunday()) {
                            this.weeklyRecurrenceController.getSundayCheckbox().setSelected(true);
                        }
                        break;
                    case MONTHLY:
                        monthlyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);
                        this.monthlyRecurrenceController.getMonthsUntil().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekItem().setDisable(true);
                        this.monthlyRecurrenceController.getNumberOfMonths().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekRadioButton().setDisable(true);
                        this.monthlyRecurrenceController.getDayNumberRadioButton().setDisable(true);
                        this.rootPane.getChildren().add(monthlyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(monthlyRecurrenceAnchorPane);
                        if (recurrencePattern.getXDay() != 0) {
                            this.monthlyRecurrenceController.getDayNumber().setText(recurrencePattern.getXDay().toString());
                            this.monthlyRecurrenceController.getMonthsUntil().setText(recurrencePattern.getOfEveryXMonthsFirstOption().toString());
                        }
                        if (recurrencePattern.getTheXMonthly() != null) {
                            this.monthlyRecurrenceController.getDayOfWeekNumberItem().getSelectionModel().select(recurrencePattern.getTheXMonthly().getOrdinal());
                            this.monthlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(recurrencePattern.getDayOfWeekMonthly().getLabel());
                            this.monthlyRecurrenceController.getNumberOfMonths().setText(recurrencePattern.getOfEveryXMonthsSecondOption().toString());
                            this.monthlyRecurrenceController.getDayNumberRadioButton().setSelected(false);
                            this.monthlyRecurrenceController.getDayOfWeekRadioButton().setSelected(true);
                        }
                        break;
                    case YEARLY:
                        yearlyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(yearlyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(yearlyRecurrenceAnchorPane);
                        this.yearlyRecurrenceController.getNumberOfYears().setText(recurrencePattern.getEveryXYears().toString());
                        this.yearlyRecurrenceController.getNumberOfYears().setDisable(true);
                        this.yearlyRecurrenceController.getDayRadioButton().setDisable(true);
                        this.yearlyRecurrenceController.getDayReferenceRadioButton().setDisable(true);
                        this.yearlyRecurrenceController.getMonthItemFirstOption().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfMonth().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfWeekItem().setDisable(true);
                        this.yearlyRecurrenceController.getMonthItemSecondOption().setDisable(true);

                        if (recurrencePattern.getMonth() != null) {
                            this.yearlyRecurrenceController.getMonthItemFirstOption().getSelectionModel().select(recurrencePattern.getMonth().getMonth());
                            this.yearlyRecurrenceController.getDayOfMonth().setText(recurrencePattern.getX().toString());
                        }
                        if (recurrencePattern.getTheXYearly() != null) {
                            this.yearlyRecurrenceController.getDayRadioButton().setSelected(false);
                            this.yearlyRecurrenceController.getDayReferenceRadioButton().setSelected(true);
                            this.yearlyRecurrenceController.getDayOfWeekItemNumber().getSelectionModel().select(recurrencePattern.getTheXYearly().getOrdinal());
                            this.yearlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(recurrencePattern.getDayOfWeekYearly().getLabel());
                            this.yearlyRecurrenceController.getMonthItemSecondOption().getSelectionModel().select(recurrencePattern.getOfX().getMonth());
                        }
                        break;
                    default:
                        break;
                }

                this.eventTime.setValue(recurrence.getStartTime());
                this.eventTime.setDisable(true);

                this.startDate.setValue(recurrenceRange.getStartDate());
                this.startDate.setDisable(true);

                this.endByRadioButton.setDisable(true);
                this.stopDate.setDisable(true);

                this.endAfterRadioButton.setDisable(true);
                this.numberOfOccurrences.setDisable(true);

                this.noEndDateRadioButton.setDisable(true);

                if (recurrenceRange.getEndByDate() != null) {
                    this.endByRadioButton.setSelected(true);
                    this.stopDate.setValue(recurrenceRange.getEndByDate());
                }

                if (recurrenceRange.getEndAfter() != 0) {
                    this.endAfterRadioButton.setSelected(true);
                    this.numberOfOccurrences.setText(recurrenceRange.getEndAfter().toString());
                }

                if (recurrenceRange.getNoEndDate() == true) {
                    this.noEndDateRadioButton.setSelected(true);
                }
            }
            // if the focus comes from the bookEventController
            if (bookEventController != null) {
                final Optional<RecurrenceRange> recurrenceRangeByEventTitleOptional = recurrenceRangeRepository.findByEventTitle(bookEventController.getEvent().getTitle());
                final Optional<RecurrencePattern> reccurencePatternByEventTitleOptional = recurrencePatternRepository.findByEventTitle(bookEventController.getEvent().getTitle());
                final Optional<Recurrence> recurrenceByEventTitleOptional = recurrenceRepository.findByEventTitle(bookEventController.getEvent().getTitle());

                RecurrenceRange recurrenceRange = recurrenceRangeByEventTitleOptional.get();
                RecurrencePattern recurrencePattern = reccurencePatternByEventTitleOptional.get();
                Recurrence recurrence = recurrenceByEventTitleOptional.get();

                // set initial values in the controller depending on the recurrence data and disable the controls
                switch (reccurencePatternByEventTitleOptional.get().getRecurrenceFrequency()) {
                    case DAILY:
                        dailyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(dailyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(dailyRecurrenceAnchorPane);
                        this.dailyRecurrenceController.getEveryRadioButton().setDisable(true);
                        this.dailyRecurrenceController.getEveryWeekdayRadioButton().setDisable(true);
                        this.dailyRecurrenceController.getNumberOfDays().setDisable(true);
                        if (recurrencePattern.getEveryXDays() != 0) {
                            this.dailyRecurrenceController.getNumberOfDays().setText(recurrencePattern.getEveryXDays().toString());
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(false);
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setDisable(true);
                        } else {
                            this.dailyRecurrenceController.getEveryRadioButton().setSelected(false);
                            this.dailyRecurrenceController.getNumberOfDays().setText("");
                            this.dailyRecurrenceController.getEveryWeekdayRadioButton().setSelected(true);
                        }
                        break;
                    case WEEKLY:
                        weeklyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(weeklyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(weeklyRecurrenceAnchorPane);
                        this.weeklyRecurrenceController.getNumberOfWeeks().setText(recurrencePattern.getEveryXWeeks().toString());
                        this.weeklyRecurrenceController.getNumberOfWeeks().setDisable(true);
                        this.weeklyRecurrenceController.getMondayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getTuesdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getWednesdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getThursdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getFridayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getSaturdayCheckbox().setDisable(true);
                        this.weeklyRecurrenceController.getSundayCheckbox().setDisable(true);
                        if (recurrencePattern.getMonday()) {
                            this.weeklyRecurrenceController.getMondayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getTuesday()) {
                            this.weeklyRecurrenceController.getTuesdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getWednesday()) {
                            this.weeklyRecurrenceController.getWednesdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getThursday()) {
                            this.weeklyRecurrenceController.getThursdayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getFriday()) {
                            this.weeklyRecurrenceController.getFridayCheckbox().setSelected(true);
                        }
                        if (recurrencePattern.getSaturday()) {
                            this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(true);
                        } else {
                            this.weeklyRecurrenceController.getSaturdayCheckbox().setSelected(false);
                        }
                        if (recurrencePattern.getSunday()) {
                            this.weeklyRecurrenceController.getSundayCheckbox().setSelected(true);
                        }
                        break;
                    case MONTHLY:
                        monthlyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.monthlyRecurrenceController.getDayNumber().setDisable(true);
                        this.monthlyRecurrenceController.getMonthsUntil().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekNumberItem().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekItem().setDisable(true);
                        this.monthlyRecurrenceController.getNumberOfMonths().setDisable(true);
                        this.monthlyRecurrenceController.getDayOfWeekRadioButton().setDisable(true);
                        this.monthlyRecurrenceController.getDayNumberRadioButton().setDisable(true);
                        this.rootPane.getChildren().add(monthlyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(monthlyRecurrenceAnchorPane);
                        if (recurrencePattern.getXDay() != 0) {
                            this.monthlyRecurrenceController.getDayNumber().setText(recurrencePattern.getXDay().toString());
                            this.monthlyRecurrenceController.getMonthsUntil().setText(recurrencePattern.getOfEveryXMonthsFirstOption().toString());
                        }
                        if (recurrencePattern.getTheXMonthly() != null) {
                            this.monthlyRecurrenceController.getDayOfWeekNumberItem().getSelectionModel().select(recurrencePattern.getTheXMonthly().getOrdinal());
                            this.monthlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(recurrencePattern.getDayOfWeekMonthly().getLabel());
                            this.monthlyRecurrenceController.getNumberOfMonths().setText(recurrencePattern.getOfEveryXMonthsSecondOption().toString());
                            this.monthlyRecurrenceController.getDayNumberRadioButton().setSelected(false);
                            this.monthlyRecurrenceController.getDayOfWeekRadioButton().setSelected(true);
                        }
                        break;
                    case YEARLY:
                        yearlyRadioButton.setSelected(true);
                        dailyRadioButton.setDisable(true);
                        weeklyRadioButton.setDisable(true);
                        monthlyRadioButton.setDisable(true);
                        yearlyRadioButton.setDisable(true);
                        this.rootPane.getChildren().add(yearlyRecurrenceAnchorPane);
                        this.recurrenceAnchorPane.getChildren().add(yearlyRecurrenceAnchorPane);
                        this.yearlyRecurrenceController.getNumberOfYears().setText(recurrencePattern.getEveryXYears().toString());
                        this.yearlyRecurrenceController.getNumberOfYears().setDisable(true);
                        this.yearlyRecurrenceController.getDayRadioButton().setDisable(true);
                        this.yearlyRecurrenceController.getDayReferenceRadioButton().setDisable(true);
                        this.yearlyRecurrenceController.getMonthItemFirstOption().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfMonth().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfWeekItemNumber().setDisable(true);
                        this.yearlyRecurrenceController.getDayOfWeekItem().setDisable(true);
                        this.yearlyRecurrenceController.getMonthItemSecondOption().setDisable(true);

                        if (recurrencePattern.getMonth() != null) {
                            this.yearlyRecurrenceController.getMonthItemFirstOption().getSelectionModel().select(recurrencePattern.getMonth().getMonth());
                            this.yearlyRecurrenceController.getDayOfMonth().setText(recurrencePattern.getX().toString());
                        }
                        if (recurrencePattern.getTheXYearly() != null) {
                            this.yearlyRecurrenceController.getDayRadioButton().setSelected(false);
                            this.yearlyRecurrenceController.getDayReferenceRadioButton().setSelected(true);
                            this.yearlyRecurrenceController.getDayOfWeekItemNumber().getSelectionModel().select(recurrencePattern.getTheXYearly().getOrdinal());
                            this.yearlyRecurrenceController.getDayOfWeekItem().getSelectionModel().select(recurrencePattern.getDayOfWeekYearly().getLabel());
                            this.yearlyRecurrenceController.getMonthItemSecondOption().getSelectionModel().select(recurrencePattern.getOfX().getMonth());
                        }
                        break;
                    default:
                        break;
                }

                this.eventTime.setValue(recurrence.getStartTime());
                this.eventTime.setDisable(true);

                this.startDate.setValue(recurrenceRange.getStartDate());
                this.startDate.setDisable(true);

                this.endByRadioButton.setDisable(true);
                this.stopDate.setDisable(true);

                this.endAfterRadioButton.setDisable(true);
                this.numberOfOccurrences.setDisable(true);

                this.noEndDateRadioButton.setDisable(true);

                if (recurrenceRange.getEndByDate() != null) {
                    this.endByRadioButton.setSelected(true);
                    this.stopDate.setValue(recurrenceRange.getEndByDate());
                }

                if (recurrenceRange.getEndAfter() != 0) {
                    this.endAfterRadioButton.setSelected(true);
                    this.numberOfOccurrences.setText(recurrenceRange.getEndAfter().toString());
                }

                if (recurrenceRange.getNoEndDate() == true) {
                    this.noEndDateRadioButton.setSelected(true);
                }
            }

        } catch (SQLException e) {

        }
    }
}
