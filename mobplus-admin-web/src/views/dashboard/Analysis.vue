<template>
    <div class="page-header-index-wide">
        <a-row :gutter="24">
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
                <metric-card :loading="loadingRt" title="今日销售额" :total="summary.paidAmount||0 | currency ">
                    <a-tooltip title="销售数据(实际付款额)" slot="action">
                        <a-icon type="info-circle-o" />
                    </a-tooltip>
                    <template slot="footer">
                        <div class="chart-metric">下单数<span>{{ summary.orderCount||0 | NumberFormat('笔') }}</span></div>
                        <div class="chart-metric">付款率<span>{{ calcPaidRate(summary) | percent }}</span></div>
                    </template>
                </metric-card>
            </a-col>
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
                <metric-card :loading="loadingRt" title="今日结算额" :total="summary.settleAmount||0 | currency">
                    <a-tooltip title="结算数据(付款额-退款额-手续费)" slot="action">
                        <a-icon type="info-circle-o" />
                    </a-tooltip>
                    <!-- <div>
                        <div class="chart-metric">退款数<span>{{ summary.refundCount | NumberFormat('笔') }}</span></div>
                        <div class="chart-metric">退款额<span>{{ summary.refundAmount | currency }}</span></div>
                    </div> -->
                    <template slot="footer">
                        <!-- <div class="chart-metric">结算数<span>{{ summary.settleCount | NumberFormat('笔') }}</span></div>
                        <div class="chart-metric">退款率<span>{{ summary.refundRate | percent }}</span></div> -->
                        <div class="chart-metric">退款数<span>{{ summary.refundCount||0 | NumberFormat('笔') }}</span></div>
                        <div class="chart-metric">退款额<span>{{ summary.refundAmount||0 | currency }}</span></div>
                    </template>
                </metric-card>
            </a-col>
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
                <metric-card :loading="loadingRt" title="今日营收额" :total="summary.profitAmount||0 | currency">
                    <a-tooltip title="营收数据(结算额-应付佣金-进货成本)" slot="action">
                        <a-icon type="info-circle-o" />
                    </a-tooltip>
                    <!-- <div>
                        <div class="chart-metric">应付佣金<span>{{ summary.commissionAmount | currency }}</span></div>
                        <div class="chart-metric">支付手续费<span>{{ summary.feeAmount | currency }}</span></div>
                    </div> -->
                    <template slot="footer">
                        <div class="chart-metric">综合支出<span>{{ calcFinalCost(summary) | currency }}</span></div>
                        <div class="chart-metric">毛利率<span>{{ calcProfitRate(summary) | percent }}</span></div>
                    </template>
                </metric-card>
            </a-col>
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" v-role="['SuperAdmin', 'Admin']">
                <metric-card :loading="loadingRt" title="今日提现额" :total="summary.agentPaidAmount||0 | currency">
                    <a-tooltip title="提现付款数据" slot="action">
                        <a-icon type="info-circle-o" />
                    </a-tooltip>
                    <!-- <div>
                        <div class="chart-metric">申请数<span>{{ summary.agentApplyCount | NumberFormat('笔') }}</span></div>
                        <div class="chart-metric">申请额<span>{{ summary.agentApplyAmount | currency }}</span></div>
                    </div> -->
                    <template slot="footer">
                        <div class="chart-metric">申请数<span>{{ summary.agentApplyCount||0 | NumberFormat('笔') }}</span></div>
                        <div class="chart-metric">付款数<span>{{ summary.agentPaidCount||0 | NumberFormat('笔') }}</span></div>
                        <!-- <div class="chart-metric">付款额<span>{{ summary.agentPaidAmount | currency }}</span></div> -->
                    </template>
                </metric-card>
            </a-col>
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" v-role="['AgentA', 'AgentB']">
                <metric-card :loading="loadingRt" title="账户余额" :total="agentBalance.balance | currency">
                    <a-tooltip title="当前账户余额" slot="action">
                        <a-icon type="info-circle-o" />
                    </a-tooltip>
                    <!-- <div>
                        <div class="chart-metric">申请数<span>{{ summary.agentApplyCount | NumberFormat('笔') }}</span></div>
                        <div class="chart-metric">申请额<span>{{ summary.agentApplyAmount | currency }}</span></div>
                    </div> -->
                    <template slot="footer">
                        <div class="chart-metric">冻结<span>{{ agentBalance.frozenAmount | currency }}</span></div>
                        <div class="chart-metric">可用<span>{{ (agentBalance.balance - agentBalance.frozenAmount) | currency }}</span></div>
                    </template>
                </metric-card>
            </a-col>
        </a-row>

        <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
            <div class="salesCard">
                <a-tabs
                    default-active-key="1"
                    size="large"
                    :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}"
                >
                    <div class="extra-wrapper" slot="tabBarExtraContent">
                        <div class="extra-item">
                            <!-- <a>今日</a>-->
                            <a @click="onPrevWeekClicked">上周</a>
                            <a @click="onCurrWeekClicked">本周</a>
                            <a @click="onPrevMonthClicked">上月</a>
                            <a @click="onCurrMonthClicked">本月</a>
                            <!-- <a @click="onCurrYearClicked">本年</a> -->
                        </div>
                        <a-range-picker
                            v-model="queryParam.timeRange"
                            :format="dateFormat"
                            :style="{width: '256px'}"
                            @change="onDateRangeChange"
                            @ok="onDateRangeChange"
                        />
                    </div>
                    <a-tab-pane loading="true" tab="销售额" key="1">
                        <a-row>
                            <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
                                <area-chart :title="orderData.title" :scale="orderData.scale" :data="lineData" />
                            </a-col>
                        </a-row>
                    </a-tab-pane>
                    <a-tab-pane tab="结算额" key="2">
                        <a-row>
                            <a-col :xl="24" :lg="24" :md="12" :sm="24" :xs="24">
                                <area-chart :title="revenueData.title" :scale="revenueData.scale" :data="lineData" />
                            </a-col>
                        </a-row>
                    </a-tab-pane>
                </a-tabs>
            </div>
        </a-card>

        <div
            class="antd-pro-pages-dashboard-analysis-twoColLayout"
            :class="isDesktop() ? 'desktop' : ''"
            v-action="['dashboard.agent.list', 'dashboard.device.list']"
        >
            <a-row :gutter="24" v-action="['dashboard.agent.list', 'dashboard.device.list']">
                <a-col
                    :xl="12"
                    :lg="24"
                    :md="24"
                    :sm="24"
                    :xs="24"
                    :span="3"
                    v-action="['dashboard.agent.list']">
                    <a-card :loading="loading" :bordered="false">
                        <template slot="title">代理销售排行 <span style="font-size:12px">TOP 10</span></template>
                        <div class="ant-table-wrapper">
                            <a-table
                                row-key="index"
                                size="small"
                                :bordered="false"
                                :columns="agentOrderColumn"
                                :dataSource="agentOrderData"
                                :pagination="false"
                            >
                            </a-table>
                        </div>
                    </a-card>
                </a-col>
                <a-col
                    :xl="devicePanelWidth"
                    :lg="24"
                    :md="24"
                    :sm="24"
                    :xs="24"
                    :span="3"
                    v-action="['dashboard.device.list']">
                    <a-card :loading="loading" :bordered="false">
                        <template slot="title">设备销售排行 <span style="font-size:12px">TOP 10</span></template>
                        <div class="ant-table-wrapper">
                            <a-table
                                row-key="index"
                                size="small"
                                :bordered="false"
                                :columns="deviceOrderColumn"
                                :dataSource="deviceOrderData"
                                :pagination="false"
                            >
                            </a-table>
                        </div>
                    </a-card>
                </a-col>
            </a-row>
        </div>
    </div>
