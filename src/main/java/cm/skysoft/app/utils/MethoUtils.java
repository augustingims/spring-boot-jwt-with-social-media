package cm.skysoft.app.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by francis on 2/15/21.
 */
public class MethoUtils {

    private static final DateTimeFormatter DATEFORMAT_CONVERTOR = DateTimeFormatter
            .ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter DATEFORMAT_CONVERTOR_EN = DateTimeFormatter
            .ofPattern("yyyy-MM-DD");

    private static final DateTimeFormatter DATEFORMAT_CONVERTOR_SMS = DateTimeFormatter
            .ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter DATETIME_FORMAT_CONVERTOR = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm");

    private static final DateTimeFormatter DATETIME_FORMAT_CONVERTOR_DATE_PIPE = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DATETIME_FORMAT_CONVERTOR_EN = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DATETIME_FORMAT_CONVERTOR_INSTANT = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm");

    private static final DateTimeFormatter DATETIME_FORMAT_CONVERTOR_INSTANT_2 = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'");

    private static final DateTimeFormatter DATETIME_FORMAT_CONVERTOR_V2 = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss");

    public static final DateTimeFormatter DATETIME_FORMAT_CONVERTOR_EN_V2 = DateTimeFormatter
            .ofPattern("yyyy-MM-DD HH:mm:ss");

    public static final String INDEFINI_TEXT = "-1";

    public static final String WEEK = "WEEK";
    public static final String MONTH = "MONTH";
    public static final String QUARTERLY = "QUARTERLY";
    public static final String BIANNUAL = "BIANNUAL";
    public static final String YEAR = "YEAR";
    private static final String[] listOfDay = { "Dimanche", "Lundi", "Mardi",
            "Mercredi", "Jeudi", "Vendredi", "Samedi" };

    private static String getDateToString(LocalDate date) {

        return date == null ? "" : DATEFORMAT_CONVERTOR.format(date);
    }

    public static LocalDateTime convertorDateTime(String dateText) {

        LocalDateTime l = null;
        try {

            dateText = dateText == null ? "" : dateText;

            l = LocalDateTime.parse(dateText, DATETIME_FORMAT_CONVERTOR);

        } catch (Exception ignored) {

        }

        return l;
    }


    public static String getDateComplet(LocalDate date) {

        int day = date.getDayOfWeek().getValue();

        if (day == 7) {

            day = 0;
        }

        String jour = listOfDay[day];

        return jour + " " + getDateToString(date);
    }

    public static String getPrefixDocumentByDate() {
        LocalDate today = LocalDate.now();
        Calendar c = Calendar.getInstance();
        String p = (today.getYear() + "").substring(2);
        p += format(today.getMonthValue(), 2);
        p += format(today.getDayOfMonth(), 2);
        p += "_";
        p += format(c.get(Calendar.HOUR_OF_DAY), 2);
        p += format(c.get(Calendar.MINUTE), 2);
        p += format(c.get(Calendar.SECOND), 2);
        p += "_";
        p += RandomStringUtils.randomNumeric(6);
        return p;
    }

    public static String getDateToStringEn(LocalDate date) {

        return date == null ? "" : DATEFORMAT_CONVERTOR_EN.format(date);
    }

    public static String getExtensionFile(String name_file) {

        String name_out = "";

        int to = name_file.lastIndexOf(".");

        name_out = name_file.substring((to + 1));

        return name_out;
    }

    public static byte[] resizeWeigth(byte[] img, int largeur, int hauteur,
                                      String ext) {
        try {
            InputStream inputStream = new ByteArrayInputStream(img);
            BufferedImage bufImg = ImageIO.read(inputStream);
            BufferedImage thumbnail = Scalr.resize(bufImg,
                    Scalr.Method.ULTRA_QUALITY, largeur, hauteur,
                    Scalr.OP_ANTIALIAS);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, ext, baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException Ignored) {
            return null;
        }
    }




