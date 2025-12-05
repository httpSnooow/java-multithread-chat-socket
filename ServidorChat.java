import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServidorChat {

    public static void main(String[] args) {
        
        int porta = 12345;

        ExecutorService poolDeThreads = Executors.newFixedThreadPool(50);

        System.out.println("--- Servidor de Chat Iniciado ---");
        System.out.println("Aguardando conexoes na porta " + porta + "...");

        GerenciadorDeClientes gerenciador = new GerenciadorDeClientes();

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            
            while (true) {
                
                Socket socketDoCliente = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + socketDoCliente.getRemoteSocketAddress());

                AtendenteCliente atendente = new AtendenteCliente(socketDoCliente, gerenciador);
                

                poolDeThreads.execute(atendente);
            }

        } catch (IOException e) {
            System.out.println("Erro no Servidor: " + e.getMessage());
        } finally {

            if (poolDeThreads != null) {
                System.out.println("Desligando o pool de threads...");
                poolDeThreads.shutdown();
            }
        }
    }
}