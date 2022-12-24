import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.text.StyledDocument;

public class NotePad extends JFrame implements ActionListener {

    private JMenuBar menuBar;
    private JToolBar toolBar,toolBar2;

    private JButton butOpen,save;
    private JTextPane textPane;
    private JTextField selectText;
    private JLabel select;
    private JFileChooser fileChooser;
    private JScrollPane scrollPane;


    public static void main(String[] args) {
        new NotePad();
    }

    public NotePad() {
        menuBar = new JMenuBar();
        toolBar2 = new JToolBar();
        save = new JButton("保存文件");
        save.addActionListener(this);
        save.setActionCommand("save");
        toolBar2.add(save);
        toolBar = new JToolBar();
        select = new JLabel("输入文件地址：");
        selectText = new JTextField(25);
        selectText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if((char)e.getKeyChar()==KeyEvent.VK_ENTER) {
                    String path = selectText.getText();
                        File f = new File(path);
                        if(!f.exists()){
                            JOptionPane.showMessageDialog(new JFrame(), "该路径不存在该文件", "路径错误",JOptionPane.WARNING_MESSAGE);

                        }else{
                            try {
                                InputStream is = new FileInputStream(f);
                                textPane.read(is, "d");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }



                }
            }
        });

        butOpen = new JButton("选择文件");
        butOpen.addActionListener(this);
        butOpen.setActionCommand("open");


        toolBar.add(select);
        toolBar.add(selectText);

        toolBar.add(butOpen);


        textPane = new JTextPane();
        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(
                        !((char)e.getKeyChar()==KeyEvent.VK_1
                        || (char)e.getKeyChar()==KeyEvent.VK_0)
                ) {
                        JOptionPane.showMessageDialog(new JFrame(), "该文件只能输入0或者1", "输入错误",JOptionPane.WARNING_MESSAGE);
                        e.consume();
                    }

                }

        });


        fileChooser = new JFileChooser();
        scrollPane = new JScrollPane(textPane);
        Container container = getContentPane();
        container.add(toolBar, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(toolBar2, BorderLayout.SOUTH);


        this.setTitle("Exercise17_21 NotePad");
        this.setSize(600, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setLocation((width - 500) / 2, (height - 400) / 2);
        this.setVisible(true);
        this.setResizable(true);

        this.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {
                        JOptionPane.showMessageDialog(new JFrame(), "文件已修改，是否保存?", "保存提示",JOptionPane.OK_OPTION);
                        int i = fileChooser.showSaveDialog(NotePad.this);
                        if (i == JFileChooser.APPROVE_OPTION) {
                            File f = fileChooser.getSelectedFile();
                            try {
                                FileOutputStream out = new FileOutputStream(f);
                                out.write(textPane.getText().getBytes());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {

                    }

                    @Override
                    public void windowIconified(WindowEvent e) {
                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });

    }

    @Override
    public void actionPerformed(ActionEvent e) {

         if (e.getActionCommand().equals("open")) {
            int i = fileChooser.showOpenDialog(NotePad.this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                byte b[]= new byte[0];
                try {
                    b = fileToByte(f.getPath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                for(int j=0;j<b.length;j++)
                {

                    System.out.print(String.format("%08d",Integer.parseInt(Integer.toBinaryString(0xff & b[j]))));
                    StyledDocument doc = textPane.getStyledDocument();
                    try
                    {
                        doc.insertString(0, String.format("%08d",Integer.parseInt(Integer.toBinaryString(0xff & b[j]))), null );
                    }
                    catch(Exception ex) { System.out.println(e); }
                }
//                try {
//                    InputStream is = new FileInputStream(f); 
//
//                    textPane.read(is, "d");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
            }
        } else if (e.getActionCommand().equals("save")) {
            int i = fileChooser.showSaveDialog(NotePad.this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    out.write(textPane.getText().getBytes());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static byte[] fileToByte(String filepath) throws IOException
    {

        byte[] bytes = null;
        FileInputStream fis = null;
        try{

            File file = new File(filepath);
            fis = new FileInputStream(file);
            bytes = new byte[(int) file.length()];
            fis.read(bytes);
        }catch(IOException e){

            e.printStackTrace();
            throw e;
        }finally{

            fis.close();
        }
        return bytes;
    }


}