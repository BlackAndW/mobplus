<template>
    <div class="panel panel-talk">
        <a-row :gutter="12">
            <a-col :span="4">
                <a-card :bordered="true" title="话术类别" class="tree-list" :loading="treeloading">
                    <a-icon
                        slot="extra"
                        type="sync"
                        @click="loadCategoryTreeData"
                        style="cursor: pointer"
                    />
                    <s-tree :dataSource="talkTreeData" @add="onQueryDict" />
                </a-card>
            </a-col>
            <a-col :span="20">
                <a-card :bordered="true" class="card-list">
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l" v-action="['cms:talk:query']">
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入话术标题"
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
                            <a-button-group>
                                <a-button
                                    icon="sync"
                                    v-action="['cms:talk:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['cms:talk:create']"
                                    @click="$refs.modal.add(currentCategory)"
                                >新增</a-button>
                                <a-button
                                    icon="delete"
                                    v-action="['cms:talk:delete']"
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
                        <span slot="action" slot-scope="text, record">
                            <a
                                v-action="['cms:talk:query']"
                                @click="$refs.talkContentListModal.show(record)"
                            >查看话术内容</a>
                            <a-divider type="vertical" />
                            <a
                                v-action="['cms:talk:edit']"
                                @click="$refs.modal.edit(record)"
                            >编辑</a>
                            <a-divider type="vertical" />
                            <a
                                v-action="['cms:talk:delete']"
                                @click="onDelete(record)"
                            >删除</a>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
      <talk-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
      <talk-content-list-modal ref="talkContentListModal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import TalkModal from '@/views/cms/modules/talk-modal';
import TalkContentListModal from '@/views/cms/modules/talk-content-list-modal';
const columns = [
    { title: '话术标题', dataIndex: 'title' },

    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/cms/talk';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        TalkModal,
        TalkContentListModal
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
            talkTreeData: []
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
                url + '?categoryId=' + this.currentCategory.key,
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
                    '/cms/talk/category/sct',
                    { pageSize: 999999 }
                );
                this.talkTreeData = this.convertTreeData(result.data);
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
.panel-app-version {
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