    public static String getDateTimeToString(LocalDateTime date) {

        return date == null ? "" : DATETIME_FORMAT_CONVERTOR
                .format(date);
    }



    public static Date convertorTextToDate(String dateText) {

        LocalDate l = null;
        try {

            dateText = dateText == null ? "" : dateText;

            l = LocalDate.parse(dateText, DATETIME_FORMAT_CONVERTOR);

        } catch (Exception ignored) {

        }

        try {

            if (l == null) {

                l = LocalDate.parse(dateText, DATEFORMAT_CONVERTOR);
            }

        } catch (Exception ignored) {

        }

        if (l != null) {
            return asDate(l);
        } else {
            return null;
        }
    }

    public static LocalDateTime convertorDateWithoutFormat(String dateText) {
        String date  = dateText == null ? "" : dateText;
        System.out.println("date " + date);
        return  LocalDateTime.parse(date);
    }

    public static LocalDate convertorTextToLocalDate(String dateText) {

        LocalDateTime l1 = null;

        LocalDate l = null;
        try {

            dateText = dateText == null ? "" : dateText;

            l1 = LocalDateTime.parse(dateText, DATETIME_FORMAT_CONVERTOR);

            l = l1.toLocalDate();

        } catch (Exception ignored) {

        }

        try {

            if (l == null) {

                l = LocalDate.parse(dateText, DATEFORMAT_CONVERTOR);
            }

        } catch (Exception ignored) {

        }

        return l;
    }

    public static String getDateToString(LocalDateTime date) {

        return date == null ? "" : DATETIME_FORMAT_CONVERTOR
                .format(date);
    }

    public static String getDatePipeToString(LocalDateTime date) {

        return date == null ? "" : DATETIME_FORMAT_CONVERTOR_DATE_PIPE
                .format(date);
    }

    public static String getLocalDateTimeToString(LocalDateTime date) {

        return date == null ? "" : DATETIME_FORMAT_CONVERTOR_V2
                .format(date);
    }

    public static String getLocalDateTime2ToString(LocalDateTime date) {

        return date == null ? "" : DATEFORMAT_CONVERTOR_SMS
                .format(date);
    }

