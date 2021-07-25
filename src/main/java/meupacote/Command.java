package meupacote;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {
            File dir = new File(String.valueOf(path));
            File[] list = dir.listFiles();
            System.out.println("Contends of " + path + "\\");
            for (File f : list) {
                if (f.isFile()) {
                    System.out.println("[File] " + f.getName());
                } else if (f.isDirectory()) {
                    System.out.println("[Dir]  " + f.getName());
                }
            }
            return path;
        }
    },
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) {
            Path p = Paths.get(String.valueOf(path), parameters[1]);
            File file = new File(String.valueOf(p));
            if (file.isDirectory()) {
                System.out.println("This command should be user with files only");
            } else {
                System.out.println("Showing " + file);
                try {
                    final InputStream input = new FileInputStream(file);
                    int content;
                    while((content = input.read()) != -1) {
                        System.out.print((char) content);
                    }
                    System.out.println();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return path;
        }
    },
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {
            System.out.println("You're in " + path.getParent() + "\\");
            return path.getParent();
        }
    },
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {
            Path p = Paths.get(String.valueOf(path), parameters[1]);
            File file = new File(String.valueOf(p));
            if (file.isFile()) {
                System.out.println("Can't open files, just directories");
            } else {
                System.out.println("Now you are in " + p + "\\" );
                path = p;
            }
            return path;
        }
    },
    DETAIL() {
        //System.out.println("DETAIL()");

        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) throws IOException {
            Path p = Paths.get(String.valueOf(path), parameters[1]);
            File file = new File(String.valueOf(p));

            System.out.println("Detailing " + file);

            if (file.isDirectory()) {
                System.out.println(parameters[1] + " is a directory [true]");
            } else {
                System.out.println(parameters[1] + " is a directory [false]");
            }

            System.out.println("Size [" + file.length() + "]");

            FileTime creationTime  = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            FileTime lastAcessTime = (FileTime) Files.getAttribute(file.toPath(), "lastAccessTime");
            FileTime lastModifiedTime = (FileTime) Files.getAttribute(file.toPath(), "lastModifiedTime");
            //Long size = (Long) Files.getAttribute(file.toPath(), "size");

            System.out.println("Created on [" + creationTime + "]");
            System.out.println("Last access time [" + lastAcessTime + "]");
            //System.out.println("Last modified time [" + lastModifiedTime + "]");
            //System.out.println("Size [" + size + "]");

            return path;
        }
    },
    EXIT() {
        //System.out.println("EXIT()");

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            //System.out.println("EXIT()");

            System.out.print("Saindo...");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
        //System.out.println("SetParameters()");
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        //System.out.println("parseCommand()");

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }
}
