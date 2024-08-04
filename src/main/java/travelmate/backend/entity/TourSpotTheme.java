package travelmate.backend.entity;

import lombok.Getter;

@Getter
public enum TourSpotTheme {
    NATURE("nature", "도심을 떠나 만끽하는 자연"),
    HISTORY("history", "역사 속으로 떠나는 시간 여행"),
    WELLBEING("wellbeing", "내 건강은 내가 챙겨, 웰빙 휴양지"),
    EXPERIENCE("experience", "내 삶을 더 다채롭게, 체험 관광"),
    INDUSTRY("industry", "빠르게 변하는 세상 이해하기, 산업 관광"),
    ART("art", "예술과 건축의 걸작 탐방"),
    FOOD("food", "미식의 세계로 떠나는 맛 여행"),
    CAFE("cafe", "멋스러운 카페와 차 한잔의 여유"),
    LEISURE("leisure", "에너지 넘치는 나를 위한, 액티브 휴양지"),
    SHOPPING("shopping", "쇼핑");


    private final String code;

    private final String defaultTitle;

    TourSpotTheme(String code, String defaultTitle) {
        this.code = code;
        this.defaultTitle = defaultTitle;
    }

}
