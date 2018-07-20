package io.renren.modules.hxy.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

/**
 * @author Songxiaoyan
 * @since 2.0.0 2018/7/18
 */
@TableName("sz_school")
public class SzSchoolEntity {

    @TableId
    private int schoolId;

    private int manageuserId;

    private String schoolName;

    private int schoolType;

    private String schoolArea;

    private String schoolAddress;

    private String schoolTel;

    private String cardImg;

    private String homepage;

    @TableLogic
    private int state;

    private String attendanceMode;

    private int istest;

    private int isparentcard;

    private int ischecksms;

    private Date intime;

    private String numtype;

    private int isvn;

    private int bkstate;

    private String backupCard;

    private String usedCard;

    private String settlementCard;

    private int packageType;

    private int paymentMethod;

    private Date packageTime;

    private int packageYear;

    private int beginMonth;

    private double preferential;

    private int fee_month;

    /**
     * 学校虚拟网全号
     */
    private String schoolNum;

    private int scale;

    private String fax;

    private String email;

    private String area;

    private String englishname;

    private int type;

    private int yearlength;

    private Date term1;

    private Date term1end;

    private Date term2;

    private Date term2end;

    private int weekends;

    private String logo;

    private String msgtypes;

    /**
     * 是否发考勤消息，发考勤短信等的开关
     */
    private int kqNumtype;

    private int projectuserId;

    /**
     * 学生虚拟网类型：1表示98短号，2-长号
     */
    private int networkType;

    /**
     *  0—合作学校，1—新增非合作学校
     */
    private int isNew;

    /**
     * 短信条数，-1不限制
     */
    private int messCount;

    /**
     * 已用条数
     */
    private int finshMessCount;

    /**
     * 订购业务包id
     */
    private int funbagId;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getManageuserId() {
        return manageuserId;
    }

    public void setManageuserId(int manageuserId) {
        this.manageuserId = manageuserId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(int schoolType) {
        this.schoolType = schoolType;
    }

    public String getSchoolArea() {
        return schoolArea;
    }

    public void setSchoolArea(String schoolArea) {
        this.schoolArea = schoolArea;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getSchoolTel() {
        return schoolTel;
    }

    public void setSchoolTel(String schoolTel) {
        this.schoolTel = schoolTel;
    }

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAttendanceMode() {
        return attendanceMode;
    }

    public void setAttendanceMode(String attendanceMode) {
        this.attendanceMode = attendanceMode;
    }

    public int getIstest() {
        return istest;
    }

    public void setIstest(int istest) {
        this.istest = istest;
    }

    public int getIsparentcard() {
        return isparentcard;
    }

    public void setIsparentcard(int isparentcard) {
        this.isparentcard = isparentcard;
    }

    public int getIschecksms() {
        return ischecksms;
    }

    public void setIschecksms(int ischecksms) {
        this.ischecksms = ischecksms;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getNumtype() {
        return numtype;
    }

    public void setNumtype(String numtype) {
        this.numtype = numtype;
    }

    public int getIsvn() {
        return isvn;
    }

    public void setIsvn(int isvn) {
        this.isvn = isvn;
    }

    public int getBkstate() {
        return bkstate;
    }

    public void setBkstate(int bkstate) {
        this.bkstate = bkstate;
    }

    public String getBackupCard() {
        return backupCard;
    }

    public void setBackupCard(String backupCard) {
        this.backupCard = backupCard;
    }

    public String getUsedCard() {
        return usedCard;
    }

    public void setUsedCard(String usedCard) {
        this.usedCard = usedCard;
    }

    public String getSettlementCard() {
        return settlementCard;
    }

    public void setSettlementCard(String settlementCard) {
        this.settlementCard = settlementCard;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPackageTime() {
        return packageTime;
    }

    public void setPackageTime(Date packageTime) {
        this.packageTime = packageTime;
    }

    public int getPackageYear() {
        return packageYear;
    }

    public void setPackageYear(int packageYear) {
        this.packageYear = packageYear;
    }

    public int getBeginMonth() {
        return beginMonth;
    }

    public void setBeginMonth(int beginMonth) {
        this.beginMonth = beginMonth;
    }

    public double getPreferential() {
        return preferential;
    }

    public void setPreferential(double preferential) {
        this.preferential = preferential;
    }

    public int getFee_month() {
        return fee_month;
    }

    public void setFee_month(int fee_month) {
        this.fee_month = fee_month;
    }

    public String getSchoolNum() {
        return schoolNum;
    }

    public void setSchoolNum(String schoolNum) {
        this.schoolNum = schoolNum;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEnglishname() {
        return englishname;
    }

    public void setEnglishname(String englishname) {
        this.englishname = englishname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getYearlength() {
        return yearlength;
    }

    public void setYearlength(int yearlength) {
        this.yearlength = yearlength;
    }

    public Date getTerm1() {
        return term1;
    }

    public void setTerm1(Date term1) {
        this.term1 = term1;
    }

    public Date getTerm1end() {
        return term1end;
    }

    public void setTerm1end(Date term1end) {
        this.term1end = term1end;
    }

    public Date getTerm2() {
        return term2;
    }

    public void setTerm2(Date term2) {
        this.term2 = term2;
    }

    public Date getTerm2end() {
        return term2end;
    }

    public void setTerm2end(Date term2end) {
        this.term2end = term2end;
    }

    public int getWeekends() {
        return weekends;
    }

    public void setWeekends(int weekends) {
        this.weekends = weekends;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMsgtypes() {
        return msgtypes;
    }

    public void setMsgtypes(String msgtypes) {
        this.msgtypes = msgtypes;
    }

    public int getKqNumtype() {
        return kqNumtype;
    }

    public void setKqNumtype(int kqNumtype) {
        this.kqNumtype = kqNumtype;
    }

    public int getProjectuserId() {
        return projectuserId;
    }

    public void setProjectuserId(int projectuserId) {
        this.projectuserId = projectuserId;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getMessCount() {
        return messCount;
    }

    public void setMessCount(int messCount) {
        this.messCount = messCount;
    }

    public int getFinshMessCount() {
        return finshMessCount;
    }

    public void setFinshMessCount(int finshMessCount) {
        this.finshMessCount = finshMessCount;
    }

    public int getFunbagId() {
        return funbagId;
    }

    public void setFunbagId(int funbagId) {
        this.funbagId = funbagId;
    }
}
