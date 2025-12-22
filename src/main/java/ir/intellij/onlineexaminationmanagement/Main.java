package ir.intellij.onlineexaminationmanagement;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Main {
    public static void main(String[] args) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(a -> System.out.println(a.getAuthority()));


    }
}
