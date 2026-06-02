public class Objetos {
    private String user;
    private String password;
    private int memory;
    private String cpu;

    // Getters and Setters
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public int getMemory() {
        return memory;
    }
    public void setMemory(int memory) {
        this.memory = memory;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCpu() {
        return cpu;
    }
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    // Método construtor
    public Objetos(String user, String password) {
        this.user = user;
        this.password = password;
    }
    public Objetos(int memory) {
        this.memory = memory;
    }
    public Objetos(String cpu){
        this.cpu = cpu;
    }
}