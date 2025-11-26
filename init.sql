-- 1. Place 테이블 생성
-- 이미지의 필드명(@Column)을 그대로 반영했습니다.
-- 주의: lattitude 오타가 이미지에 있어서 그대로 썼으나, Java 코드 수정이 가능하다면 latitude로 고치는 것을 추천합니다.
CREATE TABLE IF NOT EXISTS place (
                                     place_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     place_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    phone_number VARCHAR(50)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Recycling Types 테이블 생성 (JPA의 @ElementCollection 사용 가정)
-- 보통 '테이블명_필드명' 형식으로 생성됩니다.
CREATE TABLE IF NOT EXISTS place_recycling_types (
                                                     place_id BIGINT NOT NULL,
                                                     recycling_types VARCHAR(50),
    FOREIGN KEY (place_id) REFERENCES place(place_id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 데이터 삽입 (중심점: 인하대학교 37.450019, 126.653077 가정)

-- Case 1: [성공 예상] 인하대 후문 (약 300m 거리)
INSERT INTO place (place_name, address, latitude, longitude, phone_number)
VALUES ('인하대 분리수거장', '인천 미추홀구 인하로 100', 37.451500, 126.655000, '032-000-0001');

INSERT INTO place_recycling_types (place_id, recycling_types) VALUES (1, 'BATTERY');
INSERT INTO place_recycling_types (place_id, recycling_types) VALUES (1, 'CLOTHES');

-- Case 2: [성공 예상] 학익동 주민센터 (약 1.5km 거리 - 3km 이내)
INSERT INTO place (place_name, address, latitude, longitude, phone_number)
VALUES ('학익동 수거함', '인천 미추홀구 학익동', 37.440000, 126.660000, '032-000-0002');

INSERT INTO place_recycling_types (place_id, recycling_types) VALUES (2, 'LIGHT');

-- Case 3: [실패 예상] 송도 센트럴파크 (약 6km 거리 - 3km 밖)
INSERT INTO place (place_name, address, latitude, longitude, phone_number)
VALUES ('송도 스마트 수거함', '인천 연수구 송도동', 37.393000, 126.640000, '032-000-0003');

INSERT INTO place_recycling_types (place_id, recycling_types) VALUES (3, 'PHONE');
INSERT INTO place_recycling_types (place_id, recycling_types) VALUES (3, 'BATTERY');