</template>

<script>
import moment from 'moment';
import { dateRangeToMs, momentRange } from '@/utils/date';
import { currencyfmt, numberfmt } from '@/utils/util';
import MetricCard from './charts/MetricCard';
import AreaChart from './charts/Area';
import PieChart from './charts/Pie';
import { mixinDevice } from '@/utils/mixin';

const currencyRender = (value, row, index) => { return currencyfmt(value); };
const numberRender = (value, row, index) => { return numberfmt(value === undefined ? 0 : value, '笔'); };
const compare = (a, b) => ((a || 0) - (b || 0));

const deviceOrderColumnLeafAgent = [
    { title: '排名', dataIndex: 'index', width: 90 },
    { title: '设备编号', dataIndex: 'devSn' },
    { title: '销售量', dataIndex: 'paidCount', align: 'right', customRender: numberRender, sorter: (a, b) => compare(a.paidCount, b.paidCount) },
    { title: '销售额', dataIndex: 'paidAmount', align: 'right', customRender: currencyRender, sorter: (a, b) => compare(a.paidAmount, b.paidAmount) }
];

const deviceOrderColumnOther = [
    { title: '排名', dataIndex: 'index', width: 90 },
    { title: '设备编号', dataIndex: 'devSn' },
    { title: '所属代理', dataIndex: 'agentName' },
    { title: '销售量', dataIndex: 'paidCount', align: 'right', customRender: numberRender, sorter: (a, b) => compare(a.paidCount, b.paidCount) },
    { title: '销售额', dataIndex: 'paidAmount', align: 'right', customRender: currencyRender, sorter: (a, b) => compare(a.paidAmount, b.paidAmount) }
];

