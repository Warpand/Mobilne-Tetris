package com.example.tetris.core;

public interface SpeedManager {
    int getSpeed(boolean speedUp);
    void adjust(int score);

    class ConstantSpeedManager implements SpeedManager {
        @Override
        public int getSpeed(boolean speedUp) {
            return (speedUp) ? 5 : 1;
        }

        @Override
        public void adjust(int score) {}
    }

    class ConstantSpeedManagerHard implements SpeedManager {
        @Override
        public int getSpeed(boolean speedUp) {
            return (speedUp) ? 10 : 3;
        }

        @Override
        public void adjust(int score) {}
    }

    class AdjustingSpeedManager implements SpeedManager {
        private static final int[][] LeveLToSpeed = {
            {1, 5},
            {2, 8},
            {3, 10}
        };

        private int level = 0;
        @Override
        public int getSpeed(boolean speedUp) {
            return LeveLToSpeed[level][(speedUp) ? 1 : 0];
        }

        @Override
        public void adjust(int score) {
            if(level == 0 && score >= 2000)
                level++;
            if(level == 1 && score >= 4000)
                level++;
        }
    }
}
