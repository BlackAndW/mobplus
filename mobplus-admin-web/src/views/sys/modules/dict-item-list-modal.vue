<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel">
        <a-spin :spinning="confirmLoading">
            <a-button-group class="btn-grp-margin-top" style="margin-bottom:16px">
                <a-button icon="plus" v-action="['sys:dict:add']" @click="$refs.modal.add(model)">新增</a-button>
                <a-button icon="sync" v-action="['sys:dict:query']" @click="loadDataList" />
            </a-button-group>
            <a-table
                bordered
                ref="table"
                size="small"
                class="card-table"
                rowKey="id"
                :columns="columns"
                :dataSource="tableData"
                :pagination="false"
            >
                <template slot="statusSlot" slot-scope="text">
                    <e-tag :value="text" :items="$GeneralStatus"/>
                </template>
                <span slot="action" slot-scope="text, record">
                    <a v-action="['sys:dict:edit']" @click="$refs.modal.edit(record, model)">编辑</a>
                    <a-divider type="vertical" />
                    <a v-action="['sys:dict:delete']" @click="onDelete(record)">删除</a>
                </span>
            </a-table>
        </a-spin>
        <div slot="footer"></div>
        <sys-dict-item-modal ref="modal" @close="(refresh)=>{refresh && this.loadDataList()}" />
    </e-drawer>
</template>

<script>
import { EDrawer, STable, ETag } from '@/components';
import SysDictItemModal from '@/views/sys/modules/dict-item-modal';

const columns = [
    { title: '项目名', dataIndex: 'label' },
    { title: '项目值', dataIndex: 'value' },
    {
        title: '状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/sys/dict';
export default {
    components: {
        EDrawer,
        STable,
        ETag,
        SysDictItemModal
    },
    data () {
        return {
            title: '',
            visible: false,
            confirmLoading: false,
            model: {},
            dictId: '0',
            tableData: [],

            // 表头
            columns
        };
    },
    methods: {
        show: function (record) {
            this.title = '字典字段配置';
            this.dictId = record.id;
            this.model = record;
            this.confirmLoading = false;
            this.visible = true;
            this.loadDataList();
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
        },
        onCancel: function () {
            this.close(false);
        },
        loadDataList: async function () {
            this.confirmLoading = true;
            try {
                this.tableData = await this.$http.get(
                    url + '/' + this.dictId + '/item',
                    {}
                );
                this.confirmLoading = false;
            } catch (err) {
                if (err) {
                    console.log(err.stack);
                }
                this.confirmLoading = false;
            }
        },
        onDelete: function (record) {
            const params = [record.id];
            this.$confirm({
                title: '确认删除吗?',
                okText: '确认',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        this.confirmLoading = true;
                        const data = await this.$http.delete(
                            url + '/' + this.dictId + '/item',
                            params
                        );
                        if (data) {
                            this.$message.success(data);
                            this.loadDataList();
                        }
                        this.confirmLoading = false;
                    } catch (err) {
                        this.confirmLoading = false;
                    }
                }
            });
        }
    }
};
</script>

<style scoped>
</style>
