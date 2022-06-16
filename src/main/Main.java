package main;

import javax.swing.*;
import java.util.ArrayList;

public class Main extends JFrame implements Runnable {

    private Reader reader;
    private Process process;
    private ChargerTemplate chargerTemplate;
    private Gui gui;
    private Thread mainThread = null;

    Main() {
        initClasses();
        initGui();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    public void run() {
        reader.readLog();
        process.findMatch(reader.readedLog);
        process.analyzeChademo();
//        javax.swing.SwingUtilities.invokeLater(Gui.getInstance());
    }

    public void initClasses() {
        reader = new Reader();
        process = new Process();
        gui = new Gui(this);
        chargerTemplate = new ChargerTemplate();
    }

    public void initGui() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(gui);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setThemeUI() {
//        try {
//            UIManager.setLookAndFeel( new FlatLightLaf() );
//        } catch( Exception ex ) {
//            System.err.println( "Failed to initialize LaF" );
//        }
    }
}
