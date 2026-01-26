import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Change "123456" to whatever password you want to set
        String newHash = encoder.encode("Lakshmi");

        System.out.println("New Hash: " + newHash);
    }
}
