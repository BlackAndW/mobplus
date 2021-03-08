<template>
    <div class="panel panel-appConfig">
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
                        <div class="l" v-action="['app:config:query']" v-if="currentApp!=null">
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入配置名称"
                                    v-model="queryParam.name"
                                />
                            </a-form-item>
                             <a-form-item label="渠道名称">
                                <a-select placeholder="渠道名称" v-model="queryParam.channelId" style="width:120px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option
                                        v-for="channel in channelList"
                                        :key="channel.id"
                                        :value="channel.id"
                                    >{{ channel.name }}</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item label="版本">
                                <a-select placeholder="版本" v-model="queryParam.appVersionId" style="width:120px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option
                                        v-for="appVersion in appVersionList"
                                        :key="appVersion.id"
                                        :value="appVersion.id"
                                    >{{ appVersion.code }}
                                    </a-select-option>
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
                                    v-action="['app:config:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                                                <a-button
                                    icon="undo"
                                    v-action="['app:config:edit']"
                                    v-if="selectedRowKeys.length>0"
                                    @click="closeAd()"
                                >关闭广告</a-button>
                                <a-button
                                    icon="redo"
                                    v-action="['app:config:edit']"
                                    v-if="selectedRowKeys.length>0"
                                    @click="openAd()"
                                >打开广告</a-button>
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
                                    <li><label>配置：</label><span><br/><pre>{{ record.conf }}</pre></span></li>
                                </ul>
                            </a-col>
                        </a-row>
                        <template slot="controlSlot" slot-scope="text">
                            <span v-if="text===1">开启</span>
                            <span v-else-if="text===2">关闭</span>
                            <span v-else>未知</span>
                        </template>
                        <template slot="statusSlot" slot-scope="text">
                            <span v-if="text===1">未启用</span>
                            <span v-else>已启用</span>
                        </template>
                        <span slot="action" slot-scope="text, record">
                            <a
                                v-action="['app:config:edit']"
                                @click="$refs.modal.edit(record, currentApp)"
                            >编辑</a>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <appConfig-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AppConfigModal from '@/views/app/modules/appConfig-modal';
const columns = [
    { title: '配置名称', dataIndex: 'name' },
    {
        title: '广告开关',
        dataIndex: 'adSwitch',
        scopedSlots: { customRender: 'controlSlot' }
    },
    {
        title: '内容开关',
        dataIndex: 'contentSwitch',
        scopedSlots: { customRender: 'controlSlot' }
    },
        {
        title: '首页开关',
        dataIndex: 'indexSwitch',
        scopedSlots: { customRender: 'controlSlot' }
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

const url = '/app/config';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AppConfigModal
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
                closeAd: function (record) {
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
                title: '确认关闭广告吗?',
                okText: '确认',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        const data = await this.$http.put(url + '/ad/close', params);
                        if (data) {
                            this.$message.success(data);
                            this.$refs.table.refresh(true);
                        }
                    } catch (err) {}
                }
            });
        },
        openAd: function (record) {
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
                title: '确认打开广告吗?',
                okText: '确认',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        const data = await this.$http.put(url + '/ad/open', params);
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
        loadAppVersionList: async function (appId) {
            this.appVersionList = await this.$http.get('/app/version/sct', {
                appId: appId
            });
        },
        loadChannelList: async function () {
            this.channelList = await this.$http.get('/release/channel/sct', {});
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
.panel-appConfig {
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
