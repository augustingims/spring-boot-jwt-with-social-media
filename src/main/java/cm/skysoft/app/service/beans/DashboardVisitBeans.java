package cm.skysoft.app.service.beans;

import cm.skysoft.app.domain.User;
import org.jetbrains.annotations.NotNull;

public class DashboardVisitBeans implements Comparable<DashboardVisitBeans> {

    private User userDTO;
    private Integer totalVisit;
    private Integer totalPlannedVisitOustideTheOffice;
    private Integer totalPlannedVisitAtTheOffice;
    private Integer totalPlannedVisitPhoneCall;
    private Integer totalArchivateVisitOustideTheOffice;
    private Integer totalArchivateVisitAtTheOffice;
    private Integer totalArchivateVisitPhoneCall;
    private Integer totalVisitExecutedOustideTheOffice;
    private Integer totalVisitExecutedAtTheOffice;
    private Integer totalVisitExecutedPhoneCall;
    private Integer totalVisitNotExecutedOustideTheOffice;
    private Integer totalVisitNotExecutedAtTheOffice;
    private Integer totalVisitNotExecutedPhoneCall;
    private Integer totalVisitAwaitingReportOustideTheOffice;
    private Integer totalVisitAwaitingReportAtTheOffice;
    private Integer totalVisitAwaitingReportPhoneCall;

    private Integer numberTotalVisitForUser;
    private Integer numberTotalPlannedVisitOustideTheOffice;
    private Integer numberTotalPlannedVisitAtTheOffice;
    private Integer numberTotalPlannedVisitPhoneCall;
    private Integer numberTotalArchivateVisitOustideTheOffice;
    private Integer numberTotalArchivateVisitAtTheOffice;
    private Integer numberTotalArchivateVisitPhoneCall;
    private Integer numberTotalVisitExecutedOustideTheOffice;
    private Integer numberTotalVisitExecutedAtTheOffice;
    private Integer numberTotalVisitExecutedPhoneCall;
    private Integer numberTotalVisitNotExecutedOustideTheOffice;
    private Integer numberTotalVisitNotExecutedAtTheOffice;
    private Integer numberTotalVisitNotExecutedPhoneCall;
    private Integer numberTotalVisitAwaitingReportOustideTheOffice;
    private Integer numberTotalVisitAwaitingReportAtTheOffice;
    private Integer numberTotalVisitAwaitingReportPhoneCall;
    private Integer totalPlannedVisit;
    private Integer totalArchivateVisit;
    private Integer totalAwaitingReportVisit;
    private Integer totalVisitNotExecuted;

    public User getUser() {
        return userDTO;
    }

    public void setUser(User userDTO) {
        this.userDTO = userDTO;
    }

    public Integer getTotalVisit() {
        return totalVisit;
    }

    public void setTotalVisit(Integer totalVisit) {
        this.totalVisit = totalVisit;
    }

    public Integer getTotalPlannedVisitOustideTheOffice() {
        return totalPlannedVisitOustideTheOffice;
    }

    public void setTotalPlannedVisitOustideTheOffice(Integer totalPlannedVisitOustideTheOffice) {
        this.totalPlannedVisitOustideTheOffice = totalPlannedVisitOustideTheOffice;
    }

    public Integer getTotalPlannedVisitAtTheOffice() {
        return totalPlannedVisitAtTheOffice;
    }

    public void setTotalPlannedVisitAtTheOffice(Integer totalPlannedVisitAtTheOffice) {
        this.totalPlannedVisitAtTheOffice = totalPlannedVisitAtTheOffice;
    }

    public Integer getTotalPlannedVisitPhoneCall() {
        return totalPlannedVisitPhoneCall;
    }

    public void setTotalPlannedVisitPhoneCall(Integer totalPlannedVisitPhoneCall) {
        this.totalPlannedVisitPhoneCall = totalPlannedVisitPhoneCall;
    }

