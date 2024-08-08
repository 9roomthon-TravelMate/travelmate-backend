package travelmate.backend.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import travelmate.backend.entity.TourSpotTheme;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class TourSpotThemeConverter implements AttributeConverter<TourSpotTheme, String> {

    @Override
    public String convertToDatabaseColumn(TourSpotTheme theme) {
        if (theme == null) {
            return null;
        }
        return theme.getCode();
    }

    @Override
    public TourSpotTheme convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(TourSpotTheme.values())
                .filter(themeType -> themeType.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
