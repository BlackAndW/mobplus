<template>
    <div class="panel panel-sys-user">
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
            <a-col :span="20">
                <a-card :bordered="true" class="card-search card-list">
                    <!--       -->
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l" v-if="currentApp!=null">
                            <a-form-item hidden>
                                <a-button
                                    v-action="['cms:charge:query']"
                                    @click="change2test"
                                >切换到{{ changeType }}模式
                                </a-button>
                            </a-form-item>
                            <a-form-item label="素材类别" >
                                <a-select
                                    placeholder="选择类型"
                                    v-model="queryParam.type"
                                    style="width:120px"
                                >
                                    <a-select-option
                                        v-for="item in typeList"
                                        :key="item.name"
                                        :value="item.id"
                                    >{{ item.name }}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    icon="sync"
                                    v-action="['cms:charge:query']"
                                    @click="$refs.table.refresh(false)"
                                />
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
                                    @click="$refs.modal.add(queryParam, typeList, currentApp)"
                                >新增</a-button>
                                <a-button
                                    icon="delete"
                                    v-action="['cms:charge:edit']"
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
                            <a v-action="['cms:charge:edit']" @click="$refs.modal.edit(record, queryParam, typeList, currentApp)">编辑</a>
                            <a-divider type="vertical" />
                            <a v-action="['cms:charge:delete']" @click="onDelete(record)">删除</a>
                        </span>
                        <template slot="coverSlot" slot-scope="text">
                                <img
                                    class="cover-img"
                                    v-if="text && text.length > 0"
                                    :src="text"
                                />
                        </template>
                        <template slot="useLimitSlot" slot-scope="text">
                            <span v-if="text===1">是</span>
                            <span v-else-if="text===2">否</span>
                        </template>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <material-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag, STree } from '@/components';
import MaterialModal from '@/views/cms/modules/charge-material-modal';

const columns = [
    {
        title: '编号',
        dataIndex: 'id'
    },
    {
        title: '分类',
        dataIndex: 'typeName',
        width: 50
    },
    {
        title: '封面预览',
        dataIndex: 'videoCoverThumb',
        width: 100,
        scopedSlots: { customRender: 'coverSlot' }
    },
    {
        title: '真实使用次数',
        dataIndex: 'useNum'
    },
    {
        title: '点击次数',
        dataIndex: 'showNum'
    },
    {
        title: '虚拟使用次数',
        dataIndex: 'useNumFake'
    },
    {
        title: '限制',
        dataIndex: 'useLimit',
        scopedSlots: { customRender: 'useLimitSlot' }
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

const url = '/cms/charge/material';

export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        MaterialModal,
        STree
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 表头
            columns,
            queryParam: {},
            typeList: [],
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            changeType: '测试',
            // TreeData
            treeloading: false,
            appTreeData: [],
            currentApp: null,
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {
        this.loadAppTreeData();
        this.getTypeList();
    },
    computed: {},
    methods: {
        getTypeList () {
            return this.$http.get('/cms/charge/mtype?style=1').then(res => {
                this.typeList = res.data;
            });
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
        onSelectChange: function (selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        },
        change2test () {
            if (this.changeType !== '测试') {
                this.queryParam = {};
                this.changeType = '测试';
            } else {
                this.queryParam.type = 99;
                this.changeType = '正常';
            }
            this.$refs.table.refresh(true);
        },
        loadDataList: function (params) {
            return this.$http.get(url + '?style=1&appId=' + this.currentApp.key, Object.assign(params, this.queryParam));
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
                    { 'type': 3 }
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
.cover-img {
    width: 80px;
}
</style>
