<template>
    <div class="panel panel-appProject">
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
                <a-card :bordered="true" class="card-list">
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l" v-action="['app:project:query']" v-if="currentApp!=null">
                            <a-form-item label="版本">
                                <a-select placeholder="版本" v-model="queryParam.appVersionId" style="width:80px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option
                                        v-for="appVersion in appVersionList"
                                        :key="appVersion.id"
                                        :value="appVersion.id"
                                    >{{ appVersion.code }}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                             <a-form-item label="渠道">
                                <a-select placeholder="渠道" v-model="queryParam.channelId" style="width:80px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option
                                        v-for="channel in channelList"
                                        :key="channel.id"
                                        :value="channel.id"
                                    >{{ channel.name }}</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item label="状态">
                                <a-select placeholder="状态" v-model="queryParam.status" style="width:80px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option :value="1">未启用</a-select-option>
                                    <a-select-option :value="2">已启用</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入项目名称"
                                    v-model="queryParam.name"
                                    style="width:130px"
                                />
                            </a-form-item>
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入项目编码"
                                    v-model="queryParam.code"
                                    style="width:130px"
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
                                    v-action="['app:project:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['app:project:create']"
                                    @click="$refs.modal.add(currentApp)"
                                >新增</a-button>
                                <a-button
                                    icon="delete"
                                    v-action="['app:project:delete']"
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
                        <a-row slot="expandedRowRender" slot-scope="record" class="table-expand">
                            <a-col :span="24">
                                <ul>
                                    <li><label>备注：</label><span>{{ record.remark }}</span></li>
                                </ul>
                            </a-col>
                        </a-row>
                        <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                        <template slot="statusSlot" slot-scope="text">
                            <span v-if="text===1">未启用</span>
                            <span v-else>已启用</span>
                        </template>
                        <span slot="action" slot-scope="text, record">
                            <a
                                v-action="['app:project:edit']"
                                @click="$refs.modal.edit(record, currentApp)"
                            >编辑</a>
                            <a-divider type="vertical" />
                            <a
                                v-action="['app:project:delete']"
                                @click="onDelete(record)"
                            >删除</a>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <appProject-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AppProjectModal from '@/views/app/modules/appProject-modal';

const columns = [
    { title: '项目名称', dataIndex: 'name' },
    { title: '版本', dataIndex: 'appVersionCode', width: '6%' },
    { title: '渠道', dataIndex: 'channelName', width: '8%' },
    { title: '项目编码', dataIndex: 'code', width: '15%' },
    {
        title: '状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
    },
    {
        title: '创建时间',
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

const url = '/app/project';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AppProjectModal
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
            appTreeData: [],
            channelList: [],
            appVersionList: []
        };
    },
    created () {},
    mounted () {
        this.loadAppTreeData();
        this.loadChannelList();
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
            this.loadAppVersionList(this.currentApp.key);
            return this.$http.get(
                url + '?appId=' + this.currentApp.key,
                Object.assign(params, this.queryParam)
            );
        },
        loadChannelList: async function () {
            this.channelList = await this.$http.get('/release/channel/sct', {});
        },
        loadAppVersionList: async function (appId) {
            this.appVersionList = await this.$http.get('/app/version/sct', {
                appId: appId
            });
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
                    {}
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
.panel-appProject {
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
