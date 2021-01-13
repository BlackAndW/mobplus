import Vue from 'vue';
import ApiAuth from '@/api/auth';
import { ACCESS_TOKEN } from '@/store/mutation-types';

const user = {
    state: {
        token: '',
        name: '',
        welcome: '',
        avatar: '',
        perms: [],
        roles: [],
        info: {}
    },

    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token;
        },
        SET_INFO: (state, info) => {
            state.info = info;
            state.name = info.nickName || info.account || info.name;
            state.welcome = info.welcome;
            state.avatar = info.avatar;
        },
        SET_PERMS: (state, perms) => {
            state.perms = perms;
        },
        SET_ROLES: (state, roles) => {
            state.roles = roles;
        }
    },

    actions: {
        // 登录
        Login ({ commit }, params) {
            return new Promise((resolve, reject) => {
                ApiAuth.login(params).then(token => {
                    Vue.ls.set(ACCESS_TOKEN, token, 24 * 60 * 60 * 1000);
                    commit('SET_TOKEN', token);
                    resolve();
                }).catch(error => {
                    reject(error);
                });
            });
        },
        // 获取用户信息 权限
        GetProfile ({ commit }) {
            return new Promise((resolve, reject) => {
                ApiAuth.profile().then(user => {
                    if (user) {
                        commit('SET_INFO', user);
                        return ApiAuth.permission();
                    }
                    reject(new Error('error!'));
                }).then(perms => {
                    if (perms) {
                        if (perms.permissionList) {
                            commit('SET_PERMS', perms.permissionList);
                        }
                        if (perms.roleList) {
                            commit('SET_ROLES', perms.roleList);
                        }
                        resolve();
                    } else {
                        reject(new Error('error!'));
                    }
                }).catch(error => {
                    reject(error);
                });
            });
        },
        // 注销
        Logout ({ commit }) {
            return new Promise((resolve, reject) => {
                ApiAuth.logout().then(() => {
                    resolve();
                }).catch(() => {
                    resolve();
                }).finally(() => {
                    commit('SET_TOKEN', '');
                    commit('SET_PERMS', []);
                    Vue.ls.remove(ACCESS_TOKEN);
                });
            });
        }
    }
};

export default user;
