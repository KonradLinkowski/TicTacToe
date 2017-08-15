package application;

import java.net.URL;
import java.util.ResourceBundle;

import core.Game;
import core.Space;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class MainController extends TabPane implements Initializable {
	
	// game pane references
	@FXML
	private Label winText;
	@FXML
	private Label maxTimeLabel;
    @FXML
    private Label mainTimer;
    @FXML
    private ImageView playerOneImage;    
    @FXML
    private ImageView playerTwoImage;
    @FXML
	private AnchorPane gamePane;
	@FXML
	private Tab test;
	
    // options pane references
	@FXML
	private Label gameTimeLabel;
	@FXML
	private Slider gameTimeSlider;
    @FXML
    private Label gridSizeLabel;
    @FXML
    private CheckBox playerOneBotBox;
    @FXML
    private Slider gridSizeSlider;
    @FXML
    private CheckBox gameTimerCheckBox;
    @FXML
    private CheckBox playerTwoBotBox;
    @FXML
    private ToggleGroup pawnSets;

    // variables
	private Game game;
	private Thread gameThread;
	private int rows = 3;
	private int gridSize;
	
	private ImageSet[] imageSets = new ImageSet [] {
			new ImageSet ("o1.png", "x1.png"),
			new ImageSet ("o2.png", "x2.png"),
			new ImageSet ("o3.png", "x3.png")
	};
	private ImageSet currentImageSet = imageSets [0];
	private Timeline mainTimerScheduler;
	
	@FXML
	private void mouseClicked (MouseEvent event) {
		int temp = gridSize / rows;
		game.tryToPutSomething((int) event.getX() / temp, (int) event.getY() / temp);
	}
	
	public void putSymbol (Space mark, int x, int y) {
		int temp = gridSize / rows;
		x *= temp;
		y *= temp;
		
		ImageView imageView = new ImageView (mark == Space.X ? currentImageSet.getXImage() : currentImageSet.getOImage());
		imageView.setScaleX((double) (3 * (temp) / 4) / imageView.getImage().getWidth());
		imageView.setScaleY((double) (3 * (temp) / 4) / imageView.getImage().getHeight());
		imageView.relocate(x + temp / 2 - imageView.getImage().getWidth() / 2,
				y + temp / 2 - imageView.getImage().getHeight() / 2);
		
		ScaleTransition scaleTrans = new ScaleTransition(Duration.millis(300), imageView);
		scaleTrans.setByX(0.5);
		scaleTrans.setByY(0.5);
		scaleTrans.setAutoReverse(true);
		scaleTrans.setCycleCount(2);
		scaleTrans.play();
		
		FadeTransition ft = new FadeTransition(Duration.millis(300), imageView);
		ft.setFromValue(0.1);
		ft.setToValue(1.0);
		ft.play();
	
		gamePane.getChildren().add(imageView);
		if (game.isEnd()) {
			gameEnd();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gameTimeSlider.valueProperty().addListener(new ChangeListener<Object>() {

            @Override
            public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
                gameTimeLabel.textProperty().setValue(
                        String.valueOf((int) gameTimeSlider.getValue()) + "seconds");

            }
        });
		gridSizeSlider.valueProperty().addListener(new ChangeListener<Object>() {

            @Override
            public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
                gridSizeLabel.textProperty().setValue(
                        String.valueOf((int) gridSizeSlider.getValue()));

            }
        });
		//gameTimeLabel.textProperty().bind(Bindings.format("%.0f seconds",gameTimeSlider.valueProperty()));
		//gridSizeLabel.textProperty().bind(Bindings.format("%.0f", gridSizeSlider.valueProperty()));
		gameTimeSlider.disableProperty().bind(gameTimerCheckBox.selectedProperty().not());
		gameTimeLabel.disableProperty().bind(gameTimerCheckBox.selectedProperty().not());
		gridSize = (int) gamePane.getPrefHeight();
		newGame ();
	}
	
	@SuppressWarnings("deprecation")
	public void onClose () {
		gameThread.stop();
	}
	
	private void drawGrid () {
		Line line;
		int temp = gridSize / rows / 10;
		for (int i = 1; i < rows; i++) {
			line = new Line (gridSize / rows * i, temp, gridSize / rows * i, gridSize - temp);
			gamePane.getChildren().add(line);
		}
		for (int i = 1; i < rows; i++) {
			line = new Line (temp, gridSize / rows * i, gridSize - temp, gridSize / rows * i);
			gamePane.getChildren().add(line);
		}
	}
	
	@SuppressWarnings("deprecation")
	void newGame () {
		if (gameThread != null) {
			gameThread.stop();
			gamePane.getChildren().clear();
			mainTimerScheduler.stop();
			mainTimer.setText("00:00");
		}
		winText.setText("");
		rows = (int) gridSizeSlider.getValue();
		currentImageSet = imageSets [pawnSets.getToggles().indexOf((pawnSets.getSelectedToggle()))];
		playerOneImage.setImage(currentImageSet.getXImage());
		playerTwoImage.setImage(currentImageSet.getOImage());
		
		game = new Game (rows, playerOneBotBox.isSelected(), playerTwoBotBox.isSelected(),
				gameTimerCheckBox.isSelected(), (long)gameTimeSlider.getValue() * 1000);
		gameThread = new Thread (game);
		gameThread.start();
		
		if (game.isTimePlay()) {
			maxTimeLabel.setText(String.format("%02d:%02d", (long)gameTimeSlider.getValue() / 60, ((long)gameTimeSlider.getValue()) % 60));
		} else {
			maxTimeLabel.setText("Infinite");
		}
		
		test.getTabPane().getSelectionModel().select(test);
		
		mainTimerScheduler = new Timeline(new KeyFrame(Duration.millis(950), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	long elapsedTime = System.currentTimeMillis() - game.getStartTime();
		    	mainTimer.setText(String.format("%02d:%02d" , elapsedTime / 60000, (elapsedTime / 1000) % 60));
		    	if (game.isTimePlay() && game.isTimeEnd()) {
		    		gameEnd ();
		    	}
		    }
		}));
		mainTimerScheduler.setCycleCount(Timeline.INDEFINITE);
		mainTimerScheduler.play();
		
		drawGrid ();
	}
	
	@SuppressWarnings("deprecation")
	public void gameEnd () {
		mainTimerScheduler.stop();
		if (game.getWinSpaceBeg() != null) {
			int temp = gridSize / rows;
			
			Line line = new Line (game.getWinSpaceBeg().getX() * temp + temp / 2, game.getWinSpaceBeg().getY() * temp + temp / 2,
					game.getWinSpaceBeg().getX() * temp + temp / 2, game.getWinSpaceBeg().getY() * temp + temp / 2);
			line.setStroke(new Color (1, 0, 1, 1));
			line.setStrokeWidth(10);
			
			gamePane.getChildren().add(line);
			
			Timeline timeline = new Timeline (new KeyFrame(Duration.seconds(1),
					new KeyValue (line.endXProperty(), game.getWinSpaceEnd().getX() * temp + temp / 2),
					new KeyValue (line.endYProperty(), game.getWinSpaceEnd().getY() * temp + temp / 2)));
			timeline.play();
		}
		if (game.getEndReason() != null) {
			switch (game.getEndReason()) {
			case NO__MORE_SPACE:
				winTextAnimation("No more space");
				break;
			case X_WINS:
				winTextAnimation("X wins");
				break;
			case O_WINS:
				winTextAnimation("O wins");
				break;
			case TIMES_OUT:
				winTextAnimation("Time is out");
				break;
			}
		} else {
			winTextAnimation("Time is out");
			gameThread.stop();
		}
		/*
		/*
		Bounds bound = playerOneImage.getBoundsInLocal(); //getting co-ordinates
		playerOneImage.setEffect(new ColorInput(bound.getMinX(), bound.getMinY(),
	            bound.getWidth(), bound.getHeight(), new Color (1, 0, 1, 0.1)));
		*/
	}
	
	void winTextAnimation (String text) {
		winText.setText(text);
		ScaleTransition scaleTrans = new ScaleTransition(Duration.millis(300), winText);
		scaleTrans.setByX(0.5);
		scaleTrans.setByY(0.5);
		scaleTrans.setAutoReverse(true);
		scaleTrans.setCycleCount(2);
		scaleTrans.play();
		
		FadeTransition ft = new FadeTransition(Duration.millis(300), winText);
		ft.setFromValue(0.1);
		ft.setToValue(1.0);
		ft.play();
	}
	
	@FXML
    void createNewGame(ActionEvent event) {
		newGame ();
    }

    @FXML
    void exitGame(ActionEvent event) {
    	onClose ();
    	Platform.exit();
    }
	
}
