import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Supplier;
import javax.swing.*;

public class SystemFake {
    // System space
    static ArrayList<String> space = new ArrayList<>();

    // Folders and control of folders
    static int countF;                      // Count folders
    static boolean f1 = false;              // Existence control
    static ArrayList<String> folder1 = new ArrayList<>();
    static boolean f2 = false;
    static ArrayList<String> folder2 = new ArrayList<>();
    static boolean f3 = false;
    static ArrayList<String> folder3 = new ArrayList<>();
    static boolean fTF = false;              // Existence control

    // Users
    static Objetos user1 = new Objetos("Eduardo", "1234");
    static Objetos user2 = new Objetos("Felipe", "5678");
    static Objetos user3 = new Objetos("Kátia", "9012");
    static Objetos user4 = new Objetos("Lui", "3456");
    static Objetos user5 = new Objetos("Stefania", "7890");

    // Manual
    static Boolean mn = true;
    public static void manual(){
        if(mn.equals(true)){
            System.out.println(String.format("""
Commands:
- clear = Clear everything
- ls = Show everything
- memory = Show the memory
- cpu = Show the CPU
- ram = Show the RAM
- df -h = show the system
- mkdir = Make folders
- write = Writes in folders
- rm -w = Excludes entries in folders
- rm -r = Delete folders
- lf = Show the contents of the folders
- logOff = Log out
- poweroff = Turn off the system
"""));
        }else{
            System.out.println("Manual not found");
        }
    }

    // Memory and CPU
    static Objetos memory = new Objetos(0);
    static Objetos cpu = new Objetos("3%");

    public static void fM(){
        if(memory.getMemory() == 11){
            memory.setMemory(memory.getMemory() - 1);
            System.out.println("Full memory");
        }
    }

