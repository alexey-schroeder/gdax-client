package com.gdax;

import com.gdax.client.ExchangeDataSource;
import com.gdax.gui.MainController;
import com.gdax.strategy.SimpleStrategyTickerChangeListener;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Main extends AbstractJavaFxApplicationSupport {

    @Qualifier("mainView")
    @Autowired
    private ControllersConfiguration.ViewHolder view;

    @Autowired
    private SimpleStrategyTickerChangeListener simpleStrategyTickerChangeListener;

    @Autowired
    private ExchangeDataSource exchangeDataSource;

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(view.getView()));
        stage.setResizable(true);
        stage.centerOnScreen();

        MainController controller = (MainController) view.getController();
        stage.setOnCloseRequest(e -> controller.stop());
        stage.show();

        exchangeDataSource.addTickerChangeListener(simpleStrategyTickerChangeListener);
        exchangeDataSource.addUserTradesChangeListener(simpleStrategyTickerChangeListener);
    }

    public static void main(String[] args) {
        launchApp(Main.class, args);
    }
}
