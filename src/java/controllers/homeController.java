package tp.javafx.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.calendarfx.view.CalendarView;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.LocalTimeStringConverter;
import org.controlsfx.control.CheckComboBox;
import tp.javafx.Main;
import tp.javafx.models.*;

public class homeController implements Initializable {

    public Pane project;

    public Pane projectToAddPane;

    @FXML
    public Pane settingsCustom;
    public Button custom;

    @FXML
    public Label projectTitle;

    @FXML
    public Label projectDescription;

    @FXML
    public Pane tasksCalendar;

    @FXML
    public CalendarView calendarView;

    private Utilisateur user;

    private Boolean auth;

    @FXML
    private VBox projectsVbox;

    @FXML
    private TextField heuresDebut;

    @FXML
    private TextField minutesDebut;

    @FXML
    private Pane account;

    @FXML
    private TextField heuresFin;

    @FXML
    private TextField minutesFin;

    @FXML
    private TextField dureeDebut;

    @FXML
    private TextField dureeFin;

    @FXML
    private Label goodLB;

    @FXML
    private Label veryGoodLB;

    @FXML
    private Label excellentLB;

    public homeController(Utilisateur user, Boolean auth) {
        this.user = user;
        this.auth = auth;
    }

    @FXML
    public Label username;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Pane tasksTable;

    @FXML
    private Pane settings;

    @FXML
    private Pane projects;

    @FXML
    private Label date;

    @FXML
    private Label todayOrNot;

    @FXML
    private Button back;

    @FXML
    private DatePicker startDate1;

    @FXML
    private DatePicker endDate1;

    @FXML
    private VBox daysList;

    @FXML
    private TableView<TacheObservable> projectTableView;

    @FXML
    private TableColumn<TacheObservable, String> tacheProjet;

    @FXML
    private TableColumn<TacheObservable, String> tempsProjet;

    @FXML
    private TableColumn<TacheObservable, LocalDate> deadlineProjet;

    @FXML
    private TableColumn<TacheObservable, String> prioProjet;

    @FXML
    private TableColumn<TacheObservable, String> etatProjet;

    @FXML
    private TableColumn<TacheObservable, Void> actionsProjet;

    @FXML
    private TableView<TacheObservable> tachesTable;

    @FXML
    private TableColumn<TacheObservable, String> tacheName;

    @FXML
    private TableColumn<TacheObservable, String> tacheDuration;

    @FXML
    private TableColumn<TacheObservable, LocalDate> tacheDeadline;

    @FXML
    private TableColumn<TacheObservable, String> tachePriority;

    @FXML
    private TableColumn<TacheObservable, String> tacheEtat;

    @FXML
    private TableColumn<TacheObservable, Void> tacheActions;

    @FXML
    private TextField projectTF;
    @FXML
    private TextArea projectTA;

    @FXML
    private Label usernameLB;

    ObservableList<TacheObservable> taches = FXCollections.observableArrayList();
    ObservableList<TacheObservable> tachesProjet = FXCollections.observableArrayList();
    Projet projet = null;


