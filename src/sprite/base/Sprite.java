package sprite.base;

import base.Coordinate;
import base.Visible;
import javafx.scene.image.WritableImage;

public abstract class Sprite implements Visible, Coordinate {

    private boolean visible;
    private WritableImage image;

    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
	protected int index;
    protected WritableImage[][] imageArrays;

    public Sprite() {
        index = 0;
        visible = true;
        initImageArrays();
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setImage(WritableImage image) {

        this.image = image;
    }

    public WritableImage getImage() {

        return image;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getX() {
        return x;
    }
    
    public abstract int getTrueX();
    public abstract int getTrueY();
	
	public void updateIndex() {
		index = (index + 1) % 2;
	}
	
	protected abstract void initImageArrays();
	
	public abstract void updateImage();
}
