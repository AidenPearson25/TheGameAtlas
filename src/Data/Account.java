package Data;

public class Account {
    private String id;
    private String accountName;
    private String password;
    private int access; // Level 0 = customer, Level 1 = mod, Level 2 = admin
    
    public Account(String info) {
        String[] chunks = info.split(",");
        id = chunks[0];
        accountName = chunks[1];
        password = chunks[2];
        access = Integer.parseInt(chunks[3]);
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean checkAccess(int required) {
        return access >= required;
    }
}
