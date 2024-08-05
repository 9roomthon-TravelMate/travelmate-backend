package travelmate.backend.dto.tourApi.item;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import travelmate.backend.entity.TourSpotTheme;

import java.util.List;

public record TourApiTourInfo(
        String title,
        String addr1,
        String areacode,
        String sigungucode,
        String cat1,
        String cat2,
        String cat3,
        String contentid,
        String contenttypeid,
        String firstimage,
        String firstimage2,
        String mapx,
        String mapy
) {

    private static final List<ThemeResolver> themeResolvers = List.of(
            new ThemeResolver(TourSpotTheme.NATURE, null,
                    new IncludeCategoryFilter(List.of("A0101", "A0102"))),

            new ThemeResolver(TourSpotTheme.HISTORY, null,
                    new IncludeCategoryFilter(List.of("A0201"))),

            new ThemeResolver(TourSpotTheme.HISTORY, null,
                    new IncludeCategoryFilter(List.of("A0206")),
                    new IncludeCategoryFilter(List.of("A02060100", "A02060200"))),

            new ThemeResolver(TourSpotTheme.WELLBEING, null,
                    new IncludeCategoryFilter(List.of("A0202"))),

            new ThemeResolver(TourSpotTheme.EXPERIENCE, null,
                    new IncludeCategoryFilter(List.of("A0203"))),

            new ThemeResolver(TourSpotTheme.INDUSTRY, null,
                    new IncludeCategoryFilter(List.of("A0204"))),

            new ThemeResolver(TourSpotTheme.ART, null,
                    new IncludeCategoryFilter(List.of("A0205", "A0207", "A0208"))),

            new ThemeResolver(TourSpotTheme.ART, null,
                    new IncludeCategoryFilter(List.of("A0206")),
                    new ExcludeCategoryFilter(List.of("A02060100", "A02060200"))),

            new ThemeResolver(TourSpotTheme.FOOD, null,
                    new IncludeCategoryFilter(List.of("A0502")),
                    new ExcludeCategoryFilter(List.of("A05020900"))),

            new ThemeResolver(TourSpotTheme.CAFE, null,
                    new IncludeCategoryFilter(List.of("A0502")),
                    new IncludeCategoryFilter(List.of("A05020900"))),

            new ThemeResolver(TourSpotTheme.LEISURE,
                    new IncludeCategoryFilter(List.of("A03"))),

            new ThemeResolver(TourSpotTheme.SHOPPING,
                    new IncludeCategoryFilter(List.of("A04")))
    );

    public TourSpotTheme getTourSpotTheme() {
        for (ThemeResolver themeResolver : themeResolvers) {
            TourSpotTheme theme = themeResolver.resolve(cat1, cat2, cat3);
            if (theme != null) {
                return theme;
            }
        }
        return null;
    }
}


record ThemeResolver (
        TourSpotTheme theme,
        CategoryFilter categoryFilter1,
        CategoryFilter categoryFilter2,
        CategoryFilter categoryFilter3
) {
    ThemeResolver(TourSpotTheme theme, CategoryFilter categoryFilter1) {
        this(theme, categoryFilter1, null, null);
    }

    ThemeResolver(TourSpotTheme theme, CategoryFilter categoryFilter1, CategoryFilter categoryFilter2) {
        this(theme, categoryFilter1, categoryFilter2, null);
    }

    private boolean isFilterMatched(CategoryFilter categoryFilter, String category) {
        if (categoryFilter == null) {
            return true;
        }
        return categoryFilter.matches(category);
    }

    private boolean canResolve(String category1, String category2, String category3) {
        return isFilterMatched(categoryFilter1, category1) &&
                isFilterMatched(categoryFilter2, category2) &&
                isFilterMatched(categoryFilter3, category3);
    }

    public TourSpotTheme resolve(String category1, String category2, String category3) {
        if (canResolve(category1, category2, category3)) {
            return theme;
        }
        return null;
    }
}

interface CategoryFilter {
    boolean matches(String category);
}

@RequiredArgsConstructor
class IncludeCategoryFilter implements CategoryFilter {
    @NonNull
    private final List<String> categories;

    @Override
    public boolean matches(String category) {
        return categories.contains(category);
    }
}

@RequiredArgsConstructor
class ExcludeCategoryFilter implements CategoryFilter {
    @NonNull
    private final List<String> categories;

    @Override
    public boolean matches(String category) {
        return !categories.contains(category);
    }
}


