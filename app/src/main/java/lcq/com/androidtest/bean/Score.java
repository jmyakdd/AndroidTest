package lcq.com.androidtest.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by CRTE-CD-13 on 2018/5/8.
 */

public class Score extends DataSupport{
    private int id;
    private String sub;
    private float score;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", sub='" + sub + '\'' +
                ", score=" + score +
                ", user=" + user +
                '}';
    }
}
