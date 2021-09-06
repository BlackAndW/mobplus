<template>
    <div class="panel panel-conf">
        <a-row :gutter="12">
            <a-col :span="4">
                <a-card :bordered="true" title="配置分类" class="tree-list" :loading="treeloading">
                    <a-icon
                        slot="extra"
                        type="sync"
                        @click="loadCategoryTreeData"
                        style="cursor: pointer"
                    />
                    <s-tree class="tree-list-style" :dataSource="categoryTreeData" @add="onQueryDict" />
                </a-card>
            </a-col>
            <a-col :span="20">
                <a-card :bordered="true" class="card-list">
                    <!--       -->
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l" v-action="['sys:conf:query']">
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入关键字"
                                    v-model="queryParam.keyword"
                                />
                            </a-form-item>
                            <a-form-item>
                                <a-button
                                    type="primary"
                                    icon="search"
                                    @click="$refs.table.refresh(true)"
                                >查询</a-button>
                            </a-form-item>
                        </div>
                        <div class="r" v-if="currentCategory!=null">
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    icon="sync"
                                    v-action="['sys:conf:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['sys:conf:create']"
                                    @click="$refs.modal.add(currentCategory)"
                                >新增</a-button>
                                <a-button icon="export" v-action="['sys:conf:export']">导出</a-button>
                                <a-button icon="printer" v-action="['sys:conf:print']">打印</a-button>
                                <a-button
                                    icon="delete"
                                    v-action="['sys:conf:delete']"
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
                        :lazy="true"
                        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                    >
                        <template slot="statusSlot" slot-scope="text">
                            <e-tag :value="text" :items="$GeneralStatus"/>
                        </template>
                        <span slot="action" v-if="record.perm==2" slot-scope="text, record">
                            <a
                                v-action="['sys:conf:edit']"
                                @click="$refs.modal.edit(record, currentCategory)"
                            >编辑</a>
                            <a-divider type="vertical" />
                            <a v-action="['sys:conf:delete']" @click="onDelete(record)">删除</a>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>

        <sys-conf-modal ref="modal" @close="(refresh)=>{refresh?$refs.table.refresh(false):a=1}" />
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import SysConfModal from '@/views/sys/modules/conf-modal';

const columns = [
    { title: '配置名称', dataIndex: 'name' },
    { title: '配置键', dataIndex: 'key' },
    { title: '配置值', dataIndex: 'value' },
    {
        title: '状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
    },
    { title: '备注', dataIndex: 'remark' },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/sys/conf';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        SysConfModal
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
            loadData: this.loadDataList,

            // 分类树数据
            currentCategory: null,
            treeloading: false,
            categoryTreeData: []
        };
    },
    created () {},
    mounted () {
        this.loadCategoryTreeData();
    },
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
            return this.$http.get(
                url + '?category=' + this.currentCategory.key,
                Object.assign(params, this.queryParam)
            );
        },
        onQueryDict: function (item) {
            this.currentCategory = item;
            this.$refs.table.refresh(true);
        },
        loadCategoryTreeData: async function () {
            this.treeloading = true;
            try {
                const result = await this.$http.get(
                    '/sys/category/1001/item',
                    {}
                );
                this.categoryTreeData = this.convertTreeData(result.data);
            } finally {
                this.treeloading = false;
            }
        },
        convertTreeData: function (data) {
            return data.map(ele => {
                const item = {
                    title: ele.name,
                    key: ele.id
                };
                if (ele.children) {
                    item.children = this.convertTreeData(ele.children);
                }
                return item;
            });
        }
    }
};
</script>

<style lang="less">
.panel-conf {
    .ant-row {
        height: 100%;
        > div {
            height: 100%;
        }
    }
    .tree-list {
        height: 100%;
        .ant-card-body {
            padding: 5px 0px 0px 10px;

            .tree-wrapper .ant-menu {
                border-right: 0px;
            }
        }
    }
    .card-list .ant-card-body {
        padding: 15px 15px;
    }
}
</style>
