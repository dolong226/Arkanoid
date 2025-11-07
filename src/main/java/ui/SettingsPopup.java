package ui;

import game.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import listener.SoundHitListener;
import sound.SoundManager;

public class SettingsPopup {
    private final Stage stage;
    private final SoundManager soundManager = SoundManager.getInstance();
    private final GameController gameController;


    /**
     * Tạo cửa số settings
     * popup modal: chặn tương tác với cửa sổ khác
     * dùng kiểu cửa sổ tiện ích (Utility)
     * @param parentStage cửa sổ cha
     */
    public SettingsPopup(Stage parentStage, GameController controller) {
        this.gameController = controller;
        this.stage = new Stage();
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Settings");
        stage.setWidth(350);
        stage.setHeight(400);
        stage.setResizable(false);
    }

    public void show() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2c2c2c; -fx-border-color: #555; -fx-border-width: 2;");

        Label title = new Label("GAME SETTINGS");
        title.setStyle("-fx-font-size: 20; -fx-text-fill: #00ff88; -fx-font-weight: bold;");

        // Thanh truot am luong nhac
        Label musicLabel = new Label("Music Volume");
        musicLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        Slider musicSlider = new Slider(0,1, soundManager.getMusicVolume());
        musicSlider.setMajorTickUnit(0.2);
        musicSlider.valueProperty().addListener((obs, old, newVal) -> {
            double vol = Math.round(newVal.doubleValue() * 100) / 100.0;
            soundManager.setMusicVolume(vol);
        });

        // Thanh truot SFX
        Label sfxLabel = new Label("Sound Effects");
        sfxLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        Slider sfxSlider = new Slider(0, 1, soundManager.getSFXVolume());
        sfxSlider.setMajorTickUnit(0.2);
        sfxSlider.valueProperty().addListener((obs, old, newVal) -> {
            double vol = Math.round(newVal.doubleValue() * 100) / 100.0;
            soundManager.setSFXVolume(vol);
        });

        // Nút chọn Level
        Label levelLabel = new Label("Select Level");
        levelLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 16; -fx-font-weight: bold;");

        Button level1 = createLevelButton("LEVEL 1");
        Button level2 = createLevelButton("LEVEL 2");
        Button level3 = createLevelButton("LEVEL 3");

        // Nút Close
        Button close = new Button("Close");
        close.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 8 20;");
        close.setOnAction(e -> stage.close());

        root.getChildren().addAll(
                title, musicLabel, musicSlider, sfxLabel, sfxSlider,
                levelLabel, level1, level2, level3, close
        );

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private Button createLevelButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #4444aa; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 30;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #6666ff; -fx-text-fill: white;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #4444aa; -fx-text-fill: white;"));
        // Chưa xử lý click – bạn làm sau
        return btn;
    }
}