const agentOrderColumn = [
    { title: '排名', dataIndex: 'index', width: 90 },
    { title: '代理商', dataIndex: 'name' },
    { title: '结算量', dataIndex: 'settleCount', align: 'right', customRender: numberRender, sorter: (a, b) => compare(a.settleCount, b.settleCount) },
    { title: '结算额', dataIndex: 'settleAmount', align: 'right', customRender: currencyRender, sorter: (a, b) => compare(a.settleAmount, b.settleAmount) },
    { title: '佣金收益', dataIndex: 'commissionAmount', align: 'right', customRender: currencyRender, sorter: (a, b) => compare(a.commissionAmount, b.commissionAmount) }
];

function currMonthRange (type) {
    if (type === 'prevWeek') {
        const startDate = moment().week(moment().week() - 1).startOf('week');
        const endDate = moment().week(moment().week() - 1).endOf('week').set({ hour: 0, minute: 0, second: 0, millisecond: 0 });
        return [startDate, endDate];
    } else if (type === 'currWeek') {
        const startDate = moment().week(moment().week()).startOf('week');
        const endDate = moment().week(moment().week()).endOf('week').set({ hour: 0, minute: 0, second: 0, millisecond: 0 });
        return [startDate, endDate];
    } else if (type === 'prevMonth') {
        const startDate = moment().month(moment().month() - 1).startOf('month');
        const endDate = moment().month(moment().month() - 1).endOf('month').set({ hour: 0, minute: 0, second: 0, millisecond: 0 });
        return [startDate, endDate];
    } else if (type === 'currMonth') {
        const startDate = moment().month(moment().month()).startOf('month');
        const endDate = moment().month(moment().month()).endOf('month').set({ hour: 0, minute: 0, second: 0, millisecond: 0 });
        return [startDate, endDate];
    } else if (type === 'currYear') {
        const startDate = moment().year(moment().year()).startOf('year');
        const endDate = moment().year(moment().year()).endOf('year').set({ hour: 0, minute: 0, second: 0, millisecond: 0 });
        return [startDate, endDate];
    }
}

