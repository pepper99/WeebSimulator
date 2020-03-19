package firstMode.sprite;

import javafx.scene.image.WritableImage;

public class Sprite {

    private boolean visible;
    private WritableImage image;

    int x;
    int y;
    int dx;
    int dy;

    public Sprite() {

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
}
