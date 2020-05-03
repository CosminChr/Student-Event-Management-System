package student.event.management.ui.studentBoard;

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

public class StudentBoardController implements Initializable {
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXTextField searchAllEventsTitle;
    @FXML
    private JFXTextField searchAtendingTitle;

    private StageService stageService;

    private EventRepository eventRepository;

    private Student student;

    public StudentBoardController(Student student) {
        this.student = student;
        this.stageService = new StageService();
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
     * loads the allEventsStudentBoard controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void listAllEvents(ActionEvent event) throws Exception {
        stageService.loadAllEventsStudentBoardListView(student);
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
     * loads the login controller
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
     * handles the load of bookEvent controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void searchAllEvents(ActionEvent event) throws Exception {

        // get the event title from the textField
        String title = this.searchAllEventsTitle.getText();

        // validate the event title
        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter the event title");
            alert.show();
            return;
        }

        // get the event from the database by its title
        Optional<Event> eventFroMDatabaseOptional = eventRepository.findByTitle(title);
        if (!eventFroMDatabaseOptional.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Student");
            alert.setContentText("The event was not found");
            alert.show();
            return;
        } else {

            // create the event object
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


            // load the bookEvent conroller
            stageService.loadBookEvent(student, studentEvent);
        }
    }

    /**
     * handles the load of cancelBooking controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void searchAttending(ActionEvent event) throws Exception {

        // get the event title from the textfield
        String title = this.searchAtendingTitle.getText();

        // validate the event title
        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter the event title");
            alert.show();
            return;
        }

        // get the event from the database by its title and by studentId
        Optional<Event> eventFroMDatabaseOptional = eventRepository.findByEventTitleAndStudentId(title, student.getId());
        if (!eventFroMDatabaseOptional.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Student");
            alert.setContentText("The event was not found");
            alert.show();
            return;
        } else {

            // create the event object
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
