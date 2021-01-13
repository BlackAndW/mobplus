<template>
    <div class="panel panel-sys-depart">
        <a-card :bordered="true" class="card-list">
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="l" v-action="['sys:depart:query']">
                    <a-form-item>
                        <a-input type="text" placeholder="请输入部门名称/编号" v-model="queryParam.keyword" />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" icon="search" @click="$refs.table.refresh(true)">查询</a-button>
                    </a-form-item>
                </div>
                <div class="r">
                    <a-button-group>
                        <a-button
                            icon="sync"
                            v-action="['sys:depart:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['sys:depart:add']"
                            @click="$refs.modal.add()"
                        >新增</a-button>
                        <a-button icon="export" v-action="['sys:depart:export']">导出</a-button>
                        <a-button icon="printer" v-action="['sys:depart:print']">打印</a-button>
                        <a-button
                            icon="delete"
                            v-action="['sys:depart:delete']"
                            v-if="selectedRowKeys.length > 0"
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
                :rowSelection="{
                    selectedRowKeys: selectedRowKeys,
                    onChange: onSelectChange
                }"
            >
                <template slot="statusSlot" slot-scope="text">
                    <e-tag :value="text" :items="$GeneralStatus"/>
                </template>
                <span slot="action" slot-scope="text, record">
                    <a v-action="['sys:depart:edit']" @click="$refs.modal.edit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <a-dropdown v-action="['sys:depart:delete', 'sys:depart:add']">
                        <a class="ant-dropdown-link">
                            更多
                            <a-icon type="down" />
                        </a>
                        <a-menu slot="overlay">
                            <a-menu-item v-action="['sys:depart:add']">
                                <a @click="$refs.modal.add(record)">添加子部门</a>
                            </a-menu-item>
                            <a-menu-item v-action="['sys:depart:delete']">
                                <a @click="onDelete(record)">删除</a>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>
                </span>
            </s-table>
        </a-card>
        <sys-depart-modal
            ref="modal"
            @close="
                refresh => {
                    refresh ? $refs.table.refresh(false) : (a = 1);
                }
            "
        />
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import SysDepartModal from '@/views/sys/modules/depart-modal';

const columns = [
    {
        title: '部门代码',
        width: '180px',
        dataIndex: 'code'
    },
    {
        title: '部门名称',
        dataIndex: 'name'
    },
    {
        title: '负责人',
        dataIndex: 'leader'
    },
    {
        title: '电话号码',
        dataIndex: 'mobile'
    },
    {
        title: '传真',
        dataIndex: 'fax'
    },
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

const url = '/sys/depart';
export default {
    name: 'SysDepartList',
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        SysDepartModal
    },
    data () {
        return {
            form: this.$form.createForm(this),
            // 查询参数
            queryParam: {},
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
        onSelectChange: function (selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        },
        loadDataList: function (params) {
            return this.$http.get(url, Object.assign(params, this.queryParam));
        }
    }
};
</script>

<style lang="less" scoped></style>
