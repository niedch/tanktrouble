package MultiplayerServer.DevConsole;

import MultiplayerServer.IView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DevConsoleView extends JFrame implements IView {
    private JTextArea textArea;
    private JTextField textField;
    private JButton btn;
    private DevConsolePresenter presenter;

    public DevConsoleView(final DevConsolePresenter presenter){
        super("Server Console");
        this.presenter = presenter;

        initJFrame();

        textArea = new JTextArea();
        textArea.setEditable(false);
        this.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.submitCommand(textField.getText());
            }
        });

        panel.add(textField, BorderLayout.CENTER);
        btn = new JButton("Submit");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.submitCommand(textField.getText());
            }
        });
        panel.add(btn,BorderLayout.EAST);

        this.add(panel,BorderLayout.SOUTH);
        this.setVisible(true);
    }


    private void initJFrame() {
        this.setLocationRelativeTo(null);
        this.setSize(300, 600);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public void println(String s){
        textArea.append(s+"\n");
    }

    @Override
    public void updateView() {
        textArea.setText(presenter.convertToText());
        textField.setText(presenter.getTextFieldValue());
    }

    @Override
    public void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this,
                errorMessage,
                "Upps error happend",
                JOptionPane.ERROR_MESSAGE);
    }
}
