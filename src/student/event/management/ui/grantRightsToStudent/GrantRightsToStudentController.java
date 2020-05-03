package student.event.management.ui.grantRightsToStudent;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import student.event.management.domain.models.Student;
import student.event.management.infrastructure.repository.StudentRepository;
import student.event.management.ui.util.YesOrNoEnum;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GrantRightsToStudentController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXComboBox hasOrganisationRights;

    private Student student;

    private boolean isStudentHasOrganisationRights;

    private StudentRepository studentRepository;

    public GrantRightsToStudentController(Student student) {
        this.student = student;
        studentRepository = new StudentRepository();
    }

    /**
     * initializes the data in the controller
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // disable the controls
        // because the main responsability of this controller
        // is to grant organisation rights to the student and not modify the data
        firstName.setDisable(true);
        firstName.setVisible(true);
        lastName.setDisable(true);
        lastName.setVisible(true);
        username.setDisable(true);
        username.setVisible(true);

        // setup the initial values in the controller
        inflateUi(student);

    }

    /**
     * updates the student into the database setting it's hasOrganisationRights value
     * this function is triggered when the user clicks the save button
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void save(ActionEvent event) throws SQLException {
        // get the actual value of the hasOrganisationRights from the textfield
        String hasOrganisationRights = (String) this.hasOrganisationRights.getValue();

        // only if the user does not have organisation rights already
        // the user is prompted with a confirmation dialog
        if (hasOrganisationRights.equals(YesOrNoEnum.YES.getLabel()) && student.isHasOrganisationRights() == false) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Grant organisation rights");
            alert.setContentText("Are you sure you want to grant organisation rights to student " + student.getLastName() + " " + student.getFirstName() + " ?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {
                // the student is updated in the database
                studentRepository.updateHasOrganisationRightsByUsername(student.getUsername(), !student.isHasOrganisationRights());
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("The student " + student.getLastName() + " " + student.getLastName() + " was granted organisation rights ");
                alert.show();
            }
            return;
        }
        // only if the user does  have organisation rights already
        // the user is prompted with a confirmation dialog
        if (hasOrganisationRights.equals(YesOrNoEnum.NO.getLabel()) && student.isHasOrganisationRights() == true) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Revoke organisation rights");
            alert.setContentText("Are you sure you want to revoke organisation rights from the student " + student.getLastName() + " " + student.getFirstName() + " ?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {
                // the student is updated in the database
                studentRepository.updateHasOrganisationRightsByUsername(student.getUsername(), !student.isHasOrganisationRights());
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("The student " + student.getLastName() + " " + student.getLastName() + " doesn't have organisation rights anymore");
                alert.show();
            }
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
    public void cancel(ActionEvent event) {
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * set the values in the controller using the student received when initialized
     *
     * @param student
     */
    private void inflateUi(Student student) {
        firstName.setText(student.getFirstName());
        lastName.setText(student.getLastName());
        username.setText(student.getUsername());

        hasOrganisationRights.getItems().removeAll(hasOrganisationRights.getItems());
        hasOrganisationRights.getItems().addAll(YesOrNoEnum.YES.getLabel(), YesOrNoEnum.NO.getLabel());
        hasOrganisationRights.getSelectionModel().select(student.isHasOrganisationRights() ? 0 : 1);
        String hasOrganisationRights = (String) this.hasOrganisationRights.getValue();
        this.isStudentHasOrganisationRights = hasOrganisationRights.equals(YesOrNoEnum.YES.getLabel()) ? true : false;
    }
}
