# 💬 Java Multi-Threaded Chat Server

Este projeto consiste em uma aplicação de Chat Cliente-Servidor robusta, desenvolvida em **Java**, utilizando **Sockets TCP** e **Multithreading**. O sistema suporta múltiplos clientes simultâneos através de um `Thread Pool`, possui interface gráfica (GUI) construída com **Swing** e implementa funcionalidades de mensagens globais (broadcast) e privadas.

![Status](https://img.shields.io/badge/Status-Concluído-green)
![Java](https://img.shields.io/badge/Java-17%2B-orange)


## 📸 Screenshots

| Tela de Chat (Cliente) |
|:-------------------------:|
| ![Interface do Chat](Chat.PNG) |




## 🚀 Funcionalidades

-   **Arquitetura Cliente-Servidor:** Comunicação robusta via Sockets TCP/IP.
-   **Suporte a Múltiplos Clientes:** Uso de `ExecutorService` (Thread Pool) para gerenciar até 50 conexões simultâneas de forma eficiente.
-   **Interface Gráfica (GUI):** Cliente desenvolvido com Java Swing para uma experiência amigável (sem terminal).
-   **Broadcast:** Mensagens enviadas por um usuário são replicadas para todos os outros.
-   **Mensagens Privadas:** Comando `/msg Nome Mensagem` para enviar mensagens diretas e sigilosas.
-   **Feedback de Conexão:** O sistema avisa quando usuários entram, saem e mostra a lista de conectados ao entrar.
-   **Thread-Safety:** Uso de blocos `synchronized` para garantir a integridade da lista de usuários (evitando *Race Conditions*).

## 🛠️ Tecnologias Utilizadas

* **Java (JDK):** Linguagem principal.
* **Java.net (Sockets):** Para comunicação em rede.
* **Java.util.concurrent (ExecutorService):** Para gerenciamento eficiente de threads.
* **Java Swing:** Para construção da interface gráfica.
* **Java I/O (BufferedReader/PrintWriter):** Para fluxo de entrada e saída de dados.

## 📂 Estrutura do Projeto

* `ServidorChat.java`: Classe principal do servidor. Inicializa o Socket e o Pool de Threads.
* `ClienteGUI.java`: Interface gráfica do cliente. Gerencia conexão e exibição de mensagens.
* `AtendenteCliente.java`: A lógica (Runnable) que roda no servidor para cada cliente conectado.
* `GerenciadorDeClientes.java`: Controla a lista (Map) de usuários conectados e faz o roteamento das mensagens.
* `OuvinteServidor.java`: Thread secundária do cliente para escutar mensagens assincronamente.

## ⚙️ Como Executar

### Pré-requisitos
Certifique-se de ter o [Java JDK] instalado.

### 1. Compilar o Código
Abra o terminal na pasta do projeto e execute:
javac *.java

### 2. Iniciar o Servidor
Primeiro, inicie o servidor (ele ficará escutando na porta 12345):

java ServidorChat
Você verá a mensagem: "Aguardando conexoes na porta 12345..."

### 3. Iniciar os Clientes
Abra novos terminais (quantos quiser) e execute o cliente:

java ClienteGUI
Uma janela abrirá pedindo o IP. Se estiver rodando localmente, deixe localhost.

No chat, digite seu Apelido quando solicitado.

## 🎮 Como Usar
Chat Geral
Basta digitar a mensagem e clicar em "Enviar" ou pressionar Enter. Todos na sala verão.

Mensagem Privada
Para enviar uma mensagem apenas para um usuário específico, use a sintaxe:

/msg NomeDoDestinatario Sua Mensagem Aqui
Exemplo: /msg Guilherme Olá, preciso de ajuda no código!

## 🧠 Destaques Técnicos (Conceitos Aprendidos)
Este projeto foi desenvolvido com foco em conceitos avançados de computação distribuída:

*`Sockets & TCP Handshake`: Estabelecimento de conexão confiável entre processos distintos.

*`Multithreading vs Thread Pool`: Em vez de criar uma new Thread() infinita para cada cliente (o que esgotaria a memória), utilizei um FixedThreadPool para reutilizar threads e limitar o consumo de recursos do servidor.

*`Sincronização (Monitores)`: A classe GerenciadorDeClientes utiliza métodos synchronized para evitar que duas threads tentem modificar o Mapa de clientes ao mesmo tempo, prevenindo erros de concorrência.

*`Separação de Responsabilidades`: O Cliente possui uma Thread dedicada apenas para ouvir (OuvinteServidor ou classe interna) e a Thread principal da GUI (Event Dispatch Thread) cuida da interação, garantindo que a tela não trave enquanto espera mensagens.

