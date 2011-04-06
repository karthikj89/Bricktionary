package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

public class PlayActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Panel myPanel = new Panel(this);
		setContentView(myPanel);
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		private ActionThread _thread;
		private ArrayList<GraphicObject> _board = new ArrayList<GraphicObject>();
		private ArrayList<GraphicObject> _toolbox = new ArrayList<GraphicObject>();
		private GraphicObject _currentGraphic = null;
		private Button submitButton;
		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new ActionThread(getHolder(), this);
			GraphicObject square = new GraphicObject(BitmapFactory.decodeResource(getResources(), R.drawable.square));
			GraphicObject triangle = new GraphicObject(BitmapFactory.decodeResource(getResources(), R.drawable.triangle));
			GraphicObject triangle2 = new GraphicObject(BitmapFactory.decodeResource(getResources(), R.drawable.triangle));
			_toolbox.add(square);
			_toolbox.add(triangle);
			_toolbox.add(triangle2);
			updateToolbox();

			submitButton = new Button(context);
			submitButton.setText("Submit!");
			
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
						}
					}
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (_currentGraphic != null){
						_currentGraphic.setActive(false);
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
                    	
						if (_currentGraphic.isActive()){
							_currentGraphic.rotate();
						} else {
							setActive(_currentGraphic);
						}
                		setActive(_currentGraphic);
                    	_currentGraphic = null;
                    }
				}
				return true;
			}
		}
		
		public void setActive(GraphicObject g){
			for (GraphicObject graphic : _toolbox){
				graphic.setActive(false);
			}
			for (GraphicObject graphic : _board){
				graphic.setActive(false);
			}
			g.setActive(true);
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
			Paint paint = new Paint();
			paint.setColor(Color.BLACK); //Draw the line between the toolbox and play area
            canvas.drawLine(0, 50, 350, 50, paint);
			if (! _toolbox.isEmpty()){
				for (GraphicObject graphic : _toolbox){ //Draw all objects in the toolbox
					coords = graphic.getCoordinates();
					bitmap = graphic.getGraphic();
					if (graphic.isActive()){
						paint.setColor(Color.CYAN);
						canvas.drawRect(coords.getX(), coords.getY(), coords.getX()+50, coords.getY()+50, paint);
					}
					canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
				}
			}
			if (! _board.isEmpty()){
				for (GraphicObject graphic : _board) { //Draw all objects on the board
					bitmap = graphic.getGraphic();
					coords = graphic.getCoordinates();
					if (graphic.isActive()){
						paint.setColor(Color.CYAN);
						canvas.drawRect(coords.getX(), coords.getY(), coords.getX()+50, coords.getY()+50, paint);
					}
					canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
				}
			}
            // Draw the object that is being dragged (if there is one)
            if (_currentGraphic != null) {
                bitmap = _currentGraphic.getGraphic();
                coords = _currentGraphic.getCoordinates();
                canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
            }
			submitButton.draw(canvas);

            
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
    	private boolean _active;
    	public boolean equals(GraphicObject g){
    		return _coordinates.getX() == g.getCoordinates().getX()  && _coordinates.getY() == g.getCoordinates().getY();
    	}
    	public boolean isActive(){
        	return _active;
        }
        
        public void setActive(boolean b){
        	_active = b;
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
        
        public void rotate(){
        	Matrix matrix = new Matrix();
        	matrix.postRotate(90);
        	_bitmap = Bitmap.createBitmap(_bitmap,0,0,_bitmap.getWidth(), _bitmap.getHeight(), matrix, true);
        }
    
        public Bitmap getGraphic() {
            return _bitmap;
        }

        
        public Coordinates getCoordinates() {
            return _coordinates;
        }
    }
}