    public static String getLocalDateTimeToString(Instant date) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.UK )
                        .withZone( ZoneId.systemDefault() );

        return date == null ? "" : formatter
                .format(date);
    }

    public static String getDatesTimeToString(LocalDateTime date) {

        return date == null ? "" : DATETIME_FORMAT_CONVERTOR_INSTANT
                .format(date);
    }

    public static String getDatesTime2ToString(LocalDateTime date) {

        return date == null ? "" : DATETIME_FORMAT_CONVERTOR_INSTANT_2
                .format(date);
    }

    public static String getDateTimeTextToString(LocalDateTime date) {

        return date == null ? "" : DATETIME_FORMAT_CONVERTOR_EN
                .format(date);
        }



    public static LocalDateTime convertorTextToLocalDateTime(String dateText) {

        LocalDateTime l = null;
        try {

            dateText = dateText == null ? "" : dateText;

            l = LocalDateTime.parse(dateText, DATETIME_FORMAT_CONVERTOR);

        } catch (Exception ignored) {

        }

        try {

            if (l == null) {

                l = LocalDateTime.parse(dateText, DATETIME_FORMAT_CONVERTOR_V2);
            }

        } catch (Exception ignored) {

        }

        return l;
    }



    public static LocalDateTime convertorTextToLocalDateTimeEn(String dateText) {

        LocalDateTime l = null;

        try {

            dateText = dateText == null ? "" : dateText;

            l = LocalDateTime.parse(dateText, DATEFORMAT_CONVERTOR_EN);

        } catch (Exception ignored) {

        }

        try {

            if (l == null) {

                l = LocalDateTime.parse(dateText, DATETIME_FORMAT_CONVERTOR_V2);
            }

        } catch (Exception ignored) {

        }

        return l;
    }


    public static String formatStringToFind(String find) {

        if (find.length() > 0) {

            find = find.replace('*', '%');
            if (find.charAt(0) != '%') {
                find = "%" + find;
            }
            if (find.charAt(find.length() - 1) != '%') {
                find = find + "%";
            }
        } else {
            find = "%%";
        }
        return find;
    }

    private static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant());
    }



    public static String format(Integer value) {

        int nombre_char_max = 6;

        return format(value, nombre_char_max);
    }

    public static String format(Integer value, int nombre_char_max) {

        String v = value.toString();

        StringBuilder fixe = new StringBuilder();

        if (v.length() <= nombre_char_max) {

            int nbre_zero = nombre_char_max - v.length();

            for (int i = 0; i < nbre_zero; i++) {

                fixe.append("0");
            }

            fixe.append(v);

        } else {

            fixe = new StringBuilder(v);
        }

        return fixe.toString();
    }



    private static LocalDate convertorDate(String dateText) {

        LocalDateTime l1 = null;

        LocalDate l = null;
        try {

            dateText = dateText == null ? "" : dateText;

            l1 = LocalDateTime.parse(dateText, DATETIME_FORMAT_CONVERTOR);

            l = l1.toLocalDate();

        } catch (Exception ignored) {

        }

        try {

            if (l == null) {

                l = LocalDate.parse(dateText, DATEFORMAT_CONVERTOR);
            }

        } catch (Exception ignored) {

        }

        return l;
    }

    public static LocalDateTime convertorDateTime00H00(String dateText) {

        LocalDate l = convertorDate(dateText);

        return l != null ? l.atStartOfDay() : null;
    }

    public static LocalDateTime convertorDateTime24H00(String dateText) {

        LocalDate l = convertorDate(dateText);

        return l != null ? l.atTime(LocalTime.MAX) : null;
    }

    public static String conv(String date) {

        String value;
        LocalDate dateToconvert = LocalDate.parse(date);
        String monthValue = dateToconvert.getMonthValue() < 10 ?  "0"+dateToconvert.getMonthValue() : ""+dateToconvert.getMonthValue();
        value  = dateToconvert.getDayOfMonth() + "/"+  monthValue +"/" + dateToconvert.getYear();

        return value;
    }

    public static String getMontName(int monthValue) {


        if (monthValue <= 0) {
            monthValue = 12 + monthValue;
        }


        switch (monthValue) {
            case 1:
                return "JANVIER";
            case 2:
                return "FEVRIER";
            case 3:
                return "MARS";
            case 4:
                return "AVRIL";
            case 5:
                return "MAI";
            case 6:
                return "JUIN";
            case 7:
                return "JUILLET";
            case 8:
                return "AOUT";
            case 9:
                return "SEPTEMBRE";
            case 10:
                return "OCTOBRE";
            case 11:
                return "NOVEMBRE";
            case 12:
            default:
                return "DECEMBRE";
        }
    }

    public static String getMontNameAndYear(int monthValue, int yearValue) {


        if (monthValue <= 0) {
            monthValue = 12 + monthValue;
            yearValue = yearValue - 1;
        }


        switch (monthValue) {
            case 1:
                return "JANVIER " + "("+yearValue+")";
            case 2:
                return "FEVRIER " + "("+yearValue+")";
            case 3:
                return "MARS " + "("+yearValue+")";
            case 4:
                return "AVRIL " + "("+yearValue+")";
            case 5:
                return "MAI " + "("+yearValue+")";
            case 6:
                return "JUIN " + "("+yearValue+")";
            case 7:
                return "JUILLET " + "("+yearValue+")";
            case 8:
                return "AOUT " + "("+yearValue+")";
            case 9:
                return "SEPTEMBRE " + "("+yearValue+")";
            case 10:
                return "OCTOBRE " + "("+yearValue+")";
            case 11:
                return "NOVEMBRE " + "("+yearValue+")";
            case 12:
            default:
                return "DECEMBRE " + "("+yearValue+")";
        }
    }

}
