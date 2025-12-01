-- 테이블이 이미 존재한다고 가정합니다 (ddl-auto가 create인 경우 생략 가능)
-- CREATE TABLE IF NOT EXISTS event_join (...);

DELIMITER $$

DROP PROCEDURE IF EXISTS insert_dummy_event_join$$

CREATE PROCEDURE insert_dummy_event_join()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 500 DO
        INSERT INTO event_join (member_id, point, event_id)
        VALUES (
            CONCAT('user_', i),              -- memberId: user_1, user_2 ...
            FLOOR(50 + (RAND() * 451)),      -- point: 50 ~ 500 사이 랜덤 (500-50+1 = 451)
            1                                -- eventId: 1로 고정
        );
        SET i = i + 1;
END WHILE;
END$$

DELIMITER ;

CALL insert_dummy_event_join();
DROP PROCEDURE insert_dummy_event_join;