<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="r">
                    <a-button-group>
                        <a-button
                            icon="sync"
                            v-action="['cms:talk:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['cms:talk:create']"
                            @click="$refs.modal.add(talkId)"
                        >新增</a-button>
                        <a-button
                            icon="delete"
                            v-action="['cms:talk:edit']"
                            v-if="selectedRowKeys.length>0"
                            @click="onDelete()"
                        >删除</a-button>
                    </a-button-group>
                </div>
            </a-form>
             <s-table
                bordered
                ref="table"
                class="card-table"
                :columns="columns"
                :data="loadData"
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            >
                <template slot="keySlot" slot-scope="text">
                    <span v-if="text===1">男</span>
                    <span v-else>女</span>
                </template>
                <span slot="actionSlot" slot-scope="text, record">
                    <a v-action="['cms:talk:edit']" @click="$refs.modal.edit(record)">修改</a>
                    <a-divider type="vertical" />
                    <a v-action="['cms:talk:delete']" @click="onDelete(record)">删除</a>
                </span>
            </s-table>
        </a-spin>
        <talk-content-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </e-drawer>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag, EDrawer } from '@/components';
import TalkContentModal from '@/views/cms/modules/talk-content-modal';

const columns = [
    {
        title: '人物',
        dataIndex: 'key',
        scopedSlots: { customRender: 'keySlot' }
    },
    {
        title: '对话内容',
        dataIndex: 'value'
    },
    {
        title: '排序',
        dataIndex: 'sort'
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'actionSlot' }
    }
];

const url = '/cms/talk/content';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        EDrawer,
        TalkContentModal
    },
    data () {
        return {
            title: '',
            visible: false,
            confirmLoading: false,
            talkId: null,
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 查询参数
            queryParam: {
            },
            // 表头
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            // 加载数据方法
            loadData: this.loadDataList
        };
    },
    methods: {
        show: function (record) {
            this.title = record.title;
            this.model = record;
            this.queryParam.talkId = record.id;
            this.talkId = record.id;
            this.editData = false;
            this.visible = true;
            this.reload();
        },
        reload: function () {
            if (this.$refs.table) {
                this.$refs.table.refresh(true);
            }
        },
        onDelete: function (record) {
            var params = [];
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
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },
        onCancel: function () {
            this.close(false);
        },
         onSubmit: function () {
            const $self = this;
            $self.close(true);
        },
        loadDataList: function (params) {
            return this.$http.get(url, Object.assign(params, this.queryParam));
        },
        onSelectChange: function (selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        }
    }
};

</script>

<style lang="less" scoped>
.menu-perm-selector {
    .ant-form-item {
        margin-bottom: 0px;
    }
}
</style>
