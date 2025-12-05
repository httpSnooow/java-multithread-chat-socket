import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteGUI {

    private JFrame frame = new JFrame("Chat Socket Multi-Thread");
    private JTextField txtMensagem = new JTextField(40);
    private JTextArea areaChat = new JTextArea(20, 50);
    private JButton btnEnviar = new JButton("Enviar");
    
    private PrintWriter escritor;
    private BufferedReader leitor;
    private Socket socket;

    public static void main(String[] args) {
        new ClienteGUI().iniciar();
    }

    public void iniciar() {
        txtMensagem.setEditable(false);
        areaChat.setEditable(false);
        frame.getContentPane().add(new JScrollPane(areaChat), BorderLayout.CENTER);
        
        JPanel painelInferior = new JPanel();
        painelInferior.add(txtMensagem);
        painelInferior.add(btnEnviar);
        frame.getContentPane().add(painelInferior, BorderLayout.SOUTH);
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        ActionListener acaoEnviar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensagem();
            }
        };
        btnEnviar.addActionListener(acaoEnviar);
        txtMensagem.addActionListener(acaoEnviar);

        try {
            String ip = JOptionPane.showInputDialog(frame, "Digite o IP do Servidor:", "localhost");
            if (ip == null) System.exit(0);
            
            socket = new Socket(ip, 12345);
            leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            escritor = new PrintWriter(socket.getOutputStream(), true);

            txtMensagem.setEditable(true);
            
            new Thread(new LeitorDoServidor()).start();

        } catch (IOException ex) {
            areaChat.append("Não foi possível conectar ao servidor.\n");
            ex.printStackTrace();
        }
    }

    private void enviarMensagem() {
        String texto = txtMensagem.getText();
        if (!texto.isEmpty()) {
            escritor.println(texto);
            
            if(!texto.startsWith("/msg")) {
               areaChat.append("Eu: " + texto + "\n");
            } else {
               areaChat.append("[Comando enviado]: " + texto + "\n");
            }
            
            txtMensagem.setText("");
            txtMensagem.requestFocus();
        }
    }

    private class LeitorDoServidor implements Runnable {
        @Override
        public void run() {
            String mensagem;
            try {
                while ((mensagem = leitor.readLine()) != null) {
                    String msgFinal = mensagem;
                    SwingUtilities.invokeLater(() -> areaChat.append(msgFinal + "\n"));
                }
            } catch (IOException ex) {
                areaChat.append("Conexão perdida com o servidor.\n");
            }
        }
    }
}