    // Clear screen (adaptado para GUI também)
    public static void clearScreen() {
        if(guiMode && clearScreenCallback != null){
            clearScreenCallback.run();
            return;
        }
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            if(os.contains("win")){
                pb = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                pb = new ProcessBuilder("clear");
            }
            pb.inheritIO().start().waitFor();
            return;
        } catch (Exception e1) {
            try {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                return;
            } catch (Exception e2) {
                for (int i = 0; i < 50; i++) {
                    System.out.println();
                }
            }
        }
    }

    // Starting
    public static void start() throws InterruptedException {
        Thread.sleep(200);
        System.out.print("System ");
        Thread.sleep(500);
        System.out.print("loading");
        Thread.sleep(500);
        System.out.print(".");
        Thread.sleep(500);
        System.out.print(".");
        Thread.sleep(500);
        System.out.print(".");
        Thread.sleep(200);
        space.add("Manual.pdf");
        memory.setMemory(memory.getMemory() + 4); // 3MB to System and 1MB to Manual.pdf
        clearScreen();
    }

    // End
    public static void end() throws InterruptedException {
        Thread.sleep(200);
        System.out.print("System ");
        Thread.sleep(500);
        System.out.print("shutting down");
        Thread.sleep(500);
        System.out.print(".");
        Thread.sleep(500);
        System.out.print(".");
        Thread.sleep(500);
        System.out.print(".");
        Thread.sleep(200);
        memory.setMemory(memory.getMemory() - memory.getMemory());
        cpu.setCpu("5%");
        clearScreen();
    }

    // ---------- MODO GRÁFICO (GUI) ----------
    public static boolean guiMode = false;
    public static Supplier<String> inputProvider = null;
    public static Runnable clearScreenCallback = null;
    public static String currentUser = "";   // armazena o nome do usuário logado

    private static String readLine(String prompt) {
        System.out.print(prompt);
        if(guiMode && inputProvider != null){
            return inputProvider.get();
        } else {
            return new Scanner(System.in).nextLine();
        }
    }

    // LEITURA DE SENHA: invisível na GUI, console normal
    private static String readPassword(String prompt) {
        System.out.print(prompt);
        if (guiMode) {
            JPasswordField passwordField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(null, passwordField, prompt, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                return new String(passwordField.getPassword());
            } else {
                return "";
            }
        } else {
            return new String(System.console().readPassword());
        }
    }

    private static int readInt(String prompt) {
        return Integer.parseInt(readLine(prompt));
    }
    // ---------------------------------------

    // Log in (adaptado)
    public static String logIn() throws InterruptedException {
        System.out.println("Log in\n");
        String name = "";
        boolean in = false;
        while(!in){
            Thread.sleep(500);
            String userName = readLine("\nPlease enter your name\n");
            Thread.sleep(500);
            String userPassword = readPassword("\nPlease enter your password\n");
            Thread.sleep(500);

            if(userName.equals(user1.getUser()) && userPassword.equals(user1.getPassword())){
                name = user1.getUser();
                in = true;
            } else if(userName.equals(user2.getUser()) && userPassword.equals(user2.getPassword())){
                name = user2.getUser();
                in = true;
            } else if(userName.equals(user3.getUser()) && userPassword.equals(user3.getPassword())){
                name = user3.getUser();
                in = true;
            } else if(userName.equals(user4.getUser()) && userPassword.equals(user4.getPassword())){
                name = user4.getUser();
                in = true;
            } else if(userName.equals(user5.getUser()) && userPassword.equals(user5.getPassword())){
                name = user5.getUser();
                in = true;
            } else {
                Thread.sleep(500);
                System.out.println("\nIncorrect password or username\n");
            }
        }
        currentUser = name;   // guarda o nome do usuário
        clearScreen();
        Thread.sleep(500);
        return name;
    }

    // Reset completo do sistema (como se fosse a primeira execução)
    private static void resetSystem() {
        space.clear();
        folder1.clear();
        folder2.clear();
        folder3.clear();
        countF = 0;
        f1 = false;
        f2 = false;
        f3 = false;
        fTF = false;
        memory.setMemory(0);
        cpu.setCpu("3%");
        currentUser = "";
        // O espaço de sistema (space) será repopulado pelo start()
    }

    // cpu
    public static void cpU(){
        if(countF == 2){
            cpu.setCpu("10%");
        } else if(countF == 3){
            cpu.setCpu("15%");
        } else {
            cpu.setCpu("5%");
        }
    }

    // Terminal (método principal de comandos)
    public static String terminal(String action) throws InterruptedException {
        String backA = "\nCommand Done\n";
        switch(action){
            case "open manual.pdf":
                if (mn == true) {
                    manual();
                }else{
                    System.out.println("Not found");
                }
                break;
            case "clear":
                clearScreen();
                break;
            case "ls":
                System.out.println(space);
                break;
            case "memory":
                System.out.println("You have 10MB of memory, and you are using " + memory.getMemory() + "MB of it");
                break;
            case "cpu":
                System.out.println("You are using " + cpu.getCpu() + " of your CPU");
                break;
            case "ram":
                System.out.println("RAM in use: " + "0.9GB");
                break;
            case "df -h":
                System.out.println("CPU = l2 core" + "\nMemory = 10MG" + "\nRAM = 1GB");
                break;
            case "mkdir":
                if(countF < 3){
                    if(f1 == false){
                        if(memory.getMemory() < 11){
                            f1 = true;
                            System.out.println("Folder created");
                            fTF = true;
                            String folderName = "Folder_1";
                            countF += 1;
                            space.add(folderName);
                            memory.setMemory(memory.getMemory() + 1);
                        } else {
                            System.out.println("Full memory");
                        }
                    } else if(f2 == false){
                        if(memory.getMemory() < 11){
                            f2 = true;
                            System.out.println("Folder created");
                            fTF = true;
                            String folderName = "Folder_2";
                            countF += 1;
                            space.add(folderName);
                            memory.setMemory(memory.getMemory() + 1);
                        } else {
                            System.out.println("Full memory");
                        }
                    } else if(f3 == false){
                        if(memory.getMemory() < 11){
                            f3 = true;
                            System.out.println("Folder created");
                            fTF = true;
                            String folderName = "Folder_3";
                            countF += 1;
                            space.add(folderName);
                            memory.setMemory(memory.getMemory() + 1);
                        } else {
                            System.out.println("Full memory");
                        }
                    }
                } else {
                    System.out.println("The system no longer allows folders");
                }
                break;
            case "write":
                if(memory.getMemory() < 10){
                    if(fTF == true){
                        String fn = readLine("Do you want write in what folder?\n");
                        if(fn.equals("Folder_1") && f1 == true){
                            String writer = readLine("Write please: \n");
                            folder1.add(writer);
                            memory.setMemory(memory.getMemory() + 1);
                        } else if(fn.equals("Folder_2") && f2 == true){
                            String writer = readLine("Write please: \n");
                            folder2.add(writer);
                            memory.setMemory(memory.getMemory() + 1);
                        } else if(fn.equals("Folder_3") && f3 == true){
                            String writer = readLine("Write please: \n");
                            folder3.add(writer);
                            memory.setMemory(memory.getMemory() + 1);
                        } else {
                            System.out.println("Folder not found");
                        }
                    } else {
                        System.out.println("You don't have any folders");
                    }
                } else {
                    System.out.println("Full memory");
                }
                break;
            case "rm -w":
                String nFolder = readLine("What is the folder name:\n");
                if(nFolder.equals("Folder_1")){
                    System.out.println("What is the position of your writing?");
                    System.out.println(folder1);
                    int folderN = readInt("");
                    folder1.remove(folderN - 1);
                    memory.setMemory(memory.getMemory() - 1);
                } else if(nFolder.equals("Folder_2")){
                    System.out.println("What is the position of your writing?");
                    System.out.println(folder2);
                    int folderN = readInt("");
                    folder2.remove(folderN - 1);
                    memory.setMemory(memory.getMemory() - 1);
                } else if(nFolder.equals("Folder_3")){
                    System.out.println("What is the position of your writing?");
                    System.out.println(folder3);
                    int folderN = readInt("");
                    folder3.remove(folderN - 1);
                    memory.setMemory(memory.getMemory() - 1);
                } else {
                    System.out.println("Incorrect option");
                }
                break;
            case "rm -r":
                String nameF = readLine("Which folder do you want to delete?\n");
                if(nameF.equals("Folder_1") && f1 == true){
                    f1 = false;
                    int pq = folder1.size();
                    countF -= 1;
                    space.remove(nameF);
                    folder1.clear();
                    memory.setMemory((memory.getMemory() - 1) - pq);
                } else if(nameF.equals("Folder_2") && f2 == true){
                    f2 = false;
                    int pq = folder2.size();
                    countF -= 1;
                    space.remove(nameF);
                    folder2.clear();
                    memory.setMemory((memory.getMemory() - 1) - pq);
                } else if(nameF.equals("Folder_3") && f3 == true){
                    f3 = false;
                    int pq = folder3.size();
                    countF -= 1;
                    space.remove(nameF);
                    folder3.clear();
                    memory.setMemory((memory.getMemory() - 1) - pq);
                } else if (nameF.equals("Manual.pdf")) {
                    mn = false;
                    space.remove(nameF);
                    memory.setMemory(memory.getMemory() - 1);
                }
                break;
            case "lf":
                if(countF == 0){
                    System.out.println(" No folders");
                } else {
                    String name = readLine("What is the name of the folder to view?\n");
                    if(name.equals("Folder_1") && f1 == true){
                        System.out.println(folder1);
                    } else if(name.equals("Folder_2") && f2 == true){
                        System.out.println(folder2);
                    } else if(name.equals("Folder_3") && f3 == true){
                        System.out.println(folder3);
                    } else {
                        System.out.println("No find folder");
                    }
                }
                break;
            case "logoff":
                Thread.sleep(500);
                System.out.println("Off system");
                // Reseta TUDO para o estado inicial
                resetSystem();
                Thread.sleep(500);
                clearScreen();
                logIn();               // faz o login novamente
                start();               // inicia o sistema (adiciona Manual.pdf, etc.)
                // Exibe as mensagens de boas‑vindas novamente
                System.out.println("\nWelcome " + currentUser + "!");
                System.out.println("\nLOS - Lui Operating System\n");
                System.out.println("\nOn system\n");
                System.out.println("Write: open manual.pdf to see the manual\n");
                break;
            case "poweroff":
                end();
                backA = "break";
                break;
            default:
                backA = "Command '" + action + "' not found\n";
                break;
        }
        return backA;
    }
}
