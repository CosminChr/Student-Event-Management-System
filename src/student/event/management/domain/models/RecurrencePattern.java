package student.event.management.domain.models;

import student.event.management.domain.util.DayOfWeekEnum;
import student.event.management.domain.util.MonthEnum;
import student.event.management.domain.util.WeekOrdinalEnum;
import student.event.management.ui.util.RecurrencePatternFrequencyEnum;

import java.util.StringJoiner;

public class RecurrencePattern {

    private Long id;

    private RecurrencePatternFrequencyEnum recurrenceFrequency;

    private Integer everyXDays;

    private Boolean everyWeekday;

    private Integer everyXWeeks;

    private Boolean monday;

    private Boolean tuesday;

    private Boolean wednesday;

    private Boolean thursday;

    private Boolean friday;

    private Boolean saturday;

    private Boolean sunday;

    private Integer xDay;

    private Integer ofEveryXMonthsFirstOption;

    private WeekOrdinalEnum theXMonthly;

    private DayOfWeekEnum dayOfWeekMonthly;

    private Integer ofEveryXMonthsSecondOption;

    private Integer everyXYears;

    private MonthEnum month;

    private Integer x;

    private WeekOrdinalEnum theXYearly;

    private DayOfWeekEnum dayOfWeekYearly;

    private MonthEnum ofX;

    private RecurrencePattern() {

    }

    public static RecurrencePattern builder() {
        return new RecurrencePattern();
    }

    public Long getId() {
        return id;
    }

    public RecurrencePatternFrequencyEnum getRecurrenceFrequency() {
        return recurrenceFrequency;
    }

    public Integer getEveryXDays() {
        return everyXDays;
    }

    public Boolean getEveryWeekday() {
        return everyWeekday;
    }

    public Integer getEveryXWeeks() {
        return everyXWeeks;
    }

    public Boolean getMonday() {
        return monday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public Boolean getFriday() {
        return friday;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public Boolean getSunday() {
        return sunday;
    }

    public Integer getXDay() {
        return xDay;
    }

    public Integer getOfEveryXMonthsFirstOption() {
        return ofEveryXMonthsFirstOption;
    }

    public WeekOrdinalEnum getTheXMonthly() {
        return theXMonthly;
    }

    public DayOfWeekEnum getDayOfWeekMonthly() {
        return dayOfWeekMonthly;
    }

    public Integer getOfEveryXMonthsSecondOption() {
        return ofEveryXMonthsSecondOption;
    }

    public Integer getEveryXYears() {
        return everyXYears;
    }

    public MonthEnum getMonth() {
        return month;
    }

    public Integer getX() {
        return x;
    }

    public WeekOrdinalEnum getTheXYearly() {
        return theXYearly;
    }

    public DayOfWeekEnum getDayOfWeekYearly() {
        return dayOfWeekYearly;
    }

    public MonthEnum getOfX() {
        return ofX;
    }

    public RecurrencePattern setId(Long id) {
        this.id = id;
        return this;
    }

    public RecurrencePattern setRecurrenceFrequency(RecurrencePatternFrequencyEnum recurrenceFrequency) {
        this.recurrenceFrequency = recurrenceFrequency;
        return this;
    }

    public RecurrencePattern setEveryXDays(Integer everyXDays) {
        this.everyXDays = everyXDays;
        return this;
    }

    public RecurrencePattern setEveryWeekday(Boolean everyWeekday) {
        this.everyWeekday = everyWeekday;
        return this;
    }

    public RecurrencePattern setEveryXWeeks(Integer everyXWeeks) {
        this.everyXWeeks = everyXWeeks;
        return this;
    }

    public RecurrencePattern setMonday(Boolean monday) {
        this.monday = monday;
        return this;
    }

    public RecurrencePattern setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
        return this;
    }

    public RecurrencePattern setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
        return this;
    }

    public RecurrencePattern setThursday(Boolean thursday) {
        this.thursday = thursday;
        return this;
    }

    public RecurrencePattern setFriday(Boolean friday) {
        this.friday = friday;
        return this;
    }

    public RecurrencePattern setSaturday(Boolean saturday) {
        this.saturday = saturday;
        return this;
    }

    public RecurrencePattern setSunday(Boolean sunday) {
        this.sunday = sunday;
        return this;
    }

    public RecurrencePattern setxDay(Integer xDay) {
        this.xDay = xDay;
        return this;
    }

    public RecurrencePattern setOfEveryXMonthsFirstOption(Integer ofEveryXMonthsFirstOption) {
        this.ofEveryXMonthsFirstOption = ofEveryXMonthsFirstOption;
        return this;
    }

    public RecurrencePattern setTheXMonthly(WeekOrdinalEnum theXMonthly) {
        this.theXMonthly = theXMonthly;
        return this;
    }

    public RecurrencePattern setDayOfWeekMonthly(DayOfWeekEnum dayOfWeekMonthly) {
        this.dayOfWeekMonthly = dayOfWeekMonthly;
        return this;
    }

    public RecurrencePattern setOfEveryXMonthsSecondOption(Integer ofEveryXMonthsSecondOption) {
        this.ofEveryXMonthsSecondOption = ofEveryXMonthsSecondOption;
        return this;
    }

    public RecurrencePattern setEveryXYears(Integer everyXYears) {
        this.everyXYears = everyXYears;
        return this;
    }

    public RecurrencePattern setMonth(MonthEnum month) {
        this.month = month;
        return this;
    }

    public RecurrencePattern setX(Integer x) {
        this.x = x;
        return this;
    }

    public RecurrencePattern setTheXYearly(WeekOrdinalEnum theXYearly) {
        this.theXYearly = theXYearly;
        return this;
    }

    public RecurrencePattern setDayOfWeekYearly(DayOfWeekEnum dayOfWeekYearly) {
        this.dayOfWeekYearly = dayOfWeekYearly;
        return this;
    }

    public RecurrencePattern setOfX(MonthEnum ofX) {
        this.ofX = ofX;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RecurrencePattern.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("recurrenceFrequency='" + recurrenceFrequency + "'")
                .add("everyXDays=" + everyXDays)
                .add("everyWeekday=" + everyWeekday)
                .add("everyXWeeks=" + everyXWeeks)
                .add("monday=" + monday)
                .add("tuesday=" + tuesday)
                .add("wednesday=" + wednesday)
                .add("thursday=" + thursday)
                .add("friday=" + friday)
                .add("saturday=" + saturday)
                .add("sunday=" + sunday)
                .add("xDay=" + xDay)
                .add("ofEveryXMonthsFirstOption=" + ofEveryXMonthsFirstOption)
                .add("theXMonthly=" + theXMonthly)
                .add("dayOfWeekMonthly=" + dayOfWeekMonthly)
                .add("ofEveryXMonthsSecondOption=" + ofEveryXMonthsSecondOption)
                .add("everyXYears=" + everyXYears)
                .add("month=" + month)
                .add("x=" + x)
                .add("theXYearly=" + theXYearly)
                .add("dayOfWeekYearly=" + dayOfWeekYearly)
                .add("ofX=" + ofX)
                .toString();
    }
}
