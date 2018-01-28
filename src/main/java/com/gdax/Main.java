package com.gdax;

import com.gdax.gui.MainController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main extends AbstractJavaFxApplicationSupport {

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
