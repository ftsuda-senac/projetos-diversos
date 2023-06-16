package br.senac.tads.filerenamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileRenamerV2 {

    public static void main(String[] args) {
//        if (args == null || args.length == 0) {
//            System.out.println(
//                    """
//                FileRenamer
//                    Objetivo: Renomear os arquivos de atividades baixados do Blackboard com o nome do aluno indicado no arquivo .txt
//                    Uso: java -jar file-renamer-1.0.0.jar [diretório-absoluto]
//                    Onde: [diretório-absoluto] => Diretório onde os arquivos baixados do Blackboard foram descompactados
//                """);
//            System.exit(0);
//            return;
//        }
//        String directory = args[0];

        String directory = "C:\\senac\\2023-1\\dswa-atividade-filme";
        Path directoryPath = Paths.get(directory);

        if (!Files.exists(directoryPath)) {
            System.err.println(MessageFormat.format("O caminho \"{0}\" informado não existe", directory));
            System.exit(0);
            return;
        }

        if (!Files.isDirectory(directoryPath)) {
            System.err.println(MessageFormat.format("O caminho \"{0}\" informado não é um diretório", directory));
            System.exit(0);
            return;
        }

        System.out.println(MessageFormat.format("Renomeando arquivos do caminho \"{0}\"...", directory));
        Pattern patt = Pattern.compile("Nome: (.+) \\(");

        Map<String, String> mapFileAndName = new LinkedHashMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths.filter(p1 -> Files.isRegularFile(p1) && p1.getFileName().toString().endsWith(".txt"))
                    .forEach(p1 -> {

                        Path originalFile = p1.getName(p1.getNameCount() - 1);
                        String originalFileName = originalFile.toString().split("\\.")[0];
                        // System.out.println(originalFileName);
                        String personName = null;
                        try (BufferedReader br = new BufferedReader(new FileReader(p1.toFile()))) {
                            String st;
                            while ((st = br.readLine()) != null) {
                                Matcher matcher = patt.matcher(st);
                                if (matcher.find() && matcher.groupCount() > 0) {
                                    personName = matcher.group(1);
                                    mapFileAndName.put(originalFileName, personName);
                                    break;
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (Entry<String, String> entry : mapFileAndName.entrySet()) {
            try (Stream<Path> paths2 = Files.walk(Paths.get(directory))) {
                paths2.filter(p2 -> Files.isRegularFile(p2) && p2.getName(p2.getNameCount() - 1).toString().startsWith(entry.getKey()))
                        .forEach(p2 -> {
                            String fileExtension = p2.getFileName().toString().split("\\.")[1].toLowerCase();
                            Path newPath = Paths.get(directory, entry.getValue().replaceAll("\\s+", "-") + "." + fileExtension);
                            //System.out.println(newPath.toString());
                            try {
                                Files.move(p2, newPath, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Feito");
    }

}
