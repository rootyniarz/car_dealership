package pl.zajavka.business.management;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class DataManipulationUtil {

    public List<String> dataAsList(final String line) {
        return Arrays.asList(line.substring(line.indexOf("->") + 2).trim().split(";"));
    }
}
