package Data;

public class Account {
    private String accountName;
    private String password;
    private int access; // Level 0 = customer, Level 1 = mod, Level 2 = admin
    
    public Account(String info) {
        String[] chunks = info.split(":");
        accountName = chunks[0];
        password = chunks[1];
        access = Integer.parseInt(chunks[2]);
    }
    
    public String getName() {
        return accountName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean checkAccess(int required) {
        return access >= required;
    }
    
    public String toString() {
    	return (accountName + ":" + password + ":" + access);
    }
}
