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

INSERT INTO `ep_system_menu` VALUES ('1', '0', 'admin', 'group', 'EP', null, null, '0', 'enable', 'ep', '2018-01-20 18:39:21', '2018-01-30 17:24:43', null, '\0', '0');
INSERT INTO `ep_system_menu` VALUES ('2', '0', 'api', 'action', 'api基础接口', null, null, '0', 'enable', 'api:base', '2018-01-09 22:16:30', '2018-01-30 17:38:02', null, '\0', '0');
INSERT INTO `ep_system_menu` VALUES ('3', '0', 'backend', 'group', 'EP商户', null, null, '0', 'enable', 'ep', '2018-01-30 17:46:48', null, null, '\0', '0');
INSERT INTO `ep_system_menu` VALUES ('34', '1', 'admin', 'group', '平台系统', '/system/index', null, '1', 'enable', 'platform:system:index', '2018-01-20 15:33:22', '2018-03-23 13:58:43', '', '\0', '0');
INSERT INTO `ep_system_menu` VALUES ('35', '34', 'admin', 'group', '后台菜单', '/menu/index', null, '1', 'enable', 'platform:menu:index', '2018-01-20 15:33:22', '2018-03-19 11:50:56', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('38', '34', 'admin', 'group', '用户管理', '/user/index', null, '3', 'enable', 'platform:user:index', '2018-01-22 16:41:24', '2018-03-19 11:51:25', '', '\0', '0');
INSERT INTO `ep_system_menu` VALUES ('39', '34', 'admin', 'group', '角色管理', '/role/index', null, '2', 'enable', 'platform:role:index', '2018-01-23 20:54:00', '2018-03-19 11:51:18', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('47', '1', 'admin', 'group', '平台商家', '/index', null, '3', 'enable', 'platform:organ:index', '2018-01-29 10:52:40', '2018-03-23 13:57:17', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('48', '47', 'admin', 'group', '商家管理', '/systemOrgan/index', null, '0', 'enable', 'platform:organ:index', '2018-01-29 10:54:09', '2018-03-23 14:30:04', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('49', '1', 'admin', 'group', '平台产品', '/course/index', null, '4', 'enable', 'platform:course:index', '2018-02-05 18:03:15', '2018-03-23 13:57:37', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('50', '49', 'admin', 'group', '目录管理', '/catalog/index', null, '1', 'enable', 'platform:catalog:index', '2018-02-05 18:04:51', '2018-03-19 11:54:49', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('51', '1', 'admin', 'group', '商家教师', '/organAccount/index', null, '5', 'enable', 'platform:organAccount:index', '2018-02-06 09:39:22', '2018-03-23 13:58:01', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('52', '51', 'admin', 'group', '教师管理', '/organAccount/index', null, '0', 'enable', 'platform:organAccount:index', '2018-02-06 09:40:45', '2018-03-23 13:58:10', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('53', '34', 'admin', 'group', '商家菜单', '/menu/merchantIndex', null, '4', 'enable', 'platform:organMenu:index', '2018-02-06 10:29:33', '2018-03-19 11:51:43', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('54', '3', 'backend', 'group', '我的团队', '/organAccount/index', null, '1', 'enable', 'merchant:organAccount:merchantIndex', '2018-02-06 10:43:27', '2018-03-23 13:51:59', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('57', '54', 'backend', 'group', '团队管理', '/organAccount/index', null, '0', 'enable', 'merchant:organAccount:merchantIndex', '2018-02-06 11:17:53', '2018-03-23 13:52:11', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('58', '49', 'admin', 'group', '商家产品', '/organCourse/index', null, '2', 'enable', 'platform:organCourse:index', '2018-02-06 15:43:51', '2018-03-23 13:59:33', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('59', '1', 'admin', 'group', '平台会员', '/member/index', null, '6', 'enable', 'platform:member:index', '2018-02-06 17:53:06', '2018-03-23 13:58:21', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('60', '59', 'admin', 'group', '会员管理', '/member/index', null, '0', 'enable', 'platform:member:index', '2018-02-06 17:53:57', '2018-03-23 13:58:29', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('61', '3', 'backend', 'group', '我的产品', '/organCourse/merchantIndex', null, '2', 'enable', 'merchant:organCourse:merchantIndex', '2018-02-07 17:19:40', '2018-03-23 13:52:28', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('62', '61', 'backend', 'group', '产品管理', '/organCourse/merchantIndex', null, '1', 'enable', 'merchant:organCourse:merchantIndex', '2018-02-07 17:22:07', '2018-03-23 13:52:40', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('63', '3', 'backend', 'group', '我的称号', '/constantTag/merchantIndex', null, '3', 'enable', 'merchant:constantTag:merchantIndex', '2018-02-23 16:50:07', '2018-04-02 15:57:36', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('64', '63', 'backend', 'group', '称号管理', '/constantTag/merchantIndex', null, '0', 'enable', 'merchant:constantTag:merchantIndex', '2018-02-23 16:50:58', '2018-04-02 15:57:44', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('65', '3', 'backend', 'group', '我的订单', '/order/index', null, '4', 'enable', 'merchant:order:index', '2018-02-25 20:53:07', '2018-03-23 13:54:18', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('66', '65', 'backend', 'group', '订单管理', '/order/index', null, '0', 'enable', 'merchant:order:index', '2018-02-25 20:56:00', '2018-03-23 13:54:26', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('68', '61', 'backend', 'group', '随堂评价', '/childComment/index', null, '4', 'enable', 'merchant:childComment:index', '2018-02-27 15:31:37', '2018-03-29 15:18:24', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('70', '61', 'backend', 'group', '班次管理', '/organClass/index', null, '2', 'enable', 'merchant:organClass:index', '2018-03-02 09:29:21', '2018-03-23 13:53:32', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('72', '61', 'backend', 'group', '荣誉管理', '/childHonor/index', null, '5', 'enable', 'merchant:childHonor:index', '2018-03-06 23:00:43', '2018-04-01 12:07:05', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('73', '1', 'admin', 'group', '平台称号', '/constantTag/index', null, '2', 'enable', 'platform:constantTag:index', '2018-03-11 15:09:25', '2018-04-02 15:57:08', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('74', '73', 'admin', 'group', '称号管理', '/constantTag/constantIndex', null, '0', 'enable', 'platform:constantTag:constantIndex', '2018-03-11 15:10:18', '2018-04-02 15:57:17', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('75', '62', 'backend', 'group', '新增产品', '/organCourse/merchantCreate', null, '0', 'enable', 'merchant:organCourse:merchantCreate', '2018-03-15 14:13:03', '2018-03-18 17:40:26', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('76', '61', 'backend', 'group', '评价管理', '/classComment/index', null, '3', 'enable', 'merchant:classComment:index', '2018-03-18 00:45:37', '2018-04-01 12:08:06', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('79', '3', 'backend', 'group', '我的会员', '/organVip/index', null, '5', 'enable', 'merchant:organVip:index', '2018-03-19 23:53:30', '2018-03-23 13:54:35', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('80', '79', 'backend', 'group', '会员管理', '/organVip/index', null, '1', 'enable', 'merchant:organVip:index', '2018-03-19 23:54:13', '2018-03-23 13:54:45', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('81', '63', 'backend', 'group', '公用称号', '/constantTag/constantIndex', null, '0', 'enable', 'merchant:constantTag:constantIndex', '2018-03-27 09:53:28', '2018-04-02 15:57:53', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('82', '62', 'backend', 'group', '随堂管理', '/classSchedule/index', null, '0', 'enable', 'merchant:classSchedule:index', '2018-03-28 16:02:03', '2018-03-28 16:02:14', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('83', '61', 'backend', 'group', '随堂管理', '/classSchedule/index', null, '4', 'enable', 'merchant:classSchedule:index', '2018-03-28 16:03:21', '2018-03-28 16:03:53', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('84', '65', 'backend', 'group', '预约管理', '/order/bespeakIndex', null, '0', 'enable', 'merchant:bespeak:index', '2018-03-29 10:44:28', '2018-03-29 10:57:03', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('85', '65', 'backend', 'group', '学员统计', '/order/childIndex', null, '0', 'enable', 'merchant:order:childIndex', '2018-04-01 12:16:18', '2018-04-01 12:19:40', '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('86', '3', 'backend', 'group', '我的微信公众号', '/auth/weixin/index', null, '6', 'enable', 'merchant:weixin:index', '2018-04-23 10:01:50', '2018-04-23 10:05:43', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('87', '86', 'backend', 'group', '发送客服消息', '/auth/weixin/msgCustomSend', null, '1', 'enable', 'merchant:weixin:msgCustomSend', '2018-04-23 10:03:17', '2018-04-23 10:05:43', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('88', '3', 'backend', 'group', '微信公众号管理', '/auth/weixin/index', null, '7', 'enable', 'platform:weixin:index', '2018-04-23 10:10:50', '2018-04-23 10:13:42', '', '', '1');
INSERT INTO `ep_system_menu` VALUES ('89', '88', 'backend', 'group', '发送客服消息', '/auth/weixin/msgCustomSend', null, '1', 'enable', 'platform:weixin:msgCustomSend', '2018-04-23 10:13:22', '2018-04-23 10:13:42', '', '', '1');
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
INSERT INTO `ep_system_menu` VALUES ('109', '1', 'admin', 'group', '异常管理', '/exception/platformIndex', null, '9', 'enable', 'platform:exception:platformIndex', '2018-05-27 16:19:52', null, '', '\0', '1');
INSERT INTO `ep_system_menu` VALUES ('110', '109', 'admin', 'group', '资金异常', '/exception/wechatPayIndex', null, '1', 'enable', 'platform:exception:wechatPayIndex', '2018-05-27 16:21:11', null, '', '\0', '1');
