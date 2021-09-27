<template>
    <div>
        <a-row :gutter="12">
            <a-col :span="24">
                <a-card :bordered="true" class="card-list">
                    <!--       -->
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l" v-action="['dataManager:query']">
                            <a-form-item>
                                <a-range-picker
                                    v-decorator="[ 'time', { initialValue: [moment(moment(), 'YYYY/MM/DD'), moment(moment(), 'YYYY/MM/DD')] } ]"
                                    @change="onChangeDate"/>
                            </a-form-item>
                            <a-form-item class="float-right">
                                <div>
                                    <a-button
                                        icon="download"
                                        @click="downloadFile"
                                    >导出</a-button><br />
                                </div>
                            </a-form-item>
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    icon="sync"
                                    v-action="['dataManager:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    type="primary"
                                    icon="search"
                                    @click="$refs.table.refresh(true)"
                                >查询</a-button>
                            </a-button-group>
                        </div>
                    </a-form>
                    <!--       -->
                    <s-table
                        bordered
                        ref="table"
                        class="card-table"
                        :columns="columns"
                        :data="loadData"
                        :lazy="true"
                    >
                        <template slot="timeSlot" slot-scope="text">
                            {{ text | moment }}
                        </template>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import moment from 'moment';

const columns = [
    {
        title: '广告单元',
        dataIndex: 'ad_unit'
    },
    {
        title: '国家',
        dataIndex: 'country'
    },
    {
        title: '总展示次数',
        dataIndex: 'total_impressions'
    },
    {
        title: 'ecpm',
        dataIndex: 'total_average_ecpm_usd'
    },
    {
        title: '总点击次数',
        dataIndex: 'total_clicks'
    },
    {
        title: '广告点击率',
        dataIndex: 'total_average_ctr'
    },
    {
        title: '总收入',
        dataIndex: 'total_revenue_usd'
    },
    {
        title: '伙伴收入',
        dataIndex: 'partner_revenue_usd'
    },
    {
        title: '日期',
        dataIndex: 'date'
    }
];

const url = '/data/gamezop';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        moment
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 查询参数
            queryParam: {},
            // 表头
            columns,
            // 加载数据方法
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {},
    computed: {},
    methods: {
        moment,
        onChangeDate (date, dateString) {
            this.queryParam.startDate = dateString[0];
            this.queryParam.endDate = dateString[1];
        },
        downloadFile () {
            this.queryParam.downloadFlag = true;
            this.$refs.table.refresh(true);
            // window.location.href = 'http://res.turbovpns.com/GamezopData.xlsx';
        },
        onDelete: function (record) {
            var params = [];
            if (record !== undefined) {
                params.push(record.id);
            }
            this.$confirm({
                title: '确认删除吗?',
                okText: '确认',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        const data = await this.$http.delete(url, params);
                        if (data) {
                            this.$message.success(data);
                            this.$refs.table.refresh(true);
                        }
                    } catch (err) {}
                }
            });
        },
        loadDataList: function (params) {
            return this.$http.get(
                url,
                Object.assign(params, this.queryParam)
            );
        }
    }
};
</script>

<style lang="less">
    .float-right {
        float: right;
    }
</style>
