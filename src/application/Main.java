package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TelaPrincipalMaster.fxml"));
			ScrollPane scrollPane = loader.load();
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
		
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/gui.styles/Calixto_mini_logo_3 (2).png")));
			
			mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Marcenaria Calixto");
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