    public Integer getTotalArchivateVisitOustideTheOffice() {
        return totalArchivateVisitOustideTheOffice;
    }

    public void setTotalArchivateVisitOustideTheOffice(Integer totalArchivateVisitOustideTheOffice) {
        this.totalArchivateVisitOustideTheOffice = totalArchivateVisitOustideTheOffice;
    }

    public Integer getTotalArchivateVisitAtTheOffice() {
        return totalArchivateVisitAtTheOffice;
    }

    public void setTotalArchivateVisitAtTheOffice(Integer totalArchivateVisitAtTheOffice) {
        this.totalArchivateVisitAtTheOffice = totalArchivateVisitAtTheOffice;
    }

    public Integer getTotalArchivateVisitPhoneCall() {
        return totalArchivateVisitPhoneCall;
    }

    public void setTotalArchivateVisitPhoneCall(Integer totalArchivateVisitPhoneCall) {
        this.totalArchivateVisitPhoneCall = totalArchivateVisitPhoneCall;
    }

    public Integer getTotalVisitExecutedOustideTheOffice() {
        return totalVisitExecutedOustideTheOffice;
    }

    public void setTotalVisitExecutedOustideTheOffice(Integer totalVisitExecutedOustideTheOffice) {
        this.totalVisitExecutedOustideTheOffice = totalVisitExecutedOustideTheOffice;
    }

    public Integer getTotalVisitExecutedAtTheOffice() {
        return totalVisitExecutedAtTheOffice;
    }

    public void setTotalVisitExecutedAtTheOffice(Integer totalVisitExecutedAtTheOffice) {
        this.totalVisitExecutedAtTheOffice = totalVisitExecutedAtTheOffice;
    }

    public Integer getTotalVisitExecutedPhoneCall() {
        return totalVisitExecutedPhoneCall;
    }

    public void setTotalVisitExecutedPhoneCall(Integer totalVisitExecutedPhoneCall) {
        this.totalVisitExecutedPhoneCall = totalVisitExecutedPhoneCall;
    }

    public Integer getTotalVisitNotExecutedOustideTheOffice() {
        return totalVisitNotExecutedOustideTheOffice;
    }

    public void setTotalVisitNotExecutedOustideTheOffice(Integer totalVisitNotExecutedOustideTheOffice) {
        this.totalVisitNotExecutedOustideTheOffice = totalVisitNotExecutedOustideTheOffice;
    }

    public Integer getTotalVisitNotExecutedAtTheOffice() {
        return totalVisitNotExecutedAtTheOffice;
    }

    public void setTotalVisitNotExecutedAtTheOffice(Integer totalVisitNotExecutedAtTheOffice) {
        this.totalVisitNotExecutedAtTheOffice = totalVisitNotExecutedAtTheOffice;
    }

    public Integer getTotalVisitNotExecutedPhoneCall() {
        return totalVisitNotExecutedPhoneCall;
    }

    public void setTotalVisitNotExecutedPhoneCall(Integer totalVisitNotExecutedPhoneCall) {
        this.totalVisitNotExecutedPhoneCall = totalVisitNotExecutedPhoneCall;
    }

    public Integer getTotalVisitAwaitingReportOustideTheOffice() {
        return totalVisitAwaitingReportOustideTheOffice;
    }

    public void setTotalVisitAwaitingReportOustideTheOffice(Integer totalVisitAwaitingReportOustideTheOffice) {
        this.totalVisitAwaitingReportOustideTheOffice = totalVisitAwaitingReportOustideTheOffice;
    }

    public Integer getTotalVisitAwaitingReportAtTheOffice() {
        return totalVisitAwaitingReportAtTheOffice;
    }

    public void setTotalVisitAwaitingReportAtTheOffice(Integer totalVisitAwaitingReportAtTheOffice) {
        this.totalVisitAwaitingReportAtTheOffice = totalVisitAwaitingReportAtTheOffice;
    }

