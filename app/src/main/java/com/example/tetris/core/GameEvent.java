package com.example.tetris.core;

public interface GameEvent {
    void execute(GameLogicManager logicManager);

    class LeftClickEvent implements GameEvent {
        @Override
        public void execute(GameLogicManager logicManager) {
            logicManager.moveLeft();
        }
    }

    class RightClickEvent implements GameEvent {
        @Override
        public void execute(GameLogicManager logicManager) {
            logicManager.moveRight();
        }
    }

    class PauseEvent implements GameEvent {
        @Override
        public void execute(GameLogicManager logicManager) {
            logicManager.setPause(!logicManager.isPaused());
        }
    }

    class SetSpeedEvent implements GameEvent {
        private final boolean speedUp;

        public SetSpeedEvent(boolean speedUp) {
            this.speedUp = speedUp;
        }

        @Override
        public void execute(GameLogicManager logicManager) {
            logicManager.setSpeedUp(speedUp);
        }
    }

    class RotateRightEvent implements GameEvent {
        @Override
        public  void execute(GameLogicManager logicManager) {
            logicManager.rotateRight();
        }
    }

    class RotateLeftEvent implements GameEvent {
        @Override
        public void execute(GameLogicManager logicManager) {
            logicManager.rotateLeft();
        }
    }
}
