package application;

import hr.java.vjezbe.niti.DatumObjaveNit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	public static Stage window;

	public void start(Stage primaryStage) {

		window = primaryStage;

		try {
			Timeline prikazSlavljenika = new Timeline(
					new KeyFrame(Duration.seconds(30), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							Platform.runLater(new DatumObjaveNit("Najnoviji oglas!"));

						}
					}));
			prikazSlavljenika.setCycleCount(Timeline.INDEFINITE);
			prikazSlavljenika.play();
			BorderPane root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root, 800, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return window;
	}
}
