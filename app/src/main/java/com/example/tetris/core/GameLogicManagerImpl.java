package com.example.tetris.core;

public class GameLogicManagerImpl implements GameLogicManager {
    private static final int movementDelay = 1000 / (int)Constants.millisecondsPerFrame;
    private final TetrominoGenerator gen;
    private final BoardGravityManager gravityManager;
    private final boolean[][] board;
    private Tetromino currentTetromino;
    private int toNextMove;
    private int score;

    private boolean pause;
    private boolean done;
    private boolean speedUp;

    public GameLogicManagerImpl(TetrominoGenerator gen, BoardGravityManager gravityManager) {
        this.gen = gen;
        this.gravityManager = gravityManager;
        board = new boolean[Constants.boardWidth][Constants.boardHeight];
        getNewTetromino();
        toNextMove = movementDelay;
        score = 0;
        pause = false;
        done = false;
        speedUp = false;
    }

    private boolean isStateIllegal() {
        Iterable<Pair> blocks = currentTetromino.getBlocks();
        for(Pair p : blocks) {
            if(p.y < 0 || p.x < 0 || p.x >= Constants.boardWidth || board[p.x][p.y])
                return true;
        }
        return false;
    }

    private void checkRows() {
         score += gravityManager.checkBoard(board);
    }

    private void placeTetromino() {
        Iterable<Pair> blocks = currentTetromino.getBlocks();
        for(Pair p : blocks)
            board[p.x][p.y] = true;
    }

    private void getNewTetromino() {
        currentTetromino = Tetromino.getTetromino(Constants.boardWidth / 2, Constants.boardHeight - 1, gen.get());
    }

    private void endGame() {
        done = true;
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
        if(pause)
            return;
        currentTetromino.moveLeft();
        if(isStateIllegal())
            currentTetromino.moveRight();
    }

    @Override
    public void moveRight() {
        if(pause)
            return;
        currentTetromino.moveRight();
        if(isStateIllegal())
            currentTetromino.moveLeft();
    }

    @Override
    public void rotateRight() {
        if(pause)
            return;
        currentTetromino.rotateRight();
        if(isStateIllegal())
            currentTetromino.rotateLeft();
    }

    @Override
    public void rotateLeft() {
        if(pause)
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
