package student.event.management.ui.listAttendingEventsStudentBoard;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import student.event.management.domain.models.Booking;
import student.event.management.domain.models.Recurrence;
import student.event.management.domain.models.RecurrenceRange;
import student.event.management.domain.models.Student;
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

public class ListAttendingEventsStudentBoardController implements Initializable {
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

    private RecurrencePatternRepository recurrencePatternRepository;

    private RecurrenceRepository recurrenceRepository;

    private ObservableList<ListAttendingEventsStudentBoardController.Event> observableEventList = FXCollections.observableArrayList();

    private Student student;

    public ListAttendingEventsStudentBoardController(Student student) {
        this.student = student;
        this.eventRepository = new EventRepository();
        this.bookingRepository = new BookingRepository();
        this.recurrenceRepository = new RecurrenceRepository();
        this.recurrenceRangeRepository = new RecurrenceRangeRepository();
        this.recurrencePatternRepository = new RecurrencePatternRepository();
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
            // get all the events from the database for the logged in student
            final List<student.event.management.domain.models.Event> eventsFromDatabase = this.eventRepository.findAllByStudentId(student.getId());

            eventsFromDatabase.forEach(event -> {
                // if the event has a fixed time which is after the current time
                if (event.getEventTime() != null) {
                    if (event.getEventTime().isAfter(LocalDateTime.now())) {
                        try {
                            // get all the bookings of the event
                            List<Booking> bookingsForEvent = bookingRepository.findBookingsByEventTitle(event.getTitle());

                            // calculate the number of available places by substracting the number of bookings having the status approved
                            Integer numberOfAvailablePlaces = event.isRequiresBooking() ? event.getNumberOfPlaces() - bookingsForEvent.stream()
                                    .filter(b -> b.getStatus().getStatus().equals(BookingStatus.APPROVED.getStatus())).collect(toList()).size() : null;

                            // add the event into the observableList which will be rendered into the tableView
                            observableEventList.add(new ListAttendingEventsStudentBoardController.Event(event.getTitle(), event.getDescription(), event.getEventType(), event.getUrl(), event.getOrganisation(), event.getLocation(), false, event.getEventTime(), null, event.isRequiresBooking(), numberOfAvailablePlaces, event.getNumberOfPlaces()));

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
                                        .filter(b -> b.getStatus().getStatus().equals(BookingStatus.APPROVED.getStatus())).collect(toList()).size() : null;
                                LocalDateTime startTime = LocalDateTime.of(recurrenceRange.getStartDate(), recurrence.getStartTime());

                                // add the event into the observableList which will be rendered into the tableView
                                observableEventList.add(new ListAttendingEventsStudentBoardController.Event(event.getTitle(), event.getDescription(), event.getEventType(), event.getUrl(), event.getOrganisation(), event.getLocation(), true, event.getEventTime(), startTime, event.isRequiresBooking(), numberOfAvailablePlaces, event.getNumberOfPlaces()));
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
     * handles the delete of a booking from the database
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void cancelBooking(javafx.event.ActionEvent event) throws SQLException {

        // get the selected event from the tableView
        ListAttendingEventsStudentBoardController.Event studentEvent = (Event) tableView.getSelectionModel().getSelectedItem();

        //validate the event
        if (studentEvent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select an booking to cancel");
            alert.show();
            return;
        }

        // the user will be prompted with a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel booking");
        alert.setContentText("Are you sure you want to cancel your booking to the event " + studentEvent.getTitle() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            // delete the booking to an event for the logged in student
            if (this.bookingRepository.deleteByEventTitleAndStudentId(studentEvent.getTitle(), this.student.getId())) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Your booking to the event " + studentEvent.getTitle() + " was successfully cancelled");
                alert.show();

                // recreate the observableList
                observableEventList = FXCollections.observableArrayList();

                try {
                    // get the events of the logged in student
                    final List<student.event.management.domain.models.Event> eventsFromDatabase = this.eventRepository.findAllByStudentId(student.getId());
                    eventsFromDatabase.forEach(ev -> {
                        if (ev.getEventTime() != null) {

                            // if the event has a fixed time which is after the current time
                            if (ev.getEventTime().isAfter(LocalDateTime.now())) {
                                try {
                                    // get all the bookings of the event
                                    List<Booking> bookingsForEvent = bookingRepository.findBookingsByEventTitle(ev.getTitle());

                                    // calculate the number of available places by substracting the number of bookings having the status approved
                                    Integer numberOfAvailablePlaces = ev.isRequiresBooking() ? ev.getNumberOfPlaces() - bookingsForEvent.stream()
                                            .filter(b -> b.getStatus().getStatus().equals(BookingStatus.APPROVED.getStatus())).collect(toList()).size() : null;

                                    // add the event into the observableList which will be rendered into the tableView
                                    observableEventList.add(new ListAttendingEventsStudentBoardController.Event(ev.getTitle(), ev.getDescription(), ev.getEventType(), ev.getUrl(), ev.getOrganisation(), ev.getLocation(), false, ev.getEventTime(), null, ev.isRequiresBooking(), numberOfAvailablePlaces, ev.getNumberOfPlaces()));

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            try {
                                final Optional<RecurrenceRange> recurrenceRangebyEventOptional = recurrenceRangeRepository.findByEvent(ev);
                                final Optional<Recurrence> recurrenceByEventOptional = recurrenceRepository.findByEvent(ev);

                                // if the event is recurrent
                                if (recurrenceRangebyEventOptional.isPresent() && recurrenceByEventOptional.isPresent()) {
                                    RecurrenceRange recurrenceRange = recurrenceRangebyEventOptional.get();
                                    Recurrence recurrence = recurrenceByEventOptional.get();

                                    // if the recurrence start date is after the current date
                                    // or the dates are equal and the recurrence start time is after the current time
                                    if ((recurrenceRange.getStartDate().isAfter(LocalDate.now())
                                            || Period.between(recurrenceRange.getStartDate(), LocalDate.now()).getDays() == 0 && recurrence.getStartTime().isAfter(LocalTime.now()))) {

                                        // get all the bookings of the event
                                        List<Booking> bookingsForEvent = bookingRepository.findBookingsByEventTitle(ev.getTitle());

                                        // calculate the number of available places by substracting the number of bookings having the status approved
                                        Integer numberOfAvailablePlaces = ev.isRequiresBooking() ? ev.getNumberOfPlaces() - bookingsForEvent.stream()
                                                .filter(b -> b.getStatus().getStatus().equals(BookingStatus.APPROVED.getStatus())).collect(toList()).size() : null;
                                        LocalDateTime startTime = LocalDateTime.of(recurrenceRange.getStartDate(), recurrence.getStartTime());

                                        // add the event into the observableList which will be rendered into the tableView
                                        observableEventList.add(new ListAttendingEventsStudentBoardController.Event(ev.getTitle(), ev.getDescription(), ev.getEventType(), ev.getUrl(), ev.getOrganisation(), ev.getLocation(), true, ev.getEventTime(), startTime, ev.isRequiresBooking(), numberOfAvailablePlaces, ev.getNumberOfPlaces()));
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

                // this is a workaround that forces the columns of the tableView to be recreated
                // this refreshes the data from the tableView
                ((TableColumn) tableView.getColumns().get(0)).setVisible(false);
                ((TableColumn) tableView.getColumns().get(0)).setVisible(true);
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed! The booking to " + studentEvent.getTitle() + "could not be cancelled");
                alert.show();
            }
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Booking not cancelled");
            alert.show();
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
