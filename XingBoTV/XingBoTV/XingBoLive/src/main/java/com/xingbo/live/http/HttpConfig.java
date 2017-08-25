package com.xingbo.live.http;

public class HttpConfig {
    private final static String API_VERSION_KEY = "1";
    public final static String SERVER = "http://www.xingbo.tv";//发布版业务服务器
    public final static String FILE_SERVER="http://store.xingbo.tv/";// 最新 发布版文件服务器
    public final static String FILE_UPLOAD_API = "http://www.xingbo.tv/upload/index.php";//发布版上传接口
//    public final static String SERVER = "http://demo.starpool.cn";//开发版业务服务器
//    public final static String FILE_SERVER = "http://demo.starpool.cn/upload/";//开发版文件服务器
//    public final static String FILE_UPLOAD_API = "http://demo.starpool.cn/upload/index.php";//开发版上传接口
    @Deprecated
    public final static String API_USER_GET_CONFIG = "user/getConfig";
    /**
     * 获取大厅数据/app/1/getHome
     */
    public final static String API_GET_HOME = "/app/" + API_VERSION_KEY + "/getHome";
    /**
     * 获取历史记录/app/1/getHistoryAnchors?page=0&pagesize=10
     */
    public final static String API_GET_HISTORY = "/app/" + API_VERSION_KEY + "/getHistoryAnchors";
    /**
     * 清除历史记录/app/1/clearHistory
     */
    public final static String API_CLEAR_HISTORY = "/app/" + API_VERSION_KEY + "/clearHistory";
    /**
     * 搜索主播/app/1/searchAnchors?page=1&pagesize=10&skey=women
     */
    public final static String API_APP_SEARCH_ANCHOR = "/app/" + API_VERSION_KEY + "/searchAnchors";

    /**
     * 获取主播分类/app/1/getClassAnchors?classid=0&page=0&pagesize=10
     */
    public final static String API_GET_ANCHORS_BY_TYPE = "/app/" + API_VERSION_KEY + "/getClassAnchors";

    /**
     * 获取富豪榜数据app/1/getRichRank
     */
    public final static String API_APP_GET_RICH_RANK = "/app/" + API_VERSION_KEY + "/getRichRank";

    /**
     * 获取主播榜数据/app/1/getAnchorRank
     */
    public final static String API_APP_GET_ANCHOR_RANK = "/app/" + API_VERSION_KEY + "/getAnchorRank";

    /**
     * 获取礼物榜数据/app/1/getGiftRank
     */
    public final static String API_APP_GET_GIFT_RANK = "/app/" + API_VERSION_KEY + "/getGiftRank";

    //用户注册
    public final static String API_USER_REGISTER = "/app/" + API_VERSION_KEY + "/register";

    //手机找回密码验证/app/1/findPasswordByPhone1?authcode=1234
    public final static String API_USER_RESET_PWD = "/app/" + API_VERSION_KEY + "/findPasswordByPhone1";

    //设置新密码/app/1/findPasswordByPhone2?password1=654321&password2=654321
    public final static String API_USER_UPDATE_PWD = "/app/" + API_VERSION_KEY + "/savePassword";

    //短信验证手机
    public final static String API_USER_CHECK_PHONE = "/app/" + API_VERSION_KEY + "/phoneSms";

    //用户登录
    public final static String API_USER_LOGIN = "/app/" + API_VERSION_KEY + "/login";

    /**
     * 获取用户中心信息/app/1/getProfile
     */
    public final static String API_USER_GET_PROFILE = "/app/" + API_VERSION_KEY + "/getProfile";
    /**
     * 获取用户信息/app/1/getUserInfo?uid=10055
     */
    public final static String API_USER_GET_USER_INFO = "/app/" + API_VERSION_KEY + "/getUserInfo";
    /**
     * 修改用户信息/app/1/syncUser?nick=xx&avatar=xx&sex=xx&addr=xx&birth=xx&intro=xx
     */
    public final static String API_UPDATE_USER_INFO = "/app/" + API_VERSION_KEY + "/syncUser";

