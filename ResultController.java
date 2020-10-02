package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ResultController {

    @FXML
    public Label lbDictOperator;

    @FXML
    public Label lbDictOperand;

    @FXML
    public Label lbCountOperator;

    @FXML
    public Label lbCountOperand;

    @FXML
    public Label lbDictProgram;

    @FXML
    public Label lbProgramLength;

    @FXML
    public Label lbProgramVolume;

    @FXML
    public TextArea txarPairs;

    @FXML
    public Button btnExit;

    public void initialize(){
        lbDictOperator.setText(String.valueOf(Controller.metrics.dictOperatorCount));
        lbDictOperand.setText(String.valueOf(Controller.metrics.dictOperandCount));
        lbCountOperator.setText(String.valueOf(Controller.metrics.operatorCount));
        lbCountOperand.setText(String.valueOf(Controller.metrics.operandCount));
        lbDictProgram.setText(String.valueOf(Controller.metrics.dictProgram));
        lbProgramLength.setText(String.valueOf(Controller.metrics.programLength));
        lbProgramVolume.setText(String.valueOf(Math.round(Controller.metrics.volumeLength * 100) / 100.0));
        txarPairs.appendText(Controller.metrics.resSB.toString());
    }


}