function calcPaidRate (summary) {
    if (summary.orderCount && summary.orderCount > 0) {
        return summary.paidCount / summary.orderCount;
    }
}
function calcRefundRate (summary) {
    if (summary.paidAmount && summary.paidAmount > 0) {
        return summary.refundAmount / summary.paidAmount;
    }
}
function calcFinalCost (summary) {
    return summary.commissionAmount || 0 + summary.feeAmount || 0 + summary.costAmount || 0;
}
function calcProfitRate (summary) {
    if (summary.costAmount && summary.costAmount > 0) {
        return summary.profitAmount / summary.costAmount;
    }
}
export default {
    name: 'Analysis',
    mixins: [mixinDevice],
    components: {
        MetricCard,
        AreaChart,
        PieChart
    },
    data () {
        return {
            loadingRt: false,
            dateFormat: 'YYYY-MM-DD',
            // 查询条件参数
            queryParam: { timeRange: momentRange() },

            // 汇总摘要数据
            summary: {},

            lineData: [],
            orderData: {
                title: '销量额趋势',
                data: [],
                scale: [
                    { dataKey: 'paidCount', alias: '销售量', min: 0, color: '#FAD337', tickCount: 6 },
                    { dataKey: 'paidAmount', alias: '销售额', min: 0, color: '#3AA0FF', tickCount: 6 }
                ]
            },
            revenueData: {
                title: '结算额趋势',
                data: [],
                scale: [
                    { dataKey: 'settleCount', alias: '结算量', min: 0, color: '#FAD337', tickCount: 6 },
                    { dataKey: 'settleAmount', alias: '结算额', min: 0, color: '#3AA0FF', tickCount: 6 }
                ]
            },

            deviceOrderData: [],
            agentOrderColumn,
            agentOrderData: [],
            agentBalance: {},

            loadingTaskCount: 0
        };
    },
    // created () {
    //     setTimeout(() => {
    //         this.loading = !this.loading;
    //     }, 1000);
    // },
    computed: {
        loading: function () {
            return this.loadingTaskCount > 0;
        },
        deviceOrderColumn: function () {
            if (this.$hasPermissions(['dashboard.agent.list']) && this.$hasPermissions(['dashboard.device.list'])) {
                return deviceOrderColumnOther;
            } else if (!this.$hasPermissions(['dashboard.agent.list']) && this.$hasPermissions(['dashboard.device.list'])) {
                return deviceOrderColumnLeafAgent;
            } else {
                return [];
            }
        },
        devicePanelWidth: function () {
            if (this.$hasPermissions(['dashboard.agent.list'])) {
                return 12;
            }
            return 24;
        }
    },
    mounted () {
        this.$nextTick(() => {
            this.loadRealtimeData();
        });
        this.loadAllData();
    },
    methods: {
        calcPaidRate,
        calcRefundRate,
        calcFinalCost,
        calcProfitRate,
        onPrevWeekClicked: function () {
            this.queryParam.timeRange = currMonthRange('prevWeek');
            this.queryParam.daily = 1;
            this.loadAllData();
        },
        onCurrWeekClicked: function () {
            this.queryParam.timeRange = currMonthRange('currWeek');
            this.queryParam.daily = 1;
            this.loadAllData();
        },
        onPrevMonthClicked: function () {
            this.queryParam.timeRange = currMonthRange('prevMonth');
            this.queryParam.daily = 1;
            this.loadAllData();
        },
        onCurrMonthClicked: function () {
            this.queryParam.timeRange = currMonthRange('currMonth');
            this.queryParam.daily = 1;
            this.loadAllData();
        },
        onCurrYearClicked: function () {
            this.queryParam.timeRange = currMonthRange('currYear');
            this.queryParam.daily = 2;
            this.loadAllData();
        },
        onDateRangeChange: function () {
            this.loadAllData();
        },
        loadAllData: function () {
            this.$nextTick(() => {
                this.loadPlatformPerformanceData();
                 if (this.$hasPermissions(['dashboard.agent.list', 'dashboard.device.list'])) {
                    this.loadAgentPerformanceData();
                    this.loadDevicePerformanceData();
                 } else if (this.$hasPermissions(['dashboard.device.list']) && !this.$hasPermissions(['dashboard.agent.list'])) {
                    this.loadDevicePerformanceData();
                 }
                 if (this.$hasRoles(['AgentA', 'AgentB'])) {
                     this.loadAgentBalance();
                 }
            });
        },
        loadAgentBalance: async function () {
            try {
                this.agentBalance = await this.$http.get('/agent/info/balance/mine');
            } catch (e) {
            }
        },
        loadRealtimeData: async function () {
            this.loadingRt = true;
            try {
                const url = '/stats/finance/rt';
                const result = await this.$http.get(url);
                this.summary = result;
                this.loadingRt = false;
            } catch {
                this.loadingRt = false;
            }
        },
        loadPlatformPerformanceData: async function () {
            this.loadingTaskCount++;
            try {
                const url = '/stats/finance';
                const data = Object.assign({}, dateRangeToMs(this.queryParam.timeRange));
                delete data.timeRange;
                const result = await this.$http.get(url, data);
                this.lineData = this.setDefaultData(result, { paidCount: 0, paidAmount: 0, settleCount: 0, settleAmount: 0 });
                this.loadingTaskCount--;
            } catch {
                this.loadingTaskCount--;
            }
        },
        loadAgentPerformanceData: async function () {
            this.loadingTaskCount++;
            try {
                const url = '/stats/agent';
                const data = Object.assign({}, dateRangeToMs(this.queryParam.timeRange));
                data.pageSize = 10;
                delete data.timeRange;
                const result = await this.$http.get(url, data);
                this.agentOrderData = this.setRankNo(result);
                this.loadingTaskCount--;
            } catch {
                this.loadingTaskCount--;
            }
        },
        loadDevicePerformanceData: async function () {
            this.loadingTaskCount++;
            try {
                const url = '/stats/device';
                const data = Object.assign({}, dateRangeToMs(this.queryParam.timeRange));
                data.pageSize = 10;
                delete data.timeRange;
                const result = await this.$http.get(url, data);
                this.deviceOrderData = this.setRankNo(result);
                this.loadingTaskCount--;
            } catch {
                this.loadingTaskCount--;
            }
        },
        setRankNo: function (list) {
            for (let idx = 0; idx < list.length; idx++) {
               list[idx]['index'] = idx + 1;
            }
            return list;
        },
        setDefaultData: function (list, param) {
            list.forEach(tmp => {
                Object.keys(param).forEach((key) => {
                    if (tmp[key] === undefined) {
                        tmp[key] = param[key];
                    }
                });
                tmp['time'] = moment(tmp['id']).format('YYYY-MM-DD');
            });

            return list;
        }
    }
};
</script>

