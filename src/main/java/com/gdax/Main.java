package com.gdax;

import com.gdax.gui.MainController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main extends AbstractJavaFxApplicationSupport
{
//	public static void main(String[] args)
//	{
//		launch(args);
//	}
//
//	@Override public void start(Stage primaryStage) throws Exception
//	{
//
//		URL url = getClass().getClassLoader().getResource("fxml/main.fxml");
//		FXMLLoader loader = new FXMLLoader(url);
//		Parent root = loader.load();
//		final MainController controller = loader.getController();
//		primaryStage.setScene(new Scene(root));
//		primaryStage.setOnCloseRequest(e -> controller.stop());
//		primaryStage.show();
//	}

	@Qualifier("mainView")
	@Autowired
	private ControllersConfiguration.ViewHolder view;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(view.getView()));
		stage.setResizable(true);
		stage.centerOnScreen();

		MainController controller = (MainController) view.getController();
		stage.setOnCloseRequest(e -> controller.stop());
		stage.show();
	}

	public static void main(String[] args) {
		launchApp(Main.class, args);
	}
}
