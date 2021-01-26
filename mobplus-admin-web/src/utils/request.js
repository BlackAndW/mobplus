import Vue from 'vue';
import axios from 'axios';
import store from '@/store';

import { Modal, notification, message } from 'ant-design-vue';
import { ACCESS_TOKEN } from '@/store/mutation-types';
import { VueAxios } from './axios';

axios.defaults.baseURL = process.env.VUE_APP_API_BASE_URL;
axios.defaults.headers.post['Content-Type'] = 'application/json; charset=UTF-8';
// axios.defaults.timeout = 6000;
axios.defaults.timeout = 60000;

const showReloadAlter = function () {
    const token = Vue.ls.get(ACCESS_TOKEN);
    if (token) {
        Vue.ls.remove(ACCESS_TOKEN);
        Modal.error({
            title: '登录已过期',
            content: '很抱歉，登录已过期，请重新登录',
            okText: '重新登录',
            mask: false,
            onOk: () => {
                store.dispatch('Logout').then(() => {
                    window.location.reload();
                });
            }
        });
    }
};
const err = (error) => {
    if (error.response) {
        const data = error.response.data;
        if (error.response.status === 403) {
            notification.error({ message: '系统提示', description: '拒绝访问', duration: 4 });
        } else if (error.response.status === 401) {
            showReloadAlter();
        } else {
            notification.error({
                message: '系统提示',
                description: data || '请稍后再试',
                duration: 4
            });
        }
    }
    return Promise.reject(error);
};

// request interceptor
axios.interceptors.request.use(config => {
    const token = Vue.ls.get(ACCESS_TOKEN);
    if (token) {
        config.headers['Access-Token'] = token; // 让每个请求携带自定义 token 请根据实际情况自行修改
    }
    return config;
}, err);

// response interceptor
axios.interceptors.response.use((response) => {
    return response.data;
}, err);

const processSuccess = function (respData, resolve, reject) {
    if (respData.code === 2000) {
        resolve(respData.result || '操作成功!');
    } else if (respData.code === 4001) {
        showReloadAlter();
    } else {
        const msg = respData.message || '出错!';
        message.error(msg);

        reject(msg);
    }
};
const processError = function (error, resolve, reject) {
    //    message.error(error.message || '出错')
    reject(error);
};

const http = {
    get: function (url, data) {
        return new Promise((resolve, reject) => {
            axios
                .get(url, { params: data || {} })
                .then(response => {
                    processSuccess(response, resolve, reject);
                })
                .catch(msg => {
                    processError(msg, resolve, reject);
                });
        });
    },
    post: function (url, data) {
        return new Promise((resolve, reject) => {
            axios
                .post(url, data)
                .then(response => {
                    processSuccess(response, resolve, reject);
                })
                .catch(msg => {
                    processError(msg, resolve, reject);
                });
        });
    },
    put: function (url, data) {
        return new Promise((resolve, reject) => {
            axios
                .put(url, data)
                .then(response => {
                    processSuccess(response, resolve, reject);
                })
                .catch(msg => {
                    processError(msg, resolve, reject);
                });
        });
    },
    delete: function (url, data) {
        return new Promise((resolve, reject) => {
            axios
                .delete(url, { data: data || {} })
                .then(response => {
                    processSuccess(response, resolve, reject);
                })
                .catch(msg => {
                    processError(msg, resolve, reject);
                });
        });
    }
};
const installer = {
    vm: {},
    install (Vue) {
        Vue.use(VueAxios, http);
    }
};

export {
    installer as VueAxios
};
export default http;
