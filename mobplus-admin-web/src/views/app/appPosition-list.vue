<template>
    <div class="panel panel-appPosition">
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
                        <div class="l" v-action="['app:position:query']">
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入关键字"
                                    v-model="queryParam.keyword"
                                />
                            </a-form-item>
                            <a-form-item label="广告类型">
                                <a-select placeholder="广告类型" v-model="queryParam.type" style="width:120px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option
                                        v-for="item in AdType"
                                        :key="item.value"
                                        :value="item.value"
                                    >{{ item.label }}</a-select-option>
                                </a-select>
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
                                    v-action="['app:position:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['app:position:create']"
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
                        <template slot="typeSlot" slot-scope="text">
                            {{ $GetDictLabel($AdType, text) }}
                        </template>
                        <template slot="timeSlot" slot-scope="text">
                            {{ text | moment }}
                        </template>
                        <template slot="statusSlot" slot-scope="text">
                            <span v-if="text===1">展示</span>
                            <span v-else-if="text===2">隐藏</span>
                        </template>
                        <span slot="action" slot-scope="text, record">
                            <a
                                v-action="['app:position:edit']"
                                @click="$refs.modal.edit(record, currentApp)"
                            >编辑</a>
                            <a-divider type="vertical" />
                            <a-dropdown
                        v-action="[
                            'app:position:edit',
                            'app:position:delete'
                        ]"
                    >
                        <a class="ant-dropdown-link">
                            更多
                            <a-icon type="down" />
                        </a>
                        <a-menu slot="overlay">
                            <a-menu-item v-action="['app:position:edit']">
                                <a
                                    @click="$refs.admodal.show(record)"
                                    >关联广告位</a
                                >
                            </a-menu-item>
                            <a-menu-item
                                v-if="record.id != 0"
                                v-action="['app:position:delete']"
                            >
                                <a @click="onDelete(record)">删除</a>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <appPosition-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
        <appPosition-ad-modal ref="admodal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AppPositionModal from '@/views/app/modules/appPosition-modal';
import AppPositionAdModal from '@/views/app/modules/appPosition-ad-modal';

const columns = [
    {
        title: '展示位代码',
        dataIndex: 'code'
    },
    {
        title: '展示位名称',
        dataIndex: 'name'
    },
    {
        title: '类型',
        dataIndex: 'type',
        scopedSlots: { customRender: 'typeSlot' }
    },
    {
        title: '是否展示广告',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
    },
    {
        title: '添加时间',
        dataIndex: 'createdAt',
        scopedSlots: { customRender: 'timeSlot' }
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/app/position';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AppPositionModal,
        AppPositionAdModal
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
.panel-appPosition {
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
