package student.event.management.ui.listStudents;

import javafx.beans.property.SimpleBooleanProperty;
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
import student.event.management.infrastructure.repository.StudentRepository;
import student.event.management.ui.util.YesOrNoEnum;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListStudentsController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn firstName;
    @FXML
    private TableColumn lastName;
    @FXML
    private TableColumn username;
    @FXML
    private TableColumn hasOrganisationRights;

    private StudentRepository studentRepository;

    private StageService stageService;

    private ObservableList<ListStudentsController.Student> observableStudentList = FXCollections.observableArrayList();

    public ListStudentsController() {
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

        // initialize the columns for the tableView
        initializeColumns();
        try {
            // get all the students from the database
            final List<student.event.management.domain.models.Student> studentsFromDatabase = this.studentRepository.findAll();
            studentsFromDatabase.forEach(student -> {
                observableStudentList.add(new Student(student.getFirstName(), student.getLastName(), student.getUsername(), student.isHasOrganisationRights()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // set the students from the observableList to the tableView
        tableView.setItems(observableStudentList);
    }

    /**
     * handles  the loading of the listBookings controller
     *
     * @param event
     * @throws Exception
     */
    @FXML
    public void viewBookings(ActionEvent event) throws Exception {
        // get the selected student in the tableView
        Student student = (Student) tableView.getSelectionModel().getSelectedItem();

        // validate the student
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select a student to view its bookings");
            alert.showAndWait();
            return;
        }

        // load the listBookings controller
        stageService.loadBookingsListView(student.getFirstName(), student.getLastName());
    }

    /**
     * handles the update of a student in the database granting him organisation rights
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void grantOrganisationRights(ActionEvent event) throws SQLException {
        // get the selected student from the tableView
        Student student = (Student) tableView.getSelectionModel().getSelectedItem();

        // validate the student
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select a student to view its bookings");
            alert.showAndWait();
            return;
        }

        // validate that the student does not have organisation rights already
        if (student.isHasOrganisationRights().equals(YesOrNoEnum.YES.getLabel())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("The student " + student.getLastName() + " " + student.getLastName() + " already has organisation rights");
            alert.show();
            return;
        } else {

            // the user will be prompted with a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Grant organisation rights");
            alert.setContentText("Are you sure you want to grant organisation rights to student " + student.getLastName() + " " + student.getFirstName() + " ?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {

                // the student is updated in the database
                if (studentRepository.updateHasOrganisationRightsByUsername(student.getUsername(), true)) {

                    // the student is also updated in the tableView
                    student.setHasOrganisationRights(YesOrNoEnum.YES.getLabel());

                    tableView.setItems(FXCollections.observableArrayList());
                    tableView.setItems(observableStudentList);

                    // this is a workaround that forces the columns of the tableView to be recreated
                    // this refreshes the data from the tableView
                    ((TableColumn)tableView.getColumns().get(0)).setVisible(false);
                    ((TableColumn)tableView.getColumns().get(0)).setVisible(true);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("The student " + student.getLastName() + " " + student.getLastName() + " was granted organisation rights ");
                    alert.show();

                }
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Operation cancelled");
            }
        }
    }

    /**
     * handles the update of a student in the database revoking him organisation rights
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void revokeOrganisationRights(ActionEvent event) throws SQLException {

        // get the selected student from the tableView
        Student student = (Student) tableView.getSelectionModel().getSelectedItem();

        // validate the student
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select a student to view its bookings");
            alert.showAndWait();
            return;
        }

        // validate that the student does have organisation rights
        if (student.isHasOrganisationRights().equals(YesOrNoEnum.NO.getLabel())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("The student " + student.getLastName() + " " + student.getLastName() + " does not have organisation rights");
            alert.show();
            return;
        } else {

            // the user will be prompted with a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Revoke organisation rights");
            alert.setContentText("Are you sure you want to revoke organisation rights from the student " + student.getLastName() + " " + student.getFirstName() + " ?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {

                // update the student in the database
                if (studentRepository.updateHasOrganisationRightsByUsername(student.getUsername(), false)) {
                    student.setHasOrganisationRights(YesOrNoEnum.NO.getLabel());

                    tableView.setItems(FXCollections.observableArrayList());
                    tableView.setItems(observableStudentList);

                    // this is a workaround that forces the columns of the tableView to be recreated
                    // this refreshes the data from the tableView
                    ((TableColumn)tableView.getColumns().get(0)).setVisible(false);
                    ((TableColumn)tableView.getColumns().get(0)).setVisible(true);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("The student " + student.getLastName() + " " + student.getLastName() + " doesn't have organisation rights anymore");
                    alert.show();

                }
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Operation cancelled");
            }

        }

    }

    /**
     * initializes the table view columns
     */
    private void initializeColumns() {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        hasOrganisationRights.setCellValueFactory(new PropertyValueFactory<>("hasOrganisationRights"));
    }

    /**
     * static inener helper class that handles the tableView properties
     */
    public static class Student {
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty username;
        private final SimpleStringProperty hasOrganisationRights;


        public Student(String firstName, String lastName, String username, boolean hasOrganisationRights) {
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.username = new SimpleStringProperty(username);
            this.hasOrganisationRights = new SimpleStringProperty((hasOrganisationRights ? YesOrNoEnum.YES.getLabel() : YesOrNoEnum.NO.getLabel()));
        }

        public String getFirstName() {
            return firstName.get();
        }

        public String getLastName() {
            return lastName.get();
        }

        public String getUsername() {
            return username.get();
        }

        public String isHasOrganisationRights() {
            return hasOrganisationRights.get();
        }

        public void setFirstName(String firstName) {
            this.firstName.set(firstName);
        }

        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }

        public void setUsername(String username) {
            this.username.set(username);
        }

        public void setHasOrganisationRights(String hasOrganisationRights) {
            this.hasOrganisationRights.set(hasOrganisationRights);
        }
    }
}
