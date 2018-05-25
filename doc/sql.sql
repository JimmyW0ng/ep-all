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

DELETE FROM `ep_system_menu` WHERE `ep_system_menu`.`id` in (90,91,92);
INSERT INTO `ep_system_menu` VALUES ('90', '1', 'admin', 'group', '微信公众号管理', '/wechatFwh/index', null, '8', 'enable', 'platform:wechatFwh:index', '2018-04-23 10:32:56', '2018-05-25 09:33:49', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('91', '90', 'admin', 'group', '发送客服消息', '/weixin/msgCustomSend', null, '1', 'enable', 'platform:weixin:msgCustomSend', '2018-04-23 10:34:02', '2018-04-28 15:56:37', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('92', '90', 'admin', 'group', '自定义菜单', '/wechatFwh/menu', null, '1', 'enable', 'platform:wechaFwht:menu', '2018-04-24 21:56:52', '2018-05-25 09:35:24', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('93', '1', 'admin', 'group', '微信支付管理', '/wechatPay/index', null, '8', 'enable', 'platform:wechatPay:index', '2018-05-08 14:42:05', null, '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('94', '93', 'admin', 'group', '查询订单', '/wechatPay/orderquery', null, '1', 'enable', 'platform:wechatPay:orderquery', '2018-05-08 14:44:07', null, '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('95', '93', 'admin', 'group', '查询退款', '/wechatPay/refundquery', null, '2', 'enable', 'platform:wechatPay:refundquery', '2018-05-08 16:06:05', null, '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('96', '1', 'admin', 'group', '资金管理', '/wechatPay/platform', null, '7', 'enable', 'platform:wechatPay:platform', '2018-05-08 17:47:20', '2018-05-24 16:16:49', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('97', '96', 'admin', 'group', '支付记录', '/wechatUnifiedOrder/unifiedorderIndex', null, '1', 'enable', 'platform:wechatUnifiedOrder:unifiedorderIndex', '2018-05-08 17:49:23', '2018-05-25 09:39:04', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('98', '3', 'backend', 'group', '支付管理', '/pay/merchantIndex', null, '6', 'enable', 'platform:pay:merchantIndex', '2018-05-10 17:35:18', '2018-05-14 17:47:41', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('99', '98', 'backend', 'group', '支付订单', '/wechat/unifiedorderMerchantIndex', null, '1', 'enable', 'platform:wechat:unifiedorderMerchantIndex', '2018-05-10 17:36:25', '2018-05-14 17:47:41', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('100', '65', 'backend', 'group', '退款管理', '/order/orderRefundIndex', null, '4', 'enable', 'merchant:order:orderRefundIndex', '2018-05-15 16:50:42', '2018-05-20 22:36:54', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('101', '65', 'backend', 'group', '提现管理', '/order/classWithdraw', null, '5', 'enable', 'merchant:order:classWithdraw', '2018-05-16 22:17:38', '2018-05-20 22:36:54', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('102', '3', 'backend', 'group', '资金管理', '/pay/index', null, '6', 'enable', 'platform:pay:index', '2018-05-18 23:08:55', null, '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('103', '102', 'backend', 'group', '提现记录', '/wechatPaywithdraw/merchantRecord', null, '2', 'enable', 'merchant:wechatPaywithdraw:merchantRecord', '2018-05-18 23:30:16', '2018-05-21 09:48:02', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('104', '96', 'admin', 'group', '提现管理', '/wechatPaywithdraw/platformIndex', null, '2', 'enable', 'platform:wechatPaywithdraw:platformIndex', '2018-05-19 23:29:15', '2018-05-23 16:11:44', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('105', '102', 'backend', 'group', '支付记录', '/wechatUnifiedOrder/unifiedorderMerchantIndex', null, '3', 'enable', 'merchant:wechatUnifiedOrder:unifiedorderMerchantIndex', '2018-05-20 22:30:24', '2018-05-25 09:58:08', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('106', '102', 'backend', 'group', '提现申请', '/wechatPaywithdraw/merchantIndex', null, '1', 'enable', 'merchant:wechatPaywithdraw:merchantIndex', '2018-05-20 22:36:07', '2018-05-23 16:08:30', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('107', '96', 'admin', 'group', '退款记录', '/orderRefund/platformRecord', null, '3', 'enable', 'platform:orderRefund:platformRecord', '2018-05-22 12:45:29', '2018-05-23 21:26:21', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('108', '102', 'backend', 'group', '退款记录', '/orderRefund/merchantRecord', null, '4', 'enable', 'merchant:orderRefund:merchantRecord', '2018-05-23 15:54:44', null, '', '\0', '1');
