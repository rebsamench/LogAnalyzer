package ch.zhaw.jv19.loganalyzer;

import ch.zhaw.jv19.loganalyzer.model.AppData;
import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.view.MainAppUIController;
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    private Stage primaryStage;
    private Pane rootLayout;
    private double x, y;
    private AppDataController appDataController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        appDataController = new AppDataController();
        initRootLayout();
    }

    public void initRootLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/MainApp.fxml"));
            rootLayout = (AnchorPane) loader.load();

            rootLayout.setOnMousePressed(mouseEvent -> {
                x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneX();
            });

            rootLayout.setOnMouseDragged(mouseEvent -> {
                primaryStage.setX(mouseEvent.getScreenX() - x);
                primaryStage.setY(mouseEvent.getScreenY() - y);
            });

            Scene scene = new Scene(rootLayout, 1200, 800);
            MainAppUIController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
