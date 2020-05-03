package student.event.management.ui.register;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import student.event.management.application.service.StageService;
import student.event.management.application.service.util.PasswordEncodingService;
import student.event.management.domain.models.Student;
import student.event.management.infrastructure.repository.StudentRepository;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private AnchorPane rootPane;

    private StageService stageService;

    private StudentRepository studentRepository;

    public RegisterController() {
        this.studentRepository = new StudentRepository();
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
        // set the focus on the firstName textfield
        firstName.selectAll();
        firstName.requestFocus();
    }

    /**
     * handles the register of the user into the application
     *
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void register(javafx.event.ActionEvent actionEvent) throws Exception {

        // get the data from the textfields
        String username = this.username.getText();
        String password = this.password.getText();
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();

        // validate the data
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter all required fields");
            alert.show();
            return;
        }

        // validate that the user with this username does not exist already
        if (!studentRepository.findByUsername(username).isPresent()) {

            // encrypt the password using the SHA-256
            String encodedPassword = PasswordEncodingService.encode(password);

            // create the student object
            Student student = Student.builder()
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setUsername(username)
                    .setPassword(encodedPassword);

            // save the student into the database
            studentRepository.save(student);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("You have successfully registered ");
            alert.showAndWait();

            // go back to the login controller
            stageService.loadLogin();
            Stage stage = (Stage) this.rootPane.getScene().getWindow();
            stage.close();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("This username already exists");
            alert.showAndWait();
        }
    }

    /**
     * closes the current stage
     *
     * @param actionEvent
     */
    @FXML
    public void cancel(javafx.event.ActionEvent actionEvent) throws Exception {
        stageService.loadLogin();
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }
}
