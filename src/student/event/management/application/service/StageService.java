package student.event.management.application.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import student.event.management.domain.models.Event;
import student.event.management.domain.models.Student;
import student.event.management.ui.addEvent.AddEventController;
import student.event.management.ui.addRecurrence.AddRecurrenceController;
import student.event.management.ui.bookEvent.BookEventController;
import student.event.management.ui.cancelBooking.CancelBookingController;
import student.event.management.ui.deleteEvent.DeleteEventController;
import student.event.management.ui.eventOrganizerBoard.EventOrganizerBoardController;
import student.event.management.ui.grantRightsToStudent.GrantRightsToStudentController;
import student.event.management.ui.listAllEventsStudentBoard.ListAllEventsStudentBoardController;
import student.event.management.ui.listAttendingEventsStudentBoard.ListAttendingEventsStudentBoardController;
import student.event.management.ui.listBookings.ListBookingsController;
import student.event.management.ui.listMyEventsStudentBoard.ListMyEventsStudentBoardController;
import student.event.management.ui.studentBoard.StudentBoardController;
import student.event.management.ui.updateEvent.UpdateEventController;
import student.event.management.ui.updateOrDeleteEvent.UpdateOrDeleteEventController;
import student.event.management.ui.viewRecurrence.ViewRecurrenceController;

public class StageService {

