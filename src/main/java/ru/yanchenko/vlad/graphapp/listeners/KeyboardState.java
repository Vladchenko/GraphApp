package ru.yanchenko.vlad.graphapp.listeners;

/**
 * Tracks the current state of modifier keys (Alt, Ctrl, Shift).
 * <p>
 * Updated by keyboard event listeners and consulted by actions
 * to determine modifier-dependent behavior.
 */
public class KeyboardState {
    private boolean keyAlt = false;
    private boolean keyCtrl = false;
    private boolean keyShift = false;

    /**
     * Returns the state of the Alt key.
     *
     * @return true if Alt is pressed
     */
    public boolean isKeyAlt() {
        return keyAlt;
    }

    /**
     * Sets the state of the Alt key.
     *
     * @param keyAlt true if Alt is pressed
     */
    public void setKeyAlt(boolean keyAlt) {
        this.keyAlt = keyAlt;
    }

    /**
     * Returns the state of the Ctrl key.
     *
     * @return true if Ctrl is pressed
     */
    public boolean isKeyCtrl() {
        return keyCtrl;
    }

    /**
     * Sets the state of the Ctrl key.
     *
     * @param keyCtrl true if Ctrl is pressed
     */
    public void setKeyCtrl(boolean keyCtrl) {
        this.keyCtrl = keyCtrl;
    }

    /**
     * Returns the state of the Shift key.
     *
     * @return true if Shift is pressed
     */
    public boolean isKeyShift() {
        return keyShift;
    }

    /**
     * Sets the state of the Shift key.
     *
     * @param keyShift true if Shift is pressed
     */
    public void setKeyShift(boolean keyShift) {
        this.keyShift = keyShift;
    }
}
