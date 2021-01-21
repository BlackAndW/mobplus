<template>
    <div>
        <a-modal
        :title="title"
        :width="modalWidth"
        :visible="visible"
        :confirmLoading="confirmLoading"
        @cancel="onCancel"
        cancelText="关闭"
    >
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
                <span slot="action" slot-scope="text, record">
                    <a
                        v-action="['app:activity:edit']"
                        @click="$refs.modal.edit(record, currentAppActivity)"
                    >编辑</a>
                    <a-divider type="vertical" />
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
        scopedSlots: { customRender: 'typeSlot' }
    },
    {
        title: '任务名称',
        dataIndex: 'taskName',
        scopedSlots: { customRender: 'typeSlot' }
    },
    {
        title: '任务奖励金币',
        dataIndex: 'taskBonusCoin',
        scopedSlots: { customRender: 'ratioSlot' }
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
        show: function (record, activityId) {
            this.title = '活动名称：奖品测试';
            this.model = record;
            this.currentAppActivity = activityId;
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
        }
    }
};
</script>

<style scoped>

</style>
