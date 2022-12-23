// Import the crypto-js library

function isEmail(email) {
    return /^\w+@\w+\.\w+$/.test(email);
}

function isAddress(address) {
    return /^[\u4e00-\u9fa5a-zA-Z0-9\\s]+$/.test(address);
}

function isPhone(phone) {
    return /^\d{3}-\d{3}-\d{4}$/.test(phone) || /^\d{10,11}$/.test(phone) || /^\+?86\d{11}$/.test(phone);
}

function isUsername(username) {
    return /^[a-zA-Z][a-zA-Z0-9_.-]{4,20}$/.test(username);
}

function isName(name) {
    // return name.matches("^[\u4e00-\u9fa5a-zA-Z\\s]+$");
    return /^[\u4e00-\u9fa5a-zA-Z\s]+$/.test(name);
}

function isJobTitle(jobTitle) {
    // manager, merchant, contractor
    return /^(manager|merchant|contractor)$/.test(jobTitle.toLowerCase());
}

function isCompany(company) {
    // company1, company2, company3, company4, company5
    return /^(company1|company2|company3|company4|company5)$/.test(company.toLowerCase());
}

function isIdNumber(idNumber) {
    // 作者：毛瑞_三十课
    // 链接：https://juejin.cn/post/6844903575877861390
    //     来源：稀土掘金
    // 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    // 1. 18位身份证号码的正则表达式
    const reg = /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\d{3}[0-9Xx]$/;
    if (!reg.test(idNumber)) {
        return false;
    }

    // 2. 18位身份证号码各个数字的含义：
    // 1-6位：省、市、县、区
    // 7-14位：出生年、月、日
    // 15-17位：顺序码
    // 18位：校验码

    // 检查省份编码
    let checkProv = function (val) {
        const pattern = /^[1-9][0-9]/;
        const provs = {
            11: "北京",
            12: "天津",
            13: "河北",
            14: "山西",
            15: "内蒙古",
            21: "辽宁",
            22: "吉林",
            23: "黑龙江",
            31: "上海",
            32: "江苏",
            33: "浙江",
            34: "安徽",
            35: "福建",
            36: "江西",
            37: "山东",
            41: "河南",
            42: "湖北",
            43: "湖南",
            44: "广东",
            45: "广西",
            46: "海南",
            50: "重庆",
            51: "四川",
            52: "贵州",
            53: "云南",
            54: "西藏",
            61: "陕西",
            62: "甘肃",
            63: "青海",
            64: "宁夏",
            65: "新疆",
            71: "台湾",
            81: "香港",
            82: "澳门",
            91: "国外",
        };
        if (pattern.test(val)) {
            if (provs[val]) {
                return true;
            }
        }
        return false;
    }

    // 检查出生日期
    let checkDate = function (val) {
        let pattern = /^(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)$/;
        if (pattern.test(val)) {
            let year = val.substring(0, 4);
            let month = val.substring(4, 6);
            let date = val.substring(6, 8);
            let date2 = new Date(year + "-" + month + "-" + date);
            if (date2 && date2.getMonth() === (parseInt(month) - 1)) {
                return true;
            }
        }
        return false;
    }

    // 检查校验码
    // https://blog.csdn.net/weixin_39699061/article/details/103315330
    let checkCode = function (val) {
        let p = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
        let factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
        let parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
        let code = val.substring(17);
        if (p.test(val)) {
            let sum = 0;
            for (let i = 0; i < 17; i++) {
                // console.log("i: " + i, "val[i]: " + val[i], "factor[i]: " + factor[i]);
                sum += val[i] * factor[i];
            }
            if (parity[sum % 11] == code) {
                return true;
            }
        }
        return false;
    }

    // 检查身份证号码
    let checkID = function (val) {
        if (checkCode(val)) {
            let date = val.substring(6, 14);
            if (checkDate(date)) {
                if (checkProv(val.substring(0, 2))) {
                    return true;
                }
            }
        }
        return false;
    }

    return checkID(idNumber);
}
