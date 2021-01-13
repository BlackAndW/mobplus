import Vue from 'vue';
import Router from 'vue-router';
import { constantRouterMap } from '@/config/router.config';

Vue.use(Router);

/** 解决相同路上时报错的问题 */
const originalPush = Router.prototype.push;
Router.prototype.push = function push (location) {
    return originalPush.call(this, location).catch(err => err);
};

export default new Router({
    // mode: 'history',
    // base: process.env.BASE_URL,
    // scrollBehavior: () => ({ y: 0 }),
    routes: constantRouterMap
});
