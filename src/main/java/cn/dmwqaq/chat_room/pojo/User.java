package cn.dmwqaq.chat_room.pojo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@SuppressWarnings("unused")
public class User implements Serializable {

    private static final long serialVersionUID = -115621356111625314L;

    public enum UserSex {
        MALE(1),
        FEMALE(0);

        private int value;

        UserSex(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private String id;
    private String name;
    private String password;
    private UserSex sex;
    private Date birthday;

    public User(String id, String name, String password, UserSex sex, Date birthday) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.birthday = birthday;
    }

    public User(BigInteger id, String name, String password, Integer sex, java.sql.Date birthday) {
        this.id = id.toString();
        this.name = name;
        this.password = password;
        this.sex = (sex == 1 ? UserSex.MALE : UserSex.FEMALE);
        this.birthday = new Date(birthday.getTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserSex getSex() {
        return sex;
    }

    public void setSex(UserSex sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", password='" + password + '\'' + ", sex="
                + sex + ", birthday=" + birthday + '}';
    }
}
