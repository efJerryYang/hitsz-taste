package me.efjerryyang.webserver.service;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ValidationService {
    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    public boolean isJavascriptEnabled(String acceptHeader, String jsEnabled) {
        logger.info("checking if javascript is enabled");
        logger.info("acceptHeader: " + acceptHeader);
        logger.info("jsEnabled: " + jsEnabled);
        // chrome:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
        // firefox: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
        if (acceptHeader != null && (acceptHeader.contains("application/javascript") || Objects.equals(jsEnabled, "true"))) {
            // JavaScript is likely enabled on the client
            logger.info("javascript is enabled");
            return true;
        } else {
            // return false if we cannot determine if JavaScript is enabled
            logger.info("javascript is disabled");
            return false;
        }
    }

    public boolean isJavascriptEnabled() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String acceptHeader = request.getHeader("Accept");
        String jsEnabled = request.getParameter("jsEnabled");
        logger.info("checking if javascript is enabled");
        logger.info("acceptHeader: " + acceptHeader);
        logger.info("jsEnabled: " + jsEnabled);
        // chrome:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
        // firefox: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
        if (acceptHeader != null && (acceptHeader.contains("application/javascript") || Objects.equals(jsEnabled, "true"))) {
            // JavaScript is likely enabled on the client
            logger.info("javascript is enabled");
            return true;
        } else {
            // return false if we cannot determine if JavaScript is enabled
            logger.info("javascript is disabled");
            return false;
        }
    }

    public boolean isEmail(String email) {
        return email.matches("^\\w+@\\w+\\.\\w+$");
    }

    // match \^[\u4e00-\u9fa5_a-zA-Z0-9]+$
    // https://blog.csdn.net/weixin_39625258/article/details/114302396
    public boolean isAddress(String address) {
        return address.matches("^[\u4e00-\u9fa5a-zA-Z0-9\\s]+$");
    }

    public boolean isPhone(String phone) {
        return phone.matches("^\\d{3}-\\d{3}-\\d{4}$") || phone.matches("^\\d{10,11}$") || phone.matches("^\\+?86\\d{11}$");
    }

    public boolean isUsername(String username) {
        return username.matches("^[a-zA-Z][a-zA-Z0-9_.-]{3,19}$");
    }

    public boolean isName(String name) {
        return name.matches("^[\u4e00-\u9fa5a-zA-Z\\s]+$");
    }

    public boolean isChineseFirstnameOrLastname(String name) {
        return name.matches("^[\u4e00-\u9fa5]{1,2}$");
    }

    public boolean isEnglishFirstnameOrLastname(String name) {
        return name.matches("^[a-zA-Z]{1,20}$");
    }

    public boolean isRole(String role) {
        return role.matches("^(admin|staff|customer|default)$");
    }

    public boolean isJobTitle(String jobTitle) {
        return jobTitle.matches("^(manager|contractor|merchant)$");
    }

    public boolean isCompany(String company) {
        // company1, company2, company3
        return company.matches("^(company1|company2|company3)$");
    }

    public boolean isIdNumber(String idNumber) {
        // 1. 18位身份证号码的正则表达式
        final String reg = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}[0-9Xx]$";
        if (!idNumber.matches(reg)) {
            logger.info("idNumber.matches(reg) is false");
            return false;
        }
        return checkID(idNumber);
    }

    // 检查身份证号码
    private boolean checkID(String val) {
        if (checkCode(val)) {
            String date = val.substring(6, 14);
            if (checkDate(date)) {
                if (checkProv(val.substring(0, 2))) {
                    return true;
                }
            }
        }
        logger.info("checkID is false");
        return false;
    }

    // 检查校验码
    private boolean checkCode(String val) {
        String p = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] parity = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        String code = val.substring(17);
        if (val.matches(p)) {
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += Integer.parseInt(String.valueOf(val.charAt(i))) * factor[i];
            }
            logger.info(code + " " + parity[sum % 11]);
            if (parity[sum % 11] == code.toUpperCase().charAt(0)) {
                return true;
            }
        }
        logger.info("checkCode is false");
        return false;
    }

    // 检查出生日期
    private boolean checkDate(String val) {
        String pattern = "^(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)$";
        if (val.matches(pattern)) {
            String year = val.substring(0, 4);
            String month = val.substring(4, 6);
            String date = val.substring(6, 8);
            Date date2 = null;
            try {
                date2 = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + date);
            } catch (ParseException e) {
                logger.error("Invalid date format", e);
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date2);

            if (cal.get(Calendar.MONTH) == (Integer.parseInt(month) - 1)) {
                return true;
            }

        }
        return false;
    }

    // 检查省份编码
    private boolean checkProv(String val) {
        final String pattern = "^[1-9][0-9]";
        Map<String, String> provs = new HashMap<>();
        provs.put("11", "北京");
        provs.put("12", "天津");
        provs.put("13", "河北");
        provs.put("14", "山西");
        provs.put("15", "内蒙古");
        provs.put("21", "辽宁");
        provs.put("22", "吉林");
        provs.put("23", "黑龙江");
        provs.put("31", "上海");
        provs.put("32", "江苏");
        provs.put("33", "浙江");
        provs.put("34", "安徽");
        provs.put("35", "福建");
        provs.put("36", "江西");
        provs.put("37", "山东");
        provs.put("41", "河南");
        provs.put("42", "湖北");
        provs.put("43", "湖南");
        provs.put("44", "广东");
        provs.put("45", "广西");
        provs.put("46", "海南");
        provs.put("50", "重庆");
        provs.put("51", "四川");
        provs.put("52", "贵州");
        provs.put("53", "云南");
        provs.put("54", "西藏");
        provs.put("61", "陕西");
        provs.put("62", "甘肃");
        provs.put("63", "青海");
        provs.put("64", "宁夏");
        provs.put("65", "新疆");
        provs.put("71", "台湾");
        provs.put("81", "香港");
        provs.put("82", "澳门");
        provs.put("91", "国外");
        if (val.matches(pattern)) {
            return provs.containsKey(val);
        }
        return false;
    }

    // sanitize the input
    public String sanitizeSearchQuery(String query) {
        // Remove any HTML tags from the query
        query = query.replaceAll("<[^>]*>", "");
        // Remove any JavaScript event handlers from the query
        query = query.replaceAll("on\\w+\\s*=\\s*(\"|')[^\"']*(\"|')", "");
        // Remove any potentially malicious characters from the query
        query = query.replaceAll("[^[\\w\\s-\u4e00-\u9fa5]]", "");

        // Trim leading and trailing whitespace
        query = query.trim();
        // Check if the query contains any SQL injection keywords
        String[] injectionKeywords = {"SELECT", "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER", "TRUNCATE", "UNION"};
        for (String keyword : injectionKeywords) {
            if (query.toUpperCase().contains(keyword)) {
                throw new IllegalArgumentException("Invalid search query with SQL injection keyword: " + keyword);
            }
        }

        // Escape special characters
        query = query.replaceAll("'", "\'");
        query = query.replaceAll("\"", "\\\"");
        query = query.replaceAll("\\\\", "\\\\\\\\");
        return query;
    }
}
