package br.senac.tads.geradordadospessoais;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class NameGenerator {

    public static final List<String> NAME_PREPOSITIONS = Arrays.asList("de", "da", "das", "do", "dos", "e");

    private final Random random = new SecureRandom();

    private List<String> maleNames = new ArrayList<>();

    private List<String> femaleNames = new ArrayList<>();

    private List<String> lastNames = new ArrayList<>();

    private List<String> randomWords = new ArrayList<>();

    public NameGenerator() {
        init();
    }

    @SuppressWarnings("deprecation")
    public static String beautifyName(final String name) {
        final String[] tokens = name.trim().toLowerCase().split("\\s+");
        final StringBuilder beautifiedName = new StringBuilder();
        for (final String tok : tokens) {
            if (NAME_PREPOSITIONS.contains(tok)) {
                beautifiedName.append(tok);
            } else {
                beautifiedName.append(WordUtils.capitalize(tok, new char[]{'\'', '.'}));
            }
            beautifiedName.append(" ");
        }
        return beautifiedName.toString().trim();
    }

    public void init() {
        try {
            Path fileToRead = Paths
                    .get(NameGenerator.class.getClassLoader().getResource("first-names-male.txt").toURI());
            try ( BufferedReader reader = Files.newBufferedReader(fileToRead, Charset.forName("UTF-8"))) {
                maleNames = new ArrayList<>(reader.lines().filter(line -> StringUtils.isNotBlank(line))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            }

            fileToRead = Paths
                    .get(NameGenerator.class.getClassLoader().getResource("first-names-female.txt").toURI());
            try ( BufferedReader reader = Files.newBufferedReader(fileToRead, Charset.forName("UTF-8"))) {
                femaleNames = new ArrayList<>(reader.lines().filter(line -> StringUtils.isNotBlank(line))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            }

            fileToRead = Paths.get(NameGenerator.class.getClassLoader().getResource("last-names.txt").toURI());
            try ( BufferedReader reader = Files.newBufferedReader(fileToRead, Charset.forName("UTF-8"))) {
                lastNames = new ArrayList<>(reader.lines().filter(line -> StringUtils.isNotBlank(line))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            }

            fileToRead = Paths.get(NameGenerator.class.getClassLoader().getResource("random-words.txt").toURI());
            try ( BufferedReader reader = Files.newBufferedReader(fileToRead, Charset.forName("UTF-8"))) {
                randomWords = new ArrayList<>(reader.lines().filter(StringUtils::isNotBlank)
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
        } catch (IOException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }

    public BigDecimal generateRandomBigDecimal(BigDecimal min, BigDecimal max) {
        BigDecimal range = max.subtract(min);
        return min.add(range.multiply(new BigDecimal(Math.random())));
    }

    public LocalDate generateRandomDate(int minYear, int maxYear) {
        int year = minYear + random.nextInt(maxYear - minYear);
        int month = 1 + random.nextInt(12);
        int day = 1;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            day += random.nextInt(31);
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            day += random.nextInt(30);
        } else {
            if (year % 4 == 0 || year % 100 == 0) {
                day += random.nextInt(29);
            } else {
                day += random.nextInt(28);
            }
        }
        return LocalDate.of(year, month, day);
    }

    public String generateFullName(int gender) {
        String name;
        if (gender == 1) {
            name = maleNames.get(random.nextInt(maleNames.size()));
        } else {
            name = femaleNames.get(random.nextInt(femaleNames.size()));
        }
        String lastName = lastNames.get(random.nextInt(lastNames.size()));
        return beautifyName(name + " " + lastName);
    }

    private String normalize(String name) {
        name = Normalizer.normalize(name.toLowerCase(), Normalizer.Form.NFD);
        name = name.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return name.trim();
    }

    public String generateNickname(String fullName, String year, Set<String> existentNicknames) {
        String[] names = fullName.split("\\s+");
        if (names.length > 1) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(names[0].charAt(0));
            strBuilder.append(names[names.length - 1]);
            String nickname = normalize(strBuilder.toString());

            if (!existentNicknames.contains(nickname)) {
                existentNicknames.add(nickname);
                return nickname;
            }
            String otherNickname = nickname + year;
            if (!existentNicknames.contains(otherNickname)) {
                existentNicknames.add(otherNickname);
                return otherNickname;
            }
            do {
                String randomNickname = nickname + String.valueOf(1000 + random.nextInt(1000));
                if (!existentNicknames.contains(randomNickname)) {
                    existentNicknames.add(randomNickname);
                    return randomNickname;
                }
            } while (true);
        }
        return normalize(fullName.toLowerCase());
    }

    public String getEmailFromName(String fullName, String domain, Set<String> existentEmails) {
        fullName = normalize(fullName);
        fullName = fullName.replaceAll("\\s+", ".");
        for (String preposition : NAME_PREPOSITIONS) {
            fullName = fullName.replaceAll("\\." + preposition + "\\.", ".");
        }
        String email = fullName + "@" + domain;
        if (!existentEmails.contains(email)) {
            existentEmails.add(email);
            return email;
        }

        int count = 2;
        do {
            String seqName = fullName + String.valueOf(count);
            email = seqName + "@" + domain;
            if (!existentEmails.contains(email)) {
                existentEmails.add(email);
                return email;
            }
        } while (true);
    }

    public BigDecimal getHeight() {
        BigDecimal height = new BigDecimal(1.2f + random.nextFloat(1));
        return height.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getWeight() {
        BigDecimal weight = new BigDecimal(50f + random.nextFloat(100));
        return weight.setScale(1, RoundingMode.HALF_UP);
    }

    public void generateDataOutput(int qtd) {

        String defaultPassword = "abcd1234";

        String defaultDomain = "teste.com.br";

        String imageFolder = "D:/imagens/";

        String imageNameTemplate = "avatar-%04d.jpg";

        String outputTemplate = "addNewItem(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %d, \"%s\", \"%s\", %d, %s, \"%s\", \"%s\");";

        Set<String> existentNicknames = new HashSet<>();

        Set<String> existentEmails = new HashSet<>();

        LocalDate now = LocalDate.now();

        var httpClient = HttpClient.newHttpClient();

        int maxQtd = qtd + 1;
        for (int i = 1; i < maxQtd; i++) {
            int genderCode = random.nextInt(2);
            String fullName = generateFullName(genderCode);
            LocalDate dateOfBirth = generateRandomDate(now.getYear() - 90, now.getYear() - 18);
            long age = ChronoUnit.YEARS.between(dateOfBirth, now);

            String nickname = generateNickname(fullName, String.valueOf(dateOfBirth.getYear()), existentNicknames);

            String email = getEmailFromName(fullName, defaultDomain, existentEmails);

            int dddNumber = random.nextInt(100);
            if (dddNumber < 12) {
                dddNumber = 11;
            }
            long telephoneNumber = (1 + random.nextInt(9) * 1000_0000) + random.nextInt(1000_0000);
            int numero = 1 + random.nextInt(99);
            String fullTelephone = "(" + dddNumber + ") " + telephoneNumber;
            BigDecimal height = getHeight();
            BigDecimal weight = getWeight();

            int qtdInterests = 1 + random.nextInt(5);
            Set<String> interestsIds = new LinkedHashSet<>();
            for (int j = 0; j < qtdInterests; j++) {
                interestsIds.add(String.valueOf(1 + random.nextInt(5)));
            }

            String generatedImageUrl = "";
            String imageName = "";

            // https://www.twilio.com/blog/5-maneiras-de-fazer-uma-chamada-http-em-java
            // https://hankhank10.github.io/fakeface/
            // https://stackoverflow.com/a/65149092
            try {
                HttpRequest request = HttpRequest.newBuilder(
                        URI.create(String.format("https://fakeface.rest/face/json?gender=%s&minimum_age=%d&maximum_age=%d", genderCode == 1 ? "male" : "female", age, age)))
                        .header("accept", "application/json").build();
                var response = httpClient.send(request, new JsonBodyHandler<>(FaceData.class));
                generatedImageUrl = response.body().get().getImageUrl();

                HttpRequest imageRequest = HttpRequest.newBuilder(URI.create(generatedImageUrl)).build();
                imageName = String.format(imageNameTemplate, i);
                HttpResponse<InputStream> imageResponse = httpClient.send(imageRequest, HttpResponse.BodyHandlers.ofInputStream());
                try ( FileOutputStream imageOutput = new FileOutputStream(imageFolder + imageName)) {
                    InputStream is = imageResponse.body();
                    is.transferTo(imageOutput);
                }
            } catch (IOException | InterruptedException ex) {
                System.err.println(ex);
            }
            String output = String.format(outputTemplate, fullName, nickname, "Descrição de " + fullName, dateOfBirth.toString(), email, fullTelephone, defaultPassword, numero, height.toString(), weight.toString(), genderCode, "Arrays.asList(" + String.join(", ", interestsIds) + ")", imageName, generatedImageUrl);
            System.out.println(output);
        }
    }

    public static void main(String[] args) {
        NameGenerator generator = new NameGenerator();
        generator.generateDataOutput(200);
    }

    public static class FaceData {

        private Integer age;

        @JsonProperty("date_added")
        private String dateAdded;

        private String filename;

        private String gender;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("last_served")
        private String lastServed;

        private String source;

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getDateAdded() {
            return dateAdded;
        }

        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLastServed() {
            return lastServed;
        }

        public void setLastServed(String lastServed) {
            this.lastServed = lastServed;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

    }

}
