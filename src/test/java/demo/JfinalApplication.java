package demo;

import com.jfinal.core.JFinal;

public class JfinalApplication {
    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 80, "/");
    }
}
