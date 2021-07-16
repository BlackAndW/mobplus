<template>
    <div>
        <a-row :gutter="12">
            <a-col :span="4">
                <a-card :bordered="true" :loading="treeloading" style="overflow-x: hidden;height: 555px;">
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
                        <a-form-item label="脸书账号：">
                            <a-select
                                placeholder="选择账号"
                                v-decorator="[ 'accountId', { rules: [ { required: true, message: '请选择账号' }] } ]"
                            >
                                <a-select-option
                                    v-for="item in accountsList"
                                    :key="item.name"
                                    :value="item.id"
                                >{{ item.name }}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                        <hr color="#808080">

                        <a-form-item label="时间：">
                            <a-select
                                placeholder="日期范围"
                                v-decorator="[ 'dateBefore', { initialValue: '-1'}]"
                            >
                                <a-select-option value="-1">昨天</a-select-option>
                                <a-select-option value="3">过去3天</a-select-option>
                                <a-select-option value="7">过去7天</a-select-option>
                                <a-select-option value="30">过去30天</a-select-option>
                                <a-select-option value="90">过去90天</a-select-option>
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

                        <a-form-item label="层级：">
                            <a-select
                                placeholder="选择层级："
                                v-decorator="[ 'level', { initialValue: 'campaign' } ]"
                            >
                                <a-select-option
                                    v-for="item in levelList"
                                    :key="item.key"
                                    :value="item.value"
                                >{{ item.key }}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                        <hr color="#808080">
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
import { LevelList } from '@/datadict/datadict.js';

const columns = [
    {
        title: '名称',
        dataIndex: 'name'
    },
    {
        title: '成效',
        dataIndex: 'result',
        width: 100
    },
    {
        title: '展示次数',
        dataIndex: 'impressions',
        width: 100
    },
    {
        title: '单次成效费用',
        dataIndex: 'cost_per_result'
    },
    {
        title: '花费金额',
        dataIndex: 'spend'
    },
    {
        title: '点击量',
        dataIndex: 'clicks'
    }
];

const url = '/data/fb';
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
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            // 加载数据方法
            loadData: this.getReportData,
            // 分类树数据
            accountsList: [],
            levelList: LevelList,
            treeloading: false,
            appTreeData: []
        };
    },
    created () {},
    mounted () {
        this.getAccountList();
    },
    computed: {},
    methods: {
        getAccountList () {
            return this.$http.get(url + '/list').then(res => {
                this.accountsList = res;
            });
        },
        getReportData (params) {
            const page = Object.assign(params, this.queryParam);
            const values = this.form.getFieldsValue();
            values.pageNo = page.pageNo;
            values.pageSize = page.pageSize;
            return this.$http.post(url + '/info', values);
        }
    }
};
</script>

<style lang="less">
    .float-right {
        float: right;
    }
</style>