    public Integer getTotalVisitAwaitingReportPhoneCall() {
        return totalVisitAwaitingReportPhoneCall;
    }

    public void setTotalVisitAwaitingReportPhoneCall(Integer totalVisitAwaitingReportPhoneCall) {
        this.totalVisitAwaitingReportPhoneCall = totalVisitAwaitingReportPhoneCall;
    }

    public Integer getNumberTotalVisitForUser() {
        return numberTotalVisitForUser;
    }

    public void setNumberTotalVisitForUser(Integer numberTotalVisitForUser) {
        this.numberTotalVisitForUser = numberTotalVisitForUser;
    }

    public Integer getNumberTotalPlannedVisitOustideTheOffice() {
        return numberTotalPlannedVisitOustideTheOffice;
    }

    public void setNumberTotalPlannedVisitOustideTheOffice(Integer numberTotalPlannedVisitOustideTheOffice) {
        this.numberTotalPlannedVisitOustideTheOffice = numberTotalPlannedVisitOustideTheOffice;
    }

    public Integer getNumberTotalPlannedVisitAtTheOffice() {
        return numberTotalPlannedVisitAtTheOffice;
    }

    public void setNumberTotalPlannedVisitAtTheOffice(Integer numberTotalPlannedVisitAtTheOffice) {
        this.numberTotalPlannedVisitAtTheOffice = numberTotalPlannedVisitAtTheOffice;
    }

    public Integer getNumberTotalPlannedVisitPhoneCall() {
        return numberTotalPlannedVisitPhoneCall;
    }

    public void setNumberTotalPlannedVisitPhoneCall(Integer numberTotalPlannedVisitPhoneCall) {
        this.numberTotalPlannedVisitPhoneCall = numberTotalPlannedVisitPhoneCall;
    }

    public Integer getNumberTotalArchivateVisitOustideTheOffice() {
        return numberTotalArchivateVisitOustideTheOffice;
    }

    public void setNumberTotalArchivateVisitOustideTheOffice(Integer numberTotalArchivateVisitOustideTheOffice) {
        this.numberTotalArchivateVisitOustideTheOffice = numberTotalArchivateVisitOustideTheOffice;
    }

    public Integer getNumberTotalArchivateVisitAtTheOffice() {
        return numberTotalArchivateVisitAtTheOffice;
    }

    public void setNumberTotalArchivateVisitAtTheOffice(Integer numberTotalArchivateVisitAtTheOffice) {
        this.numberTotalArchivateVisitAtTheOffice = numberTotalArchivateVisitAtTheOffice;
    }

    public Integer getNumberTotalArchivateVisitPhoneCall() {
        return numberTotalArchivateVisitPhoneCall;
    }

    public void setNumberTotalArchivateVisitPhoneCall(Integer numberTotalArchivateVisitPhoneCall) {
        this.numberTotalArchivateVisitPhoneCall = numberTotalArchivateVisitPhoneCall;
    }

    public Integer getNumberTotalVisitExecutedOustideTheOffice() {
        return numberTotalVisitExecutedOustideTheOffice;
    }

    public void setNumberTotalVisitExecutedOustideTheOffice(Integer numberTotalVisitExecutedOustideTheOffice) {
        this.numberTotalVisitExecutedOustideTheOffice = numberTotalVisitExecutedOustideTheOffice;
    }

    public Integer getNumberTotalVisitExecutedAtTheOffice() {
        return numberTotalVisitExecutedAtTheOffice;
    }

    public void setNumberTotalVisitExecutedAtTheOffice(Integer numberTotalVisitExecutedAtTheOffice) {
        this.numberTotalVisitExecutedAtTheOffice = numberTotalVisitExecutedAtTheOffice;
    }

    public Integer getNumberTotalVisitExecutedPhoneCall() {
        return numberTotalVisitExecutedPhoneCall;
    }