    /**
     * load the eventOrganizerBoard controller
     *
     * @param student
     * @throws Exception
     */
    public void loadEventOrganizedBoard(Student student) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/eventOrganizerBoard/eventOrganizerBoard.fxml"));
        EventOrganizerBoardController eventOrganizerBoardController = new EventOrganizerBoardController(student);
        loader.setController(eventOrganizerBoardController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Student Event Management System");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the studentBoard controller
     *
     * @param student
     * @throws Exception
     */
    public void loadStudentBoard(Student student) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/studentBoard/studentBoard.fxml"));
        StudentBoardController studentBoardController = new StudentBoardController(student);
        loader.setController(studentBoardController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Student Event Management System");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the adminBoardController
     *
     * @throws Exception
     */
    public void loadAdminBoard() throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/student/event/management/ui/adminBoard/adminBoard.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Student Event Management System");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the eventsAdminBoard controller
     *
     * @throws Exception
     */
    public void loadEventsAdminBoardListView() throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/student/event/management/ui/listEventsAdminBoard/listEventsAdminBoard.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Events");
        stage.setScene(new Scene(parent));
        stage.show();

    }

    /**
     * load the allEventsStudentBoard controller
     *
     * @param student
     * @throws Exception
     */
    public void loadAllEventsStudentBoardListView(Student student) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/listAllEventsStudentBoard/listAllEventsStudentBoard.fxml"));
        ListAllEventsStudentBoardController listAllEventsStudentBoardController = new ListAllEventsStudentBoardController(student);
        loader.setController(listAllEventsStudentBoardController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("All events");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the attendingEventsStudentBoard controller
     *
     * @param student
     * @throws Exception
     */
    public void loadAttendingEventsStudentBoardListView(Student student) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/listAttendingEventsStudentBoard/listAttendingEventsStudentBoard.fxml"));
        ListAttendingEventsStudentBoardController listAttendingEventsStudentBoardController = new ListAttendingEventsStudentBoardController(student);
        loader.setController(listAttendingEventsStudentBoardController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Attending events");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the myEventsStudentBoard controller
     *
     * @param student
     * @throws Exception
     */
    public void loadMyEventsStudentBoardListView(Student student) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/listMyEventsStudentBoard/listMyEventsStudentBoard.fxml"));
        ListMyEventsStudentBoardController listMyEventsStudentBoardController = new ListMyEventsStudentBoardController(student);
        loader.setController(listMyEventsStudentBoardController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("My events");
        stage.setScene(new Scene(parent));
        stage.show();
    }


    /**
     * load the listStudents controller
     *
     * @throws Exception
     */
    public void loadStudentsListView() throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/student/event/management/ui/listStudents/listStudents.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Students");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the listBookings controller
     *
     * @param studentFirstName
     * @param studentLastName
     * @throws Exception
     */
    public void loadBookingsListView(String studentFirstName, String studentLastName) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/listBookings/listBookings.fxml"));
        ListBookingsController listBookingsController = new ListBookingsController(studentFirstName, studentLastName);
        loader.setController(listBookingsController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Bookings");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the listBookings controller
     *
     * @param event
     * @throws Exception
     */
    public void loadBookingsListView(Event event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/listBookings/listBookings.fxml"));
        ListBookingsController listBookingsController = new ListBookingsController(event);
        loader.setController(listBookingsController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Bookings");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the grantRightsToStudent controller
     *
     * @param student
     * @throws Exception
     */
    public void loadGrantRightsToStudent(Student student) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/grantRightsToStudent/grantRightsToStudent.fxml"));
        GrantRightsToStudentController grantRightsToStudentController = new GrantRightsToStudentController(student);
        loader.setController(grantRightsToStudentController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Grant/Revoke user organisation rights");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the delete event controller
     *
     * @param event
     * @throws Exception
     */
    public void loadDeleteEvent(Event event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management//ui/deleteEvent/deleteEvent.fxml"));
        DeleteEventController deleteEventController = new DeleteEventController(event);
        loader.setController(deleteEventController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Delete event");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the register controller
     *
     * @throws Exception
     */
    public void loadRegister() throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/student/event/management/ui/register/register.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the login controller
     *
     * @throws Exception
     */
    public void loadLogin() throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/student/event/management/ui/login/login.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Login");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the updateEvent controller
     *
     * @param event
     * @param student
     * @param listMyEventsStudentBoardController
     * @throws Exception
     */
    public void loadUpdateEvent(Event event, Student student, ListMyEventsStudentBoardController listMyEventsStudentBoardController) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/updateEvent/updateEvent.fxml"));
        UpdateEventController updateEventController = new UpdateEventController(event, student, listMyEventsStudentBoardController);
        loader.setController(updateEventController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update event");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the addEvent controller
     *
     * @param student
     * @throws Exception
     */
    public void loadAddEvent(Student student) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/addEvent/addEvent.fxml"));
        AddEventController addEventController = new AddEventController(student);
        loader.setController(addEventController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add event");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the updateOrDeleteEvent controller
     *
     * @param event
     * @param student
     * @throws Exception
     */
    public void loadUpdateOrDeleteEvent(Event event, Student student) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/updateOrDeleteEvent/updateOrDeleteEvent.fxml"));
        UpdateOrDeleteEventController updateOrDeleteEventController = new UpdateOrDeleteEventController(event, student);
        loader.setController(updateOrDeleteEventController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update or delete event");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the bookEvent controller
     *
     * @param student
     * @param event
     * @throws Exception
     */
    public void loadBookEvent(Student student, Event event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/bookEvent/bookEvent.fxml"));
        BookEventController bookEventController = new BookEventController(student, event);
        loader.setController(bookEventController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Book event");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the cancelBooking controller
     *
     * @param student
     * @param event
     * @throws Exception
     */
    public void loadCancelBooking(Student student, Event event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/cancelBooking/cancelBooking.fxml"));
        CancelBookingController cancelBookingController = new CancelBookingController(student, event);
        loader.setController(cancelBookingController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Cancel booking");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the addRecurence controller
     *
     * @param addEventController
     * @throws Exception
     */
    public void loadAddRecurrence(AddEventController addEventController) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/addRecurrence/addRecurrence.fxml"));
        AddRecurrenceController addRecurrenceController = new AddRecurrenceController(addEventController);
        loader.setController(addRecurrenceController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add recurrence");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the addRecurrence controller
     *
     * @param updateEventController
     * @throws Exception
     */
    public void loadAddRecurrence(UpdateEventController updateEventController) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/addRecurrence/addRecurrence.fxml"));
        AddRecurrenceController addRecurrenceController = new AddRecurrenceController(updateEventController);
        loader.setController(addRecurrenceController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add recurrence");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the addRecurrence controller
     *
     * @param updateOrDeleteEventController
     * @throws Exception
     */
    public void loadAddRecurrence(UpdateOrDeleteEventController updateOrDeleteEventController) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/addRecurrence/addRecurrence.fxml"));
        AddRecurrenceController addRecurrenceController = new AddRecurrenceController(updateOrDeleteEventController);
        loader.setController(addRecurrenceController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add recurrence");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the viewRecurrence controller
     *
     * @param deleteEventController
     * @throws Exception
     */
    public void loadViewRecurrence(DeleteEventController deleteEventController) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/viewRecurrence/viewRecurrence.fxml"));
        ViewRecurrenceController viewRecurrenceController = new ViewRecurrenceController(deleteEventController);
        loader.setController(viewRecurrenceController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("View recurrence");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the viewRecurrence controller
     *
     * @param bookEventController
     * @throws Exception
     */
    public void loadViewRecurrence(BookEventController bookEventController) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/viewRecurrence/viewRecurrence.fxml"));
        ViewRecurrenceController viewRecurrenceController = new ViewRecurrenceController(bookEventController);
        loader.setController(viewRecurrenceController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("View recurrence");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * load the viewRecurrence controller
     *
     * @param cancelBookingController
     * @throws Exception
     */
    public void loadViewRecurrence(CancelBookingController cancelBookingController) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/student/event/management/ui/viewRecurrence/viewRecurrence.fxml"));
        ViewRecurrenceController viewRecurrenceController = new ViewRecurrenceController(cancelBookingController);
        loader.setController(viewRecurrenceController);
        Parent parent = loader.load();
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("View recurrence");
        stage.setScene(new Scene(parent));
        stage.show();
    }
}
