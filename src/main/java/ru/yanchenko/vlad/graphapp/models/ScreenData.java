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
    public Point getScreenCenterPoint() {
        return screenCenterPoint;
    }

    public Color getWindowBackgroundColor() {
        return windowBackgroundColor;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
    // endregion Getters & Setters
}