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
            <a-button-group class="btn-grp-margin-top">
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
            <template slot="awardType" slot-scope="text">
                <span v-if="text===1">一等奖</span>
                <span v-else-if="text===2">二等奖</span>
                <span v-else-if="text===3">三等奖</span>
            </template>
            <template slot="iconSlot" slot-scope="text">
                <a-avatar shape="square" :src="text" v-if="text && text.length > 0"/>
            </template>
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
        <app-award-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import AppAwardModal from '@/views/app/modules/app-award-modal';

const columns = [
    {
        title: '奖品类别',
        dataIndex: 'awardType',
        scopedSlots: { customRender: 'awardType' }
    },
    {
        title: '奖品名称',
        dataIndex: 'awardName'
    },
    {
        title: '奖品图片',
        dataIndex: 'awardImgPath',
        scopedSlots: { customRender: 'iconSlot' }
    },
    {
        title: '奖品碎片',
        dataIndex: 'awardPiece'
    },
    {
        title: '奖品设置',
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
        AppAwardModal
    },
    data () {
        return {
            title: '活动名称：奖品测试',
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
            // ad pos
            model: {}
        };
    },
    computed: {},
    methods: {
        show: function (record) {
            this.title = record.name;
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
                '/app/activity/award?activityId=' + this.model.id
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
        // handleCellChange (value, key, column, record) {
        //     record[column] = value;
        //     this.$refs.table.$forceUpdate();
        // },
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

<style lang="less" scoped>
    .add-modal {
        text-align: right;
        margin-bottom: 10px;
    }
</style>
