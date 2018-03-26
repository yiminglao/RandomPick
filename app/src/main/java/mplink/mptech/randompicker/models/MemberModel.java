package mplink.mptech.randompicker.models;

/**
 * Created by Monkey Park on 3/25/2018.
 */

public class MemberModel {

    private String id;

    private String memberName;

    private String gid;

    public MemberModel()
    {

    }

    public MemberModel(String id, String memberName, String gid) {
        this.id = id;
        this.memberName = memberName;
        this.gid = gid;
    }




    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
