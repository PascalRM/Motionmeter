package ramanathan.pascal.motionmeter.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Pascal on 08.03.2018.
 */

public class Event implements Serializable{
    private String name;
    private String user;
    private Date startdate;
    private Date enddate;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    @Override
    public String toString(){
        return  name;
    }
}
