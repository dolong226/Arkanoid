package ui;

import game.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sound.SoundManager;
import ui.ImageLoad;

public class SettingsPopup {
    private final Stage stage;
    private final SoundManager soundManager = SoundManager.getInstance();
    private final GameController gameController;

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
        StackPane root = new StackPane();
        root.setPrefSize(stage.getWidth(), stage.getHeight());

        Image bgImage = ImageLoad.load("/Default/panel_bg.png");
        if (bgImage != null) {
            ImageView bgView = new ImageView(bgImage);
            bgView.setFitWidth(stage.getWidth());
            bgView.setFitHeight(stage.getHeight());
            bgView.setPreserveRatio(false);
            root.getChildren().add(bgView);
        } else {
            root.setStyle("-fx-background-color: #222;");
        }

        VBox content = new VBox(12);
        content.setPadding(new Insets(18));
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(stage.getWidth() - 40);

        Label title = new Label("GAME SETTINGS");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#e6ffe6"));

        Label musicLabel = new Label("Music Volume");
        musicLabel.setFont(Font.font("System", 13));
        musicLabel.setTextFill(Color.WHITE);

        Slider musicSlider = new Slider(0, 1, soundManager.getMusicVolume());
        musicSlider.setShowTickMarks(false);
        musicSlider.valueProperty().addListener((obs, old, nw) ->
                soundManager.setMusicVolume(Math.round(nw.doubleValue() * 100) / 100.0)
        );

        Label sfxLabel = new Label("Sound Effects");
        sfxLabel.setFont(Font.font("System", 13));
        sfxLabel.setTextFill(Color.WHITE);

        Slider sfxSlider = new Slider(0, 1, soundManager.getSFXVolume());
        sfxSlider.setShowTickMarks(false);
        sfxSlider.valueProperty().addListener((obs, old, nw) ->
                soundManager.setSFXVolume(Math.round(nw.doubleValue() * 100) / 100.0)
        );

        Button close = createImageButton("/Default/quit1.png", "/Default/quit2.png", "Close", () -> stage.close());
        close.setPrefWidth(160);
        close.setPrefHeight(44);

        content.getChildren().addAll(title, musicLabel, musicSlider, sfxLabel, sfxSlider, close);
        root.getChildren().add(content);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private Button createImageButton(String normalPath, String hoverPath, String text, Runnable onAction) {
        Image normal = ImageLoad.load(normalPath);
        Image hover = ImageLoad.load(hoverPath);

        Button btn = new Button();
        btn.setBackground(null);
        btn.setPadding(Insets.EMPTY);

        if (normal != null) {
            ImageView ivNormal = new ImageView(normal);
            ivNormal.setFitWidth(160);
            ivNormal.setFitHeight(44);
            ivNormal.setPreserveRatio(false);

            ImageView ivHover = null;
            if (hover != null) {
                ivHover = new ImageView(hover);
                ivHover.setFitWidth(160);
                ivHover.setFitHeight(44);
                ivHover.setPreserveRatio(false);
            }

            btn.setGraphic(ivNormal);

            ImageView finalIvHover = ivHover;

            btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                if (finalIvHover != null) btn.setGraphic(finalIvHover);
                btn.setScaleX(1.03);
                btn.setScaleY(1.03);
            });
            btn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                btn.setGraphic(ivNormal);
                btn.setScaleX(1.0);
                btn.setScaleY(1.0);
            });

            btn.setOnAction(e -> {
                if (onAction != null) onAction.run();
            });
        }

        return btn;
    }
}