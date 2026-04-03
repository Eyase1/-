-- 日程事件表
CREATE TABLE IF NOT EXISTS t_calendar_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日程ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '日程标题',
    description TEXT COMMENT '日程描述',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    location VARCHAR(200) COMMENT '地点',
    reminder_time DATETIME COMMENT '提醒时间',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING(待办)、IN_PROGRESS(进行中)、COMPLETED(已完成)、CANCELLED(已取消)',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_start_time (start_time),
    INDEX idx_status (status),
    INDEX idx_reminder_time (reminder_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日程事件表';


