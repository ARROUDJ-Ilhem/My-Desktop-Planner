package tp.javafx.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tp.javafx.models.Utilisateur;
import tp.javafx.models.UtilisateurDAO;

public class loginController {

    public TextField logUsername;
    public PasswordField logPassword;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label AppName;

    @FXML
    private Label title;

    @FXML
    private Button logIn;

    @FXML
    private Button signUp;

    @FXML
    private Button createNew;

    @FXML
    public PasswordField signPassword;

    @FXML
    public TextField signUsername;

    @FXML
    private Pane logPane;

    @FXML
    private Pane signUpPane;

    @FXML
    private void handleSignUp(ActionEvent event) throws SQLException {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        Utilisateur user = new Utilisateur(signUsername.getText(), signPassword.getText());
        Boolean status = utilisateurDAO.addUser(user);

        // create a javafx alert box to inform the user that the pseudo is already taken or that the user has been created successfully
        if (status) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User creation status");
            alert.setHeaderText(null);
            alert.setContentText("User created successfully!");
            alert.setOnHidden(closed -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tp/javafx/home.fxml"));
                homeController homeController = new homeController(new Utilisateur(signUsername.getText(), signPassword.getText()), true);
                loader.setController(homeController);

                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
                double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;

                // Set the stage position to the center
                stage.setX(centerX);
                stage.setY(centerY);
                stage.show();
            });
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User creation status");
            alert.setHeaderText(null);
            alert.setContentText("User with the same pseudo already exists!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogIn(ActionEvent event) throws SQLException, IOException {

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        Utilisateur user = new Utilisateur(logUsername.getText(), logPassword.getText());
        Boolean status = utilisateurDAO.logUser(user);

        // create a javafx alert box to inform the user that the pseudo is already taken or that the user has been created successfully
        if (status) {
            Utilisateur loggedUser = utilisateurDAO.getUser(user.getPseudo());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tp/javafx/home.fxml"));
            homeController homeController = new homeController(loggedUser, false);
            loader.setController(homeController);
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
            double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;

            // Set the stage position to the center
            stage.setX(centerX);
            stage.setY(centerY);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect credentials");
            alert.setHeaderText(null);
            alert.setContentText("Username or password is incorrect!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCreateNew(ActionEvent event) {
        signUpPane.toFront();
    }

    @FXML
    private void handleGoBack(ActionEvent event) {
        logPane.toFront();
    }

}