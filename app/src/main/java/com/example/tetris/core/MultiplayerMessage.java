package com.example.tetris.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MultiplayerMessage {
    public static final int RAW_MSG_SIZE = 16;
    public static final int TYPE_DONE = 0;
    public static final int TYPE_SETTINGS = 1;
    public static final int TYPE_TETROMINO_REQUEST = 2;
    public static final int TYPE_TETROMINO_BATCH = 3;
    public static final int TYPE_RIVAL_SCORE_CHANGE = 4;
    public static final int TYPE_RIVAL_GAME_OVER = 5;

    private final int type;
    private final byte[] content;

    public MultiplayerMessage(int type, byte[] content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public int size() {
        return content.length;
    }

    public int at(int i) {
        return content[i];
    }

    public byte[] toRawMessage() {
        byte[] result = new byte[RAW_MSG_SIZE];
        result[0] = (byte)type;
        result[1] = (byte)content.length;
        System.arraycopy(content, 0, result, 2, content.length);
        return result;
    }

    public static MultiplayerMessage fromRawMessage(byte[] raw) {
        byte[] content = new byte[raw[1]];
        System.arraycopy(raw, 2, content, 0, raw[1]);
        return new MultiplayerMessage(raw[0], content);
    }

    /* Low level tricks */
    public int toScore() {
        return ByteBuffer.wrap(content).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static MultiplayerMessage fromScore(int score) {
        return new MultiplayerMessage(MultiplayerMessage.TYPE_RIVAL_SCORE_CHANGE,
                ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(score).array());
    }
}
