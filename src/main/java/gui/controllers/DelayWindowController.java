package gui.controllers;

import effects.Delay;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.apache.maven.shared.utils.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class DelayWindowController extends EffectWindowController implements Initializable {

    @FXML
    private TextField delayTimeTextField;
    @FXML
    private ComboBox<TimeUnit> timeUnitComboBox;
    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Callback<ListView<TimeUnit>, ListCell<TimeUnit>> listCellCallback = callback -> new ListCell<>() {
            @Override
            protected void updateItem(TimeUnit item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                setText(isEmpty() ? null : StringUtils.capitalise(item.name().toLowerCase()));
            }
        };
        timeUnitComboBox.setCellFactory(listCellCallback);
        timeUnitComboBox.setButtonCell(listCellCallback.call(null));

        timeUnitComboBox.getItems().addAll(TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

        delayTimeTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                if (!newValue.matches("\\d+")) {
                    delayTimeTextField.setText(oldValue);
                }
            }
        });
    }

    @FXML
    @Override
    protected void applyEffect() {
        TimeUnit unit = timeUnitComboBox.getSelectionModel().getSelectedItem();
        int value = Integer.parseInt(delayTimeTextField.getText());
        if (unit == null || value < 0) {
            throw new IllegalArgumentException("Cannot use null TimeUnit and delay value cannot be negative!");
        }
        Delay d = new Delay(getAudioClip().getAudioFormat(), value, unit);
        d.apply(clip.getSamples(), bufferStartPoint, bufferEndPoint - bufferStartPoint);
        closeWindow();
    }
}
