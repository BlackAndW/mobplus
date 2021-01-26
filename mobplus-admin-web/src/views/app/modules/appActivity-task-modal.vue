<template>
    <div>
        <a-modal
        :title="title"
        :width="modalWidth"
        :visible="visible"
        :confirmLoading="confirmLoading"
        @ok="onCancel"
        @cancel="onCancel"
        cancelText="关闭"
    >
        <div class="add-modal" v-if="currentAppActivity!=null">
            <a-button-group>
                <a-button
                    icon="sync"
                    v-action="['app:activity:query']"
                    @click="$refs.table.refresh(false)"
                />
                <a-button
                    icon="plus"
                    v-action="['app:activity:create']"
                    @click="$refs.modal.add(currentAppActivity)"
                >新增</a-button>
            </a-button-group>
        </div>
        <a-spin :spinning="confirmLoading">
            <s-table
                bordered
                ref="table"
                size="small"
                class="card-table"
                @dataChanged="dataChanged"
                :columns="columns"
                :data="loadData"
                :scroll="{y:300}"
                :pageSize="99999999"
                :showPagination="false"
                :rowSelection="{
                    selectedRowKeys: selectedRowKeys,
                    onChange: onSelectChange
                }"
            >
                <template slot="taskType" slot-scope="text">
                    <span v-if="text===1">新手任务</span>
                    <span v-else-if="text===2">日常任务</span>
                </template>
                <template slot="taskStatus" slot-scope="text">
                    <span v-if="text===1">开启</span>
                    <span v-else-if="text===2">关闭</span>
                </template>
                <span slot="action" slot-scope="text, record">
                    <a
                        v-action="['app:activity:edit']"
                        @click="$refs.modal.edit(record, currentAppActivity)"
                    >编辑</a>
                    <a-divider type="vertical" />
                    <a
                        v-if="record.id != 0"
                        v-action="['app:activity:delete']"
                        @click="onDelete(record)">
                        删除
                    </a>
                </span>
            </s-table>
        </a-spin>
        </a-modal>
        <app-task-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import AppTaskModal from '@/views/app/modules/app-task-modal';

const columns = [
    {
        title: '任务类别',
        dataIndex: 'taskType',
        scopedSlots: { customRender: 'taskType' }
    },
    {
        title: '任务名称',
        dataIndex: 'taskName'
    },
    {
        title: '任务奖励金币',
        dataIndex: 'taskBonusCoin'
    },
    {
        title: '任务功能名',
        dataIndex: 'taskFunctionName'
    },
    {
        title: '任务状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'taskStatus' }
    },
    {
        title: '任务设置',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        AppTaskModal
    },
    data () {
        return {
            title: '活动名称：测试',
            modalWidth: 900,
            visible: false,
            confirmLoading: false,

            // 查询参数
            queryParam: {},
            // 表头
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],

            loadData: this.loadDataList,
            currentAppActivity: null,
            model: {}
        };
    },
    computed: {
        AdType: function () {
            return this.$DictFilterExclude(this.$AdType, [0]);
        }
    },
    methods: {
        show: function (record) {
            this.title = '活动名称：' + record.name;
            this.model = record;
            this.currentAppActivity = record.id;
            this.reload();
            this.visible = true;
        },
        onCancel: function () {
            this.close();
        },
        close: function () {
            this.$emit('close');
            this.visible = false;
            this.confirmLoading = false;
            this.selectedRowKeys = [];
            this.selectedRows = [];
        },
        loadDataList: function (params) {
            return this.$http.get(
                '/app/activity/task/?activityId=' + this.model.id,
                Object.assign(params, this.queryParam)
            );
        },
        reload: function () {
            if (this.$refs.table) {
                this.$refs.table.refresh(true);
            }
        },
        onSelectChange: function (selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        },
        handleCellChange (value, key, column, record) {
            record[column] = value;
            this.$refs.table.$forceUpdate();
        },
        dataChanged (data) {
            data.forEach(ele => {
                if (ele.checked) {
                    this.selectedRowKeys.push(ele.id);
                    this.selectedRows.push(ele);
                }
            });
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
                        const data = await this.$http.delete('/app/activity/task', params);
                        if (data) {
                            this.$message.success(data);
                            this.$refs.table.refresh(true);
                        }
                    } catch (err) {}
                }
            });
        }
    }
};
</script>

<style lang="less" scoped>
    .add-modal {
        text-align: right;
        margin-bottom: 10px;
    }
</style>
