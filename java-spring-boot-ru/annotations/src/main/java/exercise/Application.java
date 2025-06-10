package exercise;

import exercise.model.Address;
import exercise.annotation.Inspect;
import java.lang.reflect.Method;

public class Application {
    public static void main(String[] args) {
        var address = new Address("London", 12345678);

        // BEGIN
        Class<?> addressClass = address.getClass();
        Method[] methods = addressClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Inspect.class)) {
                String name = method.getName();
                String returnType = method.getReturnType().getSimpleName();
                System.out.printf("Method %s returns a value of type %s%n",
                        name, returnType);
            }
        }
        // END
    }
}
