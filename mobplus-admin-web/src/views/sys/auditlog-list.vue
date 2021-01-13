<template>
    <div class="panel panel-sysauditlog">
        <a-card :bordered="true" class="card-search card-list">
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="l" v-action="['sys:auditlog:query']">
                    <a-form-item label="时间范围">
                        <a-range-picker v-model="queryParam.timeRange" :format="dateFormat"/>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" icon="search" @click="$refs.table.refresh(true)">查询</a-button>
                    </a-form-item>
                </div>
                <div class="r">
                    <a-button-group>
                        <a-button
                            icon="sync"
                            v-action="['sys:auditlog:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['sys:auditlog:create']"
                            @click="$refs.modal.add()"
                        >新增</a-button>
                        <a-button icon="export" v-action="['sys:auditlog:export']">导出</a-button>
                        <a-button icon="printer" v-action="['sys:auditlog:print']">打印</a-button>
                        <a-button
                            icon="delete"
                            v-action="['sys:auditlog:delete']"
                            v-if="selectedRowKeys.length > 0"
                            @click="onDelete()"
                        >删除</a-button>
                        <a-button
                            icon="delete"
                            v-action="['sys:auditlog:delete']"
                            @click="onClear()"
                        >清空</a-button>
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
                :rowSelection="{
                    selectedRowKeys: selectedRowKeys,
                    onChange: onSelectChange
                }"
            >
                <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                <template slot="detailSlot" slot-scope="text">
                    <ellipsis :length="50" tooltip>{{ text }}</ellipsis>
                </template>
            </s-table>
        </a-card>
    </div>
</template>

<script>
import { dateRangeToMs, momentRange } from '@/utils/date';
import { mixinDevice } from '@/utils/mixin';
import { STable, Ellipsis } from '@/components';

const columns = [
    { title: '时间', dataIndex: 'createdAt', scopedSlots: { customRender: 'dateSlot' } },
    { title: '操作', dataIndex: 'actName' },
    { title: '详情', width: 300, dataIndex: 'detail', scopedSlots: { customRender: 'detailSlot' } },
    { title: '结果', dataIndex: 'result' },
    { title: 'IP', dataIndex: 'ipAddr' },
    { title: '操作人', dataIndex: 'userName' }
];

const url = '/sys/auditlog';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        Ellipsis
    },
    data () {
        return {
            form: this.$form.createForm(this),
            // 查询参数
            queryParam: { timeRange: momentRange() },
            dateFormat: 'YYYY-MM-DD',

            // 表头
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            // 加载数据方法
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {},
    methods: {
        onDelete: function (record) {
            let params = [];
            if (record !== undefined) {
                params.push(record.id);
            } else {
                params = this.selectedRowKeys;
            }
            if (params.length === 0) {
                return;
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
        onClear: function (record) {
            const params = [];
            this.$confirm({
                title: '确认清空所有日志吗?',
                okText: '确认',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        const data = await this.$http.delete(
                            url + '?clear=1',
                            params
                        );
                        if (data) {
                            this.$message.success(data);
                            this.$refs.table.refresh(true);
                        }
                    } catch (err) {}
                }
            });
        },
        onSelectChange: function (selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        },
        loadDataList: function (params) {
            let data = Object.assign(params, this.queryParam);
            data = Object.assign(data, dateRangeToMs(this.queryParam.timeRange));
            delete data.timeRange;
            return this.$http.get(url, data);
        }
    }
};
</script>

<style lang="less" scoped></style>
