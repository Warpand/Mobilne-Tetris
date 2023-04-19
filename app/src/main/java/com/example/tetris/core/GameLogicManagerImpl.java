package com.example.tetris.core;

import com.example.tetris.database.DataEntry;
import com.example.tetris.database.EntryDao;

public class GameLogicManagerImpl implements GameLogicManager {
    private static final int movementDelay = 1000 / (int)Constants.millisecondsPerFrame;
    private final TetrominoGenerator gen;
    private final BoardGravityManager gravityManager;
    private final ScoreCounter scoreCounter;
    private final EntryDao dao;
    private final boolean[][] board;
    private Tetromino currentTetromino;
    private int toNextMove;
    private int score;
    private int deletedRows;
    private int moves;

    private boolean pause;
    private boolean done;
    private boolean speedUp;

    public GameLogicManagerImpl(TetrominoGenerator gen, BoardGravityManager gravityManager, EntryDao dao) {
        this.gen = gen;
        this.gravityManager = gravityManager;
        scoreCounter = gravityManager.getAssociatedScoreCounter();
        this.dao = dao;
        board = new boolean[Constants.boardWidth][Constants.boardHeight];
        getNewTetromino();
        toNextMove = movementDelay;
        score = 0;
        deletedRows = 0;
        moves = 0;
        pause = false;
        done = false;
        speedUp = false;
    }

    private boolean isStateIllegal() {
        Iterable<Pair> blocks = currentTetromino.getBlocks();
        for(Pair p : blocks) {
            if(p.y < 0 || p.x < 0 || p.x >= Constants.boardWidth || p.y >= Constants.boardHeight ||
                    board[p.x][p.y])
                return true;
        }
        return false;
    }

    private void checkRows() {
         int deleted = gravityManager.checkBoard(board);
         deletedRows += deleted;
         score += scoreCounter.rowsToScore(deleted);
    }

    private void placeTetromino() {
        Iterable<Pair> blocks = currentTetromino.getBlocks();
        for(Pair p : blocks)
            board[p.x][p.y] = true;
    }

    private void getNewTetromino() {
        currentTetromino = Tetromino.spawnTetromino(gen.get());
        moves++;
    }

    private void endGame() {
        done = true;
        DataEntry dataEntry = new DataEntry(score, deletedRows, moves - 1);
        dao.insert(dataEntry);
    }

    @Override
    public void update() {
        if(done || pause)
            return;
        toNextMove -= (speedUp) ? 5 : 1;
        if(toNextMove <= 0) {
            toNextMove = movementDelay;
            currentTetromino.moveDown();
            if(isStateIllegal()) {
                currentTetromino.moveUp();
                placeTetromino();
                checkRows();
                getNewTetromino();
                if(isStateIllegal())
                    endGame();
            }
        }
    }

    @Override
    public void moveLeft() {
        if(pause || done)
            return;
        currentTetromino.moveLeft();
        if(isStateIllegal())
            currentTetromino.moveRight();
    }

    @Override
    public void moveRight() {
        if(pause || done)
            return;
        currentTetromino.moveRight();
        if(isStateIllegal())
            currentTetromino.moveLeft();
    }

    @Override
    public void rotateRight() {
        if(pause || done)
            return;
        currentTetromino.rotateRight();
        if(isStateIllegal())
            currentTetromino.rotateLeft();
    }

    @Override
    public void rotateLeft() {
        if(pause || done)
            return;
        currentTetromino.rotateLeft();
        if(isStateIllegal())
            currentTetromino.rotateRight();
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getNextTetrominoId() {
        return gen.next();
    }

    @Override
    public boolean getTile(int i, int j) {
        return board[i][j];
    }

    @Override
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    @Override
    public boolean isPaused() {
        return pause;
    }

    @Override
    public boolean isGameOver() {
        return done;
    }

    @Override
    public void setPause(boolean state) {
        pause = state;
    }

    @Override
    public void setSpeedUp(boolean state) {
        speedUp = state;
    }
}
