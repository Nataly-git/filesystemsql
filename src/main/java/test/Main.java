package test;

public class Main {
    public static void main(String[] args) {
        Update update = new Update();
        update.moveFiles("My documents", "My computer");
        System.out.println(update.getFullWay("file1"));
        update.deleteDir("Disc C");
    }
}
