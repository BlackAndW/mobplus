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
        <div class="add-modal">
            <a-button-group>
                <a-button
                    icon="sync"
                    v-action="['app:config:query']"
                    @click="$refs.table.refresh(false)"
                />
                <a-button
                    icon="plus"
                    v-action="['app:config:create']"
                    @click="$refs.modal.add()"
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
            <template slot="iconSlot" slot-scope="text">
                <a-avatar shape="square" :src="text" v-if="text && text.length > 0"/>
            </template>
            <template slot="statusSlot" slot-scope="text">
                <span v-if="text === 1">暂停</span>
                <span v-else-if="text === 2">启用</span>
            </template>
            <span slot="action" slot-scope="text, record">
                <a
                    v-action="['app:config:edit']"
                    @click="$refs.modal.edit(record)"
                >编辑</a>
                <a-divider type="vertical" />
            </span>
            </s-table>
        </a-spin>
        </a-modal>
        <appVPN-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import AppVPNModal from '@/views/app/modules/appVPN-modal';

const columns = [
    {
        title: '节点名称',
        dataIndex: 'serverName'
    },
    {
        title: '城市',
        dataIndex: 'region'
    },
    {
        title: '图标',
        dataIndex: 'b04',
        scopedSlots: { customRender: 'iconSlot' }
    },
    {
        title: '网速',
        dataIndex: 'b05'
    },
    {
        title: '节点状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
    },
    {
        title: '节点设置',
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
        AppVPNModal
    },
    data () {
        return {
            title: '节点',
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
            model: {}
        };
    },
    computed: {},
    methods: {
        show: function (record) {
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
            return this.$http.get('/app/vpn');
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

<style lang="less" scoped>
    .add-modal {
        text-align: right;
        margin-bottom: 10px;
    }
</style>
