package student.event.management.ui.updateOrDeleteEvent;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import student.event.management.domain.models.*;
import student.event.management.domain.util.EventPlace;
import student.event.management.domain.util.EventType;
import student.event.management.infrastructure.repository.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateOrDeleteEventController implements Initializable {
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
    private JFXButton updateButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXButton cancelButton;

    private Event event;

    private EventRepository eventRepository;

    private BookingRepository bookingRepository;

    private StageService stageService;

    private String initialTitle;

    private Recurrence recurrence;

    private RecurrencePattern recurrencePattern;

    private RecurrenceRange recurrenceRange;

    private RecurrenceRepository recurrenceRepository;

    private RecurrencePatternRepository recurrencePatternRepository;

    private RecurrenceRangeRepository recurrenceRangeRepository;

    private Student student;

    public UpdateOrDeleteEventController(Event event, Student student) {
        this.event = event;
        this.eventRepository = new EventRepository();
        this.bookingRepository = new BookingRepository();
        this.stageService = new StageService();
        this.recurrenceRepository = new RecurrenceRepository();
        this.recurrencePatternRepository = new RecurrencePatternRepository();
        this.recurrenceRangeRepository = new RecurrenceRangeRepository();
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

        try {

            // search for the recurrence of the event in the database
            final Optional<Recurrence> recurrenceByEventOptional = recurrenceRepository.findByEvent(this.event);

            // change the message of the button that handles the recurrence
            if (recurrenceByEventOptional.isPresent()) {
                this.addRecurrenceButton.setText("Update or remove recurrence");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // setup the initial values in the controller/
        inflateUi(event);

        final Optional<Recurrence> recurrenceByEventOptional;
        try {
            recurrenceByEventOptional = recurrenceRepository.findByEventTitle(event.getTitle());
            // if the event is recurrent
            if (recurrenceByEventOptional.isPresent()) {
                final Optional<RecurrenceRange> recurrenceRangeByEventTitleOptional = this.recurrenceRangeRepository.findByEventTitle(event.getTitle());
                final Optional<RecurrencePattern> recurrencePatternByEventTitleOptional = this.recurrencePatternRepository.findByEventTitle(event.getTitle());

                // set up the recurrence in the controller
                this.recurrence = recurrenceByEventOptional.get();
                this.recurrenceRange = recurrenceRangeByEventTitleOptional.get();
                this.recurrencePattern = recurrencePatternByEventTitleOptional.get();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
     * handles the eventType combobox change event
     *
     * @param event
     */
    @FXML
    private void eventTypeSelectionChange(ActionEvent event) {
        String eventType = (String) this.eventType.getValue();

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
     * handles the eventPlace combobox change event
     *
     * @param event
     */
    @FXML
    private void eventPlaceSelectionChange(ActionEvent event) {
        String eventPlace = (String) this.eventPlace.getValue();

        if (eventPlace.equals(EventPlace.INTERNAL.getPlace())) {
            organisation.setDisable(true);
            location.setPromptText("Location (Campus and room number)");
        } else {
            organisation.setDisable(false);
            location.setPromptText("Location");
        }
    }

    /**
     * loads the addRecurrence controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void addRecurrence(ActionEvent event) throws Exception {
        stageService.loadAddRecurrence(this);
    }

    /**
     * handles the requiresBooking radioButton change event
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
     * handles the doesNotRequireBooking radioButton change event
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
     * updates the event in the database
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void updateEvent(ActionEvent event) throws Exception {

        // get the data from the controls
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
        if (title.isEmpty() || description.isEmpty() || eventType.isEmpty() || EventType.parse(eventType).getType().equals(EventType.ONLINE.getType()) && url.isEmpty()
                || EventType.parse(eventType).getType().equals(EventType.IN_PERSON.getType()) && EventPlace.parse(eventPlace).getPlace().equals(EventPlace.INTERNAL.getPlace()) && location.isEmpty()
                || EventType.parse(eventType).getType().equals(EventType.IN_PERSON.getType()) && EventPlace.parse(eventPlace).getPlace().equals(EventPlace.EXTERNAL.getPlace()) && organisation.isEmpty() && location.isEmpty()
                || eventMoment == null && this.recurrence == null || requiresBooking && numberOfPlaces.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter all required fields");
            alert.show();
            return;
        }

        // check that the time of the event is after the current time
        if (eventMoment != null && eventMoment.isBefore(LocalDateTime.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The date cannot be before today");
            alert.show();
            return;
        }

        // validate the the modified event title does not exist in the database already
        if (eventRepository.findByTitle(title).isPresent() && !title.equals(initialTitle)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("There is already an event having this title. Please choose another name");
            alert.show();
            return;
        }

        // the user will be prompted with a dialog confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update event");
        alert.setContentText("Are you sure you want to update the event " + this.event.getTitle() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {

            // get all the event bookings
            List<Booking> eventBookings = bookingRepository.findBookingsByEventTitle(this.event.getTitle());

            // get all the bookings that should also be deleted with the event
            List<Booking> potentialBookingsToDelete = new ArrayList<>();
            if (Integer.parseInt(numberOfPlaces) < eventBookings.size()) {
                for (int i = 0; i < eventBookings.size(); i++) {
                    if (i >= Integer.parseInt(numberOfPlaces)) {
                        potentialBookingsToDelete.add(eventBookings.get(i));
                    }
                }
            }

            // delete all the event bookings
            potentialBookingsToDelete.forEach(bookingToDelete -> {
                try {
                    bookingRepository.deleteById(bookingToDelete.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            // get the event from the database by its title
            final Optional<Event> eventByTitleOptional = eventRepository.findByTitle(title);

            // create the event to be updated
            Event eventToUpdate = Event.builder()
                    .setId(eventByTitleOptional.get().getId())
                    .setTitle(title)
                    .setDescription(description)
                    .setEventType(EventType.parse(eventType))
                    .setEventPlace(EventPlace.parse(eventPlace))
                    .setUrl(url.equals("") ? null : url)
                    .setOrganisation(organisation.equals("") ? null : organisation)
                    .setLocation(location.equals("") ? null : location)
                    .setEventTime(recurrence == null ? eventMoment : null)
                    .setRequiresBooking(requiresBooking)
                    .setNumberOfPlaces(this.numberOfPlaces.isDisable() ? null : Integer.parseInt(numberOfPlaces))
                    .setCreatedBy(student.getId());

            Long lastSavedRecurrencePatternId = null;
            Long lastSavedRecurrenceRangeId = null;

            // if the event does not have a fixed date and time
            if (eventToUpdate.getEventTime() == null) {

                final Optional<Recurrence> recurrenceByEventOptional = recurrenceRepository.findByEventTitle(eventToUpdate.getTitle());
                // if the event is already recurrent
                if (recurrenceByEventOptional.isPresent()) {
                    final Optional<RecurrenceRange> recurrenceRangeByEventTitleOptional = this.recurrenceRangeRepository.findByEventTitle(eventToUpdate.getTitle());
                    final Optional<RecurrencePattern> recurrencePatternByEventTitleOptional = this.recurrencePatternRepository.findByEventTitle(eventToUpdate.getTitle());

                    this.recurrencePattern.setId(recurrencePatternByEventTitleOptional.get().getId());
                    this.recurrenceRange.setId(recurrenceRangeByEventTitleOptional.get().getId());
                    this.recurrence.setId(recurrenceByEventOptional.get().getId());
                    this.recurrence.setEventId(recurrenceByEventOptional.get().getEventId());
                    this.recurrence.setRecurrencePatternId(this.recurrencePattern.getId());
                    this.recurrence.setRecurrenceRangeId(this.recurrenceRange.getId());

                    // update the recurrence in the database too
                    if (!recurrencePatternRepository.update(recurrencePattern)
                            || !recurrenceRangeRepository.update(recurrenceRange)
                            || !recurrenceRepository.update(recurrence)) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("The recurrence could not be saved");
                        alert.showAndWait();
                        return;
                    }
                } else {
                    // if the event had a fixed date and time  but now a recurrence needs to be saved
                    // save the recurrencePattern into the database
                    if (recurrencePatternRepository.save(recurrencePattern)) {
                        Optional<Long> recurrencePatternOptional = recurrencePatternRepository.findLastId();
                        if (recurrencePatternOptional.isPresent()) {
                            // keep the saved recurrencePattern id
                            lastSavedRecurrencePatternId = recurrencePatternOptional.get();
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("The recurrence could not be saved");
                        alert.showAndWait();
                        return;
                    }
                    // save the recurrenceRange into the database
                    if (recurrenceRangeRepository.save(recurrenceRange)) {
                        Optional<Long> recurrenceRangeOptional = recurrenceRangeRepository.findLastId();
                        if (recurrenceRangeOptional.isPresent()) {
                            // keep the saved recurrenceRange id
                            lastSavedRecurrenceRangeId = recurrenceRangeOptional.get();
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("The recurrence could not be saved");
                        alert.showAndWait();
                        return;
                    }
                }
            }

            // update the event into the database
            if (eventRepository.update(eventToUpdate)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("The event " + this.event.getTitle() + " was successfully updated");
                alert.showAndWait();

                // create the recurrence linked to the event with its id as foreign key
                // and linked to the pattern and range with the their ids as foreign keys
                Recurrence recurrence = Recurrence.builder()
                        .setStartTime(this.recurrence.getStartTime())
                        .setEventId(eventToUpdate.getId())
                        .setRecurrencePatternId(lastSavedRecurrencePatternId)
                        .setRecurrenceRangeId(lastSavedRecurrenceRangeId);

                // check that a recurrence from the database by the event title does not exist
                final Optional<Recurrence> recurrenceByEventOptional = recurrenceRepository.findByEventTitle(eventToUpdate.getTitle());

                // save the recurrence into the database
                if (!recurrenceByEventOptional.isPresent()) {
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

                        // close the current stage
                        Stage stage = (Stage) this.rootPane.getScene().getWindow();
                        stage.close();
                    }
                }


            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed! The event " + this.event.getTitle() + "could not be updated");
                alert.show();
            }

            // close the current stage
            Stage stage = (Stage) this.rootPane.getScene().getWindow();
            stage.close();
            return;
        }


        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("No changes were made");
        alert.show();

        // close the current stage
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

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
                    alert.setContentText("The recurrence could not be saved");
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
        this.initialTitle = this.title.getText();

        if (recurrence != null) {
            this.eventDate.setValue(LocalDate.now());
            this.eventTime.setValue(LocalTime.now());
        } else {
            this.eventDate.setDisable(true);
            this.eventTime.setDisable(true);
        }
    }

    public Event getEvent() {
        return this.event;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public void setRecurrencePattern(RecurrencePattern recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public void setRecurrenceRange(RecurrenceRange recurrenceRange) {
        this.recurrenceRange = recurrenceRange;

        if (recurrence == null && recurrencePattern == null && this.recurrenceRange == null) {
            addRecurrenceButton.setText("Add recurrence");
            eventDate.setDisable(false);
            eventTime.setDisable(false);
        } else {
            addRecurrenceButton.setText("Update or remove recurrence");
            eventDate.setDisable(true);
            eventTime.setDisable(true);
        }
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public RecurrencePattern getRecurrencePattern() {
        return recurrencePattern;
    }

    public RecurrenceRange getRecurrenceRange() {
        return recurrenceRange;
    }
}
