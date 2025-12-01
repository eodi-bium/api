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


-- 1. 사용자들 포인트 참여

insert into point_accum_log (created_at, point, member_id)
values(now(),20,'user_1'),
      (now(),100,'user_2'),
      (now(),200,'user_3');

insert into trash_record (count, member_id, recycling_type)
values(1,'user_1','BATTERY'),
      (1,'user_2','LIGHT'),
      (1,'user_3','CLOTHES');

insert into point_accum_log_trash_record_link (point_acuum_id, trash_record_id)
values(1,1),(2,2),(3,3);


-- 2. 행사 참여

insert into event_join (event_id, point, member_id)
values(1,20,'user_1'),
      (1,100,'user_2'),
      (1,200,'user_3');

-- 3. 사용자들의 포인트 기록 남기기 -> 모두 사용했다 가정
insert into member_point (point, member_id)
values(0,'user_1'),
      (0,'user_2'),
      (0,'user_3');