    /**
     * 获取用户的粉丝列表/app/1/getUserFollowers?uid=10055
     */
    public final static String API_USER_GET_USER_FANS = "/app/" + API_VERSION_KEY + "/getUserFollowers";
    /**
     * 获取用户的关注列表/app/1/getUserFollowings?uid=10055
     */
    public final static String API_USER_GET_USER_FAVORITE = "/app/" + API_VERSION_KEY + "/getUserFollowings";
    /**
     * 获取私信列表信息
     */
    public final static String API_USER_GET_PRIVATE_MSG = "/app/" + API_VERSION_KEY + "/getSessions";
    /**
     * 获取私信未读数
     */
    public final static String API_USER_GET_UNREAD_COUNT = "/app/" + API_VERSION_KEY + "/getUserMessageCount";
    /**
     * 忽略所有未读私信消息
     */
    public final static String API_USER_GET_CANCEL_UNREAD_COUNT = "/app/" + API_VERSION_KEY + "/readAllSessions";
    /**
     * //获取系统消息列表
     */
    public final static String API_USER_GET_SYSTEM_MSG = "/app/" + API_VERSION_KEY + "/getSystemMessages";
    /**
     * 获取私信详情数据
     */
    public final static String API_USER_GET_PRIVITE_MSG_DETAIL = "/app/" + API_VERSION_KEY + "/getMessages";
    /**
     * 忽略所有消息
     */
    public final static String API_USER_IGNORE_ALL_MSG = "/app/" + API_VERSION_KEY + "/readAllSessions";
    /**
     * 获取用户相册列表
     */
    public final static String API_USER_GET_PHOTOS = "/app/" + API_VERSION_KEY + "/getMyAppPhotos";
    /**
     * 获取某用户的相册列表
     */
    public final static String API_USER_GET_ONE_PHOTOS = "/app/" + API_VERSION_KEY + "/getOneUserAppPhotos";
    /**
     * 对某用户发送私信
     */
    public final static String API_USER_GET_SENDMESSAGE = "/app/" + API_VERSION_KEY + "/sendMessage";
    /**
     * 添加多张照片到服务器
     */
    public final static String API_USER_GET_ADDPHOTOS = "/app/" + API_VERSION_KEY + "/addMyPhotos";
    /**
     * 删除照片
     */
    public final static String API_USER_GET_DELETEPHOTOS = "/app/" + API_VERSION_KEY + "/delMyPhotos";

    /**
     * 用户绑定手机
     */
    public final static String API_USER_GET_PHONE_BINDING = "/app/" + API_VERSION_KEY + "/userBindPhone";
    /**
     * 用户取消绑定
     */
    public final static String API_USER_GET_PHONE_CANCELBIND = "/app/" + API_VERSION_KEY + "/syncUser";

    /**
     * 搜索主播
     */
    public final static String API_USER_GET_SEARCH_ANCHORS = "/app/" + API_VERSION_KEY + "/searchAnchors";

    //请求进入直播房间
    public final static String API_ENTER_ROOM = "/app/" + API_VERSION_KEY + "/enterroom";

    //取得房间礼物面板初始化数据
    public final static String API_GET_GIFTS = "/app/" + API_VERSION_KEY + "/getGifts";

    /**
     * 发送公聊消息/app/1/sendPubmsg?livename=xx&rid=xx&uid=xx&msg=xx
     */
    public final static String API_SEND_COMMON_MSG = "/app/" + API_VERSION_KEY + "/sendPubmsg";

    /**
     *发送私聊消息 /app/1/sendPrimsg?livename=xx&rid=xx&uid=xx&msg=xx
     */
//    public final static String API_SEND_PRIVATE_MSG="/app/"+API_VERSION_KEY+"/sendPrimsg";

