package model.domain.member;

public class Member { // 수정은 pw, phone
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String phone;
    private String img;

    public Member() {
    }

    public Member(String id, String pwd, String name, String email, String phone) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
        this.phone = phone;

    }
    public Member(String id, String pwd, String name, String email, String phone, String img) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.img = img;
    }


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    /* 비밀번호 검사 */
    public boolean matchPassword(String pwd) {
        if (pwd == null) {
            return false;
        }
        return this.pwd.equals(pwd);
    }

    public boolean isSameMember(String id) {
        return this.id.equals(id);
    }

}