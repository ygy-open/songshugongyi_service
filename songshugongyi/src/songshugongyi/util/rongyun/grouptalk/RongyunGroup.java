package songshugongyi.util.rongyun.grouptalk;

import io.rong.methods.group.Group;
import io.rong.models.Result;
import io.rong.models.group.GroupMember;
import io.rong.models.group.GroupModel;


import static songshugongyi.util.rongyun.BaseConfig.getRongCloudInstance;

/**
 * Created by yuanopen on 2018/6/29/029.
 */
public class RongyunGroup {

    static RongyunGroup instance;

    public static RongyunGroup getInstance(){
        if(instance==null)
            instance=new RongyunGroup();

        return instance;
    }
    Group group=getRongCloudInstance().group;
    /**
     * cteated by yuanopen
     * @param groupId 群组id
     * @param groupName  群组name
     * @param groupMembers 群组成员
     */
    public  void CreateGroup(String groupId, String groupName,GroupMember[] groupMembers){


        GroupModel groupModel=new GroupModel()
                .setId(groupId)
                .setName(groupName)
                .setMembers(groupMembers);

        Result result = null;
        try {
            result=group.create(groupModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("result.toString():"+result.toString());

    }

    /**
     *
     * @param groupId
     * @param groupMembers
     */
    public  void JoinGroup(String groupId, String groupName, GroupMember[] groupMembers){


        GroupModel groupModel=new GroupModel()
                .setId(groupId)
                .setName(groupName)
                .setMembers(groupMembers);

        Result result = null;
        try {
            result=group.join(groupModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("result.toString():"+result.toString());

    }

    /**
     *
     * @param groupId
     * fun 获取群组信息
     */
    public  void getGroup(String groupId){

        GroupModel groupModel=new GroupModel()
                .setId(groupId);

        Result result = null;
        try {
            result=group.get(groupModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("result.toString():"+result.toString());

    }


    /**
     *
     * @param groupId
     * fun 退出群组
     */
    public  void quitGroup(String groupId,GroupMember[] groupMembers){

        GroupModel groupModel=new GroupModel()
                .setMembers(groupMembers)
                .setId(groupId);

        Result result = null;
        try {
            result=group.quit(groupModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("result.toString():"+result.toString());

    }

    /**
     *
     * @param groupId
     * fun 解散群组
     */
    public  void dismissGroup(String groupId){


        GroupModel groupModel=new GroupModel()
                .setId(groupId);

        Result result = null;
        try {
            result=group.dismiss(groupModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("result.toString():"+result.toString());

    }


    public static void main(String[] args) {
        RongyunGroup group=new RongyunGroup();
        group.CreateGroup("492397ac6c20","与子同阅:“带本书给家乡的孩子",new GroupMember[]{new GroupMember("ee8f05d8a567","492397ac6c20",null)});
//        group.JoinGroup("c9aedc20b2f9","南工",new GroupMember[]{new GroupMember("ee8f05d8a567","c9aedc20b2f9",null)});
    }

}
