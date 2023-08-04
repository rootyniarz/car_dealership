package pl.zajavka.business.management;

import lombok.experimental.UtilityClass;
import pl.zajavka.infrastructure.database.entity.*;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class InputDataMapper {

    public static SalesmanEntity mapSalesman(String line) {
        List<String> data = DataManipulationUtil.dataAsList(line);
        return SalesmanEntity.builder()
            .name(data.get(0))
            .surname(data.get(1))
            .pesel(data.get(2))
            .build();
    }

    public static MechanicEntity mapMechanic(String line) {
        List<String> data = DataManipulationUtil.dataAsList(line);
        return MechanicEntity.builder()
            .name(data.get(0))
            .surname(data.get(1))
            .pesel(data.get(2))
            .build();
    }

    public static CarToBuyEntity mapCarToBuy(String line) {
        List<String> data = DataManipulationUtil.dataAsList(line);
        return CarToBuyEntity.builder()
            .vin(data.get(0))
            .brand(data.get(1))
            .model(data.get(2))
            .year(Integer.parseInt(data.get(3)))
            .color(data.get(4))
            .price(new BigDecimal(data.get(5)))
            .build();
    }

    public static ServiceEntity mapService(String line) {
        List<String> data = DataManipulationUtil.dataAsList(line);
        return ServiceEntity.builder()
            .serviceCode(data.get(0))
            .description(data.get(1))
            .price(new BigDecimal(data.get(2)))
            .build();
    }

    public static PartEntity mapPart(String line) {
        List<String> data = DataManipulationUtil.dataAsList(line);
        return PartEntity.builder()
            .serialNumber(data.get(0))
            .description(data.get(1))
            .price(new BigDecimal(data.get(2)))
            .build();
    }
}
