package firstMode.sprite;

import javafx.scene.image.WritableImage;

public abstract class Sprite {

    private boolean visible;
    private WritableImage image;

    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
	protected int index;

    public Sprite() {
        index = 0;
        visible = true;
    }

    public boolean isVisible() {

        return visible;
    }

    protected void setVisible(boolean visible) {

        this.visible = visible;
    }

    public void setImage(WritableImage image) {

        this.image = image;
    }

    public WritableImage getImage() {

        return image;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {

        this.y = y;
    }

    public int getY() {

        return y;
    }

    public int getX() {

        return x;
    }
<<<<<<< HEAD
<<<<<<< Updated upstream
||||||| constructed merge base
	
	public void updateIndex() {
		index = (index + 1) % 2;
	}
	
	public abstract void updateImage();
=======
    
    public abstract int getTrueX();
    public abstract int getTrueY();
	
	public void updateIndex() {
		index = (index + 1) % 2;
	}
	
	public abstract void updateImage();
>>>>>>> Stashed changes
||||||| 9a98246
=======
	
	public void updateIndex() {
		index = (index + 1) % 2;
	}
	
	public abstract void updateImage();
>>>>>>> c452a9c8d73ffc13729cc24897efeb4937caed18
}
