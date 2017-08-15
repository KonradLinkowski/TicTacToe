package application;

import javafx.scene.image.Image;

public class ImageSet {

	private Image imageO;
	private Image imageX;
	
	public ImageSet(Image imageO, Image imageX) {
		this.imageO = imageO;
		this.imageX = imageX;
	}
	
	public ImageSet(String url1, String url2) {
		imageO = new Image (url1);
		imageX = new Image (url2);
	}
	
	public Image getOImage () {
		return imageO;
	}
	
	public Image getXImage () {
		return imageX;
	}

}
