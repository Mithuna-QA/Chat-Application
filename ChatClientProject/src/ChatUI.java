import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;

public class ChatUI{
    public ChatClient client;
    public ChatServerInt server;
    public void doConnect(){
            if (this.name.length()<2){JOptionPane.showMessageDialog(frame, "You need to type a name."); return;}
            if (this.ip.length()<2){JOptionPane.showMessageDialog(frame, "You need to type an IP."); return;}
            try{
                client=new ChatClient(this.name);
                client.setGUI(this);
                server=(ChatServerInt)Naming.lookup("rmi://"+this.ip+"/myabc");
                server.login(client);
                updateUsers(server.getConnected());
//                connect.setText("Disconnect");
            }catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't connect....");}
    }

    public void sendText()  {
        String st=tf.getText();
        if (st.toLowerCase().equals("bye")){
            st="["+this.name+"]"+" left";
            tf.setText("");
            server.logout(client);
            try {
                server.publish(st);
                frame.setVisible(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            st="["+this.name+"] "+st;
            tf.setText("");
            try {
                updateUsers(server.getConnected());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //Remove if you are going to implement for remote invocation
            try{
                server.publish(st);
            }catch(Exception e){e.printStackTrace();}
        }

    }

    public void writeMsg(String st){  tx.setText(tx.getText()+"\n"+st);  }

    public void updateUsers(Vector v){
        DefaultListModel listModel = new DefaultListModel();
        if(v!=null) for (int i=0;i<v.size();i++){
            try{  String tmp=((ChatClientInt)v.get(i)).getName();
                listModel.addElement(tmp);
            }catch(Exception e){e.printStackTrace();}
        }
        lst.setModel(listModel);
    }

//    public static void main(String [] args){
//        System.out.println("Hello World !");
//        ChatUI c=new ChatUI();
//    }

    //User Interface code.
    public ChatUI(String username){
        frame=new JFrame("Group Chat");
        JPanel main =new JPanel();
        JPanel top =new JPanel();
        JPanel cn =new JPanel();
        JPanel bottom =new JPanel();
//        ip=new JTextField();
        tf=new JTextField();
//        name=new JTextField();
//        name.setText(username);
        this.name = username;
        this.ip = "127.0.1.1";
        tx=new JTextArea();
//        connect=new JButton("Connect");
        JButton bt=new JButton("Send");
        lst=new JList();
        main.setLayout(new BorderLayout(5,5));
//        top.setLayout(new GridLayout(1,0,5,5));
        cn.setLayout(new BorderLayout(5,5));
        bottom.setLayout(new BorderLayout(5,5));
//        top.add(new JLabel("Your name: "));top.add(name);
//        top.add(new JLabel("Server Address: "));top.add(ip);
//        top.add(connect);
        cn.add(new JScrollPane(tx), BorderLayout.CENTER);
        cn.add(lst, BorderLayout.EAST);
        bottom.add(tf, BorderLayout.CENTER);
        bottom.add(bt, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);
        main.add(cn, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        main.setBorder(new EmptyBorder(10, 10, 10, 10) );
        //Events
        doConnect();
        bt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ sendText();   }  });
        tf.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ sendText();   }  });

        frame.setContentPane(main);
        frame.setSize(600,600);
        frame.setVisible(true);
    }
    JTextArea tx;
    JTextField tf;
    String name, ip;
    JButton connect;
    JList lst;
    JFrame frame;
}