    public void setNumberTotalVisitExecutedPhoneCall(Integer numberTotalVisitExecutedPhoneCall) {
        this.numberTotalVisitExecutedPhoneCall = numberTotalVisitExecutedPhoneCall;
    }

    public Integer getNumberTotalVisitNotExecutedOustideTheOffice() {
        return numberTotalVisitNotExecutedOustideTheOffice;
    }

    public void setNumberTotalVisitNotExecutedOustideTheOffice(Integer numberTotalVisitNotExecutedOustideTheOffice) {
        this.numberTotalVisitNotExecutedOustideTheOffice = numberTotalVisitNotExecutedOustideTheOffice;
    }

    public Integer getNumberTotalVisitNotExecutedAtTheOffice() {
        return numberTotalVisitNotExecutedAtTheOffice;
    }

    public void setNumberTotalVisitNotExecutedAtTheOffice(Integer numberTotalVisitNotExecutedAtTheOffice) {
        this.numberTotalVisitNotExecutedAtTheOffice = numberTotalVisitNotExecutedAtTheOffice;
    }

    public Integer getNumberTotalVisitNotExecutedPhoneCall() {
        return numberTotalVisitNotExecutedPhoneCall;
    }

    public void setNumberTotalVisitNotExecutedPhoneCall(Integer numberTotalVisitNotExecutedPhoneCall) {
        this.numberTotalVisitNotExecutedPhoneCall = numberTotalVisitNotExecutedPhoneCall;
    }

    public Integer getNumberTotalVisitAwaitingReportOustideTheOffice() {
        return numberTotalVisitAwaitingReportOustideTheOffice;
    }

    public void setNumberTotalVisitAwaitingReportOustideTheOffice(Integer numberTotalVisitAwaitingReportOustideTheOffice) {
        this.numberTotalVisitAwaitingReportOustideTheOffice = numberTotalVisitAwaitingReportOustideTheOffice;
    }

    public Integer getNumberTotalVisitAwaitingReportAtTheOffice() {
        return numberTotalVisitAwaitingReportAtTheOffice;
    }

    public void setNumberTotalVisitAwaitingReportAtTheOffice(Integer numberTotalVisitAwaitingReportAtTheOffice) {
        this.numberTotalVisitAwaitingReportAtTheOffice = numberTotalVisitAwaitingReportAtTheOffice;
    }

    public Integer getNumberTotalVisitAwaitingReportPhoneCall() {
        return numberTotalVisitAwaitingReportPhoneCall;
    }

    public void setNumberTotalVisitAwaitingReportPhoneCall(Integer numberTotalVisitAwaitingReportPhoneCall) {
        this.numberTotalVisitAwaitingReportPhoneCall = numberTotalVisitAwaitingReportPhoneCall;
    }

    public Integer getTotalPlannedVisit() {
        return totalPlannedVisit;
    }

    public void setTotalPlannedVisit(Integer totalPlannedVisit) {
        this.totalPlannedVisit = totalPlannedVisit;
    }

    public Integer getTotalArchivateVisit() {
        return totalArchivateVisit;
    }

    public void setTotalArchivateVisit(Integer totalArchivateVisit) {
        this.totalArchivateVisit = totalArchivateVisit;
    }

    public Integer getTotalAwaitingReportVisit() {
        return totalAwaitingReportVisit;
    }

    public void setTotalAwaitingReportVisit(Integer totalAwaitingReportVisit) {
        this.totalAwaitingReportVisit = totalAwaitingReportVisit;
    }

    public Integer getTotalVisitNotExecuted() {
        return totalVisitNotExecuted;
    }

    public void setTotalVisitNotExecuted(Integer totalVisitNotExecuted) {
        this.totalVisitNotExecuted = totalVisitNotExecuted;
    }

