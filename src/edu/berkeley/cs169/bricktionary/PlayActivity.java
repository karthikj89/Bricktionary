package edu.berkeley.cs169.bricktionary;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class PlayActivity extends Activity {
	TextView tv;
	static ArrayList<Piece> pieces;
	static Puzzle puzzle;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		final Panel myPanel = new Panel(this);
		//		setContentView(myPanel);

		LinearLayout layout = new LinearLayout(this); 
		LinearLayout buttonsLayout = new LinearLayout(this);

		layout.setOrientation(LinearLayout.VERTICAL); 
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

		layout.addView(myPanel, new LinearLayout.LayoutParams ( 
				LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.FILL_PARENT, 
				1.0f));  

		//Recall button
		Button recallBtn = new Button(this);
		recallBtn.setText("Recall");
		recallBtn.setClickable(true);
		recallBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Perform action on click
				myPanel.recall();
			}
		});

		//Submit button
		Button submitBtn = new Button(this); 
		submitBtn.setText("Submit"); 
		submitBtn.setClickable(true);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		submitBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Perform action on click
				int score = puzzle.calculateScore();
				builder.setMessage("Your score is: "+score)
				.setCancelable(false)
				.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
		        		Intent i = new Intent().setClass(PlayActivity.this, OutlineActivity.class);
		        		startActivity(i);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		LinearLayout.LayoutParams btnLP = new LinearLayout.LayoutParams ( 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				0.0f);
		//		btnLP.leftMargin = 250; //set button position

		buttonsLayout.addView(recallBtn, btnLP); //add recall button to view
		buttonsLayout.addView(submitBtn, btnLP); //add submit button to view
		layout.addView(buttonsLayout);
		tv = new TextView(this);
		layout.addView(tv);
		layout.setBackgroundColor(Color.WHITE);

		setContentView(layout); 
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		private ActionThread _thread;
//		private ArrayList<Piece> _board = new ArrayList<Piece>();
//		private ArrayList<Piece> _toolbox = new ArrayList<Piece>();
		private Piece _currentGraphic = null;


		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new ActionThread(getHolder(), this);

			puzzle = new Puzzle(1); //Retrieve puzzle for Level 1
			pieces = puzzle.pieces; //Retrieve the pieces for that puzzle

			int i =0;
			Piece p;
			for(i = 0 ; i < pieces.size(); i++){
				p = pieces.get(i);
				if (p.getLocation() == 6){
					p.moveTo(10+60*i,40); //set the right coordinates for each piece in toolbox
					//_toolbox.add(p); //add each piece to the toolbox
				}
			}


			updateToolbox();

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			synchronized (_thread.getSurfaceHolder()) {
				Piece p = null;
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					float x = event.getX();
					float y = event.getY();
					int i;
					Piece piece;
					//					if(!_toolbox.isEmpty())
					//						_currentGraphic = _toolbox.get(0);
					//					else if(!_board.isEmpty())
					//						_currentGraphic = _board.get(0);
					for (i = 0; i < pieces.size(); i ++ ){
						if (pieces.get(i).getLocation() == 6){
							piece = pieces.get(i);
							if (Math.abs(x-25-piece.getPos().getX()) < 25 && Math.abs(y-25-piece.getPos().getY()) < 25){
								_currentGraphic = piece;
								pieces.get(i).setLocation(8);
								//piece.moveTo(new Position((int)x,(int)y));
								updateToolbox();
								break;
							}
						}
						else if (pieces.get(i).getLocation() == 7){
							piece = pieces.get(i);
							if (Math.abs(x-25-piece.getPos().getX()) < 25 && Math.abs(y-25-piece.getPos().getY()) < 25){
								_currentGraphic = piece;
								pieces.get(i).setLocation(8);
								updateToolbox();
								break;
							}
						}
					}
					/*
					for (i=0;i<_toolbox.size();i++){
						//outside of the toolbox
						piece=_toolbox.get(i);
						if (Math.abs(x-25-piece.getPos().getX()) < 25 && Math.abs(y-25-piece.getPos().getY()) < 25){
							_currentGraphic = piece;
							_toolbox.remove(piece);
							//piece.moveTo(new Position((int)x,(int)y));
							updateToolbox();
							break;
						}
					}

					for (i=0;i<_board.size();i++){
						//inside the toolbox
						piece=_board.get(i);
						if (Math.abs(x-25-piece.getPos().getX()) < 25 && Math.abs(y-25-piece.getPos().getX()) < 25){
							_currentGraphic = piece;
							_board.remove(piece);
							updateToolbox();
							break;
						}
					}
					 */
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (_currentGraphic != null){
						_currentGraphic.setActive(false);
						int posX = (int)event.getX() - _currentGraphic.getWidth(2) / 2;
						int posY = (int)event.getY() - _currentGraphic.getHeight(2) / 2;
						_currentGraphic.moveTo(posX,posY);
						//_currentGraphic.(new Position(posX,posY));
						//						_currentGraphic.getPos().set(posX, posY);
						//_currentGraphic.setVertices(_currentGraphic.getXVertices());

						tv.setText("posX: "+posX+" posY: "+posY+" eventX "+event.getX()+" eventY "+event.getY());
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (_currentGraphic != null){
						if (event.getY() > 50){//within the board area and outside the toolbox
							int posX = Math.round((event.getX() - _currentGraphic.getWidth(2) / 2)/10)*10;
							int posY = Math.round((event.getY() - _currentGraphic.getHeight(2) / 2)/10)*10;

							_currentGraphic.moveTo(posX,posY);
							//_currentGraphic.(new Position(posX,posY));
							//							_currentGraphic.getPos().set(posX, posY);
							//	_currentGraphic.setVertices(_currentGraphic.getXVertices());
							//							
							//							_board.add(_currentGraphic);
							//							_toolbox.remove(_currentGraphic);
							_currentGraphic.setLocation(7);
							tv.setText("UP posX: "+posX+" posY: "+posY+" eventX "+event.getX()+" eventY "+event.getY());

						} else if (event.getY() <= 50){//within the toolbox area
							//							_toolbox.add(_currentGraphic);
							_currentGraphic.setLocation(6);
							updateToolbox();
							//							_board.remove(_currentGraphic);
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

		/**
		 * setActive sets all the pieces on the board and in the toolbox inactive except for the active piece
		 * @param p
		 */
		public void setActive(Piece p){
			//			for (Piece piece : _toolbox){
			//				piece.setActive(false);
			//			}
			//			for (Piece piece : _board){
			//				piece.setActive(false);
			//			}
			for (int i = 0; i < pieces.size(); i ++){
				if (pieces.get(i).getLocation() != 8 ){
					pieces.get(i).setActive(false);
				}
			}
			p.setActive(true);
		}

		/**
		 * updateToolBox rearranges the pieces in the right position
		 */
		public void updateToolbox(){
			int i = 0;
			int posX = 0;
			int posY = 0;
			Piece p;
			for (i = 0; i < pieces.size(); i ++){
				if (pieces.get(i).getLocation() == 6){
					p=pieces.get(i);
					posX = 10+60*i;
					posY = 10;
					p.moveTo(posX, posY);
				}

			}/*
			for (i=0;i<_toolbox.size();i++){
				p=_toolbox.get(i);
				posX = 10+60*i;
				posY = 10;
				p.moveTo(posX, posY);
			}*/
		}

		/**
		 * Recall takes all of the pieces on the board and puts them back into the toolbox
		 */
		public void recall(){
			int i = 0;
			int lastPiecePosition = 0;
			int posX;
			int posY;
			Piece p;
			for (i = 0; i < pieces.size(); i ++){
				if (pieces.get(i).getLocation()!= 6){
					pieces.get(i).setLocation(6);
					posX = 10 + 60 * i;
					posY = 10;
					pieces.get(i).moveTo(posX,posY);

				}
				pieces.get(i).setActive(false);
			}
			//			for (i=0;i<_board.size();i++){
			//				p = _board.get(i);
			//				if(_board.isEmpty()){//don't do anything if the board is already empty
			//					break;
			//				}
			//				if(!(_toolbox.isEmpty())){ //if the toolbox is not empty, make sure that i is the index of the last piece 
			//					lastPiecePosition = _toolbox.size();
			//					i = lastPiecePosition;
			//				} 
			//
			//				_toolbox.add(p);
			//				posX=10+60*i;
			//				posY=10;
			//				//p.setVerticesOffset(new Position(posX,posY));
			//				p.moveTo(posX, posY);
			//				
			//				p.setActive(false);
			//			}
			//	_board.clear();


		}
		public boolean toolboxEmpty(){

			for (int i =0; i < pieces.size(); i ++ ){
				if (pieces.get(i).getLocation() == 6){
					return false;
				}

			}
			return true;
		}
		public boolean boardEmpty(){

			for (int i =0; i < pieces.size(); i ++ ){
				if (pieces.get(i).getLocation() == 7){
					return false;
				}

			}
			return true;
		}
		@Override
		public void onDraw(Canvas canvas){
			canvas.drawColor(Color.WHITE);
			Paint paint = new Paint();
			paint.setColor(Color.BLACK); //Draw the line between the toolbox and play area
			canvas.drawLine(0, 50, 600, 50, paint);


			int i = 0; 
			Path path;

			//				Path puzzlePath = new Path();
			//				puzzlePath.moveTo(puzzle.getSolution().get(0).getX(), puzzle.getSolution().get(0).getY());
			//				puzzlePath.lineTo(puzzle.getSolution().get(1).getX(), puzzle.getSolution().get(1).getY());
			//				puzzlePath.lineTo(puzzle.getSolution().get(2).getX(), puzzle.getSolution().get(2).getY());
			//				puzzlePath.close();
			//				canvas.drawPath(puzzlePath, null);
			
			//make puzzle outline as bg
			path = new Path();
			path.moveTo(puzzle.getSolution().get(0).getX(), puzzle.getSolution().get(0).getY());
			path.lineTo(puzzle.getSolution().get(1).getX(), puzzle.getSolution().get(1).getY());
			path.lineTo(puzzle.getSolution().get(2).getX(), puzzle.getSolution().get(2).getY());
			path.close();
			path.offset(200, 300);
			Paint paint2 = new Paint();
			paint2.setColor(Color.GREEN);
			canvas.drawPath(path, paint2);
			
			for (int j = 0; j < pieces.size(); j++){ //Draw all objects in the toolbox
				if (pieces.get(j).getLocation() == 6){
					Piece p = pieces.get(j);
					if(p.getType()==2 || p.getType()==3 || p.getType()==4){
						
					path = new Path();
					path.moveTo(p.getVertices().get(0).getX(), p.getVertices().get(0).getY());
					path.lineTo(p.getVertices().get(1).getX(), p.getVertices().get(1).getY());
					path.lineTo(p.getVertices().get(2).getX(), p.getVertices().get(2).getY());
					path.close();
					path.offset(50*j, 40);
					//					path.offset(p.getPos().getX(), p.getPos().getY());
					canvas.drawPath(path, paint);
					}else if(p.getType()==1){
//						canvas.drawRect(p.getVertices().get(2).getX(), p.getVertices().get(0).getY(), p.getVertices().get(0).getX(), p.getVertices().get(1).getY(),paint);
						path = new Path();
						path.lineTo(p.getVertices().get(0).getX(), p.getVertices().get(0).getY());
						path.lineTo(p.getVertices().get(1).getX(), p.getVertices().get(1).getY());
						path.lineTo(p.getVertices().get(2).getX(), p.getVertices().get(2).getY());
						path.lineTo(p.getVertices().get(3).getX(), p.getVertices().get(3).getY());
						path.close();
						path.offset(50*j, 40);
					}
					i++;
					if (p.isActive()){
						paint.setColor(Color.CYAN);
						canvas.drawRect(p.getPos().getX(), p.getPos().getY(), p.getPos().getX()+50, p.getPos().getY()+50, paint);
					}
				}
				if (pieces.get(j).getLocation()==7){
					Piece p = pieces.get(j);
					if(p.getType()==2 || p.getType()==3 || p.getType()==4){
						
					path = new Path();
					path.moveTo(p.getXVertices().get(0).getX(), p.getXVertices().get(0).getY());
					path.lineTo(p.getXVertices().get(1).getX(), p.getXVertices().get(1).getY());
					path.lineTo(p.getXVertices().get(2).getX(), p.getXVertices().get(2).getY());
					path.close();
					//					path.offset(50*i, 40);

					//					path.offset(p.getPos().getX(), p.getPos().getY());

					canvas.drawPath(path, paint);
					canvas.drawPoint(p.getPos().getX(),p.getPos().getY(),paint);
					}else if(p.getType()==1){
//						canvas.drawRect(p.getVertices().get(2).getX(), p.getVertices().get(0).getY(), p.getVertices().get(0).getX(), p.getVertices().get(1).getY(),paint);
						path = new Path();
						path.lineTo(p.getVertices().get(0).getX(), p.getVertices().get(0).getY());
						path.lineTo(p.getVertices().get(1).getX(), p.getVertices().get(1).getY());
						path.lineTo(p.getVertices().get(2).getX(), p.getVertices().get(2).getY());
						path.lineTo(p.getVertices().get(3).getX(), p.getVertices().get(3).getY());
						path.close();
						path.offset(50*j, 40);
					}
					i++;

					if (p.isActive()){
						paint.setColor(Color.CYAN);
						canvas.drawRect(p.getPos().getX(), p.getPos().getY(), p.getPos().getX()+50, p.getPos().getY()+50, paint);
					}
				}
			}

			//				for (int j = 0; j < _toolbox.size(); j++){ //Draw all objects in the toolbox
			//					Piece p = _toolbox.get(j);
			//					path = new Path();
			//					path.moveTo(p.getVertices().get(0).getX(), p.getVertices().get(0).getY());
			//					path.lineTo(p.getVertices().get(1).getX(), p.getVertices().get(1).getY());
			//					path.lineTo(p.getVertices().get(2).getX(), p.getVertices().get(2).getY());
			//					path.close();
			//					path.offset(50*i, 40);
			//					//					path.offset(p.getPos().getX(), p.getPos().getY());
			//					canvas.drawPath(path, paint);
			//					i++;
			//					if (p.isActive()){
			//						paint.setColor(Color.CYAN);
			//						canvas.drawRect(p.getPos().getX(), p.getPos().getY(), p.getPos().getX()+50, p.getPos().getY()+50, paint);
			//					}
			//				}
			//			}
			//			if (! boardEmpty()){
			//				int i = 0;
			//				Path path;
			//				for (int j = 0; j < _board.size();j++) { //Draw all objects on the board
			//					Piece p = _board.get(j);
			//					path = new Path();
			//					path.moveTo(p.getXVertices().get(0).getX(), p.getXVertices().get(0).getY());
			//					path.lineTo(p.getXVertices().get(1).getX(), p.getXVertices().get(1).getY());
			//					path.lineTo(p.getXVertices().get(2).getX(), p.getXVertices().get(2).getY());
			//					path.close();
			//					//					path.offset(50*i, 40);
			//
			//					//					path.offset(p.getPos().getX(), p.getPos().getY());
			//
			//					canvas.drawPath(path, paint);
			//					canvas.drawPoint(p.getPos().getX(),p.getPos().getY(),paint);
			//					i++;
			//
			//					if (p.isActive()){
			//						paint.setColor(Color.CYAN);
			//						canvas.drawRect(p.getPos().getX(), p.getPos().getY(), p.getPos().getX()+50, p.getPos().getY()+50, paint);
			//					}
			//				}
			//			}

			// Draw the object that is being dragged (if there is one)
			if (_currentGraphic != null) {
				if(_currentGraphic.getType()==2 || _currentGraphic.getType()==3 || _currentGraphic.getType()==4){
				path = new Path();
				path.moveTo(_currentGraphic.getXVertices().get(0).getX(), _currentGraphic.getXVertices().get(0).getY());
				path.lineTo(_currentGraphic.getXVertices().get(1).getX(), _currentGraphic.getXVertices().get(1).getY());
				path.lineTo(_currentGraphic.getXVertices().get(2).getX(), _currentGraphic.getXVertices().get(2).getY());
				path.close();
				
				//path.offset(_currentGraphic.getPos().getX(), _currentGraphic.getPos().getY());
				canvas.drawPath(path, paint);
				}else if(_currentGraphic.getType()==1){
//					canvas.drawRect(_currentGraphic.getVertices().get(2).getX(), _currentGraphic.getVertices().get(0).getY(), _currentGraphic.getVertices().get(0).getX(), _currentGraphic.getVertices().get(1).getY(),paint);
					path = new Path();
					path.lineTo(_currentGraphic.getVertices().get(0).getX(), _currentGraphic.getVertices().get(0).getY());
					path.lineTo(_currentGraphic.getVertices().get(1).getX(), _currentGraphic.getVertices().get(1).getY());
					path.lineTo(_currentGraphic.getVertices().get(2).getX(), _currentGraphic.getVertices().get(2).getY());
					path.lineTo(_currentGraphic.getVertices().get(3).getX(), _currentGraphic.getVertices().get(3).getY());
					path.close();
					path.offset(50*i, 40);
				}
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
}