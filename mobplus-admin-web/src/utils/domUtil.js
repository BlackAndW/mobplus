const appTitle = 'MobPlus';
export const setDocumentTitle = function (title) {
  document.title = `${title} - ${appTitle}`;
  const ua = navigator.userAgent;
  // eslint-disable-next-line
  const regex = /\bMicroMessenger\/([\d\.]+)/
  if (regex.test(ua) && /ip(hone|od|ad)/i.test(ua)) {
    const i = document.createElement('iframe');
    i.src = '/favicon.ico';
    i.style.display = 'none';
    i.onload = function () {
      setTimeout(function () {
        i.remove();
      }, 9);
    };
    document.body.appendChild(i);
  }
};

export const title = appTitle;
export const slogan = '做全世界最好的';
export const welcome = '欢迎来到运营平台';
