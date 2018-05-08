package comviewbiksappshome.google.httpssites.discussion3;

import java.util.Date;

/**
 * Created by biksharma on 5/7/18.
 */

public class Messages {
    private String text;
    private String userName;
    private long timeStamp;

    public Messages(String text, String userName) {
        this.text = text;
        this.userName = userName;

        // set the timestamp to the current time using java.util.Date
        timeStamp = new Date().getTime();
    }

    public Messages() {
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
