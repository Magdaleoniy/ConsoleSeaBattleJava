//Класс для работы с полем боя

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map
{
    //Карта-клон, на которой мы не будем видеть корабли
    //соперника до тех пор, пока не подобьем их
    public Map clone;
    private static final int size = 10;
    private static final int plusSize = size + 2;
    //Создадимw двумерный массив для игрового поля
    private final int[][] map = new int[plusSize][plusSize];
    //И строчку с буквенными значениями координат
    private static final String nameMap = "ABCDEFGHIJ";

    //Тип "пара", который хранит в себе две переменные
    public class Pair
    {
        public int x;
        public int y;
        public Pair(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    public void InitializeMap()
    {
        for (int i = 0; i < plusSize; i++)
        {
            for (int j = 0; j < plusSize; j++)
            {
                map[i][j] = 0;
            }
        }
    }

    //Метод, который инициализирует карту-клона 
    public void InitializeClone()
    {
        clone = new Map();
        clone.InitializeMap();
    }

    //Этот метод переводит строчку с координатами в массив с целыми числами
    public List<Integer> convert(String s) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int space = s.indexOf(" ");
        int y = nameMap.indexOf(s.charAt(0)) + 1;
        int x;
        if (space == -1)
            x = Integer.parseInt(s.substring(1));
        else
            x = Integer.parseInt(s.substring(1, s.indexOf(" ")));
        if (x > 10 || y > 10)
            throw new Exception();
        List<Integer> result = new ArrayList<>();
        result.add(x);
        result.add(y);
        if (space == -1)
            return result;
        else
        {
            y = nameMap.indexOf(s.charAt(space + 1) + 1);
            x = Integer.parseInt(s.substring(space + 2));
            if (x > 10 || y > 10)
                throw new Exception();
            result.add(x);
            result.add(y);
            return result;
        }
    }

    //Метод, который проверяет, были ли сбиты все корабли у карты x
    public boolean checkMap(Map x)
    {
        int amount = 0;
        for (int i = 1; i < size; i++)
            for (int j = 1; j < size; j++)
            {
                if (x.map[i][j] == 3)
                amount++;
            }
        if (amount == 20)
            return true;
        else
            return false;
    }

    //Метод, который проверяет, можем ли мы по правилам установить палубу 
    //по заданным координатам
    public Boolean checkShip(int x, int y)
    {
        try
        {
            //Проврека, что проверяемое поле пустое
            if (map[x][y] != 1 && map[x + 1][y] != 1)
            {
                //Проверка, что соседние поля сверху и справа пустые
                if (map[x + 1][y] != 1 && map[x][y + 1] != 1)
                {
                    //Проверка, что соседние поля слева и снизу пустые
                    if (map[x - 1][y] != 1 && map[x][ y - 1] != 1)
                    {
                        //Проверка, что соседние по диагонали пустые
                        if (map[x + 1][ y + 1] != 1 && map[x + 1][y - 1] != 1 && map[x - 1][y + 1] != 1
                            && map[x - 1][ y - 1] != 1)
                        return true;
                    }
                }
            }

            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    //Метод, который проверяет, можно ли установить многопалубный корабль
    //по заданным координатам
    public boolean mayShip(int x1, int y1, int x2, int y2, int decks)
    {
        //Проверяем, лежит корабль вертикально или горизонтально
        if (x1 == x2 || y1 == y2)
        {
            //Проверяем длинну корабля
            if (Math.abs(y1 - y2) == decks - 1 || Math.abs(x1 - x2) == decks - 1)
            {
                //Проверяем, не задевает ли корабль другие корабли

                for (int i = Math.min(x1, x2); i < Math.max(x1, x2) + 1; i++)
                {
                    for (int j = Math.min(y1, y2); j < Math.max(y1, y2) + 1; j++)
                    {
                        if (!checkShip(i, j))
                            return false;
                    }
                }

                return true;

            }
        }

        return false;

    }

    //Метод, который устанавливает корабль на поле
    public void setShip(int decks, String s) throws Exception {
        try
        {
            List<Integer> coordinate = convert(s);
            if (coordinate.size() > 2)
            {
                int x1 = coordinate.get(0);
                int y1 = coordinate.get(1);
                int x2 = coordinate.get(2);
                int y2 = coordinate.get(3);

                if (mayShip(x1, y1, x2, y2, decks))
                {
                    for (int i = Math.min(x1, x2); i < Math.max(x1, x2) + 1; i++)
                    {
                        for (int j = Math.min(y1, y2); j < Math.max(y1, y2) + 1; j++)
                        {
                            map[i][j] = 1;
                        }
                    }
                    this.printMap();
                }
                else
                {
                    throw new Exception();
                }
            }
            else
            {
                int x = coordinate.get(0);
                int y = coordinate.get(1);
                if (checkShip(x, y))
                {
                    map[x][y] = 1;
                    this.printMap();
                }
                else throw new Exception();
            }
        }
        catch (Exception e)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Вы допустили ошибку при вводе координаты, попробуйте снова");
            String tryagain = reader.readLine();
            setShip(decks, tryagain);
        }
    }

    //Метод, который уничтожает клетку противника
    public boolean annihilation(Map victim, int x, int y) throws Exception {
        if (victim.map[x][y] == 1)
        {
            victim.map[x][y] = 3;
            victim.clone.map[x][y] = 3;
            //Уведомление о "сбитом" или "подбитом" корабле
            if (victim.checkShip(x, y))
            {
                System.out.println("Корабль сбит!");
            }
            else
                System.out.println("Корабль подбит!");
            return true;
        }
            else if (victim.map[x][y] == 3 || victim.map[x][y] == 2)
        {
            return true;
        }
            else if (victim.map[x][y] == 0)
        {
            victim.map[x][y] = 2;
            victim.clone.map[x][y] = 2;
            return false;
        }
            else
        throw new Exception();

    }

    //Метод, который рисует в консоли карту
    public void printMap()
    {
        for (int i = 0; i < plusSize - 1; i++)
        {
            for (int j = 0; j < plusSize - 1; j++)
            {
                //В самом вверху прописываем буквы, чтобы
                //было удобно ориентироваться на поле
                if (i == 0 && j != 0 && j != plusSize - 1)
                {
                    System.out.print(nameMap.substring(j - 1, j) + " ");
                }
                //По левому краю укажем номера строк
                else if (j == 0 && i != 0 && i != 10)
                {
                    System.out.print(i + ":" + "  ");
                }
                //Для левого верхнего края нулевую строку указывать
                //не будем, а просто сделаем отступ
                else if (i == 0)
                {
                    System.out.print("    ");
                }
                //Поскольку "10" занимает больше пикселей в консоле,
                //сделаем отступ от неё по короче (в один пробел)
                else if (i == 10 && j == 0)
                {
                    System.out.print(i + ":" + " ");
                }
                //В остальных случаях проврим, есть ли палуба в клетке.
                //Если да - выведем "Y ", если нет - "- "
                //Если палуба подбита - "X ", если подбита пустая клетка - "0 "
                else if (map[i][j] == 0)
                System.out.print("- ");
                    else if (map[i][j] == 1)
                System.out.print("Y ");
                    else if (map[i][j] == 2)
                System.out.print(0 + " ");
                    else
                System.out.print("X ");
            }
            System.out.println();

        }

    }

    //Этот метод подбирает свободную координату
    //и возвращает её с помощью типа Pair,
    //описанного после класса Game
    Pair ones() {
        Random r = new Random();
        do
        {
            int x = r.nextInt(size) + 1;
            int y = r.nextInt(size) + 1;
            do
            {
                y = r.nextInt(size) + 1;
            } while (y == x);
            //Console.WriteLine("x: " + x + " y: " + y);
            if (checkShip(x, y))
            {
                return new Pair(x, y);
            }

        } while (true);
    }

    boolean ifwhat(int j, Pair coordinate, int shiplen)
    {
        //Проверка на возможность поставить корабль:
        //вертикально, добавив палубы снизу
        if (j == 0)
        {
            if (checkShip(coordinate.x + shiplen, coordinate.y))
            {
                for (int i = coordinate.x; i < coordinate.x + shiplen + 1; i++)
                {
                    map[i][coordinate.y] = 1;
                }
                return true;
            }
            else
                return false;
        }
        //Проверка на возможно поставить корабль:
        //вертикально, добавив палубы сверху
        else if (j == 1)
        {
            if (checkShip(coordinate.x - shiplen, coordinate.y))
            {
                for (int i = coordinate.x - shiplen; i < coordinate.x + 1; i++)
                {
                    map[i][coordinate.y] = 1;
                }
                return true;
            }
            else
                return false;
        }
        //Проверка на возможно поставить корабль:
        //горизонтально, добавив палубы на правую клетку
        else if (j == 2)
        {
            if (checkShip(coordinate.x, coordinate.y + shiplen))
            {
                for (int i = coordinate.y; i < coordinate.y + shiplen + 1; i++)
                {
                    map[coordinate.x][ i] = 1;
                }
                return true;
            }
            else
                return false;
        }
        //Проверка на возможно поставить корабль:
        //горизонтально, добавив палубы на левую клетку
        else if (j == 3)
        {
            if (checkShip(coordinate.x, coordinate.y - shiplen))
            {
                for (int i = coordinate.y - shiplen; i < coordinate.y + 1; i++)
                {
                    map[coordinate.x][ i] = 1;
                }
                return true;
            }
            else
                return false;
        }
        return false;
    }

    void setRandomShip(int shiplen)
    {
        shiplen -= 1;
        boolean b;
        Random r = new Random();
        //Здесь мы берем случайную свободную координату методом ones();
        //Затем мы проверяем соседние координаты, и если какая-то из
        //них свободна, устанавливаем многопалубник, а если нет
        //то пробуем до тех пор, пока не получится
        do
        {
            Pair coordinate = ones();
            //Следующий блок запускает проверки в случайном порядке
            b = false;
            for (int i = 0; i < 20; i++)
            {
                b = ifwhat(r.nextInt(4), coordinate, shiplen);
                if (b) break;
            }
            //Console.WriteLine("ww");
        } while (b == false);
    }


    //Метод, который генерирует случайную карту
    //с установленными кораблями
    public void createRandomMap()
    {
        Random r = new Random();

        //Этот цикл расставляет однопалубные корабли
        for (int i = 0; i < 4; i++)
        {
            Pair coordinate = ones();
            map[coordinate.x][coordinate.y] = 1;
        }

        //Этот цикл расставляет двупалубные корабли
        for (int i = 0; i < 3; i++)
        {
            setRandomShip(2);
        }

        //Этот цикл расставляет трехпалубные
        for (int i = 0; i < 2; i++)
        {
            setRandomShip(3);
        }

        //Эта операция устанавливает четырехпалубник
        setRandomShip(4);

    }

}
