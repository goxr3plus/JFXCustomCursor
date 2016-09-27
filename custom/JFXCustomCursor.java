package custom;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * This class allows you to set as a Cursor in a JavaFX Scene,whatever you want
 * ,even a video!. <br>
 * <br>
 * <b>What you have to do is create a basic layout,for example:</b><br>
 * #-->A BorderPane which contains a MediaView,<br>
 * #-->A StackPane which contains an animated ImageView,<br>
 * #-->A Pane which contains an animated Rectangle or something more complex
 * etc..)<br>
 * 
 * <br>
 * <br>
 * The options are unlimited!
 * 
 * @author GOXR3PLUS
 * @param <T>
 * @Version 1.0
 */
public class JFXCustomCursor {

	private SimpleIntegerProperty hotSpotX = new SimpleIntegerProperty();
	private SimpleIntegerProperty hotSpotY = new SimpleIntegerProperty();

	private Scene scene;
	private Pane sceneRoot;
	private Pane content;
	private EventHandler<MouseEvent> eventHandler1;
	private EventHandler<MouseEvent> eventHandler2;
	private EventHandler<MouseEvent> eventHandler3;

	/**
	 * Constructor
	 * 
	 * @param scene
	 *            The Scene of your Stage
	 * @param sceneRoot
	 *            The Root of your Stage Scene
	 * @param content
	 *            The content of the JFXCustomCursor class
	 * @param hotspotX
	 *            Represents the location of the cursor inside the content on X
	 *            axis
	 * @param hotspotY
	 *            Represents the location of the cursor inside the content on Y
	 *            axis
	 */
	public JFXCustomCursor(Scene scene, Pane sceneRoot, Pane content, int hotspotX, int hotspotY) {

		// Go
		setRoot(scene, sceneRoot, content, hotspotX, hotspotY);

	}

	/**
	 * This method changes the content of the JFXCustomCursor
	 * 
	 * @param scene
	 *            The Scene of your Stage
	 * @param sceneRoot
	 *            The Root of your Stage Scene
	 * @param content
	 *            The content of the JFXCustomCursor class
	 * @param hotspotX
	 *            Represents the location of the cursor inside the content on X
	 *            axis
	 * @param hotspotY
	 *            Represents the location of the cursor inside the content on Y
	 *            axis
	 */
	public void setRoot(Scene scene, Pane sceneRoot, Pane content, int hotSpotX, int hotSpotY) {

		// Keep them in case of unRegister-reRegister
		unRegister(); // has to be called before the below happens
		this.scene = scene;
		this.sceneRoot = sceneRoot;
		this.content = content;

		// hot spots
		this.hotSpotX.set(hotSpotX);
		this.hotSpotX.set(hotSpotY);

		// cursor container
		content.setManaged(false);
		content.setMouseTransparent(true);

		// Keep the Content on the top of Scene
		ObservableList<Node> observable = sceneRoot.getChildren();
		observable.addListener((Observable osb) -> {
			if (content.getParent() != null && observable.get(observable.size() - 1) != content) {
				// move the cursor on the top
				Platform.runLater(content::toFront);
			}
		});

		if (!observable.contains(content))
			observable.add(content);

		// Add the event handlers
		eventHandler1 = evt -> {
			if (!sceneRoot.getChildren().contains(content))
				observable.add(content);
		};
		eventHandler2 = evt -> observable.remove(content);

		eventHandler3 = evt -> {
			content.setLayoutX(evt.getX() - hotSpotX);
			content.setLayoutY(evt.getY() - hotSpotY);
		};

		scene.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler1);
		scene.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandler2);
		scene.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandler3);

	}

	/**
	 * Unregisters the CustomCursor from the Scene completely
	 */
	public void unRegister() {
		if (scene != null) {
			sceneRoot.getChildren().remove(content);
			scene.removeEventFilter(MouseEvent.MOUSE_ENTERED, eventHandler1);
			scene.removeEventFilter(MouseEvent.MOUSE_EXITED, eventHandler2);
			scene.removeEventFilter(MouseEvent.MOUSE_MOVED, eventHandler3);
		}
	}

	/**
	 * Re register the CustomCursor to the Scene,<b>this method is
	 * experimental(use with caution!)</b>
	 */
	public void reRegister() {
		if (scene != null)
			setRoot(scene, sceneRoot, content, hotSpotX.get(), hotSpotY.get());
	}

	public SimpleIntegerProperty hotSpotXProperty() {
		return hotSpotX;
	}

	public SimpleIntegerProperty hotSpotYProperty() {
		return hotSpotY;
	}

}