    LocalDate localDate = LocalDate.now();
    LocalDate[] dates = {
            LocalDate.now(),
            LocalDate.now().plusDays(1),
    };

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }

    private void setDate() {
        date.setText(capitalizeFirstLetter(localDate.getDayOfWeek().toString()) + " " + capitalizeFirstLetter(localDate.getMonth().toString()) + " " + localDate.getDayOfMonth());
        if (localDate.equals(LocalDate.now())) {
            back.setDisable(true);
            todayOrNot.setText("Today");
        } else {
            back.setDisable(false);
            todayOrNot.setText("Tasks");
        }
    }

    @FXML
    private void swipeDayForward(ActionEvent event) throws IOException, SQLException {
        localDate = localDate.plusDays(1);
        setDate();
        refresh();
    }

    @FXML
    private void swipeDayBackwards(ActionEvent event) throws IOException, SQLException {
        localDate = localDate.minusDays(1);
        setDate();
        refresh();
    }

    @FXML
    private void switchToProjects(MouseEvent event) throws IOException {
        if (auth) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Configuration");
            alert.setHeaderText("Options de configuration");
            alert.setContentText("Etes vous sur de vouloir quitter?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                try {
                    Alert configAlert = new Alert(Alert.AlertType.INFORMATION);
                    configAlert.setTitle("Configuration actuelle");
                    configAlert.setHeaderText("Configuration par défaut");
                    configAlert.setContentText("Date Début: " + LocalDate.now() + "\n" +
                            "Date Fin: " + LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()) + "\n" +
                            "Créneau par défaut: 20:30 - 23:00\n" +
                            "Durée minimale d'une tache: 30 minutes");
                    configAlert.showAndWait();

                    ArrayList<Jour> jours = new ArrayList<>();

                    // add jours for the rest of the month and for each jour add a creneau of 20:30 - 23:00 and duration of 30 minutes

                    for (int i = LocalDate.now().getDayOfMonth(); i <= LocalDate.now().lengthOfMonth(); i++) {
                        Jour jour = new Jour(LocalDate.now().withDayOfMonth(i));
                        Creneau creneau = new Creneau(LocalTime.of(20, 30), LocalTime.of(23, 0), Duration.ofHours(0).plusMinutes(30));
                        jour.ajouterCreneau(creneau);
                        jours.add(jour);
                    }
                    user.setJours(jours);

                    user.setDateDebut(LocalDate.now());

                    user.setDateFin(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));

                    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

                    try {
                        utilisateurDAO.setConfiguration(user);
                        auth = false;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
        projects.toFront();

        projectsVbox.getChildren().clear();

        for (Projet projet1 : user.getProjets()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tp/javafx/projet.fxml"));
            HBox specialHBox = null;
            try {
                specialHBox = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Label projectName = (Label) specialHBox.getChildren().get(0);
            projectName.setText(projet1.getNom());
            projectName.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    project.toFront();
                    projet = projet1;
                    projectTitle.setText(projet.getNom());
                    projectDescription.setText(projet.getDescription());
                    tachesProjet.removeAll(tachesProjet);
                    for (Tache tache : projet.getTaches()) {
                        for (Jour jour : user.getJours()) {
                            for (Creneau creneau : jour.getCreneaux()) {
                                LocalTime debut = creneau.getDebut();
                                for (Tache tache1 : creneau.getTaches()) {
                                    debut = debut.minus(tache1.getDuree());
                                }
                                for (Tache tache1 : creneau.getTaches()) {
                                    debut = debut.plus(tache1.getDuree());
                                    if (tache1.getNom().equals(tache.getNom()) && tache1.getDuree().equals(tache.getDuree()) && tache1.getDeadline().equals(tache.getDeadline()) && tache1.getCategorie().getNom_cat().equals(tache.getCategorie().getNom_cat())) {
                                        tachesProjet.add(new TacheObservable(tache.getNom(), tache.getDuree(), tache.getPrio(), tache.getDeadline(), tache.getCategorie(), tache.getEtat(), (debut + " - " + debut.plus(tache.getDuree()))));
                                    }
                                }
                            }
                        }
                    }
                }
            });
            projectsVbox.getChildren().add(specialHBox);
        }


    }

    @FXML
    private void addNewProject(ActionEvent event) throws IOException {
        projectToAddPane.toFront();
    }

    private String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
                .toUpperCase();
    }

    @FXML
    private void addCategory(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tp/javafx/newCategorie.fxml"));
        Parent root = loader.load();

        newCategoryController newCategoryController = loader.getController();


        // transform color to hex and add it to the database

        newCategoryController.setSaveButtonAction(saveEvent -> {

            Color couleure = newCategoryController.colorPicker.getValue();
            String hex = "#" + Integer.toHexString(couleure.hashCode());
            String nom = newCategoryController.categorieNom.getText();

            Categorie categorie = new Categorie(nom, hex);

            if (nom.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de saisie");
                alert.setContentText("Veuillez remplir le nom");
                alert.showAndWait();
                return;
            }
            user.ajouter_categorie(categorie);

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
            try {
                utilisateurDAO.addCategorie(categorie, user);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // hide the window
            Stage stage = (Stage) newCategoryController.submit.getScene().getWindow();
            stage.close();

        });

        Stage catStage = new Stage();
        Scene catScene = new Scene(root);
        catStage.setTitle("Add New Task");
        catStage.setScene(catScene);
        catStage.show();

    }

    @FXML
    private void createNewProject(ActionEvent event) throws IOException, SQLException {

        String name = projectTF.getText();
        String description = projectTA.getText();

        if (name.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de saisie");
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return;
        }

        Projet projetAajouter = new Projet(name, description);

        user.addProjet(projetAajouter);

        project.toFront();
        projectTitle.setText(projetAajouter.getNom());
        projectDescription.setText(projetAajouter.getDescription());

        projet = projetAajouter;
    }

    @FXML
    private void switchToTasksTable(Event event) throws IOException {
        projet = null;
        tasksTable.toFront();
    }

    @FXML
    private void switchToTasksCalendar(Event event) throws IOException {
        projet = null;
        if (auth) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Configuration");
            alert.setHeaderText("Options de configuration");
            alert.setContentText("Etes vous sur de vouloir quitter?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                try {
                    Alert configAlert = new Alert(Alert.AlertType.INFORMATION);
                    configAlert.setTitle("Configuration actuelle");
                    configAlert.setHeaderText("Configuration par défaut");
                    configAlert.setContentText("Date Début: " + LocalDate.now() + "\n" +
                            "Date Fin: " + LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()) + "\n" +
                            "Créneau par défaut: 20:30 - 23:00\n" +
                            "Durée minimale d'une tache: 30 minutes");
                    configAlert.showAndWait();

                    ArrayList<Jour> jours = new ArrayList<>();

                    // add jours for the rest of the month and for each jour add a creneau of 20:30 - 23:00 and duration of 30 minutes

                    for (int i = LocalDate.now().getDayOfMonth(); i <= LocalDate.now().lengthOfMonth(); i++) {
                        Jour jour = new Jour(LocalDate.now().withDayOfMonth(i));
                        Creneau creneau = new Creneau(LocalTime.of(20, 30), LocalTime.of(23, 0), Duration.ofHours(0).plusMinutes(30));
                        jour.ajouterCreneau(creneau);
                        jours.add(jour);
                    }
                    user.setJours(jours);

                    user.setDateDebut(LocalDate.now());

                    user.setDateFin(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));

                    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

                    try {
                        utilisateurDAO.setConfiguration(user);
                        auth = false;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
        tasksCalendar.toFront();
    }

    private void setMinimumSelectableDate(DatePicker datePicker, LocalDate minDate) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(minDate));
            }
        });
    }

    @FXML
    private void switchToSettings(MouseEvent event) throws IOException {
        projet = null;
        startDate1.setValue(user.getDateDebut());
        dates[0] = user.getDateDebut();
        setMinimumSelectableDate(startDate1, dates[0]);
        dates[1] = user.getDateFin();
        endDate1.setValue(user.getDateFin());
        setMinimumSelectableDate(endDate1, dates[0].plusDays(1));
        settings.toFront();
    }

    @FXML
    private void switchToSettings2(ActionEvent event) throws IOException {
        projet = null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sur d'annuler les changements?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            settings.toFront();
        }
    }

    private List<LocalDate> getDatesInRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> datesInRange = new ArrayList<>();
        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);

        for (int i = 0; i <= numOfDays; i++) {
            LocalDate date = startDate.plusDays(i);
            datesInRange.add(date);
        }

        return datesInRange;
    }

    private TextField createNumericTextField(int type) {
        TextField textField = new TextField();

        // Restrict input to numeric values using regex
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            if (type == 0) {
                int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                int maxValue = 23;
                if (value > maxValue) {
                    textField.setText(Integer.toString(maxValue));
                }
            } else if (type == 1) {
                int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                int maxValue = 59;
                if (value > maxValue) {
                    textField.setText(Integer.toString(maxValue));
                }
            }
        });

        return textField;
    }


    // Helper method to show an alert for invalid input
    private void showInvalidInputAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText("Please enter valid numeric values for hours and minutes.");
        alert.showAndWait();
    }

    @FXML
    private void switchToCustomSettings(ActionEvent event) throws IOException {

        projet = null;

        List<LocalDate> datesInRange = getDatesInRange(startDate1.getValue(), endDate1.getValue());
        ArrayList<Jour> jours = new ArrayList<>();
        datesInRange.forEach(date -> {
            Jour jour = new Jour(date);
            jours.add(jour);
        });

        user.getJours().forEach(jour -> {
            for (Jour jour1 : jours) {
                if (jour1.getDate().equals(jour.getDate())) {
                    jour1.setCreneaux(jour.getCreneaux());
                }
            }
        });

        user.setJours(jours);
        daysList.getChildren().clear();

        for (int i = 0; i < jours.size(); i++) {
            Jour jour = jours.get(i);
            LocalDate date = jour.getDate();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tp/javafx/crenau.fxml"));
            HBox specialHBox = null;
            try {
                specialHBox = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Label label = (Label) specialHBox.getChildren().get(0);
            label.setText(capitalizeFirstLetter(date.getDayOfWeek().toString()) + " " + capitalizeFirstLetter(date.getMonth().toString()) + " " + date.getDayOfMonth());
            daysList.getChildren().add(specialHBox);

            Button button = (Button) specialHBox.getChildren().get(3);


            HBox finalSpecialHBox = specialHBox;

            jour.getCreneaux().forEach(creneau -> {
                ScrollPane scrollPane = (ScrollPane) finalSpecialHBox.getChildren().get(2);
                AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
                HBox hBox = (HBox) anchorPane.getChildren().get(0);

                Label timeSlotLabel = new Label(creneau.getDebut().getHour() + ":" + creneau.getDebut().getMinute() + "-" + creneau.getFin().getHour() + ":" + creneau.getFin().getMinute());
                timeSlotLabel.setFont(new Font("Amber EN", 15.0));
                hBox.getChildren().add(timeSlotLabel);
            });

            int finalI = i;
            button.setOnAction(e -> {
                ScrollPane scrollPane = (ScrollPane) finalSpecialHBox.getChildren().get(2);
                AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
                HBox hBox = (HBox) anchorPane.getChildren().get(0);

                Dialog<Creneau> dialog = new Dialog<>();
                dialog.setTitle("Ajouter un créneau horaire");
                dialog.setHeaderText("Entrer les heures de début et de fin du créneau horaire");

                // Set the button types
                ButtonType saveButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                // Create the text fields
                TextField startHourField = createNumericTextField(0);
                TextField startMinuteField = createNumericTextField(1);
                TextField endHourField = createNumericTextField(0);
                TextField endMinuteField = createNumericTextField(1);
                TextField tSlotHoursField = createNumericTextField(0);
                TextField tSlotMinutesField = createNumericTextField(1);

                // Create a grid pane for arranging the text fields
                GridPane grid = new GridPane();
                grid.add(new Label("Heure début:"), 0, 0);
                grid.add(startHourField, 1, 0);
                grid.add(new Label(":"), 2, 0);
                grid.add(startMinuteField, 3, 0);
                grid.add(new Label("Heure Fin:"), 0, 1);
                grid.add(endHourField, 1, 1);
                grid.add(new Label(":"), 2, 1);
                grid.add(endMinuteField, 3, 1);
                grid.add(new Label("Durée minimale de tache:"), 0, 2);
                grid.add(tSlotHoursField, 1, 2);
                grid.add(new Label(":"), 2, 2);
                grid.add(tSlotMinutesField, 3, 2);

                dialog.getDialogPane().setContent(grid);

                // Request focus on the start hour field by default
                Platform.runLater(() -> startHourField.requestFocus());

                // Convert the result to a TimeSlot object when the Save button is clicked
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == saveButtonType) {
                        try {
                            String startHour = startHourField.getText();
                            String startMinute = startMinuteField.getText();
                            String endHour = endHourField.getText();
                            String endMinute = endMinuteField.getText();
                            Integer tSlotHours = Integer.parseInt(tSlotHoursField.getText());
                            Integer tSlotMinutes = Integer.parseInt(tSlotMinutesField.getText());


                            // Create and return the TimeSlot object
                            return new Creneau(LocalTime.parse(startHour + ":" + startMinute), LocalTime.parse(endHour + ":" + endMinute), Duration.ofHours(tSlotHours).plusMinutes(tSlotMinutes));
                        } catch (NumberFormatException exception) {
                            // Handle invalid input
                            showInvalidInputAlert();
                        }
                    }
                    return null;
                });

                Optional<Creneau> result = dialog.showAndWait();
                Creneau creneau = null;
                if (result.isPresent()) {
                    creneau = result.get();
                    try {
                        jours.get(finalI).ajouterCreneau(creneau);
                        Label timeSlotLabel = new Label(creneau.getDebut().getHour() + ":" + creneau.getDebut().getMinute() + "-" + creneau.getFin().getHour() + ":" + creneau.getFin().getMinute());
                        timeSlotLabel.setFont(new Font("Amber EN", 15.0));
                        hBox.getChildren().add(timeSlotLabel);
                    } catch (IllegalArgumentException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Le créneau chevauche un autre créneau déjà existant");
                        alert.setHeaderText(null);
                        alert.setContentText(ex.getMessage());
                        alert.showAndWait();
                    } catch (ExceptionInInitializerError ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Le début de créneau doit être avant la fin de créneau, et la durée minimale de tache doit être inférieure à la moitié durée du créneau");
                        alert.setHeaderText(null);
                        alert.setContentText(ex.getMessage());
                        alert.showAndWait();
                    }
                } else {
                    // Handle case when user cancels the dialog
                }
            });
        }
        settingsCustom.toFront();
    }

    @FXML
    private void startDateChange(ActionEvent event) throws IOException {
        if (endDate1.getValue().compareTo(startDate1.getValue()) <= 0) {
            endDate1.setValue(startDate1.getValue().plusDays(1));
            dates[1] = startDate1.getValue().plusDays(1);
        }
        setMinimumSelectableDate(endDate1, startDate1.getValue().plusDays(1));
        dates[0] = startDate1.getValue();
    }

    @FXML
    private void endDateChange(ActionEvent event) throws IOException {
        dates[1] = endDate1.getValue();
    }

    @FXML
    private void handleAccount(MouseEvent event) throws IOException {
        account.toFront();
        usernameLB.setText("Username: " + user.getPseudo());

        Integer good = 0;
        for (Badge badge : user.getBadges()) {
            if (badge.equals(Badge.Good)) {
                good++;
            }
        }
        goodLB.setText(good.toString());
        Integer veryGood = 0;
        for (Badge badge : user.getBadges()) {
            if (badge.equals(Badge.VeryGood)) {
                veryGood++;
            }
        }
        veryGoodLB.setText(veryGood.toString());
        Integer excellent = 0;
        for (Badge badge : user.getBadges()) {
            if (badge.equals(Badge.Excellent)) {
                excellent++;
            }
        }
        excellentLB.setText(excellent.toString());
    }

    @FXML
    private void handleSignOut(MouseEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("/tp/javafx/login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void createNew(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tp/javafx/newTask.fxml"));
        Parent root = loader.load();

        newTaskController newTaskController = loader.getController();
        setMinimumSelectableDate(newTaskController.deadlineDP, LocalDate.now());

        user.getJours().forEach(jour -> {
            newTaskController.addJour(jour.getDate().toString());
        });

        newTaskController.jourCB.getItems().add(0, "Choissisez un jour");

        user.getCategories().forEach(categorie -> {
            newTaskController.addCategorie(categorie.getNom_cat());
        });

        newTaskController.setAddCreneaux(saveEvent -> {

            newTaskController.creneauCB.getItems().clear();

            newTaskController.creneauCB.getItems().add(0, "Choissisez un créneau");
            LocalDate jour = LocalDate.parse(newTaskController.getJour());

            if (newTaskController.heures.getText().equals("") || newTaskController.minutes.getText().equals("")) {
                user.getJours().forEach(j -> {
                    if (j.getDate().equals(jour)) {
                        j.getCreneaux().forEach(c -> {
                            newTaskController.addCreneau(c.getDebut().toString() + "-" + c.getFin().toString());
                        });
                    }
                });
            } else {
                Duration duree = Duration.ofHours(Long.parseLong(newTaskController.heures.getText())).plusMinutes(Long.parseLong(newTaskController.minutes.getText()));

                user.getJours().forEach(j -> {
                    if (j.getDate().equals(jour)) {
                        j.getCreneaux().forEach(c -> {
                            if (Duration.between(c.getDebut(), c.getFin()).compareTo(duree) >= 0) {
                                newTaskController.addCreneau(c.getDebut().toString() + "-" + c.getFin().toString());
                            }
                        });
                    }
                });

            }
        });

        newTaskController.setSaveButtonAction(saveEvent -> {

            // Information about the new task
            if (newTaskController.getNom().equals("") || newTaskController.getDeadline() == null || newTaskController.getCategorie() == null || newTaskController.heures.getText().equals("") || newTaskController.minutes.getText().equals("") || newTaskController.getPriorite() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Veuillez remplir tous les champs obligatoires notés par *");
                alert.showAndWait();
                return;
            }

            String taskName = newTaskController.getNom();
            LocalDate deadline = newTaskController.getDeadline();
            String category = newTaskController.getCategorie();
            Priorite priorite = newTaskController.getPriorite();
            Duration duree = Duration.ofHours(Long.parseLong(newTaskController.heures.getText())).plusMinutes(Long.parseLong(newTaskController.minutes.getText()));
            // Information if the time should be saved completely for the task or not
            Boolean creneauLibre = newTaskController.getBloque();

            // jour of the task
            String jour = newTaskController.getJour();
            // creneau of the task
            String creneau = newTaskController.getCreneau();

            // information if task is simple or not
            Boolean isRepeating = newTaskController.getRepeat();
            // information about the repetition of the task
            String repetition = newTaskController.getPeriode();

            // information if task is composed or not
            Boolean isComposed = newTaskController.getCompose();


            // Information about the new task's time slot and the day it should be scehduled on ManuelPlanification if neither the jour is specified nor the creneau inform the user that he has the option to plan the task atuomatically
            if (jour == null && !isRepeating && !isComposed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Voulez-vous planifier la tâche automatiquement ?");
                alert.setContentText("Si vous cliquez sur OK, la tâche sera planifiée automatiquement");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {


                    Categorie cat = user.getCategories().stream().filter(c -> c.getNom_cat().equals(category)).findFirst().get();

                    Tache tache = new Tache(taskName, duree, priorite, deadline, cat, Etat.notRealized);

                    Boolean planned = false;

                    try {
                        if (projet != null) {
                            planned = user.planifierAuto(tache, creneauLibre, projet);
                        } else {
                            planned = user.planifierAuto(tache, creneauLibre);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if (planned) {
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Information");
                        alert2.setHeaderText("La tâche a été planifiée avec succès");
                        alert2.showAndWait();
                    } else if (user.findAvailableCreneauTime(tache).equals(Duration.ZERO)) {
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Erreur");
                        alert2.setHeaderText("La tâche n'a pas été planifiée à cause de rupture de créneaux libres");
                        alert2.showAndWait();
                    } else {
                        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                        alert2.setTitle("Information");
                        alert2.setHeaderText("la tache n'a pas été planifiée mais vous pouvez la planifier comme étant une tache composée");
                        // give two options to the user
                        alert2.setContentText("Voulez-vous planifier la tâche comme étant une tâche composée ?");
                        ButtonType buttonTypeOne = new ButtonType("Oui");
                        ButtonType buttonTypeTwo = new ButtonType("Non");
                        alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                        Optional<ButtonType> result2 = alert2.showAndWait();
                        if (result2.get() == buttonTypeOne) {
                            Tcomposee tacheComposee = new Tcomposee(taskName, duree, user.findAvailableCreneauTime(tache), priorite, deadline, cat);
                            if (projet != null) {
                                try {
                                    user.planifierAutoTacheComposee(tacheComposee, creneauLibre, projet);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                try {
                                    user.planifierAutoTacheComposee(tacheComposee, creneauLibre);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            } else if (jour != null && creneau == null && !isRepeating && !isComposed) {
                LocalDate jourForTache = LocalDate.parse(newTaskController.getJour());
                Jour jourTache = user.getJours().stream().filter(j -> j.getDate().equals(jourForTache)).findFirst().get();

                Categorie cat = user.getCategories().stream().filter(c -> c.getNom_cat().equals(category)).findFirst().get();

                Tache tache = new Tache(taskName, duree, priorite, deadline, cat, Etat.notRealized);

                // find if there's creneau that can take the task

                jourTache.getCreneaux().forEach(c -> {
                    if (Duration.between(c.getDebut(), c.getFin()).compareTo(duree) >= 0) {
                        c.setLibre(creneauLibre);
                        try {
                            if (projet != null) {
                                user.planifierManuelTache(jourTache, c, tache, projet);
                            } else {
                                user.planifierManuelTache(jourTache, c, tache);
                            }

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                // inform the user no creneau is available
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Aucun créneau n'est disponible pour cette tâche dans ce jour");
                alert.showAndWait();
                return;
            } else if (jour != null && creneau != null && !isRepeating && !isComposed) {
                LocalDate jourForTache = LocalDate.parse(newTaskController.getJour());
                Jour jourTache = user.getJours().stream().filter(j -> j.getDate().equals(jourForTache)).findFirst().get();

                // find the creneau knowing that The string creneau is in the format "HH:MM-HH:MM"

                String[] creneauSplit = creneau.split("-");
                LocalTime debut = LocalTime.parse(creneauSplit[0]);
                LocalTime fin = LocalTime.parse(creneauSplit[1]);

                Creneau creneauTache = jourTache.getCreneaux().stream().filter(c -> c.getDebut().equals(debut) && c.getFin().equals(fin)).findFirst().get();

                creneauTache.setLibre(creneauLibre);

                Categorie cat = user.getCategories().stream().filter(c -> c.getNom_cat().equals(category)).findFirst().get();

                Tache tache = new Tache(taskName, duree, priorite, deadline, cat, Etat.notRealized);

                try {
                    if (projet != null)
                        user.planifierManuelTache(jourTache, creneauTache, tache, projet);
                    else
                        user.planifierManuelTache(jourTache, creneauTache, tache);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } else if (jour == null || jour.equals("Choissisez un jour")) {
                if (isRepeating) {
                    if (repetition.equals("")) {
                        // alert to inform user that he needs to choose a period
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Veuillez choisir une période");
                        alert.showAndWait();
                        return;
                    }

                    Integer period = Integer.parseInt(repetition);

                    Categorie cat = user.getCategories().stream().filter(c -> c.getNom_cat().equals(category)).findFirst().get();

                    Tsimple tache = new Tsimple(taskName, duree, priorite, deadline, cat, period);

                    Boolean planned = false;

                    try {
                        if (projet != null) {
                            planned = user.planifierAutoTacheSimple(tache, creneauLibre, projet);
                        } else {
                            planned = user.planifierAutoTacheSimple(tache, creneauLibre);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                    if (planned) {
                        // inform the user the task is planned
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("La tâche a été planifiée avec succès");
                        alert.showAndWait();
                    } else {
                        // inform the user no creneau is available
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Aucun créneau n'est disponible pour cette tâche dans cette période");
                        alert.showAndWait();
                        return;
                    }

                } else if (isComposed) {

                    if (newTaskController.heures1.getText().equals("") || newTaskController.minutes1.getText().equals("")) {
                        // alert to inform user that he needs to choose the duration
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Veuillez choisir la durée des sous tâches");
                        alert.showAndWait();
                        return;
                    }

                    Duration dureeSous = Duration.ofHours(Long.parseLong(newTaskController.heures1.getText())).plusMinutes(Long.parseLong(newTaskController.minutes1.getText()));

                    Categorie cat = user.getCategories().stream().filter(c -> c.getNom_cat().equals(category)).findFirst().get();

                    Tcomposee tache = new Tcomposee(taskName, duree, dureeSous, priorite, deadline, cat);

                    Boolean planned = false;

                    try {
                        if (projet != null) {
                            planned = user.planifierAutoTacheComposee(tache, creneauLibre, projet);
                        } else {
                            planned = user.planifierAutoTacheComposee(tache, creneauLibre);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if (planned) {
                        // inform the user the task is planned
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("La tâche a été planifiée avec succès");
                        alert.showAndWait();
                    } else if (!planned && !user.findAvailableCreneauTime(tache).equals(Duration.ZERO)) {
                        // inform the user no creneau is available
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Aucun créneau n'est disponible pour cette tâche dans cette période");
                        alert.showAndWait();
                        return;
                    } else {
                        // inform the user he can still plan the task automatically and give him two options
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText("Les sous taches ont excédé la période choisie");
                        alert.setContentText("Voulez vous planifier automatiquement cette tâche dans une autre période?");
                        ButtonType buttonTypeOne = new ButtonType("Oui");
                        ButtonType buttonTypeTwo = new ButtonType("Non");
                        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == buttonTypeOne) {
                            // plan the task automatically
                            try {
                                tache.setSousTacheDuree(user.findAvailableCreneauTime(tache));
                                if (projet != null) {
                                    user.planifierAutoTacheComposee(tache, creneauLibre, projet);
                                } else {
                                    user.planifierAutoTacheComposee(tache, creneauLibre);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            return;
                        }
                    }
                }
            }

            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // hide the window
            ((Node) (saveEvent.getSource())).getScene().getWindow().hide();
        });

        Stage taskStage = new Stage();
        Scene taskScene = new Scene(root);
        taskStage.setTitle("Add New Task");
        taskStage.setScene(taskScene);
        taskStage.show();
    }

    @FXML
    private void saveConfiguration(ActionEvent event) throws IOException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Etes vous sur de sauvegarder cette configuration ainsi que tous les taches ne faisant pas partie à cette nouvelle période seront supprimés?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                utilisateurDAO.setConfiguration(user);
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Confirmation");
                alert1.setHeaderText("Confirmation");
                alert1.setContentText("La configuration a été sauvegardée avec succès");
                alert1.showAndWait();
                auth = false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        refresh();
        switchToTasksCalendar(null);

    }

    @FXML
    private void saveConfigurationSimple(ActionEvent event) throws IOException, SQLException {
        if (heuresDebut.getText().equals("") || heuresFin.getText().equals("") || minutesDebut.getText().equals("") || minutesFin.getText().equals("") || dureeDebut.getText().equals("") || dureeFin.getText().equals("")) {
            // alert to inform user that he needs to choose the duration
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vueillez spécifier le début et la fin du creneau ainsi que la durée minimale des taches");
            alert.showAndWait();
            return;
        }

        ArrayList<Jour> jours = new ArrayList<>();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Etes vous sur de sauvegarder cette configuration ainsi que tous les taches ne faisant pas partie à cette nouvelle période seront supprimés?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                for (int i = startDate1.getValue().getDayOfMonth(); i <= endDate1.getValue().lengthOfMonth(); i++) {
                    Jour jour = new Jour(startDate1.getValue().withDayOfMonth(i));
                    Creneau creneau = new Creneau(LocalTime.of(Integer.parseInt(heuresDebut.getText()), Integer.parseInt(minutesDebut.getText())), LocalTime.of(Integer.parseInt(heuresFin.getText()), Integer.parseInt((minutesFin.getText()))), Duration.ofHours(Integer.parseInt(dureeDebut.getText())).plusMinutes(Integer.parseInt(dureeFin.getText())));
                    jour.ajouterCreneau(creneau);
                    // check if user.getJours() contains jour with the same date
                    for (Jour j : user.getJours()) {
                        if (j.getDate().equals(jour.getDate())) {
                            for (Creneau crenau1 : j.getCreneaux())
                                jour.ajouterCreneau(crenau1);
                            break;
                        }
                    }
                    jours.add(jour);
                }
                user.setJours(jours);

                user.setDateDebut(startDate1.getValue());

                user.setDateFin(endDate1.getValue());

                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

                utilisateurDAO.setConfiguration(user);
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Confirmation");
                alert1.setHeaderText("Confirmation");
                alert1.setContentText("La configuration a été sauvegardée avec succès");
                alert1.showAndWait();
                auth = false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        refresh();
        switchToTasksCalendar(null);
    }

    public void refresh() throws SQLException {
        ObservableList<Calendar> calendars = FXCollections.observableArrayList();

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        Integer nbrTachesRealises = user.getNbTachesRealisees();

        user = utilisateurDAO.getUser(user.getPseudo());
        user.setNbTachesRealisees(nbrTachesRealises);

        tachesProjet.removeAll(tachesProjet);

        if (projet != null) {
            for (Projet projet : user.getProjets()) {
                if (projet.getNom().equals(this.projet.getNom())) {
                    this.projet = projet;
                    break;
                }
            }
            for (Tache tache : projet.getTaches()) {
                for (Jour jour : user.getJours()) {
                    for (Creneau creneau : jour.getCreneaux()) {
                        LocalTime debut = creneau.getDebut();
                        for (Tache tache1 : creneau.getTaches()) {
                            debut = debut.minus(tache1.getDuree());
                        }
                        for (Tache tache1 : creneau.getTaches()) {
                            debut = debut.plus(tache1.getDuree());
                            if (tache1.getNom().equals(tache.getNom()) && tache1.getDuree().equals(tache.getDuree()) && tache1.getDeadline().equals(tache.getDeadline()) && tache1.getCategorie().getNom_cat().equals(tache.getCategorie().getNom_cat())) {
                                tachesProjet.add(new TacheObservable(tache.getNom(), tache.getDuree(), tache.getPrio(), tache.getDeadline(), tache.getCategorie(), tache.getEtat(), (debut + " - " + debut.plus(tache.getDuree()))));
                            }
                        }
                    }
                }
            }
        }
        projectTableView.setItems(tachesProjet);

        taches.removeAll(taches);

        user.getCategories().forEach(categorie -> {
            Calendar calendar = new Calendar(categorie.getNom_cat());
            calendar.setStyle(Calendar.Style.STYLE6);

            user.getJours().forEach(jour -> {
                jour.getCreneaux().forEach(creneau -> {
                    LocalTime debut = creneau.getDebut();
                    for (Tache tache : creneau.getTaches()) {
                        debut = debut.minus(tache.getDuree());
                    }
                    for (Tache tache : creneau.getTaches()) {
                        if (tache.getCategorie().getNom_cat().equals(categorie.getNom_cat())) {
                            Entry<String> newEntry = new Entry<>();
                            newEntry.setTitle(tache.getNom());
                            if (jour.getDate().isEqual(localDate)) {
                                taches.add(new TacheObservable(tache.getNom(), tache.getDuree(), tache.getPrio(), tache.getDeadline(), tache.getCategorie(), tache.getEtat(), (debut + " - " + debut.plus(tache.getDuree()))));
                            }
                            newEntry.setInterval(debut.atDate(jour.getDate()), debut.plus(tache.getDuree()).atDate(jour.getDate()));
                            calendar.addEntry(newEntry);
                        }
                        debut = debut.plus(tache.getDuree());
                    }
                });
            });

            calendars.add(calendar);
            calendar.setReadOnly(true);
        });
        CalendarSource calendarSource = new CalendarSource("Tasks");
        calendarSource.getCalendars().addAll(calendars);
        // remove all calendars from the calendar view
        calendarView.getCalendarSources().removeAll(calendarView.getCalendarSources());
        // add the new calendar source
        calendarView.getCalendarSources().add(calendarSource);
        calendarView.setShowAddCalendarButton(false);
    }

    public void showWelcomeDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setHeaderText("Welcome to My Desktop Planner App");
        alert.setContentText("Click 'Next' to continue.");

        ButtonType nextButton = new ButtonType("Next");
        alert.getButtonTypes().setAll(nextButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == nextButton) {
            showConfigurationDialog();
        }
    }

    public void showBadgeDialog() {
        System.out.println(user.getNbTaches());
        System.out.println(user.getNbTachesRealisees());
        if (user.getNbTachesRealisees() % user.getNbTaches() == 0 && user.getNbTachesRealisees() != 0) {
            user.addBadge(Badge.Good);
            for (Badge badge : user.getBadges()) {
                System.out.println(badge);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Badge");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You have earned the badge 'Good'");
            alert.showAndWait();
        }
        //find number of Good badges
        int nbGoodBadges = 0;
        for (Badge badge : user.getBadges()) {
            if (badge.equals(Badge.Good)) {
                nbGoodBadges++;
            }
        }
        // if number of Good badges is a multiple of 5, add a new badge
        if (nbGoodBadges % 3 == 0 && nbGoodBadges != 0) {
            user.addBadge(Badge.VeryGood);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Badge");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You have earned the badge 'Very Good'");
            alert.showAndWait();
        }
        //find number of Very Good badges
        int nbVeryGoodBadges = 0;
        for (Badge badge : user.getBadges()) {
            if (badge.equals(Badge.VeryGood)) {
                nbVeryGoodBadges++;
            }
        }
        // if number of Very Good badges is a multiple of 3, add a new badge of Excellent
        if (nbVeryGoodBadges % 3 == 0 && nbVeryGoodBadges != 0) {
            user.addBadge(Badge.Excellent);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Badge");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You have earned the badge 'Excellent'");
            alert.showAndWait();
        }
    }

    Callback<TableColumn<TacheObservable, Void>, TableCell<TacheObservable, Void>> cellFactory = new Callback<TableColumn<TacheObservable, Void>, TableCell<TacheObservable, Void>>() {
        @Override
        public TableCell<TacheObservable, Void> call(final TableColumn<TacheObservable, Void> param) {
            final TableCell<TacheObservable, Void> cell = new TableCell<TacheObservable, Void>() {

                private final Node customComponent;

                {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tp/javafx/actions.fxml"));
                        customComponent = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to load custom component", e);
                    }
                }

                {
                    Button buttonDone = (Button) customComponent.lookup("#done");
                    buttonDone.setOnAction(event -> {
                        // get jour and creneau

                        for (Jour jour : user.getJours()) {
                            if (jour.getDate().isEqual(localDate)) {
                                for (Creneau creneau : jour.getCreneaux()) {
                                    for (Tache tache : creneau.getTaches()) {
                                        if (tache.getNom().equals(((Tache) getTableView().getItems().get(getIndex())).getNom())) {
                                            tache.setEtat(Etat.completed);
                                            user.setNbTachesRealisees();
                                            showBadgeDialog();
                                            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                                            try {
                                                utilisateurDAO.updateTache(tache, user, creneau, jour);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                refresh();
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                }
                            }
                        }


                    });

                    Button buttonDelete = (Button) customComponent.lookup("#delete");
                    buttonDelete.setOnAction(event -> {
                        // get jour and creneau

                        for (Jour jour : user.getJours()) {
                            if (jour.getDate().isEqual(localDate)) {
                                for (Creneau creneau : jour.getCreneaux()) {
                                    for (Tache tache : creneau.getTaches()) {
                                        if (tache.getNom().equals(((Tache) getTableView().getItems().get(getIndex())).getNom())) {
                                            tache.setEtat(Etat.completed);
                                            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                                            try {
                                                utilisateurDAO.deleteTache(tache, user, jour, creneau);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                refresh();
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                }
                            }
                        }


                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(customComponent);
                    }
                }
            };
            return cell;
        }
    };

    private void showConfigurationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Configuration");
        alert.setHeaderText("Options de configuration");
        alert.setContentText("Souhaitez vous de choisir un configuration durant laquelle vous aller planifier vos tâches?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            try {
                switchToSettings(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert configAlert = new Alert(Alert.AlertType.INFORMATION);
            configAlert.setTitle("Configuration actuelle");
            configAlert.setHeaderText("Configuration par défaut");
            configAlert.setContentText("Date Début: " + LocalDate.now() + "\n" +
                    "Date Fin: " + LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()) + "\n" +
                    "Créneau par défaut: 20:30 - 23:00\n" +
                    "Durée minimale d'une tache: 30 minutes");
            configAlert.showAndWait();

            ArrayList<Jour> jours = new ArrayList<>();

            // add jours for the rest of the month and for each jour add a creneau of 20:30 - 23:00 and duration of 30 minutes

            for (int i = LocalDate.now().getDayOfMonth(); i <= LocalDate.now().lengthOfMonth(); i++) {
                Jour jour = new Jour(LocalDate.now().withDayOfMonth(i));
                Creneau creneau = new Creneau(LocalTime.of(20, 30), LocalTime.of(23, 0), Duration.ofHours(0).plusMinutes(30));
                jour.ajouterCreneau(creneau);
                jours.add(jour);
            }
            user.setJours(jours);

            user.setDateDebut(LocalDate.now());

            user.setDateFin(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

            try {
                utilisateurDAO.setConfiguration(user);
                auth = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDate();
        username.setText("Hi " + user.getPseudo());

        ObservableList<Calendar> calendars = FXCollections.observableArrayList();

        user.getCategories().forEach(categorie -> {
            Calendar calendar = new Calendar(categorie.getNom_cat());
            calendar.setStyle(Calendar.Style.STYLE6);

            user.getJours().forEach(jour -> {
                jour.getCreneaux().forEach(creneau -> {
                    LocalTime debut = creneau.getDebut();
                    for (Tache tache : creneau.getTaches()) {
                        debut = debut.minus(tache.getDuree());
                    }
                    for (Tache tache : creneau.getTaches()) {
                        if (tache.getCategorie().getNom_cat().equals(categorie.getNom_cat())) {
                            Entry<String> newEntry = new Entry<>();
                            newEntry.setTitle(tache.getNom());
                            newEntry.setInterval(debut.atDate(jour.getDate()), debut.plus(tache.getDuree()).atDate(jour.getDate()));

                            if (jour.getDate().isEqual(localDate)) {
                                taches.add(new TacheObservable(tache.getNom(), tache.getDuree(), tache.getPrio(), tache.getDeadline(), tache.getCategorie(), tache.getEtat(), (debut + " - " + debut.plus(tache.getDuree()))));
                            }
                            calendar.addEntry(newEntry);
                        }
                        debut = debut.plus(tache.getDuree());
                    }
                });
            });

            calendars.add(calendar);
            calendar.setReadOnly(true);
        });

        projectTA.setWrapText(true);

        CalendarSource calendarSource = new CalendarSource("Tasks");
        calendarSource.getCalendars().addAll(calendars);
        // remove the default calendar
        calendarView.getCalendarSources().remove(0);
        calendarView.getCalendarSources().add(calendarSource);
        calendarView.setShowAddCalendarButton(false);

        Jour jour = user.getJours().stream().filter(j -> j.getDate().equals(localDate)).findFirst().get();

        tacheName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tacheDuration.setCellValueFactory(new PropertyValueFactory<>("debutToFin"));
        tachePriority.setCellValueFactory(new PropertyValueFactory<>("prio"));
        tacheDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        tacheEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        tacheActions.setCellFactory(cellFactory);

        tacheProjet.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tempsProjet.setCellValueFactory(new PropertyValueFactory<>("debutToFin"));
        prioProjet.setCellValueFactory(new PropertyValueFactory<>("prio"));
        deadlineProjet.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        etatProjet.setCellValueFactory(new PropertyValueFactory<>("etat"));
        actionsProjet.setCellFactory(cellFactory);


        projectTableView.getColumns().addAll(tacheProjet, tempsProjet, prioProjet, deadlineProjet, etatProjet);
        projectTableView.setItems(tachesProjet);

        tachesTable.getColumns().addAll(tacheName, tacheDuration, tachePriority, tacheDeadline, tacheEtat, tacheActions);
        tachesTable.setItems(taches);

        heuresDebut.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                heuresDebut.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 23;
            if (value > maxValue) {
                heuresDebut.setText(Integer.toString(maxValue));
            }

        });

        minutesDebut.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minutesDebut.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 59;
            if (value > maxValue) {
                minutesDebut.setText(Integer.toString(maxValue));
            }

        });

        heuresFin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                heuresFin.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 23;
            if (value > maxValue) {
                heuresFin.setText(Integer.toString(maxValue));
            }

        });

        minutesFin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minutesFin.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 59;
            if (value > maxValue) {
                minutesFin.setText(Integer.toString(maxValue));
            }

        });

        dureeDebut.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                dureeDebut.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 23;
            if (value > maxValue) {
                dureeDebut.setText(Integer.toString(maxValue));
            }

        });

        dureeFin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                dureeFin.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 59;
            if (value > maxValue) {
                dureeFin.setText(Integer.toString(maxValue));
            }
        });


        if (auth) {
            Platform.runLater(() -> {
                showWelcomeDialog();
            });
        }
    }
}
