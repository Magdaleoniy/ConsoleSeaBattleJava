import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String args[]) throws Exception {
        System.out.println("Добро пожаловать в Морской бой");
        Map my = new Map();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        my.InitializeMap();
        my.printMap();

        while (true)
        {
            System.out.println("Напишите start, чтобы начать игру"
                    + "\n" + "Напишите clean, чтобы отчистить поле"
                    + "\n" + "Напишите print, чтобы показать ваше поле"
                    + "\n" + "Напишите end, чтобы завершить программу" + "\n");
            String s = reader.readLine();
            if (s.equals("start"))
            {
                Game session = new Game(my);
                session.createMap();
            }
            else if (s.equals("clean"))
            {
                my.InitializeMap();
                my.printMap();
                System.out.println();
            }
            else if (s.equals("print"))
                my.printMap();
            else if (s.equals("end"))
                break;
            else
                System.out.println("Вы ввели неверную команду :(" + "\n"
                        + "Попробуйте ещё раз");

        }
    }
}
