package pl.zajavka.business.management;

import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class InputDataCache {

    private static final Map<String, List<String>> inputData;

    static {
        try {
            inputData = readFileContent();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Map<String, List<String>> readFileContent() throws IOException {
        Path path = ResourceUtils.getFile("classpath:car-dealership-traffic-simulation.md").toPath();
        List<String> lines = Files.readAllLines(path).stream()
            .filter(line -> !line.startsWith("[//]: #"))
            .filter(line -> !line.isBlank())
            .toList();

        return lines.stream()
            .collect(Collectors.groupingBy(
                line -> line.split("->")[0].trim(),
                Collectors.mapping(
                    line -> line.substring(line.indexOf("->") + 2).trim(),
                    Collectors.toList()
                )
            ));
    }

    public static <T> List<T> getInputData(
        final Keys.InputDataGroup inputDataGroup,
        final Function<String, T> mapper
    ) {
        return Optional.ofNullable(inputData.get(inputDataGroup.toString()))
            .orElse(List.of())
            .stream()
            .map(mapper)
            .toList();
    }
}
