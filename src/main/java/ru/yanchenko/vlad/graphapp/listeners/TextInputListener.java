package ru.yanchenko.vlad.graphapp.listeners;

import ru.yanchenko.vlad.graphapp.models.presentation.GraphUiState;
import ru.yanchenko.vlad.graphapp.presentation.DrawingTimer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

/**
 * Listener for text input during vertex addition and editing operations.
 * Properly separates UI state from domain data.
 */
public class TextInputListener implements KeyListener {
    private static final Logger LOGGER = Logger.getLogger(TextInputListener.class.getName());

    private final DrawingTimer drawingTimer;
    private final GraphUiState uiState; // UI state

    public TextInputListener(DrawingTimer drawingTimer, GraphUiState uiState) {
        this.drawingTimer = drawingTimer;
        this.uiState = uiState;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        try {
            if (!isTextInputActive()) {
                return;
            }

            char character = e.getKeyChar();

            if (isValidInputCharacter(character)) {
                appendCharacterToBuffer(character);
                refreshDisplay();

                LOGGER.fine("Added character '" + character + "' to edit buffer");
            }

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error in keyTyped", ex);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            if (!isTextInputActive()) {
                return;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE:
                    handleBackspace();
                    break;
                case KeyEvent.VK_DELETE:
                    handleDelete();
                    break;
                case KeyEvent.VK_ESCAPE:
                    handleEscape();
                    break;
                case KeyEvent.VK_ENTER:
                    // Let ConfirmAction handle this
                    break;
                default:
                    // Handle other special keys if needed
                    break;
            }

        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error in keyPressed", ex);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Handle key release events if needed
        // Currently no specific handling required
    }

    /**
     * Check if text input is currently active.
     */
    private boolean isTextInputActive() {
        return uiState.isAddingVertex() || uiState.isEditingVertex();
    }

    /**
     * Check if the character is valid for input.
     */
    private boolean isValidInputCharacter(char character) {
        return Character.isLetterOrDigit(character) ||
                Character.isSpaceChar(character) ||
                isSpecialCharacter(character);
    }

    /**
     * Check if character is a special character that's allowed.
     */
    private boolean isSpecialCharacter(char character) {
        // Allow common special characters for vertex names
        return character == '_' || character == '-' || character == '.' ||
                character == '@' || character == '#' || character == '$' ||
                character == '%' || character == '&' || character == '*' ||
                character == '+' || character == '=' || character == '!' ||
                character == '?' || character == ':' || character == ';' ||
                character == ',' || character == '(' || character == ')' ||
                character == '[' || character == ']' || character == '{' ||
                character == '}' || character == '<' || character == '>' ||
                character == '|' || character == '\\' || character == '/' ||
                character == '~' || character == '`' || character == '^';
    }

    /**
     * Append character to the edit buffer.
     */
    private void appendCharacterToBuffer(char character) {
        String currentBuffer = uiState.getEditBuffer();

        // Limit buffer length to prevent extremely long names
        if (currentBuffer.length() < getMaxBufferLength()) {
            uiState.setEditBuffer(currentBuffer + character);
        } else {
            LOGGER.warning("Edit buffer length limit reached");
        }
    }

    /**
     * Get maximum buffer length.
     */
    private int getMaxBufferLength() {
        return 50; // Reasonable limit for vertex names
    }

    /**
     * Handle backspace key.
     */
    private void handleBackspace() {
        String currentBuffer = uiState.getEditBuffer();
        if (!currentBuffer.isEmpty()) {
            uiState.setEditBuffer(currentBuffer.substring(0, currentBuffer.length() - 1));
            refreshDisplay();

            LOGGER.fine("Removed character from edit buffer via backspace");
        }
    }

    /**
     * Handle delete key.
     */
    private void handleDelete() {
        // For now, delete works the same as backspace
        // Could be enhanced to delete character at cursor position
        handleBackspace();
    }

    /**
     * Handle escape key.
     */
    private void handleEscape() {
        // Clear the edit buffer
        uiState.setEditBuffer("");
        refreshDisplay();

        LOGGER.fine("Cleared edit buffer via escape key");
    }

    /**
     * Refresh the display.
     */
    private void refreshDisplay() {
        drawingTimer.getCanvasRefreshTimer().start();
    }

    /**
     * Get current edit buffer content.
     */
    public String getCurrentBuffer() {
        return uiState.getEditBuffer();
    }

    /**
     * Check if text input is in progress.
     */
    public boolean isInputInProgress() {
        return isTextInputActive() && !uiState.getEditBuffer().isEmpty();
    }

    /**
     * Clear the edit buffer.
     */
    public void clearBuffer() {
        uiState.setEditBuffer("");
        refreshDisplay();
    }

    /**
     * Set the edit buffer content.
     */
    public void setBuffer(String content) {
        if (content != null && content.length() <= getMaxBufferLength()) {
            uiState.setEditBuffer(content);
            refreshDisplay();
        } else {
            LOGGER.warning("Invalid buffer content or length exceeded");
        }
    }
}