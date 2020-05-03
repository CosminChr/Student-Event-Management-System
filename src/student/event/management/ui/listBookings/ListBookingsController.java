package student.event.management.ui.listBookings;

import javafx.beans.property.SimpleLongProperty;
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
import student.event.management.domain.dto.BookingDTO;
import student.event.management.domain.models.Event;
import student.event.management.domain.util.BookingStatus;
import student.event.management.infrastructure.repository.BookingRepository;
import student.event.management.infrastructure.repository.EventRepository;
import student.event.management.ui.util.YesOrNoEnum;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListBookingsController implements Initializable {
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn firstName;
    @FXML
    private TableColumn lastName;
    @FXML
    private TableColumn title;
    @FXML
    private TableColumn isRecurrent;
    @FXML
    private TableColumn time;
    @FXML
    private TableColumn startTime;
    @FXML
    private TableColumn status;

    private BookingRepository bookingRepository;

    private EventRepository eventRepository;

    private ObservableList<Booking> observableBookingList = FXCollections.observableArrayList();

    private String studentFirstName;

    private String studentLastName;

    private Event event;

    public ListBookingsController(String studentFirstName, String studentLastName) {
        this.bookingRepository = new BookingRepository();
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.eventRepository = new EventRepository();
    }

    public ListBookingsController(Event event) {
        this.bookingRepository = new BookingRepository();
        this.event = event;
        this.eventRepository = new EventRepository();
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

            // get all the bookings from the database and add them to the observableList
            final List<BookingDTO> joinedBookingsFromDatabase = this.bookingRepository.findAllDtos();
            joinedBookingsFromDatabase.forEach(booking -> {
                observableBookingList.add(new ListBookingsController.Booking(booking.getId(), booking.getStudentFirstName(), booking.getStudentLastName(), booking.getEventTitle(), booking.isRecurrent(), booking.getEventTime(), booking.getStartTime(), booking.getStatus()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // set the observableList items to the tableView
        tableView.setItems(observableBookingList);
        if (this.studentFirstName != null) {
            // filter the bookings by the logged in student's name
            // if the bookings to be displayed ar for the student
            filterBookingListByStudentFirstNameAndLastName();
        }
        // filter the bookings by the event title
        // if the bookings to be displayed ar for the event
        if (this.event != null) {
            filterBookingsByEventTitle();
        }
    }

    /**
     * handles the approval of a booking by the admin
     * by changing the booking status in approved
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void approveBooking(ActionEvent event) throws SQLException {

        // get the selected booking from the tableView
        Booking booking = (Booking) tableView.getSelectionModel().getSelectedItem();

        // get the event of the booking
        Optional<Event> studentEvent = eventRepository.findByBookingId(booking.getId());
        if (studentEvent.isPresent()) {

            // validate that the booking was not already approved
            if (booking.getStatus().equals(BookingStatus.APPROVED.getStatus())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("This booking was already approved");
                alert.showAndWait();
                return;
            }

            // validate that the booking needs approval
            if (!studentEvent.get().isRequiresBooking()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("This booking does not need approval");
                alert.showAndWait();
                return;
            }

            // get the bookings of the event
            final List<student.event.management.domain.models.Booking> bookingsByEventTitle = bookingRepository.findBookingsByEventTitle(studentEvent.get().getTitle());

            // validate that the event still has available places left
            if (studentEvent.get().getNumberOfPlaces() - bookingsByEventTitle.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("This event does not have any available places left");
                alert.showAndWait();
                return;
            }

            // get the booking to update by its id and update its status
            Optional<student.event.management.domain.models.Booking> bookingTopUpdateOptional = bookingRepository.findById(booking.getId());
            if (bookingTopUpdateOptional.isPresent()) {
                final student.event.management.domain.models.Booking bookingToUpdate = bookingTopUpdateOptional.get();
                bookingToUpdate.setStatus(BookingStatus.APPROVED);
                bookingRepository.updateBookingStatus(bookingToUpdate);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("The booking for the event " + studentEvent.get().getTitle() + " for " + booking.getFirstName() + " " + booking.getLastName() + " was successfully approved");
                refreshBookings();
                alert.showAndWait();
                return;
            }
        }

    }


    /**
     * handles the delete of a booking from the database
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void deleteBooking(ActionEvent event) throws SQLException {

        // get the booking from the tableView
        Booking booking = (Booking) tableView.getSelectionModel().getSelectedItem();

        // validate the booking
        if (booking == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select a booking to delete");
            alert.show();
            return;
        }

        // the user will be prompted with a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete booking");
        alert.setContentText("Are you sure you want to delete the booking to the event " + booking.getTitle() + " for the student " + booking.getLastName() + " " + booking.getFirstName() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {

            // delete the booking from the database
            if (bookingRepository.deleteById(booking.getId())) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("The booking for student" + booking.getLastName() + " " + booking.getFirstName() + "to " + booking.getTitle() + " was successfully deleted");
                observableBookingList.remove(booking);
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Failed! The booking could not be deleted");
            }

        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Booking delete cancelled");
        }
    }

    /**
     * handles the refresh of the data in the tableView
     *
     */
    public void refreshBookings() {

        // recreate the observableList
        observableBookingList = FXCollections.observableArrayList();

        try {
            // find all the bookings from the database and add them into the observableList
            final List<BookingDTO> joinedBookingsFromDatabase = this.bookingRepository.findAllDtos();
            joinedBookingsFromDatabase.forEach(booking -> {
                observableBookingList.add(new ListBookingsController.Booking(booking.getId(), booking.getStudentFirstName(), booking.getStudentLastName(), booking.getEventTitle(), booking.isRecurrent(), booking.getEventTime(), booking.getStartTime(), booking.getStatus()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // set the bookings in the observableList
        tableView.setItems(FXCollections.observableArrayList());
        tableView.setItems(observableBookingList);

        // this is a workaround that forces the columns of the tableView to be recreated
        // this refreshes the data from the tableView
        ((TableColumn) tableView.getColumns().get(0)).setVisible(false);
        ((TableColumn) tableView.getColumns().get(0)).setVisible(true);

    }

    /**
     * filters the bookings by the student firstName and lastName
     */
    public void filterBookingListByStudentFirstNameAndLastName() {
        observableBookingList.removeIf(booking -> !booking.getFirstName().equals(this.studentFirstName)
                || !booking.getLastName().equals(this.studentLastName));
    }

    /**
     * filters the bookings by the event title
     */
    public void filterBookingsByEventTitle() {
        observableBookingList.removeIf(booking -> !booking.getTitle().equals(this.event.getTitle()));
    }


    /**
     * initializes the table view columns
     */
    private void initializeColumns() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        isRecurrent.setCellValueFactory(new PropertyValueFactory<>("isRecurrent"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * static inener helper class that handles the tableView properties
     */
    public static class Booking {
        private final SimpleLongProperty id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty title;
        private final SimpleStringProperty isRecurrent;
        private final SimpleStringProperty time;
        private final SimpleStringProperty startTime;
        private final SimpleStringProperty status;


        public Booking(Long id, String firstName, String lastName, String title, boolean isRecurrent, LocalDateTime time, LocalDateTime startTime, BookingStatus status) {
            this.id = new SimpleLongProperty(id);
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.title = new SimpleStringProperty(title);
            this.isRecurrent = new SimpleStringProperty(isRecurrent ? YesOrNoEnum.YES.getLabel() : YesOrNoEnum.NO.getLabel());
            this.time = new SimpleStringProperty(time != null ? time.toString() : "-");
            this.startTime = new SimpleStringProperty(startTime != null ? startTime.toString() : "-");
            this.status = new SimpleStringProperty(status != null ? status.getStatus() : "-");
        }

        public long getId() {
            return id.get();
        }

        public String getFirstName() {
            return firstName.get();
        }

        public String getLastName() {
            return lastName.get();
        }

        public String getTitle() {
            return title.get();
        }

        public String getIsRecurrent() {
            return isRecurrent.get();
        }

        public String getTime() {
            return time.get();
        }

        public String getStartTime() {
            return startTime.get();
        }

        public String getStatus() {
            return status.get();
        }
    }
}
