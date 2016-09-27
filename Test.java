package custom;

import java.net.URISyntaxException;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Test the JFXCustomCursor
 * 
 * @author GOXR3PLUS
 *
 */
public class Test extends Application {

	/**
	 * A fade transition used here to fade-in or fade-out the custom cursor
	 */
	private FadeTransition fade;

	@Override
	public void start(Stage primaryStage) {

		// ----------------------------------------MediaPlayer - Media
		Media media = null;
		try {
			media = new Media(getClass().getResource("video.mp4").toURI().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		// ----------------------------------------------------MediaView
		MediaView mediaView = new MediaView();
		mediaView.setMediaPlayer(mediaPlayer);
		mediaView.setFitWidth(200);
		mediaView.setFitHeight(200);

		// --------------------------------------------------ImageView
		ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("download.gif")));

		// ---------------------------------------------BorderPane
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(mediaView); // add to border pane
		borderPane.setCenter(imageView); // add to border pane
		borderPane.setBottom(createAnimatedRectangle());

		// -------------------------------------------------FadeTransition
		fade = new FadeTransition(Duration.millis(750), borderPane);
		fade.setFromValue(0);
		fade.setToValue(1);

		// ---------------------------------------------------Scene Root
		StackPane root = new StackPane();
		Label label = new Label("Click to FadeIn  or FadeOut");
		label.setStyle("-fx-font-size:15;");
		root.getChildren().add(label);

		// ---------------------------------------------------Scene
		Scene scene = new Scene(root, 500, 500);
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			private boolean fadeOut = true;

			@Override
			public void handle(MouseEvent event) {
				if (fadeOut)
					fadeOut();
				else
					fadeIn();

				fadeOut = !fadeOut;
			}
		});
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(c -> {
			System.exit(0);
		});
		primaryStage.show();

		// Initialize the custom cursor
		JFXCustomCursor customCursor = new JFXCustomCursor(scene, root, borderPane, 1, 1);

		//scene.setCursor(Cursor.NONE); // with this line of code you can set the
										// default scene cursor to null
										// so you will see on the the custom
										// cursor
	}

	/**
	 * Fade out the Cursor
	 */
	public void fadeOut() {
		if (fade.getStatus() != Animation.Status.RUNNING) {
			fade.setRate(-1);
			fade.playFrom(fade.getTotalDuration());
		}
	}

	/**
	 * Fade in the Cursor
	 */
	public void fadeIn() {
		if (fade.getStatus() != Animation.Status.RUNNING) {
			fade.setRate(1);
			fade.playFromStart();
		}
	}

	/**
	 * Creates a Simple Rectangle with some animations
	 * 
	 * @return
	 */
	public Rectangle createAnimatedRectangle() {
		Rectangle rectSeq = new Rectangle(25, 25, 50, 50);
		rectSeq.setArcHeight(15);
		rectSeq.setArcWidth(15);
		rectSeq.setFill(Color.CRIMSON);
		rectSeq.setTranslateX(50);
		rectSeq.setTranslateY(50);

		RotateTransition rotateTransition = new RotateTransition(Duration.millis(2000), rectSeq);
		rotateTransition.setByAngle(180f);
		rotateTransition.setCycleCount(4);
		rotateTransition.setAutoReverse(true);

		ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), rectSeq);
		scaleTransition.setFromX(1);
		scaleTransition.setFromY(1);
		scaleTransition.setToX(2);
		scaleTransition.setToY(2);
		scaleTransition.setCycleCount(1);
		scaleTransition.setAutoReverse(true);

		SequentialTransition sequentialTransition = new SequentialTransition();
		sequentialTransition.getChildren().addAll(rotateTransition, scaleTransition);
		sequentialTransition.setCycleCount(Timeline.INDEFINITE);
		sequentialTransition.setAutoReverse(true);

		sequentialTransition.play();

		return rectSeq;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
