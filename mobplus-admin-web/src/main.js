// ie polyfill
import '@babel/polyfill';

import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store/';
import { VueAxios } from './utils/request';

// mock
// import './mock'

import bootstrap from './core/bootstrap';
import './core/use';
import './permission'; // permission control
import i18n from './locales';

Vue.config.productionTip = false;

Vue.use(VueAxios);

new Vue({
    router,
    store,
    i18n,
    created: bootstrap,
    render: h => h(App)
}).$mount('#app');
