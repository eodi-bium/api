package com.eodi.bium;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlaceServiceTest {

    @Autowired
    PlaceService placeService;


    @Test
    public void N_KM_장소를_조회한다() throws Exception {
        //given
        double N = 2.5;
        PlaceRequest placeRequst = new PlaceRequest(BATTERY, 위치, N);
        Place place1_이점오키로내 = new Place(이름, 위도, 경도, 전화번호);
        Place place2_이점오키로내 = new Place(이름, 위도, 경도, 전화번호);
        Place place3_이점오키로내 = new Place(이름, 위도, 경도, 전화번호);
        List<Place> places = new ArrayList<>();

        //when
        PlaceService = placeService.insertPlace(places);
        PlaceResposne = placeService.findPlace(placeRequst);

        //then
        assertThat(PlaceResponse.places).isEqual(places);
    }

    public void N_KM_장소를_조회한다_N이_너무크다() throws Exception {
        //given
        double N = 4.1;
        PlaceRequest placeRequst = new PlaceRequest(BATTERY, 위치, N);

        // when, then
        assertThatThrownBy(() -> placeService.findPlace(placeRequst)).isInstanceOf(
                SomethingException.class)
            .hasMessageContaining("에러메시지");
    }


    public void N_KM_장소를_조회한다_N이_없다() throws Exception {
        //given
        double N;
        PlaceRequest placeRequst = new PlaceRequest(BATTERY, 위치, N);

        // when, then
        assertThatThrownBy(() -> placeService.findPlace(placeRequst)).isInstanceOf(
                SomethingException.class)
            .hasMessageContaining("에러메시지");
    }
}
