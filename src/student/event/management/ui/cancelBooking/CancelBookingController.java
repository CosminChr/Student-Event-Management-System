package student.event.management.ui.cancelBooking;

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
import student.event.management.domain.models.Student;
import student.event.management.domain.util.EventPlace;
import student.event.management.domain.util.EventType;
import student.event.management.infrastructure.repository.BookingRepository;
import student.event.management.infrastructure.repository.RecurrenceRepository;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CancelBookingController implements Initializable {
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
    private JFXButton cancelBookingButton;
    @FXML
    private JFXButton cancelButton;

    private Student student;

    private Event event;

    private BookingRepository bookingRepository;

    private StageService stageService;

    private RecurrenceRepository recurrenceRepository;


    public CancelBookingController(Student student, Event event) {
        this.student = student;
        this.event = event;
        this.bookingRepository = new BookingRepository();
        this.stageService = new StageService();
        this.recurrenceRepository = new RecurrenceRepository();
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
        inflateUi(this.event);

    }

    /**
     * handles the delete of a booking from the database
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void cancelBooking(ActionEvent event) throws SQLException {

        // the user is prompted with a dialog confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel booking");
        alert.setContentText("Are you sure you want to cancel your booking to the event " + this.event.getTitle() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            // the booking corresponding to a specific event and student is deleted from the databsae
            if (this.bookingRepository.deleteByEventTitleAndStudentId(this.event.getTitle(), this.student.getId())) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Your booking to the event " + this.event.getTitle() + " was successfully cancelled");
                alert.show();

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed! The booking to " + this.event.getTitle() + " could not be cancelled");
                alert.show();
            }
            // close the current stage
            Stage stage = (Stage) this.rootPane.getScene().getWindow();
            stage.close();
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Booking not cancelled");
            alert.show();
            Stage stage = (Stage) this.rootPane.getScene().getWindow();
            stage.close();
        }
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
    public void cancel(ActionEvent event) {
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
