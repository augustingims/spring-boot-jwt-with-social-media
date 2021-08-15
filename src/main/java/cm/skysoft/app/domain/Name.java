package cm.skysoft.app.domain;

/**
 * Created by francis on 2/18/21.
 */

public class Name {
    private String fr;
    private String en;

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    @Override
    public String toString() {
        return "Name{" +
                "fr='" + fr + '\'' +
                ", en='" + en + '\'' +
                '}';
    }
}
