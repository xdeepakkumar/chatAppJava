import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.awt.BorderLayout;

public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare Components
    private JLabel heading = new JLabel("Client");
    private JTextArea textArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);




    //constructor
    public Client(){
        try {
            System.out.println("sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWritting();



            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("Key Released");
                if(e.getKeyCode() == 10){
                    String contentToSend = messageInput.getText();
                    textArea.append("Me : " + contentToSend + " \n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }

        });
    }

    private void createGUI(){
        this.setTitle("Client Messager [END]");
        this.setSize(400,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for components
        heading.setFont(font);
        textArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //layout setting for frame
        this.setLayout(new BorderLayout());

        //adding components to frame
        this.add(heading, BorderLayout.NORTH);
        this.add(textArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);





        this.setVisible(true);


    }
    
    //reading
    public void startReading(){
        //thread - continue reading
        Runnable r1 = () -> {
            System.out.println("reader started..");
            try {
                while(true){
               
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("server terminated the chat....");
                        JOptionPane.showMessageDialog(this, "server terminated the chat....");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    textArea.append("server: " + msg + "\n");
                }
            } catch (Exception e) {
                System.out.println("connection closed...");
            }
        };
        new Thread(r1).start();
    }
    //writting
    public void startWritting(){
        //thread - continue writting
        Runnable r2 = () -> {
            System.out.println("writting started..");
            try {
                while(!socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
                
            } catch (Exception e) {
                System.out.println("connection closed...");
            }
        };
        new Thread(r2).start();
    }

    
    public static void main(String[] args) {
        System.out.println("This is client..");
        new Client();
    }
}
