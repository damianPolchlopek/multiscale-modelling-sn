package controller;


import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;


public class Controller {

    @FXML
    private TextField xSize;

    @FXML
    private TextField ySize;

    @FXML
    private MenuItem importMenu;

    @FXML
    private MenuItem eksportMenu;

    @FXML
    public void updateImportField(){
        xSize.setText("qweqweqweq");
    }

    @FXML
    public void updateExportField(){
        ySize.setText("[Eksport] asd");
    }

}
