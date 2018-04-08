package ramanathan.pascal.motionmeter.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Pascal on 08.03.2018.
 */

public class Event implements Serializable{
    private String name;
    private String user;
    private String UID;
    private Date startdate;
    private Date enddate;
    private String passwort;
    private String beschreibung;
    private List bemerkungen;

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

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public List getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(List bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    @Override
    public String toString(){
        return  name;
    }
}
