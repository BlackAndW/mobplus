<template>
    <div class="panel panel-appFunction">
        <a-row :gutter="12">
            <a-col :span="4">
                <a-card :bordered="true" title="应用信息" class="tree-list" :loading="treeloading">
                    <a-icon
                        slot="extra"
                        type="sync"
                        @click="loadAppTreeData"
                        style="cursor: pointer"
                    />
                    <s-tree :dataSource="appTreeData" @add="onQueryDict" />
                </a-card>
            </a-col>
            <a-col :span="20">
                <a-card :bordered="true" class="card-list">
                    <!--       -->
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l" v-action="['app:function:query']">
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
                        <div class="r" v-if="currentApp!=null">
                            <a-button-group>
                                <a-button
                                    icon="sync"
                                    v-action="['app:function:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['app:function:create']"
                                    @click="$refs.modal.add(currentApp)"
                                >新增</a-button>
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
                        <template slot="functionNameSlot" slot-scope="text">
                            <span
                                v-for="item in FunctionType"
                                v-if="item.value === text"
                                :key="item.value"
                            > {{ item.label }}</span>
                        </template>
                        <span slot="adTypeListSlot" slot-scope="text, record">
                            <div
                                v-for="adTypeItem in record.adTypeList"
                                :key="adTypeItem.id"
                                :value="adTypeItem.value"
                            >
                                <span
                                    v-for="item in AdType"
                                    v-if="item.value === adTypeItem.value"
                                    :key="item.value"
                                > --{{ item.label }}--</span>
                            </div>
                        </span>
                        <span slot="action" slot-scope="text, record">
                            <a
                                v-action="['app:function:edit']"
                                @click="$refs.modal.edit(record, currentApp)"
                            >编辑</a>
                            <a-divider type="vertical" />
                            <a
                                v-if="record.id != 0"
                                v-action="['app:function:delete']"
                                @click="onDelete(record)">
                                删除
                            </a>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <appFunction-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AppFunctionModal from '@/views/app/modules/appFunction-modal';

const columns = [
    {
        title: '配置名称',
        dataIndex: 'name'
    },
    {
        title: '功能名称',
        dataIndex: 'functionCode',
        scopedSlots: { customRender: 'functionNameSlot' }
    },
    {
        title: '版本',
        dataIndex: 'versionCode'
    },
    {
        title: '渠道',
        dataIndex: 'channelName'
    },
    {
        title: '广告展示类型列表',
        dataIndex: 'adTypeList',
        scopedSlots: { customRender: 'adTypeListSlot' }
    },
    {
        title: '设置',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/app/function';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AppFunctionModal
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
            adTypeNameList: '',
            currentApp: null,
            treeloading: false,
            appTreeData: []
        };
    },
    created () {},
    mounted () {
        this.loadAppTreeData();
    },
    computed: {
        AdType: function () {
            return this.$DictFilterExclude(this.$AdType, [0]);
        },
        FunctionType: function () {
            return this.$DictFilterExclude(this.$FunctionType, [0]);
        }
    },
    methods: {
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
                url + '?appId=' + this.currentApp.key,
                Object.assign(params, this.queryParam)
            );
        },
        onQueryDict: function (item) {
            this.currentApp = item;
            this.$refs.table.refresh(true);
        },
        loadAppTreeData: async function () {
            this.treeloading = true;
            try {
                const result = await this.$http.get(
                    '/app/entity/item',
                    { }
                );
                this.appTreeData = this.convertTreeData(result.data);
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
.panel-appFunction {
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
