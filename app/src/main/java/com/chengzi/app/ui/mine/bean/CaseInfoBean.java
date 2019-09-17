package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.chengzi.app.utils.DateUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CaseInfoBean implements Serializable {
    /**
     * id : 1
     * user_id : 1
     * user_name : 葡萄干
     * user_sex : 1
     * user_birthday : 0000-00-00
     * user_codes : 132034654131313446
     * address : 1
     * phone : 6525179
     * height : 200
     * weight : 80
     * blood_type : 1
     * heart_rate : 80
     * blood_pressure_low : 80
     * blood_pressure_high : 120
     * operation : 无
     * trauma : 无
     * beauty : 无
     * allergy_drugs : 无
     * sidease : 2
     * smoke : 1
     * drink_wine : 1
     * exercise : 1
     * course : 无
     * image : ["www.vue_admin.com/uploads/2019-04-08/5cab251eb389a.mp4","www.vue_admin.com/public/uploads/2019-04-08/5cab16af6e5bc.mp4"]
     * create_time : 1554350306
     * update_time : 1554350306
     * delete_time : null
     */

    private String id;            // 病例id
    private String user_id;       // 病例所属用户id
    private String user_name;     // 用户姓名
    private String user_sex;      // 性别
    private String user_birthday; // 生日
    private String user_codes;    // 身份证号码
    private String address;       // 地址
    private String phone;         // 联系电话
    private String height;        // 身高
    private String weight;        // 体重
    private String blood_type;    // 血型 1-A 2-B 3-AB 4-O 5-其他
    private String heart_rate;    // 心率
    private String blood_pressure_low;  // 血压/低
    private String blood_pressure_high; // 血压/高
    private String operation;     // 以往手术经历
    private String trauma;        // 外伤经历
    private String beauty;        // 变美经历
    private String allergy_drugs; // 过敏药物
    private String sidease;       // 慢性病 1-有 2-无
    private String family_diseases;       // 是否有家族遗传病 1-有 2-无
    private String smoke;         // 吸烟频率 1、不吸烟 2、3支/周 3、1-4支/天 4、一天超过5支
    private String drink_wine;    // 饮酒频率 1-每年小于3/4次  2-每周小于3次 3-每天
    private String exercise;      // 锻炼频率 1-不锻炼 2-1次/周 3- 3次/周  4-大于3次/周
    private String course;        // 变美历程
    private String create_time;   // 操作时间
    private String update_time;
    private Object delete_time;
    private List<String> image;   // 图片
    private String cover_image;   // 图片
    private String user_image;      //用户头像
    private String image_type;  // 图片视频类型 0-图片视频都没有，1-图片 2-视频

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sex() {
        return !TextUtils.isEmpty(user_sex) ? user_sex : "0";
    }

    public String getUser_sex_name() {
//        return !TextUtils.isEmpty(user_sex) ? user_sex : "0";
        if (!TextUtils.isEmpty(user_sex)) {
            if (user_sex.equals("1")) {
                return "男";
            } else {
                return "女";
            }
        } else {
            return "";
        }
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public String getUser_birthday_year_month_day() {
        return DateUtils.dataTime(user_birthday);
    }

    public String getUser_birthday_name() {
//        return user_birthday;
        try {
            //计算？岁
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy");
            String year_str = formatter_year.format(curDate);
            //获取当前年
            int year_int = (int) Double.parseDouble(year_str);

            long birthday = Integer.parseInt(user_birthday);
            if (String.valueOf(birthday).length() <= 10) {
                birthday = birthday * 1000;
            }
            //生日年
            int yyyy = Integer.parseInt(DateUtils.dataTime(String.valueOf(birthday), "yyyy"));

            long ages = year_int - yyyy;
            if (ages >= 0) {
                return ages + "岁";
            } else {
                return "0岁";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0岁";
        }
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public String getUser_codes() {
        return user_codes;
    }

    public void setUser_codes(String user_codes) {
        this.user_codes = user_codes;
    }

    public String getAddress() {
        return !TextUtils.isEmpty(address) ? address : "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeight() {
        return !TextUtils.isEmpty(height) ? height : "";
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return !TextUtils.isEmpty(weight) ? weight : "";
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public String getBlood_type_name() {    // 血型 1-A 2-B 3-AB 4-O 5-其他
        if (!TextUtils.isEmpty(blood_type)) {
            if (blood_type.equals("1")) {
                return "A型";
            } else if (blood_type.equals("2")) {
                return "B型";
            } else if (blood_type.equals("3")) {
                return "AB型";
            } else if (blood_type.equals("4")) {
                return "O型";
            } else if (blood_type.equals("5")) {
                return "其他";
            }
        } else {
            return "";
        }
        return "";
    }


    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getHeart_rate() {
        return !TextUtils.isEmpty(heart_rate) ? heart_rate : "";
    }


    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getBlood_pressure_low() {
        return !TextUtils.isEmpty(blood_pressure_low) ? blood_pressure_low : "";
    }

    public void setBlood_pressure_low(String blood_pressure_low) {
        this.blood_pressure_low = blood_pressure_low;
    }

    public String getBlood_pressure_high() {
        return !TextUtils.isEmpty(blood_pressure_high) ? blood_pressure_high : "";
    }

    public void setBlood_pressure_high(String blood_pressure_high) {
        this.blood_pressure_high = blood_pressure_high;
    }

    public String getOperation() {
        return operation;
    }

    public String getOperation_name() {
        return !TextUtils.isEmpty(operation) ? operation : "未填写";
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTrauma() {
        return trauma;
    }

    public String getTrauma_name() {
        return !TextUtils.isEmpty(trauma) ? trauma : "未填写";
    }

    public void setTrauma(String trauma) {
        this.trauma = trauma;
    }

    public String getBeauty() {
        return beauty;
    }

    public String getBeauty_name() {
        return !TextUtils.isEmpty(beauty) ? beauty : "未填写";
    }

    public void setBeauty(String beauty) {
        this.beauty = beauty;
    }

    public String getAllergy_drugs() {
        return allergy_drugs;
    }

    public String getAllergy_drugs_name() {
        return !TextUtils.isEmpty(allergy_drugs) ? allergy_drugs : "未填写";
    }

    public void setAllergy_drugs(String allergy_drugs) {
        this.allergy_drugs = allergy_drugs;
    }

    public String getSidease() {
        return sidease;
    }

    public String getSidease_name() {
        return !TextUtils.isEmpty(sidease) ? sidease : "未填写";
    }

    public void setSidease(String sidease) {
        this.sidease = sidease;
    }

    public String getFamily_diseases() {    //是否有家族遗传病 1-有 2-无
        return family_diseases;
    }

    //是否有家族遗传病 1-有 2-无
    public String getFamily_diseases_name() {
        if (!TextUtils.isEmpty(family_diseases)) {
            if (family_diseases.equals("1")) {
                return "是";
            } else {
                return "否";
            }
        } else {
            return "未选择";
        }
    }

    public void setFamily_diseases(String family_diseases) {
        this.family_diseases = family_diseases;
    }

    public String getSmoke() {
        return smoke;
    }

    //吸烟频率 1、不吸烟 2、3支/周 3、1-4支/天 4、一天超过5支
    public String getSmoke_name() {
        if (!TextUtils.isEmpty(smoke)) {
            if (smoke.equals("1")) {
                return "不吸烟";
            } else if (smoke.equals("2")) {
                return "3支/周";
            } else if (smoke.equals("3")) {
                return "1-4支/天";
            } else {
                return "一天超过5支";
            }
        } else {
            return "未选择";
        }
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getDrink_wine() {
        return drink_wine;
    }

    //  // 饮酒频率 1-每年小于3/4次 2-每周小于3次 3-每天
    public String getDrink_wine_name() {
        if (!TextUtils.isEmpty(drink_wine)) {
            if (drink_wine.equals("1")) {
                return "每年小于3/4次";
            } else if (drink_wine.equals("2")) {
                return "每周小于3次";
            } else {
                return "每天";
            }
        } else {
            return "未选择";
        }
    }

    public void setDrink_wine(String drink_wine) {
        this.drink_wine = drink_wine;
    }

    public String getExercise() {
        return exercise;
    }

    ////锻炼频率 1-不锻炼 2-1次/周 3- 3次/周 4->3次/周(大于3次/周)
    public String getExercise_name() {
        if (!TextUtils.isEmpty(exercise)) {
            if (exercise.equals("1")) {
                return "不锻炼";
            } else if (exercise.equals("2")) {
                return "1次/周";
            } else if (exercise.equals("3")) {
                return "3次/周";
            } else {
                return ">3次/周";
            }
        } else {
            return "未选择";
        }
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Object getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Object delete_time) {
        this.delete_time = delete_time;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }
}