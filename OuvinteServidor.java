import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class OuvinteServidor implements Runnable {

    private Socket socket;

    public OuvinteServidor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader doServidor = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
        ) {
            String mensagemDoServidor;

            while ((mensagemDoServidor = doServidor.readLine()) != null) {
                
                System.out.println(mensagemDoServidor);
                
            }

        } catch (IOException e) {

            System.out.println("Voce foi desconectado do servidor.");
        }
    }
}