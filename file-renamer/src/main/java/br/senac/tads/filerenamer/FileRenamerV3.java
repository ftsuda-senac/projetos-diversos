package br.senac.tads.filerenamer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileRenamerV3 {

    private static final Pattern NOME_PATTERN = Pattern.compile("Nome: (.+) \\(\\d+\\)");
    private static final Pattern DATA_PATTERN = Pattern.compile("Data do envio: (.+)");
    private static final Pattern ARQUIVO_ORIGINAL_PATTERN = Pattern.compile("Nome do arquivo original: (.+)");

    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "EEEE, d 'de' MMMM 'de' yyyy HH'h'mm'min'ss's' z", new Locale("pt", "BR"));
    private static final DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final String EXCEL_FILE_PREFIX = "zz_entregas";
    private static final String EXCEL_FILE_EXTENSION = ".xlsx";

    public static void main(String[] args) {
        String directory = "E:\\senac\\25-2\\4b\\3-SpringBoot1-Produtos";
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

        // ETAPA 1: Renomear arquivos com o nome do aluno
        System.out.println(MessageFormat.format("Renomeando arquivos do caminho \"{0}\"...", directory));
        renomearArquivos(directory);

        // ETAPA 2: Extrair dados dos arquivos TXT
        System.out.println(MessageFormat.format("Extraindo dados dos arquivos do caminho \"{0}\"...", directory));

        List<DadosAluno> listaAlunos = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(directoryPath)) {
            List<Path> arquivosTxt = paths
                    .filter(p -> Files.isRegularFile(p) && p.getFileName().toString().endsWith(".txt"))
                    .filter(p -> !temSufixoNumerado(p.getFileName().toString()))
                    .collect(Collectors.toList());

            for (Path arquivoTxt : arquivosTxt) {
                DadosAluno dados = extrairDadosDoArquivo(arquivoTxt);
                if (dados != null) {
                    listaAlunos.add(dados);
                    System.out.println("Extraído: " + dados.getNome());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (!listaAlunos.isEmpty()) {
            String excelFileName = gerarNomeArquivoExcel(directoryPath);
            String excelPath = directory + "\\" + excelFileName;
            gerarPlanilhaExcel(listaAlunos, excelPath);
            System.out.println(MessageFormat.format("Planilha gerada: {0}", excelPath));
        } else {
            System.out.println("Nenhum arquivo de entrega encontrado.");
        }

        System.out.println("Feito");
    }

    private static boolean temSufixoNumerado(String fileName) {
        // Verifica se o nome do arquivo tem sufixo _2, _3, etc antes da extensão
        String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        return Pattern.matches(".*_\\d+$", nameWithoutExt);
    }

    private static String gerarNomeArquivoExcel(Path directoryPath) {
        // Extrai o nome do diretório atual e do diretório pai
        String nomeDiretorio = directoryPath.getFileName().toString();
        Path parentPath = directoryPath.getParent();
        String nomeDiretorioPai = parentPath != null ? parentPath.getFileName().toString() : "";

        // Normaliza os nomes removendo acentos e caracteres especiais
        String sufixo = "_" + normalizarNome(nomeDiretorioPai) + "_" + normalizarNome(nomeDiretorio);

        return EXCEL_FILE_PREFIX + sufixo + EXCEL_FILE_EXTENSION;
    }

    private static String normalizarNome(String nome) {
        // Remove acentos
        String normalizado = Normalizer.normalize(nome, Normalizer.Form.NFD);
        normalizado = normalizado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Remove caracteres especiais, mantendo apenas letras, números, hífens, underlines e pontos
        normalizado = normalizado.replaceAll("[^a-zA-Z0-9\\-_.]", "");

        return normalizado;
    }

    private static void renomearArquivos(String directory) {
        Pattern patt = Pattern.compile("Nome: (.+) \\(");
        Map<String, String> mapFileAndName = new LinkedHashMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths.filter(p1 -> Files.isRegularFile(p1) && p1.getFileName().toString().endsWith(".txt"))
                    .forEach(p1 -> {
                        Path originalFile = p1.getName(p1.getNameCount() - 1);
                        String[] fileParts = originalFile.toString().split("\\.");
                        String originalFileName;
                        if (fileParts.length <= 2) {
                            originalFileName = fileParts[0];
                        } else {
                            originalFileName = String.join(".", Arrays.copyOfRange(fileParts, 0, fileParts.length - 1));
                        }

                        try (BufferedReader br = new BufferedReader(new FileReader(p1.toFile()))) {
                            String st;
                            while ((st = br.readLine()) != null) {
                                Matcher matcher = patt.matcher(st);
                                if (matcher.find() && matcher.groupCount() > 0) {
                                    String personName = matcher.group(1);
                                    mapFileAndName.put(originalFileName, personName);
                                    System.out.println("Encontrado: " + personName);
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
                            String[] fileParts = p2.getFileName().toString().split("\\.");
                            String fileExtension = fileParts[fileParts.length - 1].toLowerCase();
                            String fileName;
                            Path newPath;
                            int i = 0;
                            // Trata casos onde a entrega tem mais de 1 arquivo
                            do {
                                i++;
                                if (i < 2) {
                                    fileName = entry.getValue().replaceAll("\\s+", "-");
                                } else {
                                    fileName = entry.getValue().replaceAll("\\s+", "-") + "_" + i;
                                }
                                newPath = Paths.get(directory, fileName + "." + fileExtension);
                            } while (Files.exists(newPath));

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

        System.out.println("Arquivos renomeados com sucesso.");
    }

    private static DadosAluno extrairDadosDoArquivo(Path arquivoTxt) {
        DadosAluno dados = new DadosAluno();
        List<String> arquivosOriginais = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoTxt.toFile()))) {
            String linha;
            String secaoAtual = null;
            StringBuilder conteudoSecao = new StringBuilder();

            while ((linha = br.readLine()) != null) {
                // Extrai nome do aluno
                Matcher nomeMatcher = NOME_PATTERN.matcher(linha);
                if (nomeMatcher.find()) {
                    dados.setNome(nomeMatcher.group(1));
                    continue;
                }

                // Extrai data de envio
                Matcher dataMatcher = DATA_PATTERN.matcher(linha);
                if (dataMatcher.find()) {
                    String dataOriginal = dataMatcher.group(1);
                    dados.setDataEnvio(converterData(dataOriginal));
                    continue;
                }

                // Extrai nomes dos arquivos originais
                Matcher arquivoMatcher = ARQUIVO_ORIGINAL_PATTERN.matcher(linha);
                if (arquivoMatcher.find()) {
                    arquivosOriginais.add(arquivoMatcher.group(1));
                    continue;
                }

                // Detecta início de seções
                if (linha.equals("Campo do envio:")) {
                    salvarSecao(dados, secaoAtual, conteudoSecao.toString());
                    secaoAtual = "campo_envio";
                    conteudoSecao = new StringBuilder();
                    continue;
                }

                if (linha.equals("Comentários:")) {
                    salvarSecao(dados, secaoAtual, conteudoSecao.toString());
                    secaoAtual = "comentarios";
                    conteudoSecao = new StringBuilder();
                    continue;
                }

                if (linha.equals("Arquivos:")) {
                    salvarSecao(dados, secaoAtual, conteudoSecao.toString());
                    secaoAtual = "arquivos";
                    conteudoSecao = new StringBuilder();
                    continue;
                }

                // Acumula conteúdo da seção atual
                if (secaoAtual != null && !secaoAtual.equals("arquivos")) {
                    if (conteudoSecao.length() > 0) {
                        conteudoSecao.append("\n");
                    }
                    conteudoSecao.append(linha);
                }
            }

            // Salva última seção
            salvarSecao(dados, secaoAtual, conteudoSecao.toString());

            // Concatena arquivos originais
            dados.setArquivos(String.join(" | ", arquivosOriginais));

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return dados;
    }

    private static void salvarSecao(DadosAluno dados, String secao, String conteudo) {
        if (secao == null || conteudo == null) {
            return;
        }

        String textoLimpo = conteudo.trim();

        if (secao.equals("campo_envio")) {
            if (!textoLimpo.equals("Não há dados de texto de envio para este exercício.")) {
                dados.setCampoEnvio(textoLimpo);
            }
        } else if (secao.equals("comentarios")) {
            if (!textoLimpo.equals("Não há comentários de alunos para este exercício.")) {
                dados.setComentarios(textoLimpo);
            }
        }
    }

    private static String converterData(String dataOriginal) {
        try {
            // Remove o fuso horário para parsing mais simples
            String dataSemFuso = dataOriginal.replace(" BRT", "").replace(" BRST", "");

            // Mapa para converter nomes dos dias e meses
            Map<String, String> diasSemana = new HashMap<>();
            diasSemana.put("Segunda-feira", "Segunda-feira");
            diasSemana.put("Terça-feira", "Terça-feira");
            diasSemana.put("Quarta-feira", "Quarta-feira");
            diasSemana.put("Quinta-feira", "Quinta-feira");
            diasSemana.put("Sexta-feira", "Sexta-feira");
            diasSemana.put("Sábado", "Sábado");
            diasSemana.put("Domingo", "Domingo");

            // Parse manual da data
            Pattern datePattern = Pattern.compile(
                    "(\\w+-?\\w*), (\\d{1,2}) de (\\w+) de (\\d{4}) (\\d{2})h(\\d{2})min(\\d{2})s");
            Matcher matcher = datePattern.matcher(dataSemFuso);

            if (matcher.find()) {
                int dia = Integer.parseInt(matcher.group(2));
                String mesNome = matcher.group(3);
                int ano = Integer.parseInt(matcher.group(4));
                int hora = Integer.parseInt(matcher.group(5));
                int minuto = Integer.parseInt(matcher.group(6));
                int segundo = Integer.parseInt(matcher.group(7));

                int mes = converterMes(mesNome);

                LocalDateTime dateTime = LocalDateTime.of(ano, mes, dia, hora, minuto, segundo);
                return dateTime.format(OUTPUT_DATE_FORMATTER);
            }
        } catch (Exception ex) {
            System.err.println("Erro ao converter data: " + dataOriginal);
            ex.printStackTrace();
        }
        return dataOriginal;
    }

    private static int converterMes(String mesNome) {
        return switch (mesNome.toLowerCase()) {
            case "janeiro" -> 1;
            case "fevereiro" -> 2;
            case "março" -> 3;
            case "abril" -> 4;
            case "maio" -> 5;
            case "junho" -> 6;
            case "julho" -> 7;
            case "agosto" -> 8;
            case "setembro" -> 9;
            case "outubro" -> 10;
            case "novembro" -> 11;
            case "dezembro" -> 12;
            default -> 1;
        };
    }

    private static void gerarPlanilhaExcel(List<DadosAluno> listaAlunos, String caminhoArquivo) {
        // Apaga arquivo existente se houver
        Path arquivoExcel = Paths.get(caminhoArquivo);
        try {
            Files.deleteIfExists(arquivoExcel);
        } catch (IOException ex) {
            System.err.println("Erro ao apagar arquivo existente: " + caminhoArquivo);
            ex.printStackTrace();
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Entregas");

            // Estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Cria cabeçalho
            Row headerRow = sheet.createRow(0);
            String[] colunas = {"Nome", "Data de Envio", "Campo de Envio", "Comentários", "Arquivos"};
            for (int i = 0; i < colunas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colunas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Preenche dados
            int rowNum = 1;
            for (DadosAluno aluno : listaAlunos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(aluno.getNome() != null ? aluno.getNome() : "");
                row.createCell(1).setCellValue(aluno.getDataEnvio() != null ? aluno.getDataEnvio() : "");
                row.createCell(2).setCellValue(aluno.getCampoEnvio() != null ? aluno.getCampoEnvio() : "");
                row.createCell(3).setCellValue(aluno.getComentarios() != null ? aluno.getComentarios() : "");
                row.createCell(4).setCellValue(aluno.getArquivos() != null ? aluno.getArquivos() : "");
            }

            // Ajusta largura das colunas
            for (int i = 0; i < colunas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Salva arquivo
            try (FileOutputStream fileOut = new FileOutputStream(caminhoArquivo)) {
                workbook.write(fileOut);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Classe interna para armazenar dados do aluno
    static class DadosAluno {
        private String nome;
        private String dataEnvio;
        private String campoEnvio;
        private String comentarios;
        private String arquivos;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDataEnvio() {
            return dataEnvio;
        }

        public void setDataEnvio(String dataEnvio) {
            this.dataEnvio = dataEnvio;
        }

        public String getCampoEnvio() {
            return campoEnvio;
        }

        public void setCampoEnvio(String campoEnvio) {
            this.campoEnvio = campoEnvio;
        }

        public String getComentarios() {
            return comentarios;
        }

        public void setComentarios(String comentarios) {
            this.comentarios = comentarios;
        }

        public String getArquivos() {
            return arquivos;
        }

        public void setArquivos(String arquivos) {
            this.arquivos = arquivos;
        }
    }
}
