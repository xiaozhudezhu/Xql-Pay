package com.swinginwind.xql.pay.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TMembers {
    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : UserID
     */
    private Integer userid;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : Name
     */
    private String name;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : ID
     */
    private String id;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : Password
     */
    private String password;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : UserStatus
     */
    private String userstatus;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : TokenCode
     */
    private String tokencode;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : YearsPlay
     */
    private String yearsplay;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : WeChatID
     */
    private String wechatid;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : Gender
     */
    private String gender;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : Age
     */
    private String age;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : PhoneNum
     */
    private String phonenum;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : Level
     */
    private String level;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : HoursPractice
     */
    private String hourspractice;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : AchivingLevel
     */
    private String achivinglevel;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : Country
     */
    private String country;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : Province
     */
    private String province;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : City
     */
    private String city;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : HeadImgUrl
     */
    private String headimgurl;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : Language
     */
    private String language;

    /**
     * 
     * �� : tmembers
     * ��Ӧ�ֶ� : CreateTime
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createtime;
    
    private String loginToken;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus == null ? null : userstatus.trim();
    }

    public String getTokencode() {
        return tokencode;
    }

    public void setTokencode(String tokencode) {
        this.tokencode = tokencode == null ? null : tokencode.trim();
    }

    public String getYearsplay() {
        return yearsplay;
    }

    public void setYearsplay(String yearsplay) {
        this.yearsplay = yearsplay == null ? null : yearsplay.trim();
    }

    public String getWechatid() {
        return wechatid;
    }

    public void setWechatid(String wechatid) {
        this.wechatid = wechatid == null ? null : wechatid.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum == null ? null : phonenum.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getHourspractice() {
        return hourspractice;
    }

    public void setHourspractice(String hourspractice) {
        this.hourspractice = hourspractice == null ? null : hourspractice.trim();
    }

    public String getAchivinglevel() {
        return achivinglevel;
    }

    public void setAchivinglevel(String achivinglevel) {
        this.achivinglevel = achivinglevel == null ? null : achivinglevel.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
}