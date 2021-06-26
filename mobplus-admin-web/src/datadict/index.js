import Vue from 'vue';
import {
    GeneralStatus, SysCategoryStatus, AdType, FunctionType, GameStatus, RecommendedType, ChannelType, GenStatus,
    CountryList, DimensionsList, MetricsList
} from './datadict';

Vue.prototype.$GeneralStatus = GeneralStatus;// 通用状态  用户, 角色
Vue.prototype.$AdType = AdType;
Vue.prototype.$FunctionType = FunctionType;
Vue.prototype.$SysCategoryStatus = SysCategoryStatus;
Vue.prototype.$GameStatus = GameStatus;
Vue.prototype.$RecommendedType = RecommendedType;
Vue.prototype.$ChannelType = ChannelType;
Vue.prototype.$GenStatus = GenStatus;
Vue.prototype.$CountryList = CountryList;
Vue.prototype.$DimensionsList = DimensionsList;
Vue.prototype.$MetricsList = MetricsList;

export const Gender = [
    { value: 0, label: '女' },
    { value: 1, label: '男' }
];
Vue.prototype.$Gender = Gender;

export const KeywordType = [
    { value: 1, label: '点播' },
    { value: 2, label: '包月' }
];
Vue.prototype.$KeywordType = KeywordType;

/* =============================================== */
export function GetDictLabel (status, val) {
    for (let idx = 0; idx < status.length; idx++) {
        if (status[idx].value !== undefined && status[idx].value === val) {
            return status[idx].label;
        }
    }
    return '未知';
};
export function GetDict (status, val) {
    for (let idx = 0; idx < status.length; idx++) {
        if (status[idx].value !== undefined && status[idx].value === val) {
            return status[idx];
        }
    }
    return {};
};
export function DictFilter (status, val) {
    return status.filter(ele => {
        if (ele.value === val) {
            return true;
        }
        for (let idx = 0; idx < val.length; idx++) {
            if (ele.value === val[idx]) {
                return true;
            }
        }
        return false;
    });
};

export function DictFilterExclude (status, val) {
    return status.filter(ele => {
        if (ele.value === val) {
            return true;
        }
        for (let idx = 0; idx < val.length; idx++) {
            if (ele.value !== val[idx]) {
                return true;
            }
        }
        return false;
    });
};

Vue.prototype.$GetDictLabel = GetDictLabel;
Vue.prototype.$GetDict = GetDict;
Vue.prototype.$DictFilter = DictFilter;
Vue.prototype.$DictFilterExclude = DictFilterExclude;