    /**
     * 获取私聊消息未读数量 /app/1/getUserMessageCount
     */
    public final static String API_GET_MESSAGE_COUNT = "/app/" + API_VERSION_KEY + "/getUserMessageCount";
    /**
     * 发送私信消息 /app/1/sendMessage?livename=xx&rid=xx&uid=xx&msg=xx
     */
    public final static String API_SEND_PRIVATE_MSG = "/app/" + API_VERSION_KEY + "/sendMessage";
    /**
     * 手机停播 /app/1/stopPublish
     */
    public final static String API_LIVE_CLOSE = "/app/" + API_VERSION_KEY + "/stopPublish";
    /**
     * 获取在线人数app/1/getOnlines?livename=xx&rid=xx
     */
    public final static String API_APP_GET_ROOM_ONLINE = "/app/" + API_VERSION_KEY + "/getOnlines";

    /**
     * 获得房间用户信息/app/1/getRoomUserInfo?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_GET_ROOM_USER_INFO = "/app/" + API_VERSION_KEY + "/getRoomUserInfo";

    /**
     * 获得房间可点歌曲列表/app/1/getSongs?livename=xx&rid=xx
     */
    public final static String API_APP_GET_ROOM_ANCHOR_SONGS = "/app/" + API_VERSION_KEY + "/getSongs";

    /**
     * 确定点歌/app/1/sendSong?livename=xx&rid=xx&songid=xx
     */
    public final static String API_APP_BOOK_SONG = "/app/" + API_VERSION_KEY + "/sendSong";
    /**
     * 自选歌曲确定点歌/app/1/sendSongEx?rid=10033&uid=10033&name=甜蜜蜜
     */
    public final static String API_APP_BOOK_SONG_ = "/app/" + API_VERSION_KEY + "/sendSongEx";
    /**
     * 获取已点歌曲列表/app/1/getSongOrders?livename=xx&rid=xx
     */
    public final static String API_APP_GET_ROOM_BOOKED_SONGS = "/app/" + API_VERSION_KEY + "/getSongOrders";

    /**
     * 发送飞屏信息/app/1/sendFlyword?livename=xx&rid=xx&word=xx
     */
    public final static String API_APP_SEND_FLY_WORD = "/app/" + API_VERSION_KEY + "/sendFlyword";

    /**
     * 发送广播信息/app/1/sendLaba?livename=xx&rid=xx&word=xx
     */
    public final static String API_APP_SEND_BROAD_CAST = "/app/" + API_VERSION_KEY + "/sendLaba";
    /**
     * 送礼物/app/1/sendGift?livename=xx&rid=xx&uid=xx&gid=xx&num=xx&usebag=xx
     */
    public final static String API_APP_SEND_GIFT = "/app/" + API_VERSION_KEY + "/sendGift";
    /**
     * 获取心跳鲜花/app/1/heartBeat?livename=xx&rid=xx
     */
    public final static String API_APP_HEART_FLOWER = "/app/" + API_VERSION_KEY + "/heartBeat";

    /**
     * 发送鲜花/app/1/sendFlower?livename=xx&rid=xx&num=xx
     */
    public final static String API_APP_SEND_FLOWER = "/app/" + API_VERSION_KEY + "/sendFlower";

    /**
     * 举报完家/app/1/reportUser?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_REPORT_USER = "/app/" + API_VERSION_KEY + "/reportUser";

    /**
     * 关注用户/app/1/addFollow?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_ADD_FOLLOW = "/app/" + API_VERSION_KEY + "/addFollow";

    /**
     * 取消关注用户/app/1/cancelFollow?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_CANCEL_FOLLOW = "/app/" + API_VERSION_KEY + "/cancelFollow";

    /**
     * 设置用户为管理员/app/1/addMute?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_ADD_ADMIN = "/app/" + API_VERSION_KEY + "/addAdmin";

    /**
     * 取消用户为管理员/app/1/cancelAdmin?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_CANCEL_ADMIN = "/app/" + API_VERSION_KEY + "/cancelAdmin";

    /**
     * 禁言用户/app/1/addMute?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_ADD_MUTE = "/app/" + API_VERSION_KEY + "/addMute";

    /**
     * 取消禁言用户/app/1/cancelMute?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_CANCEL_MUTE = "/app/" + API_VERSION_KEY + "/cancelMute";

    /**
     * 踢人/app/1/addKick?livename=xx&rid=xx&uid=xx
     */
    public final static String API_APP_ADD_KICK = "/app/" + API_VERSION_KEY + "/addKick";

