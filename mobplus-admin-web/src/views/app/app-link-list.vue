<template>
    <div class="panel">
        <a-row :gutter="12">
            <a-col :span="4">
                <a-card :bordered="true" title="应用信息" class="tree-list" :loading="treeloading">
                    <a-icon
                        slot="extra"
                        type="sync"
                        @click="loadAppTreeData"
                        style="cursor: pointer"
                    />
                    <s-tree class="tree-list-style" :dataSource="appTreeData" @add="onQueryDict" />
                </a-card>
            </a-col>
            <!--       -->
            <a-col :span="20">
                <a-card :bordered="true" class="card-search card-list">
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="r" v-if="currentApp!=null">
                            <a-button-group>
                                <a-button
                                    icon="sync"
                                    v-action="['app:link:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['app:link:create']"
                                    @click="$refs.modal.add(currentApp)"
                                >新增</a-button>
                                <a-button
                                    icon="delete"
                                    v-action="['app:link:delete']"
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
                        <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                        <span slot="actionSlot" slot-scope="text, record">
                            <a v-action="['app:link:edit']" @click="$refs.modal.edit(record, currentApp)">编辑</a>
                            <a-divider type="vertical" />
                            <a v-action="['app:link:delete']" @click="onDelete(record)">删除</a>
                        </span>
                        <template slot="imgSlot" slot-scope="text">
                                <img
                                    class="link-img"
                                    v-if="text && text.length > 0"
                                    :src="text"
                                />
                        </template>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <app-link-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AppLinkModal from '@/views/app/modules/app-link-modal';

const columns = [
    {
        title: '名称',
        dataIndex: 'name'
    },
    {
        title: '预览图',
        dataIndex: 'imgPath',
        width: 200,
        scopedSlots: { customRender: 'imgSlot' }
    },
    {
        title: '跳转链接',
        dataIndex: 'imgUrl'
    },
    {
        title: '点击次数',
        dataIndex: 'clickNum'
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

const url = '/app/link/';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AppLinkModal
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            queryParam: {},
            // 表头
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            currentApp: null,
            treeloading: false,
            appTreeData: [],
            // 加载数据方法
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {
        this.loadAppTreeData();
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

<style lang="less" scoped>
.link-img {
    width: 180px;
}
</style>
