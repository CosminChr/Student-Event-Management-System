package student.event.management.ui.listEventsAdminBoard;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import student.event.management.application.service.StageService;
import student.event.management.domain.models.Booking;
import student.event.management.domain.models.Recurrence;
import student.event.management.domain.models.RecurrencePattern;
import student.event.management.domain.models.RecurrenceRange;
import student.event.management.domain.util.BookingStatus;
import student.event.management.domain.util.EventType;
import student.event.management.infrastructure.repository.*;
import student.event.management.ui.util.YesOrNoEnum;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.util.stream.Collectors.toList;

public class ListEventsAdminBoardController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn title;
    @FXML
    private TableColumn description;
    @FXML
    private TableColumn eventType;
    @FXML
    private TableColumn url;
    @FXML
    private TableColumn organisation;
    @FXML
    private TableColumn location;
    @FXML
    private TableColumn isRecurrent;
    @FXML
    private TableColumn eventTime;
    @FXML
    private TableColumn startTime;
    @FXML
    private TableColumn requiresBooking;
    @FXML
    private TableColumn availableNumberOfPlaces;
    @FXML
    private TableColumn numberOfPlaces;

    private EventRepository eventRepository;

    private BookingRepository bookingRepository;

    private RecurrenceRangeRepository recurrenceRangeRepository;

    private RecurrenceRepository recurrenceRepository;

    private RecurrencePatternRepository recurrencePatternRepository;

    private StageService stageService;

    private ObservableList<Event> observableEventList = FXCollections.observableArrayList();

    public ListEventsAdminBoardController() {
        this.eventRepository = new EventRepository();
        this.bookingRepository = new BookingRepository();
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
        // initialize the columns for the tableView
        initializeColumns();
        try {

            // get all the events from the database and add them to the observableList
            final List<student.event.management.domain.models.Event> eventsFromDatabase = this.eventRepository.findAll();
            eventsFromDatabase.forEach(event -> {
                if (event.getEventTime() != null) {
                    if (event.getEventTime().isAfter(LocalDateTime.now())) {
                        try {
                            // get all the bookings of the event
                            List<Booking> bookingsForEvent = bookingRepository.findBookingsByEventTitle(event.getTitle());

                            // calculate the number of available places by substracting the number of bookings having the status approved
                            Integer numberOfAvailablePlaces = event.isRequiresBooking() ? event.getNumberOfPlaces() - bookingsForEvent.stream()
                                    .filter(b -> b.getStatus().getStatus().equals(BookingStatus.APPROVED.getStatus())).collect(toList()).size() : null;

                            // add the event into the observableList which will be rendered into the tableView
                            observableEventList.add(new ListEventsAdminBoardController.Event(event.getTitle(), event.getDescription(), event.getEventType(), event.getUrl(), event.getOrganisation(), event.getLocation(), false, event.getEventTime(), null, event.isRequiresBooking(), numberOfAvailablePlaces, event.getNumberOfPlaces()));

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    try {
                        final Optional<RecurrenceRange> recurrenceRangebyEventOptional = recurrenceRangeRepository.findByEvent(event);
                        final Optional<Recurrence> recurrenceByEventOptional = recurrenceRepository.findByEvent(event);

                        // if the event is recurrent
                        if (recurrenceRangebyEventOptional.isPresent() && recurrenceByEventOptional.isPresent()) {
                            RecurrenceRange recurrenceRange = recurrenceRangebyEventOptional.get();
                            Recurrence recurrence = recurrenceByEventOptional.get();

                            // if the recurrence start date is after the current date
                            // or the dates are equal and the recurrence start time is after the current time
                            if ((recurrenceRange.getStartDate().isAfter(LocalDate.now())
                                    || Period.between(recurrenceRange.getStartDate(), LocalDate.now()).getDays() == 0 && recurrence.getStartTime().isAfter(LocalTime.now()))) {

                                // get all the bookings of the event
                                List<Booking> bookingsForEvent = bookingRepository.findBookingsByEventTitle(event.getTitle());

                                // calculate the number of available places by substracting the number of bookings having the status approved
                                Integer numberOfAvailablePlaces = event.isRequiresBooking() ? event.getNumberOfPlaces() - bookingsForEvent.stream()
                                        .filter(b -> b.getStatus().equals(BookingStatus.APPROVED.getStatus())).collect(toList()).size() : null;
                                LocalDateTime startTime = LocalDateTime.of(recurrenceRange.getStartDate(), recurrence.getStartTime());

                                // add the event into the observableList which will be rendered into the tableView
                                observableEventList.add(new ListEventsAdminBoardController.Event(event.getTitle(), event.getDescription(), event.getEventType(), event.getUrl(), event.getOrganisation(), event.getLocation(), true, event.getEventTime(), startTime, event.isRequiresBooking(), numberOfAvailablePlaces, event.getNumberOfPlaces()));
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // set the events from the observableList to the tableView
        tableView.setItems(observableEventList);
    }

    /**
     * handles the loading of the listBookings controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    public void viewBookings(ActionEvent event) throws Exception {

        // get the selected item from the tableView
        Event studentEvent = (Event) tableView.getSelectionModel().getSelectedItem();

        // validate the event
        if (studentEvent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select an event to view its bookings");
            alert.showAndWait();
            return;
        }

        if (studentEvent.requiresBooking.getValue().equals(YesOrNoEnum.NO.getLabel())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("This event does not require booking hence it does not have any bookings");
            alert.showAndWait();
            return;
        }

        // create an event object, give its title from the tableView and pass it to the loadBookings controller
        student.event.management.domain.models.Event ev = student.event.management.domain.models.Event.builder()
                .setTitle(studentEvent.getTitle());

        stageService.loadBookingsListView(ev);
    }

    /**
     * handles the delete of an event from the database
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void deleteEvent(ActionEvent event) throws SQLException {

        // get the selected event from the tableView
        ListEventsAdminBoardController.Event studentEvent = (Event) tableView.getSelectionModel().getSelectedItem();

        // validate the event
        if (studentEvent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select an event to delete");
            alert.showAndWait();
            return;
        }
        // the user will be prompted with a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete event");
        alert.setContentText("Are you sure you want to delete the event " + studentEvent.getTitle() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {

            final Optional<Recurrence> recurrenceByEventTitleOptional = recurrenceRepository.findByEventTitle(studentEvent.getTitle());
            final Optional<RecurrencePattern> recurrencePatternByEventTitleOptional = recurrencePatternRepository.findByEventTitle(studentEvent.getTitle());
            final Optional<RecurrenceRange> recurrenceRangeBEventTitleOptional = recurrenceRangeRepository.findByEventTitle(studentEvent.getTitle());

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
            bookingRepository.deleteByEventTitle(studentEvent.getTitle());

            // delete the event from the database
            if (eventRepository.deleteByTitle(studentEvent.getTitle())) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("The event " + studentEvent.getTitle() + " was successfully deleted");
                alert.showAndWait();

                // remove the deleted student from the tableView
                observableEventList.remove(studentEvent);
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed! The event " + studentEvent.getTitle() + " could not be deleted");
                alert.showAndWait();
            }

        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Event delete cancelled");
            alert.showAndWait();
        }
    }

    /**
     * initializes the table view columns
     */
    private void initializeColumns() {
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        eventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        url.setCellValueFactory(new PropertyValueFactory<>("url"));
        organisation.setCellValueFactory(new PropertyValueFactory<>("organisation"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        isRecurrent.setCellValueFactory(new PropertyValueFactory<>("isRecurrent"));
        eventTime.setCellValueFactory(new PropertyValueFactory<>("eventTime"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        requiresBooking.setCellValueFactory(new PropertyValueFactory<>("requiresBooking"));
        availableNumberOfPlaces.setCellValueFactory(new PropertyValueFactory<>("availableNumberOfPlaces"));
        numberOfPlaces.setCellValueFactory(new PropertyValueFactory<>("numberOfPlaces"));
    }

    /**
     * static inener helper class that handles the tableView properties
     */
    public static class Event {
        private final SimpleStringProperty title;
        private final SimpleStringProperty description;
        private final SimpleStringProperty eventType;
        private final SimpleStringProperty url;
        private final SimpleStringProperty organisation;
        private final SimpleStringProperty location;
        private final SimpleStringProperty isRecurrent;
        private final SimpleStringProperty eventTime;
        private final SimpleStringProperty startTime;
        private final SimpleStringProperty requiresBooking;
        private final SimpleStringProperty availableNumberOfPlaces;
        private final SimpleStringProperty numberOfPlaces;


        public Event(String title, String description, EventType eventType, String url, String organisation, String location, boolean isRecurrent, LocalDateTime eventTime, LocalDateTime startTime, boolean requiresBooking, Integer availableNumberOfPlaces, Integer numberOfPlaces) {
            this.title = new SimpleStringProperty(title);
            this.description = new SimpleStringProperty(description);
            this.eventType = new SimpleStringProperty(EventType.parse(eventType.getType()).getType());
            this.url = new SimpleStringProperty(url != null ? url : "-");
            this.organisation = new SimpleStringProperty(organisation != null ? organisation : "-");
            this.location = new SimpleStringProperty(location != null ? location : "-");
            this.isRecurrent = new SimpleStringProperty(isRecurrent ? YesOrNoEnum.YES.getLabel() : YesOrNoEnum.NO.getLabel());
            this.eventTime = new SimpleStringProperty(eventTime != null ? eventTime.toString() : "-");
            this.startTime = new SimpleStringProperty(startTime != null ? startTime.toString() : "-");
            this.requiresBooking = new SimpleStringProperty(requiresBooking ? YesOrNoEnum.YES.getLabel() : YesOrNoEnum.NO.getLabel());
            this.availableNumberOfPlaces = new SimpleStringProperty(requiresBooking ? availableNumberOfPlaces.toString() : "-");
            this.numberOfPlaces = new SimpleStringProperty(requiresBooking ? numberOfPlaces.toString() : "-");
        }


        public String getTitle() {
            return title.get();
        }

        public String getDescription() {
            return description.get();
        }

        public String getEventType() {
            return eventType.get();
        }

        public String getUrl() {
            return url.get();
        }

        public String getOrganisation() {
            return organisation.get();
        }

        public String getLocation() {
            return location.get();
        }

        public String getIsRecurrent() {
            return isRecurrent.get();
        }

        public String getEventTime() {
            return eventTime.get();
        }

        public String getStartTime() {
            return startTime.get();
        }

        public String getRequiresBooking() {
            return requiresBooking.get();
        }

        public String getAvailableNumberOfPlaces() {
            return availableNumberOfPlaces.get();
        }

        public String getNumberOfPlaces() {
            return numberOfPlaces.get();
        }
    }
}
