package br.com.projeto;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class LeitorComms {
    private static final String[] CABECALHO = {
        "X", "Y", "OBJECTID", "ID", "CD", "NANOS",
        "VZMPAT", "VZMMPPT", "PCDIFMMPT", "DSFAIXADIF"
    };

    // Lê o CSV e devolve uma lista de registros
    public List<RegistroCSV> lerCsv(Reader leitor) {
        List<RegistroCSV> registros = new ArrayList<>();

        try (
            CSVParser parser = CSVFormat.EXCEL.builder()
                .setHeader(CABECALHO)
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build()
                .parse(leitor)
        ) {
            for (CSVRecord registro : parser) {
                String codigoEstacao = registro.get("CD");
                String vazaoMedia = registro.get("VZMPAT");
                String faixaDiferenca = registro.get("DSFAIXADIF");

                registros.add(new RegistroCSV(codigoEstacao, vazaoMedia, faixaDiferenca));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return registros;
    }

    // Retorna texto formatado para exibição
    public String formatarRegistros(List<RegistroCSV> registros) {
        if (registros == null || registros.isEmpty()) {
            return "Nenhum registro encontrado.";
        }

        StringBuilder sb = new StringBuilder();
        for (RegistroCSV r : registros) {
            sb.append(String.format("Estação: %s | Vazão Média: %s | Faixa: %s%n",
                    r.codigoEstacao(), r.vazaoMedia(), r.faixaDiferenca()));
        }
        return sb.toString();
    }

    // Filtra lista conforme o texto digitado
    public List<RegistroCSV> filtrarRegistros(List<RegistroCSV> registros, String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return registros;
        }

        String termo = filtro.toLowerCase(Locale.ROOT);
        return registros.stream()
            .filter(r -> r.codigoEstacao().toLowerCase(Locale.ROOT).contains(termo)
                      || r.faixaDiferenca().toLowerCase(Locale.ROOT).contains(termo))
            .collect(Collectors.toList());
    }
}
