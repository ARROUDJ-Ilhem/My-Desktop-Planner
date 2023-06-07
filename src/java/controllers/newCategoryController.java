package tp.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class newCategoryController {
    public ColorPicker colorPicker;
    public TextField categorieNom;
    public Button submit;

    private EventHandler<ActionEvent> saveButtonAction;

    public void setSaveButtonAction(EventHandler<ActionEvent> saveButtonAction) {
        this.saveButtonAction = saveButtonAction;
    }

    @FXML
    private void onSaveButtonClick(ActionEvent event) {
        // Call the action event handler method in HomeController
        if (saveButtonAction != null) {
            saveButtonAction.handle(event);
        }
    }

}
