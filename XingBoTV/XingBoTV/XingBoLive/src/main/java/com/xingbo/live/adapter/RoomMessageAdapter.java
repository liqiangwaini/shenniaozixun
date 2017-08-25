package com.xingbo.live.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.emotion.EmotionEntity;
import com.xingbo.live.emotion.Emotions;
import com.xingbo.live.entity.Join;
import com.xingbo.live.entity.RoomMessage;
import com.xingbo.live.entity.SendGift;
import com.xingbo.live.entity.msg.BaseMsg;
import com.xingbo.live.entity.msg.CommonMsg;
import com.xingbo.live.entity.msg.CommonMsgBody;
import com.xingbo.live.entity.msg.GiftMsg;
import com.xingbo.live.entity.msg.JoinMsg;
import com.xingbo.live.entity.msg.LuckyGiftRetAwardMsg;
import com.xingbo.live.entity.msg.SystemMsg;
import com.xingbo.live.entity.msg.SystemTypeMsg;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.DpOrSp2PxUtil;
import com.xingbo.live.util.DrawableUtils;
import com.xingbo.live.util.HtmlUtils;
import com.xingbo.live.util.MsgHelper;
import com.xingbo.live.view.CenteredImageSpan;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.view.FrescoImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by xingbo_szd on 2016/7/11.
 */
public class RoomMessageAdapter extends BaseAdapter {
    private Context context;
    private List<RoomMessage> mList=new ArrayList<>();

    public RoomMessageAdapter(Context context, List<RoomMessage> msgList) {
        this.context = context;
        mList.clear();
        mList.addAll(msgList);
    }

    public void setData(List<RoomMessage> msgList) {
        mList.clear();
        mList.addAll(msgList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RoomMessage roomMessage = mList.get(i);
        switch (roomMessage.getType()) {
            case RoomMessage.COMMON_MSG:  //公聊
                MessageViewHolder viewHolder;
                if (view != null && view.getTag() != null && view.getTag() instanceof MessageViewHolder) {
                    viewHolder = (MessageViewHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_room_message, null);
                    viewHolder = new MessageViewHolder(view);
                    view.setTag(viewHolder);
                }
                if (roomMessage != null && roomMessage.getCommonMsg() != null && roomMessage.getCommonMsg().getData() != null && roomMessage.getCommonMsg().getData().getFuser() != null && roomMessage.getCommonMsg().getData().getMsg() != null && roomMessage.getCommonMsg().getData().getFuser().getNick() != null) {
                    Drawable drawableLeft = new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), XingBoConfig.RICH_LV_ICONS[Integer.parseInt(roomMessage.getCommonMsg().getData().getFuser().getRichlvl())]));
                    drawableLeft.setBounds(0, 0, drawableLeft.getIntrinsicWidth(), (int) (drawableLeft.getMinimumHeight()));
                    if (roomMessage.getCommonMsg().getData().getTuser() != null) {
                        viewHolder.message.setText(addSmileySpans(drawableLeft, "  " + roomMessage.getCommonMsg().getData().getFuser().getNick() + "  ", "@" + roomMessage.getCommonMsg().getData().getTuser().getNick() + "  " + roomMessage.getCommonMsg().getData().getMsg()));
                    } else {
                        viewHolder.message.setText(addSmileySpans(drawableLeft, "  " + roomMessage.getCommonMsg().getData().getFuser().getNick() + "  ", roomMessage.getCommonMsg().getData().getMsg()));
                    }
                }
                break;
            case RoomMessage.SEND_GIFT:  //送礼
                GiftHolder giftHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof GiftHolder) {
                    giftHolder = (GiftHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_gift, null);
                    giftHolder = new GiftHolder();
                    giftHolder.msg = (TextView) view.findViewById(R.id.item_main_room_sendgift_msg);
                    view.setTag(giftHolder);
                }
                final GiftHolder giftHolders = giftHolder;
                final SendGift giftMsg = roomMessage.getGiftMsg().getData();
                final Drawable drawableLvl = new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), XingBoConfig.RICH_LV_ICONS[Integer.parseInt(giftMsg.getFuser().getRichlvl())]));
                drawableLvl.setBounds(0, 0, drawableLvl.getIntrinsicWidth(), (int) (drawableLvl.getMinimumHeight()));
                Log.e("IMAGE", HttpConfig.FILE_SERVER + giftMsg.getGift().getIcon());
