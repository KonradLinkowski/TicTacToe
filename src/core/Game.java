package core;

import core.player.Bot;
import core.player.Player;

public class Game implements Runnable{

	private Board board;
	
	private Player player1;
	private Player player2;
	private Player activePlayer;
	private boolean timePlay;
	private long maxTime;
	
	private Vector winSpaceBeg;
	private Vector winSpaceEnd;
	
	private boolean end = false;
	private long startTime;
	
	private Reason endReason;
	

	public Game(int size, boolean isPlayer1Bot, boolean isPlayer2Bot, boolean timePlay, long maxTime) {
		board = new Board (size);
		this.player1 = isPlayer1Bot ? new Bot (Space.X, board) : new Player (Space.X, board);
		this.player2 = isPlayer2Bot ? new Bot (Space.O, board) : new Player (Space.O, board);
		activePlayer = player1;
		this.timePlay = timePlay;
		this.maxTime = maxTime;
		startTime = System.currentTimeMillis();
	}
	
	public void nextTurn () {
		activePlayer.nextTurn();
		changeActivePlayer();
	}
	
	public Space getCurrentMark () {
		return activePlayer.getMark();
	}
	
	private void changeActivePlayer () {
		if (activePlayer == player1) {
			activePlayer = player2;
		} else {
			activePlayer = player1;
		}
	}
	
	public boolean tryToPutSomething (int x, int y) {
		if (!end && board.isInBounds(x, y) && board.get(x, y) == Space.NONE) {
			activePlayer.setCoordinates(x, y);
			return true;
		}
		return false;
	}
	
	public boolean isTimeEnd () {
		if (System.currentTimeMillis() - startTime >= maxTime) {
			return true;
		}
		return false;
	}
	
	private boolean isGameEnd () {
		
		if (timePlay && isTimeEnd ()) {
			endReason = Reason.TIMES_OUT;
			return true;
		}
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.get(i, j) != Space.NONE && threeInRow (board.get(i, j), i, j)) {
					endReason = board.get(i, j) == Space.O ? Reason.O_WINS : Reason.X_WINS;
					return true;
				}
			}
		}
		if (!board.hasEmptySpaces()) {
			endReason = Reason.NO__MORE_SPACE;
			return true;
		}
		return false;
	}
	
	private boolean threeInRow (Space mark, int x, int y) {
		if (board.isInBounds(x - 1, y - 1) && board.isInBounds(x + 1, y + 1)
				&& board.get(x - 1, y - 1) == mark && board.get(x + 1, y + 1) == mark) {
			winSpaceBeg = new Vector (x - 1, y - 1);
			winSpaceEnd = new Vector (x + 1, y + 1);
			return true;
		}
		if (board.isInBounds(x, y - 1) && board.isInBounds(x, y + 1)
				&& board.get(x, y - 1) == mark && board.get(x, y + 1) == mark) {
			winSpaceBeg = new Vector (x, y - 1);
			winSpaceEnd = new Vector (x, y + 1);
			return true;
		}
		if (board.isInBounds(x - 1, y) && board.isInBounds(x + 1, y)
				&& board.get(x - 1, y) == mark && board.get(x + 1, y) == mark) {
			winSpaceBeg = new Vector (x - 1, y);
			winSpaceEnd = new Vector (x + 1, y);
			return true;
		}
		if (board.isInBounds(x + 1, y - 1) && board.isInBounds(x - 1, y + 1)
				&& board.get(x + 1, y - 1) == mark && board.get(x - 1, y + 1) == mark) {
			winSpaceBeg = new Vector (x + 1, y - 1);
			winSpaceEnd = new Vector (x - 1, y + 1);
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		while (!end) {
			nextTurn ();
			end = isGameEnd ();
		}
	}
	
	public boolean isTimePlay () {
		return timePlay;
	}
	
	public long getStartTime () {
		return startTime;
	}
	
	public Player getActivePlayer () {
		return activePlayer;
	}
	
	public Player getPlayerOne () {
		return player1;
	}
	
	public Player getPlayerTwo () {
		return player2;
	}
	
	public long getMaxTime () {
		return maxTime;
	}
	
	public boolean isEnd () {
		return end = isGameEnd();
	}
	
	public Vector getWinSpaceBeg () {
		return winSpaceBeg;
	}
	
	public Vector getWinSpaceEnd () {
		return winSpaceEnd;
	}

	public Reason getEndReason() {
		return endReason;
	}
}
