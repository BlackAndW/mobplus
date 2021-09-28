export const PageSize = [20, 50, 100];

export const GeneralStatus = [
    { value: 1, label: '未启用' },
    { value: 2, label: '正常' },
    { value: 4, label: '使用中' },
    { value: 5, label: '禁用' }
];

export const SysCategoryStatus = [
    { value: 0, label: '所有' },
    { value: 1, label: '未启用' },
    { value: 2, label: '正常' },
    { value: 4, label: '使用中' },
    { value: 5, label: '禁用' }
];

export const AdType = [
    { value: 0, label: '所有' },
    { value: 1, label: '开屏--Splash' },
    { value: 2, label: '横屏--Banner' },
    { value: 3, label: '插屏--Interstitial' },
    { value: 4, label: '插屏视频--InterstitialVideo' },
    { value: 5, label: '原生模板--NativeExpress' },
    { value: 6, label: '激励视频--RewardVideo' },
    { value: 7, label: 'Draw信息流--Draw' },
    { value: 8, label: 'Draw横版信息流--Draw' }
];

export const FunctionType = [
    { value: 0, label: '所有' },
    { value: 1, label: '垃圾清理' },
    { value: 2, label: '手机加速' },
    { value: 3, label: '微信清理' },
    { value: 4, label: 'QQ清理' },
    { value: 5, label: '病毒优化' },
    { value: 6, label: '网络优化' },
    { value: 7, label: '手机降温' },
    { value: 8, label: '手机省电' }
];

export const taskFunctionList = [
    { value: 1, label: '拨号' },
    { value: 2, label: '定位' },
    { value: 3, label: '弹窗' },
    { value: 4, label: '清理' },
    { value: 5, label: '加速' },
    { value: 6, label: '杀毒' },
    { value: 7, label: '微信' },
    { value: 8, label: '降温' },
    { value: 9, label: '网络优化' }
];

export const RecommendedType = [
    { value: 0, label: '不限' },
    { value: 1, label: '热门爆款' },
    { value: 9, label: '精彩推荐' }
];

export const GameStatus = [
    { value: 1, label: '未启用' },
    { value: 2, label: '已启用' }
];

export const ChannelType = [
    { value: 0, label: '所有' },
    { value: 1, label: '应用市场' },
    { value: 2, label: '厂商渠道' }
];

export const GenStatus = [
    { value: 0, label: '所有' },
    { value: 1, label: '未启用' },
    { value: 2, label: '已启用' }
];

export const CountryList = [
    { name: '美国', value: 'US', name_en: 'United States', summary_en: 'San Francisco', iconUrl: 'flag_us.png', id: 1 },
    { name: '英国', value: 'GB', name_en: 'United Kindom', summary_en: 'London', iconUrl: 'flag_gb.png', id: 2 },
    { name: '德国', value: 'DE', name_en: 'Germany', summary_en: 'Frankfurt', iconUrl: 'flag_de.png', id: 3 },
    { name: '意大利', value: 'IT', name_en: 'Italy', summary_en: 'Milan', iconUrl: 'flag_it.png', id: 4 },
    { name: '澳大利亚', value: 'AU', name_en: 'Australia', summary_en: 'Sydney', iconUrl: 'flag_au.png', id: 5 },
    { name: '香港', value: 'HK', name_en: 'Hong Kong', summary_en: 'Hong Kong', iconUrl: 'flag_hk.png', id: 6 },
    { name: '日本', value: 'JP', name_en: 'Japan', summary_en: 'Tokyo', iconUrl: 'flag_jp.png', id: 7 },
    { name: '韩国', value: 'KR', name_en: 'Korea', summary_en: 'Seoul', iconUrl: 'flag_kr.png', id: 8 },
    { name: '新加坡', value: 'SG', name_en: 'Singapore', summary_en: 'Singapore', iconUrl: 'flag_sg.png', id: 9 },
    { name: '印度', value: 'IN', name_en: 'India', summary_en: 'Mumbai', iconUrl: 'flag_in.png', id: 10 },
    { name: '加拿大', value: 'CA', name_en: 'Canada', summary_en: 'Toronto', iconUrl: 'flag_ca.png', id: 11 },
    { name: '中国', value: 'CN', name_en: 'China', summary_en: 'China', iconUrl: 'flag_cn.png', id: 12 }
];

export const DimensionsList = [
    { key: '应用', value: 'APP', width: 100 },
    { key: '广告单元', value: 'AD_UNIT', width: 100 },
    { key: '日期', value: 'DATE', width: 100 },
    { key: '国家', value: 'COUNTRY', width: 100 },
    { key: '广告类型', value: 'AD_TYPE', width: 100 },
    { key: '格式', value: 'FORMAT', width: 100 },
    { key: '月份', value: 'MONTH', width: 100 },
    { key: '星期', value: 'WEEK', width: 100 }
];

export const MetricsList = [
    { key: '估算收入', value: 'ESTIMATED_EARNINGS', width: 100 },
    { key: '千次展示费用', value: 'IMPRESSION_RPM', width: 120 },
    { key: '请求次数', value: 'AD_REQUESTS', width: 100 },
    { key: '匹配率', value: 'MATCH_RATE', width: 100 },
    { key: '匹配请求数', value: 'MATCHED_REQUESTS', width: 100 },
    { key: '展示率', value: 'SHOW_RATE', width: 100 },
    { key: '展示次数', value: 'IMPRESSIONS', width: 100 },
    { key: '展示点击率', value: 'IMPRESSION_CTR', width: 100 },
    { key: '点击次数', value: 'CLICKS', width: 100 }
];

export const LevelList = [
    { key: '广告系列', value: 'campaign', width: 100 },
    { key: '广告组', value: 'adset', width: 100 },
    { key: '广告', value: 'ad', width: 100 }
];

export const ChargeOrderList = [
    { key: '日期', value: 'createdAt' },
    { key: '权重', value: 'weight' },
    { key: '使用次数', value: 'useNum' }
];
