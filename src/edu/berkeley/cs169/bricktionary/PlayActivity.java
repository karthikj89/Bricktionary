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
		private ArrayList<GraphicObject> _board = new ArrayList<GraphicObject>();
		private ArrayList<GraphicObject> _toolbox = new ArrayList<GraphicObject>();
		private GraphicObject _currentGraphic = null;
		
		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new ActionThread(getHolder(), this);
			GraphicObject square = new GraphicObject(BitmapFactory.decodeResource(getResources(), R.drawable.square));
			GraphicObject triangle = new GraphicObject(BitmapFactory.decodeResource(getResources(), R.drawable.triangle));
			_toolbox.add(square);
			_toolbox.add(triangle);
			updateToolbox();
			setFocusable(true);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			synchronized (_thread.getSurfaceHolder()) {
				GraphicObject graphic = null;
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					float x = event.getX();
					float y = event.getY();
					for (GraphicObject g : _toolbox){
						if (Math.abs(x-25-g.getCoordinates().getX()) < 25 && Math.abs(y-25-g.getCoordinates().getY()) < 25){
							_currentGraphic = g;
							_toolbox.remove(g);
							break;
						}
					}
					for (GraphicObject g : _board){
						if (Math.abs(x-25-g.getCoordinates().getX()) < 25 && Math.abs(y-25-g.getCoordinates().getY()) < 25){
							_currentGraphic = g;
							_board.remove(g);
							break;
						}
					}
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (_currentGraphic != null){
						_currentGraphic.getCoordinates().setX((int)event.getX() - _currentGraphic.getGraphic().getWidth() / 2);
						_currentGraphic.getCoordinates().setY((int)event.getY() - _currentGraphic.getGraphic().getHeight() / 2);
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (_currentGraphic != null){
                    	if (event.getY() > 50){
                    		_currentGraphic.getCoordinates().setX(Math.round((event.getX() - _currentGraphic.getGraphic().getWidth() / 2)/10)*10);
    						_currentGraphic.getCoordinates().setY(Math.round((event.getY() - _currentGraphic.getGraphic().getHeight() / 2)/10)*10);
                    		_board.add(_currentGraphic);
                    	} else if (event.getY() <= 50){
                    		_toolbox.add(_currentGraphic);
                    		updateToolbox();
                    	}
                    	_currentGraphic = null;
                    }
				}
				return true;
			}
		}
		public void updateToolbox(){
			int i = 0;
			for (GraphicObject graphic : _toolbox){
				graphic.getCoordinates().setX(10+40*i);
				graphic.getCoordinates().setY(10);
				i++;
			}
		}
	
		@Override
		public void onDraw(Canvas canvas){
			canvas.drawColor(Color.WHITE);
			Bitmap bitmap;
			GraphicObject.Coordinates coords;
			if (! _toolbox.isEmpty()){
				for (GraphicObject graphic : _toolbox){
					coords = graphic.getCoordinates();
					bitmap = graphic.getGraphic();
					canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
				}
			}
			if (! _board.isEmpty()){
				for (GraphicObject graphic : _board) {
					bitmap = graphic.getGraphic();
					coords = graphic.getCoordinates();
					canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
				}
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
    	public boolean equals(GraphicObject g){
    		return _coordinates.getX() == g.getCoordinates().getX()  && _coordinates.getY() == g.getCoordinates().getY();
    	}
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
    
        public GraphicObject(Bitmap bitmap) {
            _bitmap = bitmap;
            _coordinates = new Coordinates();

        }
    
        public Bitmap getGraphic() {
            return _bitmap;
        }

        
        public Coordinates getCoordinates() {
            return _coordinates;
        }
    }
}
