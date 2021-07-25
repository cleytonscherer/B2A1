package meupacote;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileSystem {

    public static final String ROOT = "D:" + File.separator + "B2A1" + File.separator + "hd";

    public FileSystem() {

        System.out.println("FileSystem()");

        executar();
    }

    private void executar() {
        System.out.println("Executar()");

        final Scanner scanner = new Scanner(System.in);

        System.out.println("Bem vindo ao sistema de arquivos!");

        var stop = false;
        var currentPath = Paths.get(ROOT);

        while (!stop) {
            try {
                System.out.print("$> ");
                final var command = Command.parseCommand(scanner.nextLine());
                currentPath = command.execute(currentPath);
                stop = command.shouldStop();
            } catch (UnsupportedOperationException | IOException ex) {
                System.out.printf("%s", ex.getMessage()).println();
            }
        }

        System.out.println("Sistema de arquivos encerrado.");
    }

    public static void main(String[] args) {

        // https://www.delftstack.com/pt/howto/java/java-switch-enum/
        // http://www.java2s.com/Tutorials/Java/Java_io/0020__Java_io_File_Operation.htm
        // https://examples.javacodegeeks.com/core-java/nio/file-nio/path/java-nio-file-path-example/

        new FileSystem();
    }
}
