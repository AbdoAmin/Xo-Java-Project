package clientxo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyGui extends JFrame implements KeyListener {

    GameController myController;
    JTextArea txtArea = new JTextArea(20, 50);
    JScrollPane scPane = new JScrollPane(txtArea);
    JTextField txtField = new JTextField(40);

    public MyGui(GameController myController) {
        this.myController = myController;
        this.setLayout(new FlowLayout());

        JButton okButton = new JButton("Send");
        txtField.addKeyListener(this);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                myController.sendMessage(txtField.getText());
                txtField.setText("");
            }

        });

        add(scPane);
        add(txtField);
        add(okButton);

        //exit prog
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                myController.unRegister();
                System.exit(0);
            }
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ENTER) {
            myController.sendMessage(txtField.getText());
            txtField.setText("");
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void displayMessage(String myMessage) {
        txtArea.append(myMessage + "\n");
    }
};
