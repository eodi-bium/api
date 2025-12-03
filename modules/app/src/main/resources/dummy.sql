-- 테이블이 이미 존재한다고 가정합니다 (ddl-auto가 create인 경우 생략 가능)
-- CREATE TABLE IF NOT EXISTS event_join (...);

DELIMITER $$

DROP PROCEDURE IF EXISTS insert_dummy_event_join$$

CREATE PROCEDURE insert_dummy_event_join()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 500
        DO
            INSERT INTO event_join (member_id, point, event_id)
            VALUES (CONCAT('user_', i), -- memberId: user_1, user_2 ...
                    FLOOR(50 + (RAND() * 451)), -- point: 50 ~ 500 사이 랜덤 (500-50+1 = 451)
                    1 -- eventId: 1로 고정
                   );
            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insert_dummy_event_join();
DROP PROCEDURE insert_dummy_event_join;

-- 1. 사용자들 포인트 참여

insert into point_accum_log (created_at, point, member_id)
values (now(), 20, 'user_1'),
       (now(), 100, 'user_2'),
       (now(), 200, 'user_3');

insert into trash_record (count, member_id, recycling_type)
values (1, 'user_1', 'BATTERY'),
       (1, 'user_2', 'LIGHT'),
       (1, 'user_3', 'CLOTHES');

insert into point_accum_log_trash_record_link (point_acuum_id, trash_record_id)
values (1, 1),
       (2, 2),
       (3, 3);


-- 2. 행사 참여

insert into event_join (event_id, point, member_id)
values (1, 20, 'user_1'),
       (1, 100, 'user_2'),
       (1, 200, 'user_3');

-- 3. 사용자들의 포인트 기록 남기기 -> 모두 사용했다 가정
insert into member_point (point, member_id)
values (0, 'user_1'),
       (0, 'user_2'),
       (0, 'user_3');



-- 무한 쿼리 더미 데이터
DELIMITER $$

DROP PROCEDURE IF EXISTS generate_disaster_data$$

CREATE PROCEDURE generate_disaster_data()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE target_user VARCHAR(50) DEFAULT '본인_아이디';

    SET autocommit = 0;

    -- 1. [Event & EventJoin]
    -- 30,000개의 '서로 다른 이벤트'를 만들고, 거기에 다 참여시킵니다.
    -- 결과: List<EventRecord> 사이즈가 30,000이 됩니다.
    SET i = 1;
    WHILE i <= 30000 DO
            -- 이벤트 생성
            INSERT INTO event (gift_name, count, start_date, end_date, announcement_date)
            VALUES (CONCAT('Event_', i), 100, NOW(), NOW(), NOW());

            -- 방금 만든 이벤트 ID 가져오기 (AUTO_INCREMENT 가정)
            SET @last_event_id = LAST_INSERT_ID();

            -- 참여 기록 생성
INSERT INTO event_join (member_id, event_id, point)
VALUES (target_user, @last_event_id, 100);

SET i = i + 1;

            IF i % 1000 = 0 THEN COMMIT; END IF;
END WHILE;

    -- 2. [TrashRecord]
    -- 500,000건의 쓰레기 기록 생성
    -- 결과: List<TrashRecordResponse> 사이즈가 500,000이 됩니다. (이게 치명타)
    SET i = 1;
    WHILE i <= 500000 DO
            INSERT INTO trash_record (member_id, recycling_type, count)
            VALUES (target_user, 'BATTERY', 1);

            SET i = i + 1;

            IF i % 5000 = 0 THEN COMMIT; END IF;
END WHILE;

COMMIT;
SET autocommit = 1;
END$$

DELIMITER ;

-- 실행 (데이터가 많아 1~3분 정도 걸릴 수 있습니다)
CALL generate_disaster_data();
