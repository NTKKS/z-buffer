package main;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            new Controller(window.getRaster());
            window.setVisible(true);
        });
    }
}

