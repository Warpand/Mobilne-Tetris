package com.example.tetris.core;

public class OriginalGravityManager implements BoardGravityManager {
    private static final int[] pointsMap = {0, 40, 100, 300, 1200};
    private boolean checkRow(int j, boolean[][] board) {
        boolean r = true;
        for (boolean[] column : board)
            r &= column[j];
        return r;
    }
    @Override
    public int checkBoard(boolean[][] board) {
        int deleted = 0;
        int j = 0;
        while(j < board[0].length) {
            if(checkRow(j, board)) {
                deleted++;
                for(int i = 0; i < board.length; i++) {
                    if (board[i].length - 1 - j >= 0)
                        System.arraycopy(board[i], j + 1, board[i], j, board[i].length - 1 - j);
                    board[i][board[i].length - 1] = false;
                }
            }
            else
                j++;
        }
        return pointsMap[deleted];
    }
}
