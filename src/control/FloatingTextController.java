package control;

import java.util.ArrayList;
import java.util.Iterator;

import base.Coordinate;
import base.Visible;
import javafx.scene.paint.Color;

public class FloatingTextController {
	
	private ArrayList<FloatingText> floatingTexts;
	
	public FloatingTextController() {
		floatingTexts = new ArrayList<FloatingText>();
	}
	
	public void addText(String message, int x, int y, Color color) {
		floatingTexts.add(new FloatingText(message, x, y, color));
	}
	
	public void addText(String message, int x, int y) {
		floatingTexts.add(new FloatingText(message, x, y, Color.WHITE));
	}
	
	public boolean isEmpty() {
		return floatingTexts.isEmpty();
	}
	
	public ArrayList<FloatingText> getFloatingTexts(){
		return floatingTexts;
	}
	
	public void updateTimer() {
		for (Iterator<FloatingText> it = floatingTexts.iterator(); it.hasNext();) {
			FloatingText f = it.next();
			if(f.isEnded()) {
				it.remove();
			}
			else {
				f.updateTimer();
			}
		}
	}
	
	class FloatingText implements Visible, Coordinate {
		public static final double START_TIME = 80;
		
		private String message;
		private double timer;
		private int x;
		private int y;
		private boolean visible;
		private Color color;
		
		public FloatingText(String message, int x, int y, Color color) {
			startTimer();
			setVisible(true);
			setMessage(message);
			setX(x);
			setY(y);
			setColor(color);
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
		public void startTimer() {
			setTimer(START_TIME);
		}

		public boolean isEnded() {
			return getTimer() == 0;
		}
		
		public void updateTimer() {
			setTimer(getTimer() - 1);
			if(getTimer() % 5 == 0) {
				setY(getY() - 1);
			}
		}

		public double getTimer() {
			return timer;
		}

		public void setTimer(double timer) {
			this.timer = timer;
		}

	    @Override
	    public boolean isVisible() {
	        return visible;
	    }

	    @Override
	    public void setVisible(boolean visible) {
	        this.visible = visible;
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

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}
	}
}
