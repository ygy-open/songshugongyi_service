package songshugongyi.util.rongyun.message;

import io.rong.messages.BaseMessage;
import io.rong.messages.CmdMsgMessage;
import io.rong.messages.ContactNtfMessage;
import io.rong.methods.message.Message;
import io.rong.methods.message._private.Private;
import io.rong.models.Result;
import io.rong.models.group.GroupModel;
import io.rong.models.message.MessageModel;
import io.rong.models.message.PrivateMessage;
import io.rong.models.message.SystemMessage;
import songshugongyi.bean.message.FriendMessage;
import songshugongyi.util.GsonUtils;


import static songshugongyi.util.rongyun.BaseConfig.getRongCloudInstance;
import static songshugongyi.util.rongyun.BaseConfig.getRongyunPrivateInstance;

/**
 * Created by yuanopen on 2018/6/30/030.
 */
public class RongyunMessage {

    Message mPrivate=getRongCloudInstance().message;

    public void send(){

        PrivateMessage message=new PrivateMessage()
                .setSenderId("yuan")
                .setTargetId(new String[]{"admin","jiajia","zhangsan"})
                .setObjectName("RC:TxtMsg")
                .setContent(new BaseMessage() {
                    @Override
                    public String getType() {
                        return "RC:TxtMsg";
                    }

                    @Override
                    public String toString() {
                        return "{\n" +
                                "    \"content\":\"交个朋友吧！！哈哈\",\n" +
                                "    \"extra\":\"交个朋友吧\"\n" +
                                "}！";
                    }
                });
        Result result = null;
        try {
            result=mPrivate.msgPrivate.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result.toString():"+result.toString());
    }

    public void sendAddFreindMessage(String target_id, final String content){

        SystemMessage message=new SystemMessage()
                .setSenderId("admin")
                .setTargetId(new String[]{target_id})
                .setObjectName("RC:TxtMsg")
                .setContent(new BaseMessage() {
                    @Override
                    public String getType() {
                        return "RC:TxtMsg";
                    }

                    @Override
                    public String toString() {
                        return "{\n"+
                                "    \"content\":\t\""+content+"\",\n" +
                                "    \"extra\":\"好友申请\"\n" +
                                "}！";
                    }
                });

        Result result = null;
        try {
            result=mPrivate.system.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result.toString():"+result.toString());
    }

    public void sendAgreeGroupMessage(String target_id, final String content){

        SystemMessage message=new SystemMessage()
                .setSenderId("admin")
                .setTargetId(new String[]{target_id})
                .setObjectName("RC:TxtMsg")
                .setContent(new BaseMessage() {
                    @Override
                    public String getType() {
                        return "RC:TxtMsg";
                    }

                    @Override
                    public String toString() {
                        return "{\n"+
                                "    \"content\":\t\""+content+"\",\n" +
                                "    \"extra\":\"群组申请\"\n" +
                                "}！";
                    }
                });

        Result result = null;
        try {
            result=mPrivate.system.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result.toString():"+result.toString());
    }


    public void sendAddNotification(String target_id, final String name, final String extra,int type){
        // 发送通 ContactNtfMessage(String operation, String extra, String sourceUserId, String targetUserId, String message
        ContactNtfMessage message=new ContactNtfMessage(""+type,extra,"admin",target_id,name);

        SystemMessage systemMessage=new SystemMessage()
                .setSenderId("admin")
                .setTargetId(new String[]{target_id})
                .setObjectName("RC:ContactNtf")
                .setContent(message);
//                .setContent(new BaseMessage() {
//                    @Override
//                    public String getType() {
//                        return "RC:ContactNtf";
//                    }
//
//                    @Override
//                    public String toString() {
//                        return "{\n"+
//                                "    \"content\":\t\""+name+"\",\n" +
//
//                                "    \"extra\":"+image_url+"}！";
//                    }
//                });

        Result result = null;
        try {
            result=mPrivate.system.send(systemMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result.toString():"+result.toString());
    }


    public static void main(String[] args) {
        RongyunMessage message=new RongyunMessage();

        message.sendAddFreindMessage("ee8f05d8a567","源愿圆远请求添加好友");
//        FriendMessage message1=new FriendMessage();
//        message1.setContent("添加");
//        message1.setTarget_id("ee8f05d8a567");
//         message1.setTarget_name("小松鼠");
//        message1.setUser_id("922B3DB7B8B0CA707ACA0550DC58E239");
//        message1.setType(1);
//        message1.setUser_name("源愿圆远");
//        message1.setUser_avatar("http://p8la0sgrt.bkt.clouddn.com//ee8f05d8a567_1532093706229_avatar");
////       String ={"content":"添加","target_id":"ee8f05d8a567","target_name":"小松鼠","type":1,"user_avatar":"http://p8la0sgrt.bkt.clouddn.com//ee8f05d8a567_1532093706229_avatar","user_id":"922B3DB7B8B0CA707ACA0550DC58E239","user_name":"源愿圆远"}
//        message.sendAddNotification("ee8f05d8a567","源", GsonUtils.ModuleTojosn(message1),1);

    }

}
