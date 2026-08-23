import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        BCryptPasswordEncoder decoder = new BCryptPasswordEncoder();
        // Change "123456" to whatever password you want to set
        String newHash = encoder.encode("Lakshmi");
        String decode = decoder.encode("$2a$10$IL/YD4M65WgQm.awBtDgCug9a5k9/NvaxlwweIDlzI2XpbiLwCvP2");
        System.out.println("New Hash: " + decode);
    }
}