    /**
     * 获取粉丝榜数据//app/1/getFansRank?livename=xx&rid=xx&type=live/day/week/month
     */
    public final static String API_APP_GET_USER_FANS_LIST = "/app/" + API_VERSION_KEY + "/getOneUserFansRank";

    /**
     * 获取守护列表/app/1/getGuards?livename=xx&rid=xx
     */
    public final static String API_APP_GET_GUARD_LIST = "/app/" + API_VERSION_KEY + "/getGuards";
    /**
     * 获取守护信息 /app/1/getGuardInfo?rid=10033&uid=10033
     */
    public final static String API_APP_GET_GUARD_INFO = "/app/" + API_VERSION_KEY + "/getGuardInfo";
    /**
     * 开通守护/app/1/sendGuard?rid=10033&uid=10033&month=1
     */
    public final static String API_APP_SEND_GUARD = "/app/" + API_VERSION_KEY + "/sendGuard";
    /**
     * 获取商品列表app/1/getPayGoods?type=alipay|weixin
     */
    public final static String API_APP_GET_PRODUCT_LIST = "/app/" + API_VERSION_KEY + "/getPayGoods";

    /**
     * 生成支付订单信息/app/1/alipaymobile?goodid=alipay_1
     */
    public final static String API_APP_GET_PAY_ORDER = "/app/" + API_VERSION_KEY + "/alipaymobile";

    /**
     * 生成微信支付订单信息/app/1/wxpaymobile?goodid=weixin_1
     */
    public final static String API_APP_GET_WX_PAY_ORDER = "/app/" + API_VERSION_KEY + "/wxpaymobile";
    /**
     * 绑定友盟用户设备信息
     */
    public final static String API_APP_GET_UMENG_DEVICE_BIND = "/app/" + API_VERSION_KEY + "/bindUserDeviceToken";
    /**
     * 意见反馈/app/1/reguest?word=haha
     */
    public final static String API_APP_USER_FEED_BACK = "/app/" + API_VERSION_KEY + "/reguest";
    /**
     * 获取发现活动页面/app/1/getActivitys
     */
    public final static String API_APP_DISC_WEB_ACT = "/app/" + API_VERSION_KEY + "/getActivitys";
    /**
     * 获取发现商城页面/app/1/getShop
     */
    public final static String API_APP_DISC_WEB_SHOP = "/app/" + API_VERSION_KEY + "/getShop";
    /**
     * 获取发现节目预告页面/app/1/getTrailers
     */
    public final static String API_APP_DISC_WEB_TRA = "/app/" + API_VERSION_KEY + "/getTrailers";
    /**
     * 获取发现关于我们页面/app/1/aboutUs
     */
    public final static String API_APP_ABOUT = "/app/" + API_VERSION_KEY + "/aboutUs";


    /**
     * 新加的接口
     * Created by zhubing on 2016/5/5.
     */


    /**
     * 获取红包个数的接口/app/1/redpacket
     * / redpacket /getrednum/?uid=1011318&rid=50061
     */
    public final static String API_APP_REDP_NUM = "/redpacket/getrednum";
    /**
     * 获取红包剩余时间
     * /redpacket/getredlift/?uid=1011318&rid=50061
     */
    public final static String API_APP_REDP_LIFT = "/redpacket/getredlift";
    /**
     * 获取房间人获得红包分配接口（58秒(即剩余两秒时)请求接口）
     * 用于后台统计人数 分配
     * / redpacket / callsaveonline/?uid=1011318&rid=50061
     */
    public final static String API_APP_REDP_ONLINE = "/redpacket/callsaveonline";

    /**
     * 每个人活动的红包接口（60-65秒用接口）抽取红包时调用
     * / redpacket/savebonus/?uid=1011318&rid=50061&fid=1011318
     */
    public final static String API_APP_REDP_BONUS = "/redpacket/savebonus";

