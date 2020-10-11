import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game
{
    public Map my;
    public Game(Map my) throws Exception {
        this.my = my;
    }

    //Интерфейс, реализующий метод случайного перемешивания списка
    public interface IShuffle
    {
        void shuffle(List<Integer> x);
    }

    //Базовый класс для компьютеров-противников
    public abstract class Bot
    {
        protected int step = 1;
        public Map bot = new Map(), enemy;
        //Все боты обязательно должны вести огонь, но по-разному
        //Для этого используем абстрактный метод в базовом классе,
        //который переопределим у каждого бота 
        public abstract void shoot() throws Exception;
        public void initializeMap()
        {
            bot.InitializeMap();
            bot.InitializeClone();
            enemy.InitializeClone();
            bot.createRandomMap();
        }
    }

    //Бот легкой сложности, который поражает поля случайным образом
    class EasyBot extends Bot {
        public String type;
                    public EasyBot(Map enemy)
        {
            this.enemy = enemy;
            initializeMap();
        }
                    public EasyBot(Map enemy, String type)
        {
            this.enemy = enemy;
            this.type = type;
            initializeMap();
            present();
        }

        //Обязательное переопределение абстрактного метода
        @Override
        public void shoot() throws Exception {
            //Этот цикл для выстрелов компьютера
            while (true)
            {
                System.out.println(step + "-ый ход компьютера");
                Random r = new Random();
                int x = r.nextInt(9) + 1;
                int y = r.nextInt(9) + 1;
                if (!bot.annihilation(enemy, x, y))
                    break;
                step++;
            }
        }

        //Виртуальный метод, который предсавляет бота пользователю
        public void present()
        {
            System.out.println("\n" + "Приветствую, я " + type + " противник. Приятной игры" + "\n");
        }

    }

    //Клас MdediumBot расширяет класс EasyBot и реализует интерфейс IShuffle
    class MediumBot extends EasyBot implements IShuffle
    {
        boolean action = false;
        int x = -1, y = -1;
        List<Integer> forShoot = new ArrayList<>();
        Random r = new Random();

        //Конструктор, который обращается к конструктору родителя
        public MediumBot(Map enemy)
        {
            super(enemy);
            type = "средний";
            present();
        }

        @Override
        public void shoot() throws Exception {
            //Этот цикл для выстрелов компьютера
            while (true)
            {
                //Если бот попал по кораблю, он будет стрелять по соседним клеткам,
                //чтобы его добить
                System.out.println(step + "-ый ход компьютера");
                if ((action || forShoot.size() < 4) && x != -1)
                {
                    if (forShoot.size() < 4)
                    {
                        forShoot.add(1);
                        forShoot.add(2);
                        forShoot.add(3);
                        forShoot.add(4);
                        shuffle(forShoot);
                    }
                    if (forShoot.get(0) == 1)
                    {
                        if (!enemy.checkShip(x + 1, y))
                        {
                            action = bot.annihilation(enemy, x + 1, y);
                            if (!action)
                                forShoot.remove(forShoot.get(0));
                            else
                                x += 1;
                        }
                        else
                            forShoot.remove(forShoot.get(0));

                    }
                    if (forShoot.get(0) == 2)
                    {
                        if (!enemy.checkShip(x - 1, y))
                        {
                            action = bot.annihilation(enemy, x - 1, y);
                            if (!action)
                                forShoot.remove(forShoot.get(0));
                            else
                                x -= 1;
                        }
                        else
                            forShoot.remove(forShoot.get(0));

                    }
                    if (forShoot.get(0) == 3)
                    {
                        if (!enemy.checkShip(x, y + 1))
                        {
                            action = bot.annihilation(enemy, x, y + 1);
                            if (!action)
                                forShoot.remove(forShoot.get(0));
                            else
                                y += 1;
                        }
                        else
                            forShoot.remove(forShoot.get(0));

                    }
                    if (forShoot.get(0) == 4)
                    {
                        if (!enemy.checkShip(x, y - 1))
                        {
                            action = bot.annihilation(enemy, x, y - 1);
                            if (!action)
                                forShoot.remove(forShoot.get(0));
                            else
                                y -= 1;
                        }
                        else
                            forShoot.remove(forShoot.get(0));

                    }
                }
                else
                {
                    forShoot.clear();
                    x = r.nextInt(9) + 1;
                    y = r.nextInt(9) + 1;
                    action = bot.annihilation(enemy, x, y);
                }

                if (action == false)
                    break;
                step++;
            }
        }

        //Переопределение виртуального метода с добавлением ещё одной строчки
        @Override
        public void present()
        {
            super.present();
            System.out.println("Но придется постараться ;)" + "\n");
        }

        //Обязательная реализация члена интерфейса IShuffle
        public void shuffle(List<Integer> x)
        {
            Random r = new Random();
            for (int i = x.size() - 1; i > 0; i--)
            {
                int j = r.nextInt(i + 1);
                int t = x.get(i);
                int t1 = x.get(j);
                x.remove(t);
                x.remove(x.get(j));
                x.add(i, t1);
                x.add(j, t);
            }
        }
    }

    public void createMap() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        my.InitializeMap();
        System.out.println("Если вы хотите сами установить корабли, введите: YES");
        System.out.println("Если вы хотите расставить корабли случайно, введите: NO");
        String s = reader.readLine();
        if (s.equals("YES"))
        {
            System.out.println("Введите координаты первого однопалубника:");
            my.setShip(1, reader.readLine());
            System.out.println("Введите координаты второго однопалубника:");
            my.setShip(1, reader.readLine());
            System.out.println("Введите координаты третьего однопалубника:");
            my.setShip(1, reader.readLine());
            System.out.println("Введите координаты четвертого однопалубника:");
            my.setShip(1, reader.readLine());

            System.out.println("Координаты многопалубных кораблей вводите через пробел в формате:"
                    + "\n" + "X1 X2"
                    + "\n" + "То есть: координата начала и координата конца");

            System.out.println("Введите координаты первого двупалубника:");
            my.setShip(2, reader.readLine());
            System.out.println("Введите координаты второго двупалубника:");
            my.setShip(2, reader.readLine());
            System.out.println("Введите координаты третьего двупалубника:");
            my.setShip(2, reader.readLine());

            System.out.println("Введите координаты первого трехпалубника:");
            my.setShip(3, reader.readLine());
            System.out.println("Введите координаты второго трехпалубника:");
            my.setShip(3, reader.readLine());

            System.out.println("Введите координаты четырехпалубника:");
            my.setShip(4, reader.readLine());

            System.out.println("Спасибо, что установили корабли :)");
            System.out.println("\n" + "Игра началась!" + "\n");
            start();
        }
        else
        {
            my.createRandomMap();
            my.printMap();
            System.out.println("Ваши корабли расставлены случайным образом");
            start();
        }
    }

    public void start() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Выбирете желаемую сложность: 1, 2");
        String dif = reader.readLine();
        if (dif.equals("1"))
            battle(new EasyBot(my, "легкий"));
        else if (dif.equals("2"))
            battle(new MediumBot(my));
        else
        {
            System.out.println("Такой сложности не существует :(");
            System.out.println("Попробуйте ещё раз:");
            start();
        }
    }
    public void battle(Bot enemy) throws Exception
    {
        try
        {
            boolean end = false;
            //Игра будет продолжаться, пока один из игроков не уничтожит все корабли противника
            while (!end)
            {
                //Этот цикл для выстрелов игрока
                while (true)
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("(end - завершить игру, print - показать вашу карту, printe - показать карту соперника)" + "\n"
                            + "Введите координаты поля, которое хотите поразить:");
                    String s = reader.readLine();
                    if (s.equals("print"))
                        my.printMap();
                    else if (s.equals("printe"))
                        enemy.bot.clone.printMap();
                    else if (s.equals("end"))
                    {
                        end = true;
                        break;
                    }
                    else
                    {
                        //С помощью метода convert класса Map переводим 
                        //строчку с координатами в массив с числами
                        List<Integer> coordinate = my.convert(s);
                        int x = coordinate.get(0);
                        int y = coordinate.get(1);
                        if (!my.annihilation(enemy.bot, x, y))
                        {
                            enemy.bot.clone.printMap();
                            break;
                        }
                        System.out.println("\n" + "Вы подбили корабль!" + "\n");
                        enemy.bot.clone.printMap();
                    }
                }

                if (end)
                    break;

                System.out.println("\n" + "Вы промахнулись" + "\n");

                //Этот цикл для выстрелов компьютера
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                enemy.shoot();

                System.out.println("\n" + "Ваше поле после атак противника: " + "\n");
                my.printMap();

                if (my.checkMap(my))
                {
                    System.out.println("Вы проиграли :(");
                    end = !end;
                }
                if (enemy.bot.checkMap(enemy.bot))
                {
                    System.out.println("Вы победили :)");
                    end = !end;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Вы ввели координаты не по правилам :( " + "\n"
                    + "Попробуйте ещё раз" + "\n");
            this.battle(enemy);
        }
    }

}
