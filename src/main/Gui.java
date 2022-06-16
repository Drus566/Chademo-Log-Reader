package main;

import javax.swing.*;
import java.awt.*;

public class Gui extends JPanel {
    private Dimension size;
    private Main main;

    public Gui(Main main) {
        this.main = main;
        setPanelSize();
    }

    private void setPanelSize() {
        size = new Dimension(640, 740);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }
}
