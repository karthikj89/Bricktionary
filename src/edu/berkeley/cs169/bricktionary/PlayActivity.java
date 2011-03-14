package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlayActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(new Panel(this));
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		private ActionThread _thread;
		private ArrayList<GraphicObject> _graphics = new ArrayList<GraphicObject>();
		private GraphicObject _currentGraphic = null;
		
		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new ActionThread(getHolder(), this);
			setFocusable(true);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			synchronized (_thread.getSurfaceHolder()) {
				GraphicObject graphic = null;
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (_graphics.isEmpty()){
						graphic = new GraphicObject(BitmapFactory.decodeResource(getResources(), R.drawable.square));
						graphic.getCoordinates().setX(Math.round((event.getX() - graphic.getGraphic().getWidth() /2)/10)*10);
						graphic.getCoordinates().setY(Math.round((event.getX() - graphic.getGraphic().getHeight() /2)/10)*10);
						_currentGraphic = graphic;
						_graphics.add(graphic);
					} else{
						float x = event.getX();
						float y = event.getY();
						for (GraphicObject g : _graphics){
							if (Math.abs(x-25-g.getCoordinates().getX()) < 25 && Math.abs(y-25-g.getCoordinates().getY()) < 25){
								_currentGraphic = g;

							}
						}
					}
					
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (_currentGraphic != null){
						_currentGraphic.getCoordinates().setX(Math.round((event.getX() - _currentGraphic.getGraphic().getWidth() / 2)/10)*10);
						_currentGraphic.getCoordinates().setY(Math.round((event.getY() - _currentGraphic.getGraphic().getHeight() / 2)/10)*10);
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
                    _currentGraphic = null;
				}
				return true;
			}
		}
	
		@Override
		public void onDraw(Canvas canvas){
			canvas.drawColor(Color.BLACK);
			Bitmap bitmap;
			GraphicObject.Coordinates coords;
			for (GraphicObject graphic : _graphics) {
                bitmap = graphic.getGraphic();
                coords = graphic.getCoordinates();
                canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
            }
            // draw current graphic at last...
            if (_currentGraphic != null) {
                bitmap = _currentGraphic.getGraphic();
                coords = _currentGraphic.getCoordinates();
                canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
            }
        }
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
        }
    
        public void surfaceCreated(SurfaceHolder holder) {
            _thread.setRunning(true);
            _thread.start();
        }
    
        public void surfaceDestroyed(SurfaceHolder holder) {
            // simply copied from sample application LunarLander:
            // we have to tell thread to shut down & wait for it to finish, or else
            // it might touch the Surface after we return and explode
            boolean retry = true;
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }
	}
	class ActionThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;
        
        public ActionThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }
        
        public void setRunning(boolean run) {
            _run = run;
        }
        
        public SurfaceHolder getSurfaceHolder() {
            return _surfaceHolder;
        }
        
        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    _panel.onDraw(c);
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
    
    class GraphicObject {
        public class Speed {
            public static final int X_DIRECTION_RIGHT = 1;
            public static final int X_DIRECTION_LEFT = -1;
            public static final int Y_DIRECTION_DOWN = 1;
            public static final int Y_DIRECTION_UP = -1;

            private int _x = 1;
            private int _y = 1;

            private int _xDirection = X_DIRECTION_RIGHT;
            private int _yDirection = Y_DIRECTION_DOWN;
            
            /**
             * @return the _xDirection
             */
            public int getXDirection() {
                return _xDirection;
            }
            
            /**
             * @param direction the _xDirection to set
             */
            public void setXDirection(int direction) {
                _xDirection = direction;
            }

            public void toggleXDirection() {
                if (_xDirection == X_DIRECTION_RIGHT) {
                    _xDirection = X_DIRECTION_LEFT;
                } else {
                    _xDirection = X_DIRECTION_RIGHT;
                }
            }
            
            /**
             * @return the _yDirection
             */
            public int getYDirection() {
                return _yDirection;
            }

            /**
             * @param direction the _yDirection to set
             */
            public void setYDirection(int direction) {
                _yDirection = direction;
            }

            public void toggleYDirection() {
                if (_yDirection == Y_DIRECTION_DOWN) {
                    _yDirection = Y_DIRECTION_UP;
                } else {
                    _yDirection = Y_DIRECTION_DOWN;
                }
            }
            
            /**
             * @return the _x
             */
            public int getX() {
                return _x;
            }

            /**
             * @param speed the _x to set
             */
            public void setX(int speed) {
                _x = speed;
            }

            /**
             * @return the _y
             */
            public int getY() {
                return _y;
            }

            /**
             * @param speed the _y to set
             */
            public void setY(int speed) {
                _y = speed;
            }
            
            public String toString() {
                String xDirection;
                String yDirection;
                if (_xDirection == X_DIRECTION_RIGHT) {
                    xDirection = "right";
                } else {
                    xDirection = "left";
                }
                if (_yDirection == Y_DIRECTION_UP) {
                    yDirection = "up";
                } else {
                    yDirection = "down";
                }
                return "Speed: x: " + _x + " | y: " + _y + " | xDirection: " + xDirection + " | yDirection: " + yDirection;
            }
        }
        
        /**
         * Contains the coordinates of the graphic.
         */
        public class Coordinates {
            private int _x = 100;
            private int _y = 0;
    
            public int getX() {
                return _x + _bitmap.getWidth() / 2;
            }
    
            public void setX(int value) {
                _x = value - _bitmap.getWidth() / 2;
            }
    
            public int getY() {
                return _y + _bitmap.getHeight() / 2;
            }
    
            public void setY(int value) {
                _y = value - _bitmap.getHeight() / 2;
            }
            
            public String toString() {
                return "Coordinates: (" + _x + "/" + _y + ")";
            }
        }
        
        private Bitmap _bitmap;
        private Coordinates _coordinates;
        private Speed _speed;
    
        public GraphicObject(Bitmap bitmap) {
            _bitmap = bitmap;
            _coordinates = new Coordinates();
            _speed = new Speed();
        }
    
        public Bitmap getGraphic() {
            return _bitmap;
        }

        public Speed getSpeed() {
            return _speed;
        }
        
        public Coordinates getCoordinates() {
            return _coordinates;
        }
    }
}
