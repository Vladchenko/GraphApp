package ru.yanchenko.vlad.graphapp.listeners;

public class KeyboardState {
    private boolean keyAlt = false;
    private boolean keyCtrl = false;
    private boolean keyShift = false;

    public boolean isKeyAlt() {
        return keyAlt;
    }

    public void setKeyAlt(boolean keyAlt) {
        this.keyAlt = keyAlt;
    }

    public boolean isKeyCtrl() {
        return keyCtrl;
    }

    public void setKeyCtrl(boolean keyCtrl) {
        this.keyCtrl = keyCtrl;
    }

    public boolean isKeyShift() {
        return keyShift;
    }

    public void setKeyShift(boolean keyShift) {
        this.keyShift = keyShift;
    }
}
