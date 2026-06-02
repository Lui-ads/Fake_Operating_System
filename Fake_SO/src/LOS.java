import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LOS {
    SystemFake systen = new SystemFake(); // Cria o Sistema
    // Scanner sy = new Scanner(System.in);  // (mantido como referência, mas não usado na GUI)

    private JTextArea terminalOutput;      // Área de saída do terminal
    private JTextField textField;          // Campo de entrada de comandos
    private JFrame screen;                 // Referência à janela principal (para poder fechá-la no poweroff)
    private BlockingQueue<String> inputQueue = new ArrayBlockingQueue<>(1); // Fila para comunicação entre a GUI e o sistema
    private boolean systemOn = true;       // Controla se o sistema está ligado
    private static final int MAX_LINES = 500; // Limite de linhas para evitar travamento

    public LOS() {
        // Ativa o modo gráfico no SystemFake
        SystemFake.guiMode = true;

        // Provedor de entrada: pega o texto do campo de texto da GUI
        SystemFake.inputProvider = () -> {
            try {
                return inputQueue.take(); // Aguarda até que um comando seja digitado
            } catch (InterruptedException e) {
                return "";
            }
        };

        // Screen
        screen = new JFrame(); // Cria a janela
        screen.setSize(1280, 720); // Tamanho da tela
        screen.setTitle("Lui Operating System"); // Nome da tela
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Garante que ela pare o programa ao fechar a janela
        screen.setResizable(false); // Garante que a tela não possa ser mudada de tamanho
        screen.setLocationRelativeTo(null); // Faz a janela aparecer no meio da tela

        // Definição das cores e borda baseadas na imagem
        Color fundoTerminal = new Color(245, 242, 230); // Bege claro do fundo
        Color corTexto = new Color(30, 30, 30);         // Texto escuro
        Color corBotao = new Color(150, 190, 230);      // Azul acinzentado do botão
        Border borda = BorderFactory.createLineBorder(new Color(180, 180, 180), 2); // Borda da janela

        // Criando a área de saída grande do terminal que estava faltando no seu escopo
        terminalOutput = new JTextArea(); // Cria o componente da área de saída de texto
        terminalOutput.setFont(new Font("Monospaced", Font.PLAIN, 18)); // Define a fonte monoespaçada para a saída
        terminalOutput.setBackground(fundoTerminal); // Define a cor de fundo bege para a área de saída
        terminalOutput.setForeground(corTexto); // Define a cor do texto da área de saída
        terminalOutput.setBorder(borda); // Aplica a borda cinza na área de saída
        terminalOutput.setEditable(false); // Impede o usuário de digitar diretamente na área de saída
        terminalOutput.setLineWrap(true); // Ativa a quebra de linha automática (evita barras laterais)
        terminalOutput.setWrapStyleWord(true); // Quebra a linha por palavra inteira

        // Adiciona uma barra de rolagem à área de saída (evita travamento quando o conteúdo cresce)
        JScrollPane scrollPane = new JScrollPane(terminalOutput);
        scrollPane.setBounds(50, 40, 1180, 500); // Posição e tamanho da área com rolagem
        scrollPane.setBorder(borda); // Aplica a mesma borda
        screen.add(scrollPane); // Adiciona o painel com rolagem no lugar da área pura

        // ====================================================================
        // INTERCEPTADOR: Redireciona os prints para dentro do JTextArea da janela
        // ====================================================================
        PrintStream interceptor = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> {
                    terminalOutput.append(String.valueOf((char) b)); // Adiciona o caractere na área de saída
                    limitLines(); // Remove linhas antigas se ultrapassar o limite (evita travamento)
                });
            }
        });
        System.setOut(interceptor); // Aplica o redirecionamento global no Java
        // ====================================================================

        // Button
        screen.setLayout(null); // Permite setar o botão
        JButton button = new JButton("Send"); // Cria o botão
        button.setBounds(1030, 560, 200, 50); // Posição do botão
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Define a fonte
        button.setForeground(corTexto); // Cor da escrita
        button.setBackground(corBotao); // Cor do botão
        button.setBorder(borda); // Aplica a borda cinza no botão
        screen.add(button); // Adiciona na janela

        // Text
        textField = new JTextField(); // Cria o campo de texto
        textField.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Mude a fonte para parecer um terminal
        textField.setBounds(50, 560, 960, 50); // Posição
        textField.setBackground(Color.WHITE); // Define a cor de fundo branca para o campo de entrada
        textField.setForeground(corTexto); // Define a cor do texto para o campo de entrada
        textField.setBorder(borda); // Aplica a borda cinza no campo de entrada
        screen.add(textField); // Adiciona na janela

        // Callback para o comando "clear" funcionar na GUI (limpa o JTextArea)
        SystemFake.clearScreenCallback = () -> {
            SwingUtilities.invokeLater(() -> terminalOutput.setText(""));
        };

        screen.setVisible(true); // Deixa tudo visível

        // Thread principal do sistema (roda em segundo plano para não travar a interface)
        new Thread(() -> {
            try {
                SystemFake.start(); // Inicializa o sistema (loading...)
                String user = SystemFake.logIn(); // Faz o login do usuário
                System.out.println("\nWelcome " + user + "!");
                System.out.println("\nLOS - Lui Operating System\n");
                System.out.println("\nOn system\n");
                System.out.println("Write: open manual.pdf to see the manual\n");

                // Loop principal que processa os comandos
                while (systemOn) {
                    SystemFake.fM(); // Verifica se a memória está cheia
                    SystemFake.cpU(); // Atualiza o uso da CPU
                    String action = inputQueue.take(); // Aguarda o usuário digitar um comando
                    // NÃO ECOA O COMANDO NA TELA (removido System.out.println(action))
                    String result = SystemFake.terminal(action.toLowerCase()); // Executa o comando
                    if (result.equals("break")) { // Comando "poweroff" retorna break
                        systemOn = false;
                        System.out.println("System Off");
                        // Fecha a janela após o desligamento completo
                        SwingUtilities.invokeLater(() -> screen.dispose());
                        break; // Sai do loop while
                    } else {
                        System.out.println(result); // Mostra o resultado do comando
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Esse método é acionado quando você clica no botão Enviar ou aperta Enter
        button.addActionListener(e -> enviarComando());
        textField.addActionListener(e -> enviarComando());
    }

    // Método auxiliar para enviar o comando digitado para a fila (usado pelo botão e pelo Enter)
    private void enviarComando() {
        String cmd = textField.getText().trim(); // Pega a escrita
        if (!cmd.isEmpty()) { // Garante que terá um texto a ser enviado
            textField.setText(""); // Limpa o campo de escrita
            inputQueue.offer(cmd); // Coloca o comando na fila para o sistema processar
            textField.requestFocus(); // Devolve o foco e o cursor piscando para o campo de digitação
        }
    }

    // Limita o número de linhas do JTextArea (evita travamento quando a tela fica cheia)
    private void limitLines() {
        Document doc = terminalOutput.getDocument();
        try {
            String text = doc.getText(0, doc.getLength()); // Pega todo o texto atual
            String[] lines = text.split("\n"); // Divide em linhas
            if (lines.length > MAX_LINES) { // Se ultrapassou o limite
                int excess = lines.length - MAX_LINES; // Quantas linhas a mais
                int removeUpTo = 0;
                for (int i = 0; i < excess; i++) {
                    removeUpTo += lines[i].length() + 1; // Soma o tamanho da linha + o \n
                }
                doc.remove(0, removeUpTo); // Remove as linhas mais antigas
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LOS::new); // Garante que a GUI seja criada na thread correta
    }
}
