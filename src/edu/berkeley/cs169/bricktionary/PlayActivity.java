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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayActivity extends Activity {
	TextView tv;
	Puzzle puzzle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Panel myPanel = new Panel(this);

		LinearLayout layout = new LinearLayout(this);
		LinearLayout buttonsLayout = new LinearLayout(this);

		layout.setOrientation(LinearLayout.VERTICAL);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

		layout.addView(myPanel, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 1.0f));

		// Recall button
		Button recallBtn = new Button(this);
		recallBtn.setText("Recall");
		recallBtn.setClickable(true);
		recallBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Perform action on click
				myPanel.recall();
			}
		});

		// Submit button
		Button submitBtn = new Button(this);
		submitBtn.setText("Submit");
		submitBtn.setClickable(true);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		submitBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				int score = puzzle.calculateScore();
				builder.setMessage("Your score: "+score)
				.setCancelable(false)
				.setPositiveButton("Replay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = new Intent().setClass(PlayActivity.this, OutlineActivity.class);
						startActivity(i);
					}
				})
				.setNegativeButton("Next Level", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						int nextLevel = GlobalVariables.getCurrentLevel()+1;
						GlobalVariables.setCurrentLevel(nextLevel);
						Intent i = new Intent().setClass(PlayActivity.this, OutlineActivity.class);
						startActivity(i);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		LinearLayout.LayoutParams btnLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);

		buttonsLayout.addView(recallBtn, btnLP); // add recall button to view
		buttonsLayout.addView(submitBtn, btnLP); // add submit button to view
		buttonsLayout.setBackgroundColor(Color.TRANSPARENT);
		layout.addView(buttonsLayout);

		tv = new TextView(this);
		layout.addView(tv);
		Drawable bg = this.getResources().getDrawable(R.drawable.brick);
		layout.setBackgroundDrawable(bg);
		//		layout.setBackgroundColor(Color.BLUE);

		setContentView(layout);
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		private ActionThread _thread;
		private ArrayList<Piece> _board = new ArrayList<Piece>();
		private ArrayList<Piece> _toolbox = new ArrayList<Piece>();
		private Piece _currentGraphic = null;

		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			_thread = new ActionThread(getHolder(), this);

			puzzle = new Puzzle(GlobalVariables.getCurrentLevel()); // Retrieve puzzle for Level 1
			ArrayList<Piece> pieces = puzzle.pieces; // Retrieve the pieces for
			// that puzzle

			for (int i = 0; i < pieces.size(); i++) {
				Piece p = pieces.get(i);
				p.moveTo(10 + 70 * i, 10); // set the right coordinates for each
				// piece in toolbox
				_toolbox.add(p); // add each piece to the toolbox
			}

			updateToolbox();

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// synchronized (_thread.getSurfaceHolder()) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				float x = event.getX();
				float y = event.getY();

				for (Piece piece : _toolbox) {
					if (Math.abs(x - 25 - piece.getPos().getX()) < 25
							&& Math.abs(y - 25 - piece.getPos().getY()) < 25) {
						_currentGraphic = piece;
						_toolbox.remove(piece);
						setActive(piece);
						updateToolbox();
						break;
					}
				}

				for (Piece piece : _board) {
					if (Math.abs(x - piece.getPos().getX()) < 25
							&& Math.abs(y - piece.getPos().getY()) < 25) {
						_currentGraphic = piece;
						_board.remove(piece);
						setActive(piece);
						updateToolbox();
						break;
					}
				}

			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (_currentGraphic != null) {
					_currentGraphic.setActive(false);
					int posX = (int) event.getX()
					- _currentGraphic.getWidth(_currentGraphic
							.getType()) / 2;
					int posY = (int) event.getY()
					- _currentGraphic.getHeight(_currentGraphic
							.getType()) / 2;
					_currentGraphic.moveTo(posX, posY);
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (_currentGraphic != null) {
					if (event.getY() > 80) {// within the board area and outside
						// the toolbox
						int posX = Math.round((event.getX() - _currentGraphic
								.getWidth(_currentGraphic.getType()) / 2) / 10) * 10;
						int posY = Math
						.round((event.getY() - _currentGraphic
								.getHeight(_currentGraphic.getType()) / 2) / 10) * 10;

						// //Deals with overlap
						// for(int i = 0; i<_board.size();i++){
						// Piece p = _board.get(i);
						// if((p.getPos().getX()<=event.getX() &&
						// p.getPos().getX()+(p.getWidth(p.getType())/2) >=
						// event.getX()) &&
						// (p.getPos().getY()<=event.getY() &&
						// p.getPos().getY()+(p.getHeight(p.getType())/2) >=
						// event.getY())) {
						// posX = Math.round(((event.getX() +
						// p.getWidth(p.getType())/2)-
						// _currentGraphic.getWidth(_currentGraphic.getType()) /
						// 2)/10)*10;
						// posY = Math.round(((event.getY() +
						// p.getHeight(p.getType())/2)-
						// _currentGraphic.getHeight(_currentGraphic.getType())
						// / 2)/10)*10;;
						// break;
						// }
						// }

						_currentGraphic.moveTo(posX, posY);

						tv.setText("type " + _currentGraphic.getType()
								+ " POS " + _currentGraphic.getPos().getX());
						_board.add(_currentGraphic);
						_toolbox.remove(_currentGraphic);
					} else if (event.getY() <= 80) {// within the toolbox area
						_toolbox.add(_currentGraphic);
						_board.remove(_currentGraphic);
						updateToolbox();
					}

					if (_currentGraphic.isActive()) {
						_currentGraphic.rotate();
					}

					setActive(_currentGraphic);
					_currentGraphic = null;
				}
			}
			return true;
		}

		// }

		/**
		 * setActive sets all the pieces on the board and in the toolbox
		 * inactive except for the active piece
		 * 
		 * @param p
		 */
		public void setActive(Piece p) {
			for (Piece piece : _toolbox) {
				piece.setActive(false);
			}
			for (Piece piece : _board) {
				piece.setActive(false);
			}
			p.setActive(true);
		}

		/**
		 * updateToolBox rearranges the pieces in the right position
		 */
		public void updateToolbox() {
			int posX = 0;
			int posY = 0;
			for (int i = 0; i < _toolbox.size(); i++) {
				Piece p = _toolbox.get(i);
				posX = 10 + 70 * i;

				switch (p.getType()) {
				case Piece.square:
					posY = 10;
					break;
				case Piece.mediumTriangle:
					posY = 60;
					break;
				case Piece.largeTriangle:
					posY = 70;
					break;
				default:
					posY = 50;
					break;
				}

				p.moveTo(posX, posY);
				i++;
			}
		}

		/**
		 * Recall takes all of the pieces on the board and puts them back into
		 * the toolbox
		 */
		public void recall() {
			int lastPiecePosition = 0;
			int posX = 0;
			int posY = 0;

			for (int i = 0; i < _board.size(); i++) {
				Piece p = _board.get(i);

				if (!(_toolbox.isEmpty())) { // if the toolbox is not empty,
					// make sure that i is the index
					// of the last piece
					lastPiecePosition = _toolbox.size();
					posX = 10 + 70 * lastPiecePosition;
				} else {
					posX = 10 + 70 * i;
				}

				switch (p.getType()) {
				case Piece.square:
					posY = 10;
					break;
				case Piece.mediumTriangle:
					posY = 60;
					break;
				case Piece.largeTriangle:
					posY = 70;
					break;
				default:
					posY = 50;
					break;
				}

				p.moveTo(posX, posY);
				p.setActive(false);
				_toolbox.add(p);

			}
			_board.clear();
		}

		@Override
		public void onDraw(Canvas canvas){
			//draw the background
			BitmapDrawable background;
			background = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.brick));
			background.setBounds(0, 0, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
			background.draw(canvas);

			//paint for the pieces
			Paint piecePaint = new Paint();
			piecePaint.setColor(Color.RED);

			Paint paint = new Paint();
			paint.setColor(Color.BLACK); 
			canvas.drawLine(0, 80, 600, 80, paint); //Draw the line between the toolbox and play area

			Display display = getWindowManager().getDefaultDisplay(); 
			int displayWidth = display.getWidth();
			int displayHeight = display.getHeight();
			
			if(GlobalVariables.outlineOn){
				//Draw puzzle in the background 
				Path puzzlePath = new Path();
				ArrayList<Position> puzzlePositions = puzzle.getSolution();
				for(int i = 0; i < puzzlePositions.size();i++){
					puzzlePath.lineTo(puzzlePositions.get(i).getX(), 
							puzzlePositions.get(i).getY());
				}
				puzzlePath.close();
				puzzlePath.offset(displayWidth/2-50, displayHeight/2-50);
				canvas.drawPath(puzzlePath, paint);
			}else {
				//do not display outline
			}
			
			int posY = 0;
			if (! _toolbox.isEmpty()){
				Path toolboxPiecePath;

				for (int j = 0; j < _toolbox.size(); j++){ //Draw all objects in the toolbox
					Piece toolboxPiece = _toolbox.get(j);

					toolboxPiecePath = new Path();
					ArrayList<Position> toolboxPieceVertices = toolboxPiece.getVertices();

					for(int i = 0; i < toolboxPieceVertices.size();i++){
						if(i==0){
							toolboxPiecePath.moveTo(toolboxPieceVertices.get(i).getX(), 
									toolboxPieceVertices.get(i).getY());							
						}else{
							toolboxPiecePath.lineTo(toolboxPieceVertices.get(i).getX(), 
									toolboxPieceVertices.get(i).getY());
						}
					}

					toolboxPiecePath.close();

					switch(toolboxPiece.getType()){
					case Piece.square: posY=10; break;
					case Piece.mediumTriangle: posY=60; break;
					case Piece.largeTriangle: posY=70; break;
					default: posY=50;break;
					}

					toolboxPiecePath.offset(10+70*j, posY);
					canvas.drawPath(toolboxPiecePath, piecePaint);

					if (toolboxPiece.isActive()){
						paint.setColor(Color.CYAN);
						canvas.drawRect(toolboxPiece.getPos().getX(), 
								toolboxPiece.getPos().getY(), 
								toolboxPiece.getPos().getX()+50, 
								toolboxPiece.getPos().getY()+50, paint);
					}
				}
			}
			if (! _board.isEmpty()){
				Path boardPiecePath;

				for (int j = 0; j < _board.size();j++) { //Draw all objects on the board		
					Piece boardPiece = _board.get(j);

					boardPiecePath = new Path();
					ArrayList<Position> boardPieceVertices = boardPiece.getXVertices();

					for(int i = 0; i < boardPieceVertices.size();i++){
						if(i==0){
							boardPiecePath.moveTo(boardPieceVertices.get(i).getX(), 
									boardPieceVertices.get(i).getY());							
						}else{
							boardPiecePath.lineTo(boardPieceVertices.get(i).getX(), 
									boardPieceVertices.get(i).getY());
						}
					}

					boardPiecePath.close();
					canvas.drawPath(boardPiecePath, piecePaint);

					if (boardPiece.isActive()){
						paint.setColor(Color.CYAN);
						canvas.drawRect(boardPiece.getPos().getX(), 
								boardPiece.getPos().getY(), 
								boardPiece.getPos().getX()+50, 
								boardPiece.getPos().getY()+50, paint);
					}
				}
			}

			// Draw the object that is being dragged (if there is one)
			if (_currentGraphic != null) {
				Path path = new Path();

				ArrayList<Position> pieceVertices = _currentGraphic.getXVertices();

				for(int i = 0; i < pieceVertices.size();i++){
					if(i==0){
						path.moveTo(pieceVertices.get(i).getX(), 
								pieceVertices.get(i).getY());
					}else{
						path.lineTo(pieceVertices.get(i).getX(), 
								pieceVertices.get(i).getY());
					}
				}

				path.close();
				canvas.drawPath(path, piecePaint);
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		public void surfaceCreated(SurfaceHolder holder) {
			_thread.setRunning(true);
			_thread.start();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// simply copied from sample application LunarLander:
			// we have to tell thread to shut down & wait for it to finish, or
			// else
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