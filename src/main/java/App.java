import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import entity.jackson.Cat;
import entity.jackson.Category;
import entity.jackson.Color;


import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;


public class App {

    public static void main(String[] args) {
        runJackson();
    }

    private static void runJackson(){
        Category category = new Category();
        category.setId(10);
        category.setName("Кошки");
        category.setDescription("Кошки с родословной");

        Cat murzik = new Cat(); // объект будет передан для преобразования в json строку
        murzik.setId(11);
        murzik.setName("Мурзик");
        murzik.setAge(0);
        murzik.setColor(Color.WHITE);
        murzik.setBirth(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
        murzik.setAdditionalInfo("Кот со всеми прививками");

        Cat.Habit habit = new Cat.Habit();
        habit.setName("Ладит с детьми");
        habit.setDescription("Отличная привычка");
        habit.setGood(true);
        murzik.getHabits().add(habit);

        // у объектов установлены взаимные ссылки
        murzik.setCategory(category);
        category.getCats().add(murzik);


        Cat vasilii = new Cat(); // объект не будет передан для преобразования в json строку
        vasilii.setId(12);
        vasilii.setName("Василий");
        vasilii.setCategory(category);
        category.getCats().add(vasilii);

        // преобразование объекта к json строке
        ObjectMapper mapperToJson = new ObjectMapper();

        String jsonCat = null;
        String jsonCats = null;

        try {
            // mapperToJson.writeValue(new File("cat.json"), murzik);
            // объект к строке
            jsonCat = mapperToJson.writeValueAsString(murzik);
            System.out.println("---JSON CAT---");
            System.out.println(jsonCat);

            // список объектов к строке
            jsonCats = mapperToJson.writeValueAsString(Arrays.asList(murzik, murzik, murzik));
            System.out.println("---JSON CATS---");
            System.out.println(jsonCats);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        // создание объекта из json строки
        Cat catFromJson = null;
        Cat[] catsArrFromJson = null;
        ArrayList<Cat> catsListFromJson = null;

        ObjectMapper mapperFromJson = new ObjectMapper();

        try {
            // чтение из файла
            // System.out.println(
              // mapperFromJson.readValue(new File("cat.json"), Cat.class));

            catFromJson = mapperFromJson.readValue(jsonCat, Cat.class);
            catsArrFromJson = mapperFromJson.readValue(jsonCats, Cat[].class);
            // для коллекций:
            // вариант 1
            CollectionType type = mapperFromJson.getTypeFactory()
                    .constructCollectionType(ArrayList.class, Cat.class);
            catsListFromJson = mapperFromJson.readValue(jsonCats, type);

            // вариант 2
            // catsListFromJson = mapperFromJson.readValue(jsonCats,
            // new TypeReference<ArrayList<Cat>>() {});

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("CAT FROM JSON: " + catFromJson);
        System.out.println("---МАССИВ Cat[]---" + Arrays.toString(catsArrFromJson));
    }
}
