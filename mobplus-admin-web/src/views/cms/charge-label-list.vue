<template>
    <div class="panel panel-sys-user">
        <a-row :gutter="12">
            <a-col :span="24">
                <a-card :bordered="true" class="card-search card-list">
                    <!--       -->
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l">
                            <a-form-item>
                                <a-input placeholder="请输入标签名" v-model="queryParam.searchText" />
                            </a-form-item>
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    type="primary"
                                    icon="search"
                                    @click="$refs.table.refresh(true)"
                                >查询</a-button>
                            </a-button-group>
                        </div>
                        <div class="r">
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    icon="sync"
                                    v-action="['cms:charge:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['cms:charge:create']"
                                    @click="$refs.modal.add()"
                                >新增</a-button>
                                <a-button
                                    icon="delete"
                                    v-action="['cms:charge:delete']"
                                    v-if="selectedRowKeys.length>0"
                                    @click="onDelete()"
                                >删除</a-button>
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
                        :lazy="false"
                        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                    >
                        <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                        <span slot="actionSlot" slot-scope="text, record">
                            <a v-action="['cms:charge:edit']" @click="$refs.modal.edit(record)">编辑</a>
                            <a-divider type="vertical" />
                            <a v-action="['cms:charge:delete']" @click="onDelete(record)">删除</a>
                        </span>
                        <template slot="type" slot-scope="text">
                            <span v-if="text===1">热门</span>
                            <span v-else-if="text===2">普通</span>
                        </template>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <label-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import LabelModal from '@/views/cms/modules/charge-label-modal';

const columns = [
    {
        title: '中文名称',
        dataIndex: 'name'
    },
    {
        title: '英文名称',
        dataIndex: 'enName'
    },
    {
        title: '标签类型',
        dataIndex: 'type',
        scopedSlots: { customRender: 'type' }
    },
    {
        title: '添加时间',
        dataIndex: 'createdAt',
        scopedSlots: { customRender: 'dateSlot' }
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'actionSlot' }
    }
];

const url = '/cms/charge/label';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        LabelModal,
        STree
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 表头
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            queryParam: {},
            // // TreeData
            // treeloading: false,
            // appTreeData: [],
            // currentApp: null,
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {
        // this.loadAppTreeData();
    },
    computed: {},
    methods: {
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
        onSelectChange: function (selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        },
        loadDataList: function (params) {
            return this.$http.get(
                url,
                Object.assign(params, this.queryParam)
            );
        }
        // onQueryDict: function (item) {
        //     this.currentApp = item;
        //     this.$refs.table.refresh(true);
        // },
        // loadAppTreeData: async function () {
        //     this.treeloading = true;
        //     try {
        //         const result = await this.$http.get(
        //             '/app/entity/item',
        //             { 'type': 3 }
        //         );
        //         this.appTreeData = this.convertTreeData(result.data);
        //     } finally {
        //         this.treeloading = false;
        //     }
        // },
        // convertTreeData: function (data) {
        //     return data.map(ele => {
        //         const item = {
        //             title: ele.name,
        //             key: ele.id
        //         };
        //         if (ele.children) {
        //             item.children = this.convertTreeData(ele.children);
        //         }
        //         return item;
        //     });
        // }
    }
};
</script>

<style lang="less" scoped>
</style>
