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
