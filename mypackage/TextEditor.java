package mypackage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextEditor extends JFrame implements ActionListener {
    
    JTextArea editor;
    JTextArea linenumbers;
    Font font = new Font("JetBrains Mono",Font.BOLD,18);

    JMenuBar menuBar;
    JMenu jMenu;
    JMenuItem openItem,saveItem,saveAsItem,exitItem;
    JFileChooser fileChooser;
    File currentFile;

    TextEditor(){

        setSize(1080,720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initEditor();
        initMenuBar();

        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){

        if(e.getSource() == openItem){
            openFile();
            updateLineNumber();
        }
        if(e.getSource() == saveItem){
            saveFile();
        }
        if(e.getSource() == saveAsItem){
            saveAs();
        }
        if(e.getSource() == exitItem){
            System.exit(0);
        }
    }
    void initEditor(){
        
        linenumbers = new JTextArea("   1 \n");
        linenumbers.setFont(font);
        linenumbers.setBackground(new Color(30,34,40));
        linenumbers.setForeground(Color.LIGHT_GRAY);
        linenumbers.setEditable(false);
        linenumbers.setMargin(new Insets(5, 5, 5, 5));

        editor = new JTextArea();
        editor.setFont(font);
        editor.setBackground(new Color(40,44,52));
        editor.setForeground(Color.WHITE);
        editor.setMargin(new Insets(5, 5, 5, 5));
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);

        editor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateLineNumber();
            }
            public void removeUpdate(DocumentEvent e) {
                updateLineNumber();
            }
            public void changedUpdate(DocumentEvent e) {
                updateLineNumber();
            }
        });

        JScrollPane sp = new JScrollPane(editor);
        sp.setRowHeaderView(linenumbers);
        add(sp,BorderLayout.CENTER);
    }
    void initMenuBar(){

        menuBar = new JMenuBar();
        
        jMenu = new JMenu("File");
    
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save As");
        exitItem = new JMenuItem("Exit");

        jMenu.add(openItem);
        jMenu.add(saveItem);
        jMenu.add(saveAsItem);
        jMenu.add(exitItem);

        menuBar.add(jMenu);

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        exitItem.addActionListener(this);

        setJMenuBar(menuBar);
    }

    private void openFile(){
        int option = fileChooser.showOpenDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
            currentFile = fileChooser.getSelectedFile();
            try(BufferedReader br = new BufferedReader(new FileReader(currentFile))){
                editor.read(br,null);
            }
            catch(IOException ex){
                JOptionPane.showMessageDialog(this, "Could not open file.");
            }
        }
    }

    private void saveFile(){
        if(currentFile != null){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(currentFile))){
                editor.write(bw);
            } 
            catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save file");
            }
        }
        else{
            saveAs();
        }
    }
    private void saveAs(){
        int option = fileChooser.showSaveDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
            currentFile = fileChooser.getSelectedFile();
            saveFile();
        }
    }

    void updateLineNumber() {
        int n = editor.getLineCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            sb.append(String.format("%4d ", i));
            if (i != n) {
                sb.append("\n");
            }
        }
        linenumbers.setText(sb.toString());
    }
}
