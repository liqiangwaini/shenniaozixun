package com.xingbo.live.emotion;

import com.xingbo.live.util.MsgHelper;

import java.util.ArrayList;
import java.util.List;

public class Emotions {
    public static String BASE_EMOTICON_INFO="\"[/微笑]\":\"1\",\"[/撇嘴]\":\"2\",\"[/色]\":\"3\",\"[/发呆]\":\"4\",\"[/得意]\":\"5\",\"[/流泪]\":\"6\",\"[/害羞]\":\"7\",\"[/闭嘴]\":\"8\",\"[/睡]\":\"9\",\"[/大哭]\":\"10\",\"[/尴尬]\":\"11\",\"[/发怒]\":\"12\",\"[/调皮]\":\"13\",\"[/呲牙]\":\"14\",\"[/惊讶]\":\"15\",\"[/难过]\":\"16\",\"[/酷]\":\"17\",\"[/冷汗]\":\"18\",\"[/抓狂]\":\"19\",\"[/撅嘴]\":\"20\",\"[/偷笑]\":\"21\",\"[/可爱]\":\"22\",\"[/白眼]\":\"23\",\"[/傲慢]\":\"24\",\"[/饥饿]\":\"25\",\"[/困]\":\"26\",\"[/惊恐]\":\"27\",\"[/流汗]\":\"28\",\"[/憨笑]\":\"29\",\"[/大兵]\":\"30\",\"[/奋斗]\":\"31\",\"[/咒骂]\":\"32\",\"[/疑问]\":\"33\",\"[/嘘]\":\"34\",\"[/晕]\":\"35\",\"[/折磨]\":\"36\",\"[/衰]\":\"37\",\"[/骷髅]\":\"38\",\"[/敲打]\":\"39\",\"[/再见]\":\"40\",\"[/擦汗]\":\"41\",\"[/抠鼻]\":\"42\",\"[/鼓掌]\":\"43\",\"[/糗大了]\":\"44\",\"[/坏笑]\":\"45\",\"[/左哼哼]\":\"46\",\"[/右哼哼]\":\"47\",\"[/哈欠]\":\"48\",\"[/鄙视]\":\"49\",\"[/委屈]\":\"50\",\"[/快哭了]\":\"51\",\"[/阴险]\":\"52\",\"[/亲亲]\":\"53\",\"[/吓]\":\"54\",\"[/可怜]\":\"55\",\"[/菜刀]\":\"56\",\"[/西瓜]\":\"57\",\"[/啤酒]\":\"58\",\"[/篮球]\":\"59\",\"[/乒乓]\":\"60\",\"[/咖啡]\":\"61\",\"[/饭]\":\"62\",\"[/猪头]\":\"63\",\"[/玫瑰]\":\"64\",\"[/凋谢]\":\"65\",\"[/示爱]\":\"66\",\"[/爱心]\":\"67\",\"[/心碎]\":\"68\",\"[/蛋糕]\":\"69\",\"[/闪电]\":\"70\",\"[/炸弹]\":\"71\",\"[/刀]\":\"72\",\"[/足球]\":\"73\",\"[/瓢虫]\":\"74\",\"[/手机]\":\"75\",\"[/月亮]\":\"76\",\"[/太阳]\":\"77\",\"[/礼物]\":\"78\",\"[/抱抱]\":\"79\",\"[/强]\":\"80\",\"[/喝彩]\":\"81\",\"[/握手]\":\"82\",\"[/胜利]\":\"83\",\"[/抱拳]\":\"84\",\"[/勾引]\":\"85\",\"[/拳头]\":\"86\",\"[/差劲]\":\"87\",\"[/爱你]\":\"88\",\"[/NO]\":\"89\",\"[/OK]\":\"90\",\"[/擦鼻血]\":\"91\",\"[/哼哼哼]\":\"92\",\"[/吐舌头]\":\"93\",\"[/哇啊啊]\":\"94\",\"[/猥琐笑]\":\"95\",\"[/呦呦呦]\":\"96\",\"[/眨眼]\":\"97\"";
    public final static String EMOTICON_REGEX="\\[/[\u0391-\uFFE5]+\\]";
    public static final String pathBase = "emotions/";

    public static final List<EmotionEntity> DATA = format();

    public static String path(String name) {
        return pathBase + name;
    }

    public static List<EmotionEntity> format() {
        List<EmotionEntity> emotionEntityList = new ArrayList<>();
        BASE_EMOTICON_INFO=BASE_EMOTICON_INFO.replace("\"","");
        String [] items=BASE_EMOTICON_INFO.split(",");
        for (int i=0;i<64;i++){
            String [] temp=items[i].split(":");
            String code = temp[1];
            String name = temp[0];
            EmotionEntity emotionEntity = EmotionEntity.fromAssert(name, path("px_m" + code + ".png"));
            EmotionManager.registerEmotion(emotionEntity);
            EmotionWithSizeManager.registerEmotion(emotionEntity);
            MsgHelper.registerEmotion(emotionEntity);
            emotionEntityList.add(emotionEntity);
        }
        return emotionEntityList;
    }

}