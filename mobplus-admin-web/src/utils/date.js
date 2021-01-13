import moment from 'moment';

export function convertDateFmt (strDate) {
    // if ((typeof date) == Date) {
    //     return formatDate(strDate, 'yyyy-MM-dd hh:mm:ss');
    // }
    return formatCSTDate(strDate, 'yyyy-MM-dd hh:mm:ss');
}

export function millisecondToDate (millisecond) {
    return formatDate(new Date(millisecond), 'yyyy-MM-dd hh:mm:ss');
}
// 格式化CST日期的字串
export function formatCSTDate (strDate, format) {
    const date = new Date(strDate);
    // if ((typeof date) != Date) {
    //     return strDate;
    // }
    return formatDate(date, format);
}

// 格式化日期,
export function formatDate (date, format) {
    // if ( (typeof date) != Date){
    //     return date;
    // }
    var paddNum = function (num) {
        num += '';
        return num.replace(/^(\d)$/, '0$1');
    };
    // 指定格式字符
    var cfg = {
        yyyy: date.getFullYear(), // 年 : 4位
         yy: date.getFullYear().toString().substring(2), // 年 : 2位
         M: date.getMonth() + 1, // 月 : 如果1位的时候不补0
         MM: paddNum(date.getMonth() + 1), // 月 : 如果1位的时候补0
         d: date.getDate(), // 日 : 如果1位的时候不补0
         dd: paddNum(date.getDate()), // 日 : 如果1位的时候补0
         h: date.getHours(), // 时
         hh: paddNum(date.getHours()), // 时
         m: date.getMinutes(), // 分
         mm: paddNum(date.getMinutes()), // 分
         s: date.getSeconds(), // 秒
         ss: paddNum(date.getSeconds()) // 秒
    };
    format || (format = 'yyyy-MM-dd hh:mm:ss');
    return format.replace(/([a-z])(\1)*/ig, function (m) { return cfg[m]; });
}

export function momentRange () {
    const today = moment().set({ hour: 0, minute: 0, second: 0, millisecond: 0 });
    return [moment(today - 86400 * 1000 * 7), today];
}
export function dateRangeToMs (range) {
    return {
        startAt: range[0].valueOf(),
        endAt: range[1].valueOf() + 86400000
    };
}
