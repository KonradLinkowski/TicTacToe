package core.player;

import core.Board;
import core.Space;

public class Bot extends Player {

	private Space enemyMark;
	
	private boolean firstTurn = true;
	
	public Bot(Space mark, Board board) {
		super(mark, board);
		enemyMark = mark == Space.O ? Space.X : Space.O;
	}
	
	@Override
	public void nextTurn () {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.get(i, j) == Space.NONE && winSituation (i, j)) {
					return;
				}
			}
		}
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.get(i, j) == Space.NONE && middleSituation (i, j)) {
					return;
				}
			}
		}
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.get(i, j) == Space.NONE && twoInRowSituation (i, j)) {
					return;
				}
			}
		}
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.get(i, j) == Space.NONE && normalSituation (i, j)) {
					return;
				}
			}
		}
		if (firstTurn && board.get(board.getSize() / 2, board.getSize() / 2) == Space.NONE) {
			board.set(board.getSize() / 2, board.getSize() / 2, symbol);
			firstTurn = false;
			return;
		}
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.get(i, j) == Space.NONE) {
					board.set(i, j, symbol);
					return;
				}
			}
		}
	}
	
	private boolean middleSituation (int x, int y) {
		if (board.isInBounds(x - 1, y - 1) && board.isInBounds(x + 1, y + 1)
				&& board.get(x - 1, y - 1) == enemyMark && board.get(x + 1, y + 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x, y - 1) && board.isInBounds(x, y + 1)
				&& board.get(x, y - 1) == enemyMark && board.get(x, y + 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x - 1, y) && board.isInBounds(x + 1, y)
				&&board.get(x - 1, y) == enemyMark && board.get(x + 1, y) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 1, y - 1) && board.isInBounds(x - 1, y - 1)
				&& board.get(x + 1, y - 1) == enemyMark && board.get(x - 1, y - 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		return false;
	}
	
	private boolean twoInRowSituation (int x, int y) {
		if (board.isInBounds(x - 2, y - 2) && board.isInBounds(x - 1, y - 1)
				&& board.get(x - 2, y - 2) == enemyMark && board.get(x - 1, y - 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x, y - 2) && board.isInBounds(x, y - 1)
				&& board.get(x, y - 2) == enemyMark && board.get(x, y - 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 2, y - 2) && board.isInBounds(x + 1, y - 1)
				&& board.get(x + 2, y - 2) == enemyMark && board.get(x + 1, y - 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 2, y) && board.isInBounds(x + 1, y)
				&& board.get(x + 2, y) == enemyMark && board.get(x + 1, y) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 2, y + 2) && board.isInBounds(x + 1, y + 1)
				&& board.get(x + 2, y + 2) == enemyMark && board.get(x + 1, y + 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x, y + 2) && board.isInBounds(x, y + 1)
				&& board.get(x, y + 2) == enemyMark && board.get(x, y + 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x - 2, y + 2) && board.isInBounds(x - 1, y + 1)
				&& board.get(x - 2, y + 2) == enemyMark && board.get(x - 1, y + 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x - 2, y) && board.isInBounds(x - 1, y)
				&& board.get(x - 2, y) == enemyMark && board.get(x - 1, y) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		return false;
	}
	
	private boolean winSituation (int x, int y) {
		if (board.isInBounds(x - 2, y - 2) && board.isInBounds(x - 1, y - 1)
				&& board.get(x - 2, y - 2) == symbol && board.get(x - 1, y - 1) == symbol) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x, y - 2) && board.isInBounds(x, y - 1)
				&& board.get(x, y - 2) == symbol && board.get(x, y - 1) == symbol) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 2, y - 2) && board.isInBounds(x + 1, y - 1)
				&& board.get(x + 2, y - 2) == symbol && board.get(x + 1, y - 1) == symbol) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 2, y) && board.isInBounds(x + 1, y)
				&& board.get(x + 2, y) == symbol && board.get(x + 1, y) == symbol) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 2, y + 2) && board.isInBounds(x + 1, y + 1)
				&& board.get(x + 2, y + 2) == symbol && board.get(x + 1, y + 1) == symbol) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x, y + 2) && board.isInBounds(x, y + 1)
				&& board.get(x, y + 2) == symbol && board.get(x, y + 1) == symbol) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x - 2, y + 2) && board.isInBounds(x - 1, y + 1)
				&& board.get(x - 2, y + 2) == symbol && board.get(x - 1, y + 1) == symbol) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x - 2, y) && board.isInBounds(x - 1, y)
				&& board.get(x - 2, y) == symbol && board.get(x - 1, y) == symbol) {
			board.set(x, y, symbol);
			return true;
		}
		return false;
	}

	private boolean normalSituation (int x, int y) {
		if (board.isInBounds(x - 1, y - 1) && board.get(x - 1, y - 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x, y - 1) && board.get(x, y - 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 1, y - 1) && board.get(x + 1, y - 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 1, y) && board.get(x + 1, y) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x + 1, y + 1) && board.get(x + 1, y + 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x, y + 1) && board.get(x, y + 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x - 1, y + 1) && board.get(x - 1, y + 1) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		if (board.isInBounds(x - 1, y) && board.get(x - 1, y) == enemyMark) {
			board.set(x, y, symbol);
			return true;
		}
		return false;
	}
}

