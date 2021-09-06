<template>
    <div class="panel panel-talk-category">
        <a-card :bordered="true" class="card-search card-list">
            <!--       -->
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="l" v-action="['cms:talk:query']">
                    <a-form-item>
                        <a-input type="text" placeholder="请输入关键字" v-model="queryParam.keyword" />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" icon="search" @click="$refs.table.refresh(true)">查询</a-button>
                    </a-form-item>
                </div>
                <div class="r">
                    <a-button-group class="btn-grp-margin-top">
                        <a-button
                            icon="sync"
                            v-action="['cms:talk:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['cms:talk:create']"
                            @click="$refs.modal.add(0)"
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
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            >
                <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                <template slot="levelSlot" slot-scope="text">
                    <span v-if="text===0">一级类目</span>
                    <span v-else>二级类目</span>
                </template>
                <span slot="action" slot-scope="text, record">
                    <a v-action="['cms:talk:edit']" @click="$refs.modal.edit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <a-dropdown v-action="['cms:talk:create', 'cms:talk:delete']">
                        <a class="ant-dropdown-link">
                            更多
                            <a-icon type="down" />
                        </a>
                        <a-menu slot="overlay">
                            <a-menu-item v-action="['cms:talk:create']" v-if="record.parentId==0">
                                <a @click="$refs.modal.add(record.id)">添加子分类</a>
                            </a-menu-item>
                            <a-menu-item v-action="['cms:talk:delete']">
                                <a @click="onDelete(record)">删除</a>
                            </a-menu-item>
                        </a-menu>
                        </a-dropdown>
                </span>
            </s-table>
        </a-card>
        <talk-category-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import TalkCategoryModal from '@/views/cms/modules/talk-category-modal';

const columns = [
    { title: '话术类别', dataIndex: 'name', width: '238px' },
    { title: '级别', dataIndex: 'level', scopedSlots: { customRender: 'levelSlot' } },
    { title: '排序', dataIndex: 'sort' },
    {
        title: '添加时间',
        dataIndex: 'createdAt',
        scopedSlots: { customRender: 'dateSlot' }
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/cms/talk/category';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        TalkCategoryModal
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

<style lang="less" scoped>
</style>
