package student.event.management.ui.adminBoard;

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
import student.event.management.infrastructure.repository.StudentRepository;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminBoardController implements Initializable {

    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField searchEventTitle;
    @FXML
    private JFXTextField searchBookingsTitle;
    @FXML
    private StackPane rootPane;

    private StageService stageService;

    private StudentRepository studentRepository;

    private EventRepository eventRepository;

    public AdminBoardController() {
        this.stageService = new StageService();
        this.studentRepository = new StudentRepository();
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
     * loads the listStudents controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void listStudents(ActionEvent event) throws Exception {
        stageService.loadStudentsListView();
    }

    /**
     * loads the listEventsForAdminBoard controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void listEvents(ActionEvent event) throws Exception {
        stageService.loadEventsAdminBoardListView();
    }

    /**
     * logs out the user from the application and closes the stage
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
     * searches the student in the database
     * and if he/she exists load the grantRightsToStudent controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void searchStudent(ActionEvent event) throws Exception {

        // get the values from textfields
        String lastName = this.lastName.getText();
        String firstName = this.firstName.getText();

        // validate the student firstName and lastName
        if (lastName.isEmpty() || firstName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter the event title");
            alert.show();
            return;
        }

        // search the student by firstName and lastName
        List<Student> students = studentRepository.findByLastNameAndFirstName(lastName, firstName);

        // validate that a student with that name exissts
        if (students.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student");
            alert.setContentText("The student was not found");
            alert.show();
            return;
        }

        // if the student exist load the grantRights controller
        if (!students.isEmpty()
                && students.size() == 1) {
            Student student = students.get(0);
            stageService.loadGrantRightsToStudent(student);
        }
    }

    /**
     * searches the event in the database
     * and if it exists load the deleteEvent controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void searchEvent(ActionEvent event) throws Exception {
        // get the event title from the textfield
        String eventTitle = this.searchEventTitle.getText();

        // validate the event title value
        if (eventTitle.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter the event title");
            alert.show();
            return;
        }

        // search the event in the database
        Optional<Event> studentEventOptional = eventRepository.findByTitle(eventTitle);

        // if the event exists open the deleteEvent controller
        if (studentEventOptional.isPresent()) {
            stageService.loadDeleteEvent(studentEventOptional.get());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Event");
            alert.setContentText("The event was not found");
            alert.show();
            return;
        }
    }

    /**
     * searches the bookings for the event in the database
     * and if they exists load the loadBookings controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void searchEventBookings(ActionEvent event) throws Exception {

        // get the event title from the textfield
        String eventTitle = this.searchBookingsTitle.getText();

        // validate the event title value
        if (eventTitle.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please  enter the event title");
            alert.show();
            return;
        }

        // search the event in the database
        Optional<Event> studentEventOptional = eventRepository.findByTitle(eventTitle);

        // if the event exists open the loadBookings controller
        if (studentEventOptional.isPresent()) {
            stageService.loadBookingsListView(studentEventOptional.get());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Event");
            alert.setContentText("The event was not found");
            alert.show();
            return;
        }
    }
}
