package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	public static MainController mainController;
	
	@Override
	public void start(Stage stage) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
		Parent root;
		try {
			root = loader.load();
			mainController = loader.getController();
			Scene s = new Scene (root);
			stage.setScene(s);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop () {
		mainController.onClose();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
