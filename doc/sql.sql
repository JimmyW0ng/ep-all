# ep_system_role_authority
INSERT INTO `ep`.`ep_system_role_authority` (`role_id`, `menu_id`, `create_at`, `update_at`, `remark`, `del_flag`, `version`)
VALUES ('1', '2', now(), NULL, NULL, FALSE, '0');
INSERT INTO ep_system_role_authority (role_id, menu_id) SELECT
                                                          3,
                                                          m.id
                                                        FROM ep_system_menu AS m
                                                        WHERE m.target = 'admin';
INSERT INTO ep_system_role_authority (role_id, menu_id) SELECT
                                                          2,
                                                          m.id
                                                        FROM ep_system_menu AS m
                                                        WHERE m.target = 'backend';
ALTER TABLE `ep_organ_class_catalog`
ADD COLUMN `dura_type`  enum('minute','hour','day') NOT NULL DEFAULT 'minute' COMMENT '时长类型:分;时;天' AFTER `duration`;

ALTER TABLE `ep_organ_class_schedule`
ADD COLUMN `dura_type`  enum('minute','hour','day') NOT NULL DEFAULT 'minute' COMMENT '时长类型:分;时;天' AFTER `duration`;

# ep_wechat_openid
ALTER TABLE `ep_wechat_openid`
ADD COLUMN `create_at`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `mobile`,
ADD COLUMN `update_at`  datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `create_at`,
ADD COLUMN `remark`  varchar(255) NULL COMMENT '备注' AFTER `update_at`,
ADD COLUMN `del_flag`  bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记' AFTER `remark`,
ADD COLUMN `version`  bigint(20) NOT NULL DEFAULT 0 COMMENT '版本' AFTER `del_flag`;