<style lang="less">
// .antd-pro-pages-dashboard-analysis-twoColLayout {
//     .ant-table-pagination.ant-pagination{
//         margin: 10px 0;
//     }
// }
</style>
<style lang="less" scoped>
.extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
        display: inline-block;
        margin-right: 24px;

        a {
            margin-left: 24px;
        }
    }
}

.antd-pro-pages-dashboard-analysis-twoColLayout {
    .ant-card {
        margin-top: 24px;
        height: 540px;
    }
}

.antd-pro-pages-dashboard-analysis-salesCard {
    height: calc(100% - 24px);
    /deep/ .ant-card-head {
        position: relative;
    }
}

.dashboard-analysis-iconGroup {
    i {
        margin-left: 16px;
        color: rgba(0, 0, 0, 0.45);
        cursor: pointer;
        transition: color 0.32s;
        color: black;
    }
}
.analysis-salesTypeRadio {
    position: absolute;
    right: 54px;
    bottom: 12px;
}

.chart-metric {
    display: inline-block;
    font-size: 14px;
    line-height: 22px;

    margin-right: 12px;
    span{
        &::before{
            content:' ';
        }
        /*margin-left: 6px;*/
        /*margin-right: 12px;*/
    }
    .metric-icon {
        font-size: 12px;
        i {
            font-size: 12px;
            transform: scale(0.83);
        }
    }
}
</style>
