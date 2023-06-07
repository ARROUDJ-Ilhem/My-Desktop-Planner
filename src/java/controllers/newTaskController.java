package tp.javafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tp.javafx.models.Priorite;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class newTaskController implements Initializable {

    public CheckBox repeatCB;
    public ChoiceBox jourCB;
    public ChoiceBox categorieCB;
    public TextField periodeTF;
    public TextField nomTF;
    public DatePicker deadlineDP;
    public CheckBox bloqueCB;
    public ChoiceBox creneauCB;
    public Button saveButton;
    public Label creneauLB;
    public Label periodeLB;
    public Label bloqueLB;
    public Label composeLB;
    public CheckBox composeCB;
    public Label jourLB;
    public Label repeatLB;
    public TextField heures;
    public TextField minutes;
    public TextField heures1;
    public TextField minutes1;
    public Label sousLB;
    public ChoiceBox importanceCB;

    // create functions to get the values of the fields

    public String getNom() {
        return nomTF.getText();
    }

    public LocalDate getDeadline() {
        return deadlineDP.getValue();
    }

    public int getHeures() {
        return Integer.parseInt(heures.getText());
    }

    public int getMinutes() {
        return Integer.parseInt(minutes.getText());
    }

    public String getCategorie() throws NullPointerException {
        if (categorieCB.getValue() == null) {
            return null;
        }
        return categorieCB.getValue().toString();
    }

    public Priorite getPriorite() {
        return Priorite.valueOf(importanceCB.getValue().toString());
    }

    public String getCreneau() throws NullPointerException {
        if (creneauCB.getValue() == null) {
            return null;
        }
        return creneauCB.getValue().toString();
    }

    public boolean getCompose() {
        return composeCB.isSelected();
    }


    public String getPeriode() {
        return periodeTF.getText();
    }

    public boolean getBloque() {
        return !bloqueCB.isSelected();
    }

    public boolean getRepeat() {
        return repeatCB.isSelected();
    }

    public String getJour() throws NullPointerException {
        if (jourCB.getValue() == null) {
            return null;
        }
        return jourCB.getValue().toString();
    }

    // create function to add choices to categorieCB

    public void addCategorie(String categorie) {
        categorieCB.getItems().add(categorie);
    }

    // create function to add choices to creneauCB

    public void addCreneau(String creneau) {
        creneauCB.getItems().add(creneau);
    }

    // create function to add choices to jourCB

    public void addJour(String jour) {
        jourCB.getItems().add(jour);
    }

    // create function to check if the repeat checkbox is selected or not, if it is selected, the periodeTF is enabled, else it is disabled, set the function on the repeatCB

    @FXML
    public void checkRepeat(ActionEvent event) throws IOException {
        if (repeatCB.isSelected()) {
            periodeTF.setDisable(false);
            periodeLB.setDisable(false);
            sousLB.setDisable(true);
            heures1.setDisable(true);
            minutes1.setDisable(true);
            composeCB.setDisable(true);
            composeLB.setDisable(true);
            jourCB.setDisable(true);
            jourLB.setDisable(true);
            creneauCB.setDisable(true);
            creneauLB.setDisable(true);
        } else {
            periodeTF.setDisable(true);
            periodeLB.setDisable(true);
            if (composeCB.isSelected()) {
                sousLB.setDisable(false);
                heures1.setDisable(false);
                minutes1.setDisable(false);
            }
            composeCB.setDisable(false);
            composeLB.setDisable(false);
            jourCB.setDisable(false);
            jourLB.setDisable(false);
            creneauCB.setDisable(false);
            creneauLB.setDisable(false);
        }
    }

    // create function to check if a choice is selected from the jourCB or not, if it is selected, the creneauCB is enabled, else it is disabled


    @FXML
    public void checkCompose(ActionEvent event) throws IOException {
        if (composeCB.isSelected()) {
            sousLB.setDisable(false);
            heures1.setDisable(false);
            minutes1.setDisable(false);
            repeatCB.setDisable(true);
            repeatLB.setDisable(true);
            periodeLB.setDisable(true);
            periodeTF.setDisable(true);
            creneauCB.setDisable(true);
            creneauLB.setDisable(true);
            jourCB.setDisable(true);
            jourLB.setDisable(true);
        } else {
            sousLB.setDisable(true);
            heures1.setDisable(true);
            minutes1.setDisable(true);
            creneauCB.setDisable(false);
            creneauLB.setDisable(false);
            periodeTF.setDisable(false);
            periodeLB.setDisable(false);
            repeatLB.setDisable(false);
            repeatCB.setDisable(false);
            jourCB.setDisable(false);
            jourLB.setDisable(false);
        }
    }

    private EventHandler<ActionEvent> addCreneaux;

    public void setAddCreneaux(EventHandler<ActionEvent> addCreneaux) {
        this.addCreneaux = addCreneaux;
    }


    private EventHandler<ActionEvent> saveButtonAction;

    public void setSaveButtonAction(EventHandler<ActionEvent> saveButtonAction) {
        this.saveButtonAction = saveButtonAction;
    }

    @FXML
    public void checkJour(ActionEvent event) {
        String selectedOption = (String) jourCB.getSelectionModel().getSelectedItem();
        if (selectedOption.equals("Choissisez un jour")) {
            creneauCB.setDisable(true);
            creneauLB.setDisable(true);
            composeCB.setDisable(false);
            composeLB.setDisable(false);
            repeatLB.setDisable(false);
            repeatCB.setDisable(false);
            periodeLB.setDisable(false);
            periodeTF.setDisable(false);
            if (composeCB.isSelected()) {
                sousLB.setDisable(false);
                heures1.setDisable(false);
                minutes1.setDisable(false);
            }
        } else {
            creneauCB.setDisable(false);
            creneauLB.setDisable(false);
            composeCB.setDisable(true);
            composeLB.setDisable(true);
            repeatLB.setDisable(true);
            repeatCB.setDisable(true);
            periodeLB.setDisable(true);
            periodeTF.setDisable(true);
            sousLB.setDisable(true);
            heures1.setDisable(true);
            minutes1.setDisable(true);
        }

        if (addCreneaux != null && !selectedOption.equals("Choissisez un jour")) {
            addCreneaux.handle(event);
        }
    }

    @FXML
    private void onSaveButtonClick(ActionEvent event) {
        // Call the action event handler method in HomeController
        if (saveButtonAction != null) {
            saveButtonAction.handle(event);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Priorite> prioriteList = FXCollections.observableArrayList(Priorite.values());

        importanceCB.setItems(prioriteList);

        heures.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                heures.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 23;
            if (value > maxValue) {
                heures.setText(Integer.toString(maxValue));
            }

        });

        heures1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                heures.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 23;
            if (value > maxValue) {
                heures.setText(Integer.toString(maxValue));
            }

        });

        minutes.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minutes.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 59;
            if (value > maxValue) {
                minutes.setText(Integer.toString(maxValue));
            }
        });

        minutes1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minutes.setText(newValue.replaceAll("[^\\d]", ""));
            }

            int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
            int maxValue = 59;
            if (value > maxValue) {
                minutes.setText(Integer.toString(maxValue));
            }
        });
    }
}
