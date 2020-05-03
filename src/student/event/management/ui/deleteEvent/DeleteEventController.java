package student.event.management.ui.deleteEvent;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import student.event.management.application.service.StageService;
import student.event.management.domain.models.Event;
import student.event.management.domain.models.Recurrence;
import student.event.management.domain.models.RecurrencePattern;
import student.event.management.domain.models.RecurrenceRange;
import student.event.management.domain.util.EventPlace;
import student.event.management.domain.util.EventType;
import student.event.management.infrastructure.repository.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeleteEventController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextArea description;
    @FXML
    private JFXComboBox eventType;
    @FXML
    private JFXComboBox eventPlace;
    @FXML
    private JFXTextField url;
    @FXML
    private JFXTextField organisation;
    @FXML
    private JFXTextField location;
    @FXML
    private JFXButton viewRecurrenceButton;
    @FXML
    private JFXDatePicker eventDate;
    @FXML
    private JFXTimePicker eventTime;
    @FXML
    private JFXRadioButton requiresBooking;
    @FXML
    private JFXRadioButton doesNotRequireBooking;
    @FXML
    private JFXTextField numberOfPlaces;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXButton cancelButton;

    private Event event;

    private EventRepository eventRepository;

    private BookingRepository bookingRepository;

    private RecurrenceRepository recurrenceRepository;

    private RecurrenceRangeRepository recurrenceRangeRepository;

    private RecurrencePatternRepository recurrencePatternRepository;

    private StageService stageService;

    public DeleteEventController(Event event) {
        this.event = event;
        eventRepository = new EventRepository();
        bookingRepository = new BookingRepository();
        this.recurrenceRepository = new RecurrenceRepository();
        this.recurrenceRangeRepository = new RecurrenceRangeRepository();
        this.recurrencePatternRepository = new RecurrencePatternRepository();
        this.stageService = new StageService();
    }

    /**
     * initializes the data in the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setDisable(true);
        title.setVisible(true);
        description.setDisable(true);
        description.setVisible(true);
        eventType.setDisable(true);
        eventType.setVisible(true);
        eventPlace.setDisable(true);
        eventPlace.setVisible(true);
        eventDate.setDisable(true);
        eventDate.setVisible(true);
        eventTime.setDisable(true);
        eventTime.setVisible(true);
        this.location.setDisable(true);
        this.location.setVisible(true);
        requiresBooking.setDisable(true);
        doesNotRequireBooking.setDisable(true);
        numberOfPlaces.setDisable(true);
        numberOfPlaces.setVisible(true);

        // setup the initial values in the controller
        inflateUi(event);
    }

    /**
     * handles the delete of an event from the database
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void deleteEvent(ActionEvent event) throws Exception {

        // the user is prompted with a dialog confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete event");
        alert.setContentText("Are you sure you want to delete the event " + this.event.getTitle() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {

            final Optional<Recurrence> recurrenceByEventTitleOptional = recurrenceRepository.findByEventTitle(this.event.getTitle());
            final Optional<RecurrencePattern> recurrencePatternByEventTitleOptional = recurrencePatternRepository.findByEventTitle(this.event.getTitle());
            final Optional<RecurrenceRange> recurrenceRangeBEventTitleOptional = recurrenceRangeRepository.findByEventTitle(this.event.getTitle());

            // if the event is recurrent then the recurrence is also deleted
            if (recurrenceByEventTitleOptional.isPresent() && recurrencePatternByEventTitleOptional.isPresent() && recurrenceRangeBEventTitleOptional.isPresent()) {

                if (!recurrenceRepository.deleteById(recurrenceByEventTitleOptional.get().getId())
                        || !recurrenceRangeRepository.deleteById(recurrenceRangeBEventTitleOptional.get().getId())
                        || !recurrencePatternRepository.deleteById(recurrencePatternByEventTitleOptional.get().getId())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("The recurrence could not be deleted");
                    alert.showAndWait();
                    return;
                }
            }

            // delete all the bookings for the event to be deleted
            bookingRepository.deleteByEventTitle(this.event.getTitle());

            // delete the event from the database
            if (eventRepository.deleteByTitle(this.event.getTitle())) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("The event " + this.event.getTitle() + " was successfully deleted");
                alert.showAndWait();

                // close the current stage
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.close();
                return;
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed! The event " + this.event.getTitle() + " could not be deleted");
                alert.showAndWait();
            }
        }


        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("No changes were made");
        alert.show();

        // close the current stage
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * opens the viewRecurrence controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void viewRecurrence(ActionEvent event) throws Exception {
        stageService.loadViewRecurrence(this);
    }

    /**
     * closes the CancelBooking stage
     *
     * @param event
     */
    @FXML
    private void cancel(ActionEvent event) throws Exception {
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * set the values in the controller using the event received when initialized
     *
     * @param event
     */
    private void inflateUi(Event event) {
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        ObservableList<String> types = FXCollections.observableArrayList(EventType.IN_PERSON.getType(), EventType.ONLINE.getType());
        eventType.setItems(types);
        eventType.getSelectionModel().select(EventType.parse(event.getEventType().getType()).getType().equals(EventType.IN_PERSON.getType()) ? 0 : 1);
        ObservableList<String> places = FXCollections.observableArrayList(EventPlace.INTERNAL.getPlace(), EventPlace.EXTERNAL.getPlace());
        eventPlace.setItems(places);
        eventPlace.getSelectionModel().select(EventPlace.parse(event.getEventPlace().getPlace()).getPlace().equals(EventPlace.INTERNAL.getPlace()) ? 0 : 1);

        String eventType = (String) this.eventType.getSelectionModel().getSelectedItem();
        String eventPlace = (String) this.eventPlace.getSelectionModel().getSelectedItem();

        if (EventType.parse(eventType).getType().equals(EventType.IN_PERSON.getType())
                && EventPlace.parse(eventPlace).getPlace().equals(EventPlace.INTERNAL.getPlace())) {
            this.url.setDisable(true);
            this.organisation.setDisable(true);

        }
        if (EventType.parse(eventType).getType().equals(EventType.IN_PERSON.getType())
                && EventPlace.parse(eventPlace).getPlace().equals(EventPlace.EXTERNAL.getPlace())) {
            this.url.setDisable(true);

        }
        if (EventType.parse(eventType).equals(EventType.ONLINE.getType())) {
            this.organisation.setDisable(true);
            this.location.setDisable(true);
        }

        eventDate.setValue(event.getEventTime() != null ? event.getEventTime().toLocalDate() : null);
        eventTime.setValue(event.getEventTime() != null ? event.getEventTime().toLocalTime() : null);
        location.setText(event.getLocation());
        this.requiresBooking.setSelected(true);
        numberOfPlaces.setText(String.valueOf(event.getNumberOfPlaces()));

        // if the event is not recurrent then disable the view recurrence button
        try {
            if (!this.recurrenceRepository.findByEvent(this.event).isPresent()) {
                this.viewRecurrenceButton.setDisable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Event getEvent() {
        return this.event;
    }
}
