package ru.yanchenko.vlad.graphapp.models;

import java.awt.*;

/**
 * Data related to screen like width, height, center.
 */
public class ScreenData {

    // region Fields
    // Field used to find out a size of a screen
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    // Width of a screen
    private final int screenWidth = (int) screenSize.getWidth();
    // Height of a screen
    private final int screenHeight = (int) screenSize.getHeight();
    private final Color windowBackgroundColor = new Color(0, 0, 0);
    // Center of a plane (screen)
    private final Point screenCenterPoint = new Point(screenWidth / 2, screenHeight / 2);
    // endregion Fields

    // region Getters & Setters
    /**
     * Returns the center point of the screen.
     *
     * @return the screen center point
     */
    public Point getScreenCenterPoint() {
        return screenCenterPoint;
    }

    /**
     * Returns the window background color.
     *
     * @return the background color (black)
     */
    public Color getWindowBackgroundColor() {
        return windowBackgroundColor;
    }

    /**
     * Returns the screen width in pixels.
     *
     * @return the screen width
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Returns the screen height in pixels.
     *
     * @return the screen height
     */
    public int getScreenHeight() {
        return screenHeight;
    }
    // endregion Getters & Setters
}