package br.senac.tads.geradordadospessoais;

import com.github.javafaker.Faker;
import java.util.Locale;

// Ver https://www.baeldung.com/java-faker
// Ver https://github.com/DiUS/java-faker
public class DataFakerGenerator {

    public static void main(String[] args) {

        Faker faker = new Faker(new Locale("pt", "BR"));
        for (int i = 0; i < 50; i++) {
            System.out.println(faker.commerce().productName());
        }
        
        for (int i = 0; i < 50; i++) {
            //System.out.println(faker.food().dish());
            System.out.println(faker.name().fullName());
        }
        
    }

}
