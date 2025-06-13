package exercise;

import net.datafaker.Faker;
import exercise.model.User;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.LongStream;

class Data {
    private static final int ITEMS_COUNT = 10;

    private static int idCounter = ITEMS_COUNT;

    public static List<User> getUsers() {
        Faker faker = new Faker();

        List<Long> ids = LongStream
            .range(1, ITEMS_COUNT + 1)
            .boxed()
            .toList();

        List<User> users = new ArrayList<>();

        for (int i = 0; i < ITEMS_COUNT; i++) {
            var user = new User(ids.get(i), faker.name().fullName(), faker.internet().emailAddress());
            users.add(user);
        }

        List<User> admins = List.of(
            new User(getNextId(), "Glynn Joinsey", "gjoinsey1@blogger.com"),
            new User(getNextId(), "Sarina Crosi", "scrosi4@cam.ac.uk"),
            new User(getNextId(), "Emmit Brundle", "brundle@cam.ac.uk")
        );

        users.addAll(admins);
        Collections.shuffle(users);

        return users;
    }

    public static long getNextId() {
        long id = ++idCounter;
        return id;
    }
}
