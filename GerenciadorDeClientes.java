import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeClientes {

    private Map<String, PrintWriter> clientes = new HashMap<>();

    public synchronized void adicionarCliente(String nome, PrintWriter cliente) {
        this.clientes.put(nome, cliente);
        System.out.println("Gerenciador: " + nome + " adicionado. Total: " + clientes.size());
    }

    public synchronized void removerCliente(String nome) {
        if (nome != null) {
            this.clientes.remove(nome);
            System.out.println("Gerenciador: " + nome + " removido. Total: " + clientes.size());
        }
    }

    public synchronized void enviarBroadcast(String mensagem, String remetenteNome) {
        for (Map.Entry<String, PrintWriter> entrada : clientes.entrySet()) {
            String nomeDestino = entrada.getKey();
            PrintWriter escritorDestino = entrada.getValue();

            if (!nomeDestino.equals(remetenteNome)) {
                escritorDestino.println(mensagem);
            }
        }
    }

    public synchronized void enviarMensagemPrivada(String destinatario, String mensagem, String remetente) {
        PrintWriter escritorDestino = clientes.get(destinatario);

        if (escritorDestino != null) {
            escritorDestino.println("[Privado de " + remetente + "]: " + mensagem);
        } else {
            PrintWriter escritorRemetente = clientes.get(remetente);
            if (escritorRemetente != null) {
                escritorRemetente.println("Erro: Usuario " + destinatario + " nao encontrado.");
            }
        }
    }

    public synchronized String getListaConectados() {
        int total = clientes.size();
        
        String nomes = String.join(", ", clientes.keySet());
        
        return "Total de " + total + " conectados: [ " + nomes + " ]";
    }
}