//                new AsyncTask<String, Void, Bitmap>() {
//                    @Override
//                    protected Bitmap doInBackground(String... params) {
//                        return DrawableUtils.getBitMBitmap(params[0]);
//                    }
//
//                    protected void onPostExecute(Bitmap result) {
//                        giftHolders.msg.setText(showSendGift(drawableLvl, "  " + giftMsg.getFuser().getNick() + "   ",
// "送给主播" + giftMsg.getNum() + "个 " + giftMsg.getGift().getName() + "  ", new BitmapDrawable(context.getResources(), result)));
////                        result.recycle();
//                    }
//                }.execute(HttpConfig.FILE_SERVER + giftMsg.getGift().getIcon());
                giftHolders.msg.setText(showSendGift(drawableLvl, "  " + giftMsg.getFuser().getNick() + "   ",
                        "送了" + giftMsg.getNum() + "个 " + giftMsg.getGift().getName() + "  "));
                break;
            case RoomMessage.JOIN_ROOM:  //进入房间

            case RoomMessage.LEAVE_ROOM:  //离开房间
                JoinHolder joinHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof JoinHolder) {
                    joinHolder = (JoinHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_join, null);
                    joinHolder = new JoinHolder();
                    joinHolder.nick = (TextView) view.findViewById(R.id.item_main_room_join_nick);
                    view.setTag(joinHolder);
                }
                if (roomMessage != null && roomMessage.getJoinMsg() != null && roomMessage.getJoinMsg().getData() != null && roomMessage.getJoinMsg().getData().getFuser() != null && roomMessage.getJoinMsg().getData().getFuser().getNick() != null) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), XingBoConfig.RICH_LV_ICONS[Integer.parseInt(roomMessage.getJoinMsg().getData().getFuser().getRichlvl())]));
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), (int) drawable.getMinimumHeight());
                    String text = "";
                    if (roomMessage.getType() == RoomMessage.LEAVE_ROOM) {
                        text = "   离开了房间";
                    } else {
                        text = "   进入房间";
                    }
                    joinHolder.nick.setText(joinRoom(drawable, roomMessage.getJoinMsg().getData().getFuser().getNick(), text));
                }
                break;
            case RoomMessage.ADD_MUTE:  //被管理员禁言
            case RoomMessage.CANCEL_MUTE:   //取消禁言
            case RoomMessage.ADD_ADMIN:  //设置为管理员
            case RoomMessage.CENCEL_ADMIN:  //取消管理员
            case RoomMessage.KICK_OUT:  //被踢出房间
                SystemHolder systemHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof SystemHolder) {
                    systemHolder = (SystemHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_system, null);
                    systemHolder = new SystemHolder();
                    systemHolder.type = (TextView) view.findViewById(R.id.item_main_room_system_type);
                    view.setTag(systemHolder);
                }
                String type = "";
                if (roomMessage.getType() == RoomMessage.ADD_MUTE) {
                    type = "   被管理员禁言";
                } else if (roomMessage.getType() == RoomMessage.CANCEL_MUTE) {
                    type = "   被管理员取消禁言";
                } else if (roomMessage.getType() == RoomMessage.ADD_ADMIN) {
                    type = "   成为了房间管理员";
                } else if (roomMessage.getType() == RoomMessage.CENCEL_ADMIN) {
                    type = "   被取消了房间管理身份";
                } else if (roomMessage.getType() == RoomMessage.KICK_OUT) {
                    type = "   被管理员踢出了房间";
                }
                if (roomMessage != null && roomMessage.getMuteMsg() != null && roomMessage.getMuteMsg().getData() != null && roomMessage.getMuteMsg().getData().getTuser() != null && roomMessage.getMuteMsg().getData().getTuser().getNick() != null) {
                    systemHolder.type.setText(manageMessage(roomMessage.getMuteMsg().getData().getTuser().getNick() + type));
                }
                break;
            case RoomMessage.ANCHOR_LVL_UP:  //主播升级
                AnchorLvlHolder anchorLvlHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof AnchorLvlHolder) {
                    anchorLvlHolder = (AnchorLvlHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_system, null);
                    anchorLvlHolder = new AnchorLvlHolder();
                    anchorLvlHolder.type = (TextView) view.findViewById(R.id.item_main_room_system_type);
                    view.setTag(anchorLvlHolder);
                }
                if (roomMessage != null && roomMessage.getAnchorLvUpMsg() != null && roomMessage.getAnchorLvUpMsg().getData() != null && roomMessage.getAnchorLvUpMsg().getData().getFuser() != null &&
                        roomMessage.getAnchorLvUpMsg().getData().getFuser().getNick() != null) {
                    anchorLvlHolder.type.setText(lvlChange("恭喜 " + roomMessage.getAnchorLvUpMsg().getData().getFuser().getNick() + "升到 " + roomMessage.getAnchorLvUpMsg().getData().getLvl() + "级"));
                }
                break;
            case RoomMessage.USER_LVL_UP:  //用户升级
                UserLvlHolder userLvlHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof UserLvlHolder) {
                    userLvlHolder = (UserLvlHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_system, null);
                    userLvlHolder = new UserLvlHolder();
                    userLvlHolder.type = (TextView) view.findViewById(R.id.item_main_room_system_type);
                    view.setTag(userLvlHolder);
                }
                if (roomMessage != null && roomMessage.getUserRichUpMsg() != null && roomMessage.getUserRichUpMsg().getData() != null && roomMessage.getUserRichUpMsg().getData().getFuser() != null &&
                        roomMessage.getUserRichUpMsg().getData().getFuser().getNick() != null) {
                    userLvlHolder.type.setText(lvlChange("恭喜 " + roomMessage.getUserRichUpMsg().getData().getFuser().getNick() + " 升到 " + roomMessage.getUserRichUpMsg().getData().getLvl() + "级!"));
                }
                break;
            case RoomMessage.OPEN_GUARD:
                OpenGuardHolder openGuardHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof OpenGuardHolder) {
                    openGuardHolder = (OpenGuardHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_system, null);
                    openGuardHolder = new OpenGuardHolder();
                    openGuardHolder.type = (TextView) view.findViewById(R.id.item_main_room_system_type);
                    view.setTag(openGuardHolder);
                }
                if (roomMessage != null && roomMessage.getGuardMsg() != null && roomMessage.getGuardMsg().getData() != null && roomMessage.getGuardMsg().getData().getFuser() != null &&
                        roomMessage.getGuardMsg().getData().getFuser().getNick() != null) {
                    openGuardHolder.type.setText(lvlChange("恭喜 " + roomMessage.getGuardMsg().getData().getFuser().getNick() + " 开通了守护 " + roomMessage.getGuardMsg().getData().getLvl() + "级!"));
                }
                break;
            case RoomMessage.ANCHOR_LVL_CHANGE:  //主播经验值变化
                AnchorLvlChangeHolder anchorLvlChangeHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof AnchorLvlChangeHolder) {
                    anchorLvlChangeHolder = (AnchorLvlChangeHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_system, null);
                    anchorLvlChangeHolder = new AnchorLvlChangeHolder();
                    anchorLvlChangeHolder.type = (TextView) view.findViewById(R.id.item_main_room_system_type);
                    view.setTag(anchorLvlChangeHolder);
                }
                if (roomMessage != null && roomMessage.getLvMsg() != null && roomMessage.getLvMsg().getData() != null && roomMessage.getLvMsg().getData().getLvl() != null) {
                    anchorLvlChangeHolder.type.setText(lvlChange("恭喜主播经验值增加到" + roomMessage.getLvMsg().getData().getLvl().getExp()));
                }
                break;
            case RoomMessage.SYSTEM_MESSAGE:  //系统公告
                SystemMessageHolder systemMessageHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof SystemMessageHolder) {
                    systemMessageHolder = (SystemMessageHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_system, null);
                    systemMessageHolder = new SystemMessageHolder();
                    systemMessageHolder.type = (TextView) view.findViewById(R.id.item_main_room_system_type);
                    view.setTag(systemMessageHolder);
                }
                if (roomMessage != null && roomMessage.getSystemMsg() != null && roomMessage.getSystemMsg().getData() != null && roomMessage.getSystemMsg().getData().getMsg() != null) {
                    systemMessageHolder.type.setText(showSystemMessage(roomMessage.getSystemMsg().getData().getMsg()));
                }
                break;
            case RoomMessage.GRREN_MESSAGE:  //绿色信息
                GreenMessageHolder greenMessageHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof SystemMessageHolder) {
                    greenMessageHolder = (GreenMessageHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_system, null);
                    greenMessageHolder = new GreenMessageHolder();
                    greenMessageHolder.text = (TextView) view.findViewById(R.id.item_main_room_system_type);
                    view.setTag(greenMessageHolder);
                }
                if(roomMessage.getContent()!=null){
                    greenMessageHolder.text.setText(manageMessage(roomMessage.getContent()));
                }
                break;
            case RoomMessage.LUCKY_RETURN_AWARD:  //幸运礼物返奖信息
                LuckyretAwardHolder luckyretAwardHolder = null;
                if (view != null && view.getTag() != null && view.getTag() instanceof LuckyretAwardHolder) {
                    luckyretAwardHolder = (LuckyretAwardHolder) view.getTag();
                } else {
                    view = View.inflate(context, R.layout.item_main_room_system, null);
                    luckyretAwardHolder = new LuckyretAwardHolder();
                    luckyretAwardHolder.text = (TextView) view.findViewById(R.id.item_main_room_system_type);
                    view.setTag(luckyretAwardHolder);
                }
                if(roomMessage.getLuckyGiftRetAwardMsg()!=null&&roomMessage.getLuckyGiftRetAwardMsg().getData()!=null&&roomMessage.getLuckyGiftRetAwardMsg().getData().getMsg()!=null){
                    luckyretAwardHolder.text.setText(showSystemMessage(roomMessage.getLuckyGiftRetAwardMsg().getData().getMsg()));
                }

                break;
        }
        return view;
    }

    //系统公告
    public Spannable showSystemMessage(String text) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_red_color)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    //管理权限
    public Spannable manageMessage(String text) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_white_color)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    //等级变更
    public Spannable lvlChange(String text) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_yellow_color)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    //送礼
    public Spannable showSendGift(Drawable richlvl, String nick, String text/*, Drawable giftAvatar*/) {
//        giftAvatar.setBounds(0, 0, giftAvatar.getIntrinsicWidth() / 2, giftAvatar.getIntrinsicHeight() / 2);
//        giftAvatar.setBounds(0, 0, DpOrSp2PxUtil.dp2pxConvertInt(context, 20), DpOrSp2PxUtil.dp2pxConvertInt(context, 20));
        String image = "image";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(image + nick + text);
        spannableStringBuilder.setSpan(new ImageSpan(richlvl), 0, image.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_yellow_color)), image.length(), nick.length() + image.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_red_color)), image.length() + nick.length(), nick.length() + image.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.setSpan(new ImageSpan(giftAvatar), (image + nick + text).length(), (image + nick + text + image).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    public Spannable systemShowInRoom(String notice, String text) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(notice + text);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, notice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK), notice.length(), notice.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    public Spannable joinRoom(Drawable richlvl, String nick, String text) {
        String image = "";
        SpannableStringBuilder spBuilder = new SpannableStringBuilder(image + nick + text);
        spBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_yellow_color)), image.length(), (image + nick).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_white_color)), image.length() + nick.length(), (image + nick + text).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spBuilder.setSpan(new ImageSpan(richlvl), 0, image.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spBuilder;
    }

    /**
     * 替换表情
     * @param text
     * @return
     */
    public Spannable addSmileySpans(Drawable drawableLeft, String fuser, String text) {
        String imageLeft = "image";
        SpannableStringBuilder spBuilder = new SpannableStringBuilder(imageLeft + fuser + text);
        spBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_yellow_color)), imageLeft.length(), imageLeft.length() + fuser.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spBuilder.setSpan(new ImageSpan(drawableLeft), 0, imageLeft.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.ceeeeee)), imageLeft.length() + fuser.length(), imageLeft.length() + fuser.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Matcher matcher = MsgHelper.PATTERN.matcher(text);
        while (matcher.find()) {
            try {
                Emotions.format();
                EmotionEntity emotionEntity = MsgHelper.emotionMap.get(matcher.group());
                Drawable drawable = Drawable.createFromStream(context.getAssets().open(emotionEntity.getSource()), null);
                drawable.setBounds(0, 0, DpOrSp2PxUtil.dp2pxConvertInt(context,25), DpOrSp2PxUtil.dp2pxConvertInt(context, 25));
                spBuilder.setSpan(new ImageSpan(drawable), imageLeft.length() + fuser.length() + matcher.start(), imageLeft.length() + fuser.length() + matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                emotionEntity = null;
            } catch (Exception e) {
                Log.e("exception", e.getMessage());
                e.printStackTrace();
            }
        }
        return spBuilder;
    }

    class LuckyretAwardHolder{
        TextView text;
    }
    class GreenMessageHolder{
        TextView text;
    }

    class SystemMessageHolder {
        TextView type;
    }

    class AnchorLvlChangeHolder {
        TextView type;
    }

    class OpenGuardHolder {
        TextView type;
    }

    class UserLvlHolder {
        TextView type;
    }

    class AnchorLvlHolder {
        TextView type;
    }

    class SystemHolder {
        TextView type;
    }

    class JoinHolder {
        TextView nick;
    }


    class GiftHolder {
        TextView msg;
    }

    class MessageViewHolder {
        TextView message;

        public MessageViewHolder(View view) {
            message = (TextView) view.findViewById(R.id.tv_item_room_message);
        }
    }
}