    /**
     * 红包领取完 时 请求接口 看是都还有红包
     * /redpacket/ getroomredpacketinfo/?uid=1011318&rid=50061
     */
    public final static String API_APP_REDP_RPINFO = "/redpacket/getroomredpacketinfo";

    /**
     * 判断是否登陆
     * /app/1/clickPacket
     */
    public final static String API_APP_CLICK_PACKET = "/app/" + API_VERSION_KEY + "/clickPacket";

    /**
     * 关注用户
     * http://www.xingbo.tv/app/1/getMyFollowLiveAnchors
     * getMyFollowLiveAnchors
     */
    public final static String API_APP_MYFOLLOW = "/app/" + API_VERSION_KEY + "/getMyFollowLiveAnchors";

    /**
     * 首页 热门
     * http://www.xingbo.tv/app/1/getHotRecommendAnchors
     * getHotRecommendAnchors
     */
    public final static String API_APP_HOT_R_ANCHOR = "/app/" + API_VERSION_KEY + "/getHotRecommendAnchors";
    /**
     * 首页手机直播
     */
    public final static String API_USER_GET_PHONE_R_ANCHOR = "/app/" + API_VERSION_KEY + "/getAppPublishAnchor";

    /**
     * 忘记密码 新增
     * http://www.xingbo.tv/app/1/appResetPassword2
     * appResetPassword2
     */
    public final static String API_APP_RESET_PASSWORD2 = "/app/" + API_VERSION_KEY + "/appResetPassword2";

    /**
     * 获取我的收入统计记录
     * http://www.xingbo.tv/app/1/getMyGainSum
     * getMyGainSum
     */
    public final static String API_APP_GET_USER_INCOME = "/app/" + API_VERSION_KEY + "/getMyGainSum";

    /**
     * 获取我的每天收入统计日志记录
     * http://www.xingbo.tv/app/1/getMyGainPerdayLog
     * getMyGainPerdayLog
     */
    public final static String API_APP_GET_USER_INCOME_LIST = "/app/" + API_VERSION_KEY + "/getMyGainPerdayLog";

    /**
     * 获取我的充值记录
     * http://www.xingbo.tv/app/1/getMyPaylog
     * getMyPaylog
     */
    public final static String API_APP_GET_USER_PAY_LOG_LIST = "/app/" + API_VERSION_KEY + "/getMyPaylog";

    /**
     * 获取我的送礼记录
     * http://www.xingbo.tv/app/1/getMySendLog
     * getMySendLog
     */
    public final static String API_APP_GET_USER_SEND_LOG_LIST = "/app/" + API_VERSION_KEY + "/getMySendLog";
    /**
     * 自动更新版本
     */
    public final static String API_APP_GET_AUTODATE_INSTALLPACKAGE = "/app/" + API_VERSION_KEY + "/getInstallPackage";

    /**
     * 删除与某用户的会话
     */
    public final static String API_APP_GET_USER_DELETE_USER_SESSION = "/app/" + API_VERSION_KEY + "/deleteUserSession";
    /**
     * 通过渠道名称获取渠道ID
     */
    public final  static  String API_APP_USER_GET_CHANNEL_ID="/app/"+API_VERSION_KEY+"/searchChannelByAppName";
    //开播,获取直播地址
    public final static String API_APP_GET_LIVE_URL = "/app/" + API_VERSION_KEY + "/publishLive";
    //开播封面信息
    public final static String API_APP_GET_LIVE_COVER_MSG = "/app/" + API_VERSION_KEY + "/goPublish";
    //申请成为主播
    public final static String API_APP_APPLY_TOBE_ANCHOR = "/app/" + API_VERSION_KEY + "/applyAnchorCommit";
    //开播心跳包
    public final static String API_APP_BEAT_HEART = "/app/" + API_VERSION_KEY + "/updatePublishHeartbeat";
    //官方、巡管禁播功能
    public final static String API_APP_CLOSE_PUSH_LIVE = "/app/" + API_VERSION_KEY + "/closeOnePushLive";
}