    @Override
    public String toString() {
        return "DashboardVisitBeans{" +
                "userDTO=" + userDTO +
                ", totalVisit=" + totalVisit +
                ", totalPlannedVisitOustideTheOffice=" + totalPlannedVisitOustideTheOffice +
                ", totalPlannedVisitAtTheOffice=" + totalPlannedVisitAtTheOffice +
                ", totalPlannedVisitPhoneCall=" + totalPlannedVisitPhoneCall +
                ", totalArchivateVisitOustideTheOffice=" + totalArchivateVisitOustideTheOffice +
                ", totalArchivateVisitAtTheOffice=" + totalArchivateVisitAtTheOffice +
                ", totalArchivateVisitPhoneCall=" + totalArchivateVisitPhoneCall +
                ", totalVisitExecutedOustideTheOffice=" + totalVisitExecutedOustideTheOffice +
                ", totalVisitExecutedAtTheOffice=" + totalVisitExecutedAtTheOffice +
                ", totalVisitExecutedPhoneCall=" + totalVisitExecutedPhoneCall +
                ", totalVisitNotExecutedOustideTheOffice=" + totalVisitNotExecutedOustideTheOffice +
                ", totalVisitNotExecutedAtTheOffice=" + totalVisitNotExecutedAtTheOffice +
                ", totalVisitNotExecutedPhoneCall=" + totalVisitNotExecutedPhoneCall +
                ", totalVisitAwaitingReportOustideTheOffice=" + totalVisitAwaitingReportOustideTheOffice +
                ", totalVisitAwaitingReportAtTheOffice=" + totalVisitAwaitingReportAtTheOffice +
                ", totalVisitAwaitingReportPhoneCall=" + totalVisitAwaitingReportPhoneCall +
                ", numberTotalVisitForUser=" + numberTotalVisitForUser +
                ", numberTotalPlannedVisitOustideTheOffice=" + numberTotalPlannedVisitOustideTheOffice +
                ", numberTotalPlannedVisitAtTheOffice=" + numberTotalPlannedVisitAtTheOffice +
                ", numberTotalPlannedVisitPhoneCall=" + numberTotalPlannedVisitPhoneCall +
                ", numberTotalArchivateVisitOustideTheOffice=" + numberTotalArchivateVisitOustideTheOffice +
                ", numberTotalArchivateVisitAtTheOffice=" + numberTotalArchivateVisitAtTheOffice +
                ", numberTotalArchivateVisitPhoneCall=" + numberTotalArchivateVisitPhoneCall +
                ", numberTotalVisitExecutedOustideTheOffice=" + numberTotalVisitExecutedOustideTheOffice +
                ", numberTotalVisitExecutedAtTheOffice=" + numberTotalVisitExecutedAtTheOffice +
                ", numberTotalVisitExecutedPhoneCall=" + numberTotalVisitExecutedPhoneCall +
                ", NnumberTotalVisitNotExecutedOustideTheOffice=" + numberTotalVisitNotExecutedOustideTheOffice +
                ", numberTotalVisitNotExecutedAtTheOffice=" + numberTotalVisitNotExecutedAtTheOffice +
                ", numberTotalVisitNotExecutedPhoneCall=" + numberTotalVisitNotExecutedPhoneCall +
                ", numberTotalVisitAwaitingReportOustideTheOffice=" + numberTotalVisitAwaitingReportOustideTheOffice +
                ", numberTotalVisitAwaitingReportAtTheOffice=" + numberTotalVisitAwaitingReportAtTheOffice +
                ", numberTotalVisitAwaitingReportPhoneCall=" + numberTotalVisitAwaitingReportPhoneCall +
                ", totalPlannedVisit=" + totalPlannedVisit +
                ", totalArchivateVisit=" + totalArchivateVisit +
                ", totalAwaitingReportVisit=" + totalAwaitingReportVisit +
                ", toatlVisitNotExecuted=" + totalVisitNotExecuted +
                '}';
    }

    @Override
    public int compareTo(@NotNull DashboardVisitBeans o) {
        return o.totalVisit.compareTo(this.totalVisit);
    }
}
