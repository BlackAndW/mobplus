import Vue from 'vue';
import Vue2Filters from 'vue2-filters';
import * as filters from './filter';
import { millisecondToDate } from '@/utils/date';
import { percentfmt, decimalfmt, numberfmt } from '@/utils/util';

Vue.use(Vue2Filters);

Object.keys(filters).forEach(key => {
    Vue.filter(key, filters[key]);
});

Vue.filter('fen2yuan', function (value) {
    if (value) {
        return Number(value / 100).toFixed(2);
    }
});
Vue.filter('percent', percentfmt);
Vue.filter('decimal', decimalfmt);
Vue.filter('NumberFormat', numberfmt);

Vue.filter('datefmt', function (value) {
    return millisecondToDate(value);
});
Vue.filter('datems', millisecondToDate);
