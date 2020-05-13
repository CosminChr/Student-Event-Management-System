package student.event.management.ui.addEvent;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import student.event.management.application.service.StageService;
import student.event.management.domain.models.*;
import student.event.management.domain.util.EventPlace;
import student.event.management.domain.util.EventType;
import student.event.management.infrastructure.repository.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddEventController implements Initializable {
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
    private JFXButton addRecurrenceButton;
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
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private AnchorPane rootPane;

    private GenericRepository genericRepository;

    private EventRepository eventRepository;

    private RecurrenceRepository recurrenceRepository;

    private RecurrencePatternRepository recurrencePatternRepository;

    private RecurrenceRangeRepository recurrenceRangeRepository;

    private StageService stageService;

    private Student student;

    private Recurrence recurrence;

    private RecurrencePattern recurrencePattern;

    private RecurrenceRange recurrenceRange;

    public AddEventController(Student student) {

        this.genericRepository = new GenericRepository();
        this.eventRepository = new EventRepository();
        this.recurrenceRepository = new RecurrenceRepository();
        this.recurrencePatternRepository = new RecurrencePatternRepository();
        this.recurrenceRangeRepository = new RecurrenceRangeRepository();
        this.stageService = new StageService();
        this.student = student;
    }

    /**
     * initializes the data in the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // populate comboboxes
        eventType.getItems().removeAll(eventType.getItems());
        eventType.getItems().addAll(EventType.IN_PERSON.getType(), EventType.ONLINE.getType());
        eventPlace.getItems().removeAll(eventType.getItems());
        eventPlace.getItems().addAll(EventPlace.INTERNAL.getPlace(), EventPlace.EXTERNAL.getPlace());

        //set default radio buttons' state
        this.requiresBooking.setSelected(true);
        this.doesNotRequireBooking.setSelected(false);

        //set default values for the date and time of the event
        this.eventDate.setValue(LocalDate.now());
        this.eventTime.setValue(LocalTime.now());

        // set regex validation listener that does not permit any character other than numbers
        numberOfPlaces.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numberOfPlaces.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * handles the change event for the eventType combobox
     *
     * @param event
     */
    @FXML
    private void eventTypeSelectionChange(ActionEvent event) {
        String eventType = (String) this.eventType.getValue();

        // handles the event place combobox, organisation, location and url textfields state
        if (eventType.equals(EventType.ONLINE.getType())) {
            eventPlace.setDisable(true);
            organisation.setDisable(true);
            location.setDisable(true);
            url.setDisable(false);
        } else {
            eventPlace.setDisable(false);
            organisation.setDisable(false);
            location.setDisable(false);
            url.setDisable(true);
        }
    }

    /**
     * handles the change event for the eventPlace combobox
     *
     * @param event
     */
    @FXML
    private void eventPlaceSelectionChange(ActionEvent event) {
        String eventPlace = (String) this.eventPlace.getValue();

        // handles the organisation and location textfields' state
        if (eventPlace.equals(EventPlace.INTERNAL.getPlace())) {
            organisation.setDisable(true);
            location.setPromptText("Location (Campus and room number)");
        } else {
            organisation.setDisable(false);
            location.setPromptText("Location");
        }
    }

    /**
     * loads the AddRecurrence controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void addRecurrence(ActionEvent event) throws Exception {
        stageService.loadAddRecurrence(this);
    }

    /**
     * handles the change event for the requiresBooking radio button
     *
     * @param event
     */
    @FXML
    private void requiresBookingRadioButtonChange(ActionEvent event) {
        this.requiresBooking.setSelected(true);
        this.doesNotRequireBooking.setSelected(false);
        this.numberOfPlaces.setDisable(false);
    }

    /**
     * handles the change event for the doesNotRequireBooking radio button
     *
     * @param event
     */
    @FXML
    private void doesNotRequireBookingRadioButtonChange(ActionEvent event) {
        this.requiresBooking.setSelected(false);
        this.doesNotRequireBooking.setSelected(true);
        this.numberOfPlaces.setDisable(true);
    }

    /**
     * saves the event into the database
     * this function is triggered when the user clicks the save button
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void addEvent(ActionEvent event) throws SQLException {

        // get the values from controls
        String title = this.title.getText();
        String description = this.description.getText();
        String eventType = (String) this.eventType.getValue();
        String eventPlace = (String) this.eventPlace.getValue();
        String url = (String) this.url.getText();
        String organisation = (String) this.organisation.getText();
        String location = this.location.getText();
        boolean requiresBooking = this.requiresBooking.isSelected() ? true : false;
        String numberOfPlaces = this.numberOfPlaces.getText();


        LocalDateTime eventMoment = null;
        if (eventDate.getValue() != null && eventTime != null) {
            eventMoment = LocalDateTime.of(eventDate.getValue(), eventTime.getValue());
        }

        // validate that the fields and comboboxes are not empty
        if ((eventType == null || title.isEmpty() || description.isEmpty() || eventType.isEmpty() || EventType.parse(eventType).getType().equals(EventType.ONLINE.getType()) && url.isEmpty())
                || (EventType.parse(eventType).getType().equals(EventType.IN_PERSON.getType()) && EventPlace.parse(eventPlace).getPlace().equals(EventPlace.INTERNAL.getPlace()) && location.isEmpty())
                || (EventType.parse(eventType).getType().equals(EventType.IN_PERSON.getType()) && EventPlace.parse(eventPlace).getPlace().equals(EventPlace.EXTERNAL.getPlace()) && organisation.isEmpty() && location.isEmpty())
                || (eventMoment == null || (requiresBooking && numberOfPlaces.isEmpty()))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter all required fields");
            alert.show();
            return;
        }

        // check that the time of the event is after the current time
        if (eventMoment != null && !eventTime.isDisabled() && eventMoment.isBefore(LocalDateTime.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The date cannot be before today");
            alert.show();
            return;
        }

        // create the event object
        Event studentEvent = Event.builder()
                .setTitle(title)
                .setDescription(description)
                .setEventType(EventType.parse(eventType))
                .setEventPlace(EventPlace.parse(eventPlace))
                .setUrl(url.isEmpty() ? null : url)
                .setOrganisation(organisation.isEmpty() ? null : organisation)
                .setLocation(location.isEmpty() ? null : location)
                .setEventTime(recurrence == null ? eventMoment : null)
                .setRequiresBooking(requiresBooking)
                .setNumberOfPlaces(this.numberOfPlaces.isDisable() ? null : Integer.parseInt(numberOfPlaces))
                .setCreatedBy(student.getId());

        try {

            // if the event is recurrent then the recurrence also has to be persisted
            if (recurrence != null) {

                Long lastSavedRecurrencePatternId = null;
                Long lastSavedRecurrenceRangeId = null;

                // try to save the recurrencePattern and get it's id from the database
                if (recurrencePatternRepository.save(recurrencePattern)) {
                    Optional<Long> recurrencePatternOptional = recurrencePatternRepository.findLastId();
                    if (recurrencePatternOptional.isPresent()) {
                        lastSavedRecurrencePatternId = recurrencePatternOptional.get();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("The recurrence could not be saved");
                    alert.showAndWait();
                    return;
                }

                // try to save the recurrenceRange and get it's id from the database
                if (recurrenceRangeRepository.save(recurrenceRange)) {
                    Optional<Long> recurrenceRangeOptional = recurrenceRangeRepository.findLastId();
                    if (recurrenceRangeOptional.isPresent()) {
                        lastSavedRecurrenceRangeId = recurrenceRangeOptional.get();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("The recurrence could not be saved");
                    alert.showAndWait();
                    return;
                }

                // try to save the event and get it's id from the database
                if (eventRepository.save(studentEvent)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    Long lastEventId = null;
                    Optional<Long> lastEventIdOptional = eventRepository.findLastId();
                    if (lastEventIdOptional.isPresent()) {
                        lastEventId = lastEventIdOptional.get();
                    }

                    // while creating the recurrence object set links to the event, recurrencePattern and recurrenceRange
                    Recurrence recurrence = Recurrence.builder()
                            .setStartTime(this.recurrence.getStartTime())
                            .setEventId(lastEventId)
                            .setRecurrencePatternId(lastSavedRecurrencePatternId)
                            .setRecurrenceRangeId(lastSavedRecurrenceRangeId);

                    // try to save the event's recurrence
                    if (!recurrenceRepository.save(recurrence)) {

                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("The recurrence could not be saved");
                        alert.showAndWait();
                        return;
                    } else {
                        alert.setHeaderText(null);
                        alert.setContentText("The event was successfully saved");
                        alert.showAndWait();
                        Stage stage = (Stage) this.rootPane.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setContentText("The event " + studentEvent.getTitle() + " was not saved because it already exists");
                    alert.show();
                }
            } else {
                // if the event is not recurrent I save it without the recurrence
                if (eventRepository.save(studentEvent)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("The event was successfully saved");
                    alert.showAndWait();
                    Stage stage = (Stage) this.rootPane.getScene().getWindow();
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setContentText("The event " + studentEvent.getTitle() + " was not saved because it already exists");
                    alert.show();
                }
            }

        } catch (
                SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The event could not be saved");
        }
    }

    /**
     * closes the AddEvent stage
     *
     * @param event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public void setRecurrencePattern(RecurrencePattern recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public void setRecurrenceRange(RecurrenceRange recurrenceRange) {
        this.recurrenceRange = recurrenceRange;
        // handles the text for the button opens up the stage that handles the recurrence state
        if (recurrence == null && recurrencePattern == null && recurrenceRange == null) {
            addRecurrenceButton.setText("Add recurrence");
            eventDate.setDisable(false);
            eventTime.setDisable(false);
        } else {
            addRecurrenceButton.setText("Remove recurrence");
            eventDate.setDisable(true);
            eventTime.setDisable(true);
        }
    }

    public Recurrence getRecurrence() {
        return this.recurrence;
    }

    public RecurrenceRange getRecurrenceRange() {
        return this.recurrenceRange;
    }

    public RecurrencePattern getRecurrencePattern() {
        return this.recurrencePattern;
    }
}
