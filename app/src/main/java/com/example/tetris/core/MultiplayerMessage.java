package com.example.tetris.core;

public class MultiplayerMessage {
    public static final int RAW_MSG_SIZE = 16;
    public static final int TYPE_DONE = 0;

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
}
