package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML
    private TextArea txarFilePath;

    @FXML
    private Button btnDoFileChoice;

    @FXML
    private Button btnDoHalstead;

    public static Halstead metrics;

    private static void getAlert(String msg){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        Stage scene = (Stage) alert.getDialogPane().getScene().getWindow();
        scene.getIcons().add(new Image(Main.class.getResourceAsStream("groovyLogo.png")));

        alert.showAndWait();
    }

    public void initialize(){

        //////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////
        txarFilePath.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                txarFilePath.getParent().requestFocus();
            }
        });
        //////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////
        btnDoFileChoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Выберите файл для дешифрования");
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файлы Groovy", "*.groovy"));

                Parent firstL     = btnDoFileChoice.getParent();
                Parent secondL    = firstL.getParent();
                Scene  mainScene  = secondL.getScene();
                Window mainWindow = mainScene.getWindow();

                File file = chooser.showOpenDialog(mainWindow);
                if (file != null) {
                    txarFilePath.setText(file.getAbsolutePath());
                }

            }
        });
        //////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////
        btnDoHalstead.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    //СОЗДАЁМ ОБЪЕКТ МЕТРИКИ ХОЛСТЕДА!!!!!!
                    metrics = new Halstead(new File(txarFilePath.getText()));

                    Stage resStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("resLayout.fxml"));
                    resStage.setScene(new Scene(root, 342, 534));
                    resStage.initModality(Modality.APPLICATION_MODAL);
                    resStage.getIcons().add(new Image(Main.class.getResourceAsStream("groovyLogo.png")));
                    resStage.setResizable(false);
                    resStage.show();

                } catch (IOException a){
                   getAlert("Ошибка чтения файла!");
                  a.printStackTrace();
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////
    }

}
