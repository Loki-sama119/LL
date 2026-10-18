-- Run after database/demo.sql and database/transaction_permissions.sql.
-- MySQL 8.x. Existing get_user_by_id/insert_user are in stored_procedures.sql.
USE demo;

DROP PROCEDURE IF EXISTS get_all_users;
DROP PROCEDURE IF EXISTS update_user;
DROP PROCEDURE IF EXISTS delete_user;

DELIMITER $$
CREATE PROCEDURE get_all_users()
BEGIN
    SELECT id, name, email, country FROM users ORDER BY id ASC;
END$$

CREATE PROCEDURE update_user(
    IN p_id INT, IN p_name VARCHAR(120),
    IN p_email VARCHAR(220), IN p_country VARCHAR(120)
)
BEGIN
    UPDATE users SET name = p_name, email = p_email, country = p_country
    WHERE id = p_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE delete_user(IN p_id INT)
BEGIN
    DECLARE changed_rows INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    START TRANSACTION;
    -- Delete junction table rows before parent row to obey FK constraints.
    DELETE FROM user_permission WHERE user_id = p_id;
    DELETE FROM users WHERE id = p_id;
    SET changed_rows = ROW_COUNT();
    COMMIT;
    SELECT changed_rows AS affected_rows;
END$$
DELIMITER ;

-- Manual validation:
-- CALL get_all_users();
-- CALL update_user(1, 'Minh Updated', 'minh@codegym.vn', 'Viet Nam');
-- CALL delete_user(999999);
