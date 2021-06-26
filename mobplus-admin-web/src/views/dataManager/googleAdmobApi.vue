<template>
    <div>
        <a-row :gutter="12">
            <a-col :span="4">
                <a-card :bordered="true" :loading="treeloading" style="overflow-x: hidden;height: 630px;">
                    <a-button-group>
                        <a-button
                            type="primary"
                            @click="$refs.table.refresh(true)"
                        >获取数据</a-button>
                        <a-button
                            icon="sync"
                            @click="$refs.table.refresh(true)"
                        />
                    </a-button-group>
                    <hr color="#808080">

                    <a-form :form="form" id="form" ref="form" layout="vertical">
                        <a-form-item label="谷歌账号：">
                            <a-select
                                placeholder="选择账号"
                                v-decorator="[ 'accountId', { initialValue: 'pub-9840918607046381' } ]"
                            >
                                <a-select-option
                                    v-for="item in accountsList"
                                    :key="item.name"
                                    :value="item.accountId"
                                >{{ item.name }}
                                <a-form-item style="margin:0px; padding:0px">
                                    <a-input
                                        v-show="false"
                                        :id="item.name"
                                        v-decorator="[ 'domain', { initialValue: item.domain } ]"
                                    />
                                </a-form-item>
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                        <hr color="#808080">

                        <a-form-item label="时间：">
                            <a-select
                                placeholder="日期范围"
                                v-decorator="[ 'dateBefore', { initialValue: '7'}]"
                            >
                                <a-select-option value="7">过去7天</a-select-option>
                                <a-select-option value="30">过去30天</a-select-option>
                                <a-select-option value="1">昨日</a-select-option>
                                <a-select-option value="0">今日累计</a-select-option>
                                <a-select-option value="01">本月累计</a-select-option>
                                <a-select-option value="02">
                                    自定义
                                    <a-form-item style="margin:0px; padding:0px; position=absolute">
                                        <a-range-picker style="margin-top: 5px" v-decorator="[ 'dateRange' ]" @change="onChangeDate"/>
                                    </a-form-item>
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                        <hr color="#808080">

                        <a-form-item label="维度：">
                            <a-select
                                mode="multiple"
                                placeholder="选择维度"
                                v-decorator="[ 'dimensions', { initialValue: ['APP'] } ]"
                            >
                                <a-select-option
                                    v-for="item in dimensionsList"
                                    :key="item.key"
                                    :value="item.value"
                                >{{ item.key }}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                        <hr color="#808080">

                        <a-form-item label="指标：">
                            <a-select
                                mode="multiple"
                                placeholder="选择指标"
                                v-decorator="[ 'metrics', { initialValue: ['ESTIMATED_EARNINGS', 'IMPRESSION_RPM', 'AD_REQUESTS', 'IMPRESSIONS', 'CLICKS']}]"
                            >
                                <a-select-option
                                    v-for="item in metricsList"
                                    :key="item.key"
                                    :value="item.value"
                                >{{ item.key }}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-form>
                </a-card>
            </a-col>
            <a-col :span="20">
                <a-card :bordered="true" class="card-list">
                    <!--       -->
                    <s-table
                        bordered
                        ref="table"
                        class="card-table"
                        :columns="columns"
                        :data="loadData"
                        :lazy="true"
                        :scroll="{ x: 10 }"
                    >
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import { DimensionsList, MetricsList } from '@/datadict/datadict.js';
// import axios from 'axios';

const url = '/ad/account';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 查询参数
            queryParam: {},
            // 表头
            columns: [],
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            // 加载数据方法
            loadData: this.getReportData,
            // 分类树数据
            accountsList: [],
            dimensionsList: DimensionsList,
            metricsList: MetricsList,
            treeloading: false,
            appTreeData: []
        };
    },
    created () {},
    mounted () {
        this.getColumns();
        this.getAccountList();
    },
    computed: {},
    methods: {
        // 动态展示列
        getColumns () {
            // 清空列值
            this.columns = [];
            this.form.getFieldValue('dimensions').forEach(value => {
                DimensionsList.forEach(item => {
                    if (value === item.value) {
                        this.columns.push({ title: item.key, dataIndex: item.value, width: item.width, fixed: 'left' });
                    }
                });
            });

            this.form.getFieldValue('metrics').forEach(value => {
                MetricsList.forEach(item => {
                    if (value === item.value) {
                        this.columns.push({ title: item.key, dataIndex: item.value, width: item.width });
                    }
                });
            });
            // 空白列不设宽度以适应弹性布局
            this.columns.push({});
        },
        getAccountList () {
            return this.$http.get(url + '/list').then(res => {
                this.accountsList = res.data;
            });
        },

        getReportData (params) {
            this.getColumns();

            const page = Object.assign(params, this.queryParam);
            const values = this.form.getFieldsValue();
            values.pageNo = page.pageNo;
            values.pageSize = page.pageSize;
            console.log(values);
            return this.$http.post(url + '/report', values);
        }
    }
};
</script>

<style lang="less">
    .float-right {
        float: right;
    }
</style>
