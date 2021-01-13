import md5 from 'md5';
import sha1 from 'sha1';

export function timeFix () {
  const time = new Date();
  const hour = time.getHours();
  return hour < 9 ? '早上好' : hour <= 11 ? '上午好' : hour <= 13 ? '中午好' : hour < 20 ? '下午好' : '晚上好';
}

export function welcome () {
  const arr = ['休息一会儿吧', '准备吃什么呢?', '要不要打一把 DOTA', '我猜你可能累了'];
  const index = Math.floor(Math.random() * arr.length);
  return arr[index];
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent () {
  const event = document.createEvent('HTMLEvents');
  event.initEvent('resize', true, true);
  event.eventType = 'message';
  window.dispatchEvent(event);
}

export function handleScrollHeader (callback) {
  let timer = 0;

  let beforeScrollTop = window.pageYOffset;
  callback = callback || function () {};
  window.addEventListener(
    'scroll',
    event => {
      clearTimeout(timer);
      timer = setTimeout(() => {
        let direction = 'up';
        const afterScrollTop = window.pageYOffset;
        const delta = afterScrollTop - beforeScrollTop;
        if (delta === 0) {
          return false;
        }
        direction = delta > 0 ? 'down' : 'up';
        callback(direction);
        beforeScrollTop = afterScrollTop;
      }, 50);
    },
    false
  );
}

/**
 * Remove loading animate
 * @param id parent element id or class
 * @param timeout
 */
export function removeLoadingAnimate (id = '', timeout = 1500) {
  if (id === '') {
    return;
  }
  setTimeout(() => {
    document.body.removeChild(document.getElementById(id));
  }, timeout);
}

/**
 *
 *
 *
 *
 *
 **/

 /**
  * 格式化支付信息
  * @param {*} type
  * @param {*} info
  */
export function GetPayInfo (type, info) {
    if (type === 1) {
        return '微信号:' + info.wechat + ' 微信名:' + info.wechatName;
    }
    if (type === 2) {
        return '支付宝账号:' + info.alipay + ' 支付宝名:' + info.alipayName;
    }
    if (type === 3) {
        return '开户行:' + info.bankName + ' 户名:' + info.bankAccountName + ' 账号:' + info.bankAccount;
    }
    return info;
}

/**
 * 格式化佣金数据
 * @param {*} type
 * @param {*} info
 */
export function GetCommissionInfo (type, info) {
    if (type === 1) {
        return '设备:' + info.deviceSn + ' 订单:' + info.orderSn;
    }
    return info;
}

export function percentfmt (value) {
    if (value !== undefined) {
        const v = Number(value * 100).toFixed(2);
        return v + '%';
    }
    return '--';
}
export function decimalfmt (value) {
    if (value !== undefined) {
        return Number(value * 100).toFixed(2);
    }
    return '--';
}
export function numberfmt (value, unit = '') {
    if (value !== undefined) {
        const fmt = value.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,'); // 将整数部分逢三一断
        return fmt + unit;
    }
    return '--';
}
export function currencyfmt (value, unit = '$') {
    if (value !== undefined) {
        const fmt = Number(value).toFixed(2).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,'); // 将整数部分逢三一断
        return unit + fmt;
    }
    return '--';
}

export function cryptoEncode (content) {
    return sha1(md5(content));
}

export function serialize (obj) {
    return Object.keys(obj).map(function (key) {
        if (obj[key] !== undefined) {
            return ''.concat(encodeURIComponent(key), '=').concat(encodeURIComponent(obj[key]));
        } else {
            return ''.concat(encodeURIComponent(key), '=');
        }
    }).join('&');
};
