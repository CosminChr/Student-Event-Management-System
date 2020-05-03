package student.event.management.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import student.event.management.application.service.StageService;
import student.event.management.application.service.util.PasswordEncodingService;
import student.event.management.domain.models.Administrator;
import student.event.management.domain.models.Student;
import student.event.management.infrastructure.repository.AdministratorRepository;
import student.event.management.infrastructure.repository.StudentRepository;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private AnchorPane rootPane;

    private StudentRepository studentRepository;

    private AdministratorRepository administratorRepository;

    private StageService stageService;

    public LoginController() {
        this.studentRepository = new StudentRepository();
        this.administratorRepository = new AdministratorRepository();
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

    }


    /**
     * handles the login of the user into the application
     *
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void login(javafx.event.ActionEvent actionEvent) throws Exception {

        // get the username and the password from the textfields
        String username = this.username.getText();
        String password = this.password.getText();

        // validate the username and password
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter all required fields");
            alert.show();
            return;
        }

        // search the student into the database
        Optional<Student> studentOptional = studentRepository.findByUsername(username);
        if (studentOptional.isPresent()) {

            // encrypt the password using the SHA-256
            String encodedPassword = PasswordEncodingService.encode(password);

            // check the encrypted password introduced by the user matches the password from the database
            if (studentOptional.get().getPassword().equals(encodedPassword)) {

                // close the login stage
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.close();

                // if the student has organisation rights
                if (studentOptional.get().isHasOrganisationRights()) {
                    // load the eventOrganizerBoard controller
                    stageService.loadEventOrganizedBoard(studentOptional.get());
                } else {
                    // load the studentBoard controller
                    stageService.loadStudentBoard(studentOptional.get());
                }
                return;

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Invalid password");
                alert.show();
            }


        }
        // if the user exists in the administrator table
        else if (administratorRepository.findByUsername(username).isPresent()) {
            Optional<Administrator> administratorOptional = administratorRepository.findByUsername(username);

            // encrypt the password using the SHA-256
            String encodedPassword = PasswordEncodingService.encode(password);

            if (administratorOptional.get().getPassword().equals(encodedPassword)) {

                // close the login stage
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.close();

                // load the adminBoard controller
                stageService.loadAdminBoard();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Invalid password");
                alert.show();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("This account does not exists");
            alert.show();
        }
    }

    /**
     * closes the current stage
     *
     * @param event
     */
    @FXML
    public void cancel(javafx.event.ActionEvent event) {
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * loads the register controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    public void register(ActionEvent event) throws Exception {
        stageService.loadRegister();
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

}
