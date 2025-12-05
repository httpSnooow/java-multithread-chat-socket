import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

public class AtendenteCliente implements Runnable {

    private Socket socketDoCliente;
    private GerenciadorDeClientes gerenciador;
    private PrintWriter paraCliente;
    private String nomeCliente; 

    public AtendenteCliente(Socket socketDoCliente, GerenciadorDeClientes gerenciador) {
        this.socketDoCliente = socketDoCliente;
        this.gerenciador = gerenciador;
    }

    @Override
    public void run() {
        try {
            this.paraCliente = new PrintWriter(socketDoCliente.getOutputStream(), true);
            BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketDoCliente.getInputStream()));

            paraCliente.println("Bem-vindo! Use /msg 'Apelido do Usuario' 'Mensagem' para mandar mensagens privadas.");
            paraCliente.println("Digite seu apelido para entrar:");
            String apelido = doCliente.readLine();
            
            if (apelido == null || apelido.trim().isEmpty()) {
                apelido = "Anonimo_" + socketDoCliente.getPort();
            }
            this.nomeCliente = apelido;

            gerenciador.adicionarCliente(this.nomeCliente, this.paraCliente);

            String infoOnline = gerenciador.getListaConectados();
            paraCliente.println("------------------------------------------");
            paraCliente.println(infoOnline);
            paraCliente.println("------------------------------------------");

            String msgEntrada = "--- " + this.nomeCliente + " entrou no chat ---";
            System.out.println(msgEntrada);
            gerenciador.enviarBroadcast(msgEntrada, this.nomeCliente);

            String linhaRecebida;
            while ((linhaRecebida = doCliente.readLine()) != null) {
                
                if ("sair".equalsIgnoreCase(linhaRecebida.trim())) {
                    break;
                }

                if (linhaRecebida.startsWith("/msg ")) {
                    String[] partes = linhaRecebida.split(" ", 3);
                    
                    if (partes.length == 3) {
                        String destinatario = partes[1];
                        String mensagem = partes[2];
                        gerenciador.enviarMensagemPrivada(destinatario, mensagem, this.nomeCliente);
                    } else {
                        paraCliente.println("Erro: Use /msg [Nome] [Mensagem]");
                    }
                } 
                else {
                    String msgFormatada = "[" + this.nomeCliente + "]: " + linhaRecebida;
                    gerenciador.enviarBroadcast(msgFormatada, this.nomeCliente);
                }
            }

        } catch (IOException e) {
            System.out.println("Erro na conexão com " + nomeCliente);
        } finally {
            if (nomeCliente != null) {
                gerenciador.removerCliente(nomeCliente);
                gerenciador.enviarBroadcast("--- " + nomeCliente + " saiu do chat ---", "Servidor");
            }
            try { socketDoCliente.close(); } catch (IOException e) {}
        }
    }
}