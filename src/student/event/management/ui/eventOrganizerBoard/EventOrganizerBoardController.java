package student.event.management.ui.eventOrganizerBoard;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import student.event.management.application.service.StageService;
import student.event.management.domain.models.Event;
import student.event.management.domain.models.Student;
import student.event.management.infrastructure.repository.EventRepository;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventOrganizerBoardController implements Initializable {
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXTextField searchMyEventsTitle;
    @FXML
    private JFXTextField searchAllEventsTitle;
    @FXML
    private JFXTextField searchAtendingTitle;

    private StageService stageService;

    private Student student;

    private EventRepository eventRepository;

    public EventOrganizerBoardController(Student student) {
        this.stageService = new StageService();
        this.student = student;
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

    }


    /**
     * loads the listAllEvents controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void listAllEvents(ActionEvent event) throws Exception {
        stageService.loadAllEventsStudentBoardListView(student);
    }

    /**
     * loads the listMyEvents controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void listMyEvents(ActionEvent event) throws Exception {
        stageService.loadMyEventsStudentBoardListView(student);
    }

    /**
     * loads the listAttendingEvents controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void listAttendingEvents(ActionEvent event) throws Exception {
        stageService.loadAttendingEventsStudentBoardListView(student);
    }

    /**
     * logs out the user from the application
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void logout(ActionEvent event) throws Exception {
        stageService.loadLogin();
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * loads the addEvent controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void createEvent(ActionEvent event) throws Exception {
        stageService.loadAddEvent(this.student);
    }


    /**
     * search an event in the events organised by the user
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void searchMyEvents(ActionEvent event) throws Exception {

        // gets the event title from the textfield
        String title = this.searchMyEventsTitle.getText();

        // validate the event's title
        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter the event title");
            alert.show();
            return;
        }

        // search the event in the database
        Optional<Event> eventFroMDatabaseOptional = eventRepository.findByTitle(title);
        if (!eventFroMDatabaseOptional.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Student");
            alert.setContentText("The event was not found");
            alert.show();
            return;
        } else {
            Event studentEvent = Event.builder()
                    .setId(eventFroMDatabaseOptional.get().getId())
                    .setTitle(eventFroMDatabaseOptional.get().getTitle())
                    .setDescription(eventFroMDatabaseOptional.get().getDescription())
                    .setEventType(eventFroMDatabaseOptional.get().getEventType())
                    .setEventPlace(eventFroMDatabaseOptional.get().getEventPlace())
                    .setUrl(eventFroMDatabaseOptional.get().getUrl())
                    .setOrganisation(eventFroMDatabaseOptional.get().getOrganisation())
                    .setLocation(eventFroMDatabaseOptional.get().getLocation())
                    .setEventTime(eventFroMDatabaseOptional.get().getEventTime())
                    .setNumberOfPlaces(eventFroMDatabaseOptional.get().getNumberOfPlaces())
                    .setCreatedBy(eventFroMDatabaseOptional.get().getCreatedBy());

            // load the updateOrDelete controller
            stageService.loadUpdateOrDeleteEvent(studentEvent, student);
        }
    }

    /**
     * search an event in all the events from the database
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void searchAllEvents(ActionEvent event) throws Exception {
        // gets the event title from the textfield
        String title = this.searchAllEventsTitle.getText();

        // validate the event's title
        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter the event title");
            alert.show();
            return;
        }

        // search the event in the database
        Optional<Event> eventFroMDatabaseOptional = eventRepository.findByTitle(title);
        if (!eventFroMDatabaseOptional.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Student");
            alert.setContentText("The event was not found");
            alert.show();
            return;
        } else {
            Event studentEvent = Event.builder()
                    .setId(eventFroMDatabaseOptional.get().getId())
                    .setTitle(eventFroMDatabaseOptional.get().getTitle())
                    .setDescription(eventFroMDatabaseOptional.get().getDescription())
                    .setEventType(eventFroMDatabaseOptional.get().getEventType())
                    .setEventPlace(eventFroMDatabaseOptional.get().getEventPlace())
                    .setUrl(eventFroMDatabaseOptional.get().getUrl())
                    .setOrganisation(eventFroMDatabaseOptional.get().getOrganisation())
                    .setLocation(eventFroMDatabaseOptional.get().getLocation())
                    .setEventTime(eventFroMDatabaseOptional.get().getEventTime())
                    .setNumberOfPlaces(eventFroMDatabaseOptional.get().getNumberOfPlaces())
                    .setCreatedBy(eventFroMDatabaseOptional.get().getCreatedBy());
            // load the bookEvent controller
            stageService.loadBookEvent(student, studentEvent);
        }
    }

    @FXML
    private void searchAttending(ActionEvent event) throws Exception {
        // gets the event title from the textfield
        String title = this.searchAtendingTitle.getText();

        // validate the event's title
        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter the event title");
            alert.show();
            return;
        }

        // search the event in the database
        Optional<Event> eventFroMDatabaseOptional = eventRepository.findByEventTitleAndStudentId(title, student.getId());
        if (!eventFroMDatabaseOptional.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Student");
            alert.setContentText("The event was not found");
            alert.show();
            return;
        } else {
            Event studentEvent = Event.builder()
                    .setId(eventFroMDatabaseOptional.get().getId())
                    .setTitle(eventFroMDatabaseOptional.get().getTitle())
                    .setDescription(eventFroMDatabaseOptional.get().getDescription())
                    .setEventType(eventFroMDatabaseOptional.get().getEventType())
                    .setEventPlace(eventFroMDatabaseOptional.get().getEventPlace())
                    .setUrl(eventFroMDatabaseOptional.get().getUrl())
                    .setOrganisation(eventFroMDatabaseOptional.get().getOrganisation())
                    .setLocation(eventFroMDatabaseOptional.get().getLocation())
                    .setEventTime(eventFroMDatabaseOptional.get().getEventTime())
                    .setNumberOfPlaces(eventFroMDatabaseOptional.get().getNumberOfPlaces())
                    .setCreatedBy(eventFroMDatabaseOptional.get().getCreatedBy());

            // load the cancelBooking controller
            stageService.loadCancelBooking(student, studentEvent);
        }
    }
}
