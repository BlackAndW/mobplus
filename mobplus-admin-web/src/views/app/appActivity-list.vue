<template>
    <div class="panel panel-appActivity">
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
                        <div class="l" v-action="['app:activity:query']">
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入关键字"
                                    v-model="queryParam.keyword"
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
                                    v-action="['app:activity:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['app:activity:create']"
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
                        <template slot="statusSlot" slot-scope="text">
                            <span v-if="text===1">开启</span>
                            <span v-else-if="text===2">关闭</span>
                        </template>
                        <span slot="actionAward" slot-scope="text, record">
                            <a
                                v-action="['app:activity:query']"
                                @click="$refs.awardModal.show(record)"
                            >编辑</a>
                            <a-divider type="vertical" />
                        </span>
                        <span slot="actionTask" slot-scope="text, record">
                            <a
                                v-action="['app:activity:query']"
                                @click="$refs.taskModal.show(record, activityId)"
                            >编辑</a>
                            <a-divider type="vertical" />
                        </span>
                        <span slot="action" slot-scope="text, record">
                            <a
                                v-action="['app:activity:edit']"
                                @click="$refs.modal.edit(record, currentApp)"
                            >编辑</a>
                            <a-divider type="vertical" />
                            <a
                                v-if="record.id != 0"
                                v-action="['app:activity:delete']"
                                @click="onDelete(record)">
                                删除
                            </a>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <appActivity-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
        <appActivity-award-modal ref="awardModal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
        <appActivity-task-modal ref="taskModal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AppActivityModal from '@/views/app/modules/appActivity-modal';
import AppActivityAwardModal from '@/views/app/modules/appActivity-award-modal';
import AppActivityTaskModal from '@/views/app/modules/appActivity-task-modal';

const columns = [
    {
        title: '活动名称',
        dataIndex: 'name'
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
        title: '抽奖次数',
        dataIndex: 'drawCounts'
    },
    {
        title: '消耗金币',
        dataIndex: 'needCoin'
    },
    {
        title: '金币上限',
        dataIndex: 'maxBonusCoin'
    },
    {
        title: '金币下限',
        dataIndex: 'minBonusCoin'
    },
    {
        title: '活动状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
    },
    {
        title: '奖品设置',
        dataIndex: 'actionAward',
        align: 'center',
        scopedSlots: { customRender: 'actionAward' }
    },
    {
        title: '任务设置',
        dataIndex: 'actionTask',
        align: 'center',
        scopedSlots: { customRender: 'actionTask' }
    },
    {
        title: '活动设置',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/app/activity';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AppActivityModal,
        AppActivityAwardModal,
        AppActivityTaskModal
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
            this.loadAppVersionList(this.currentApp.key);
            this.loadChannelList(this.currentApp.key);
            return this.$http.get(
                url + '?appId=' + this.currentApp.key,
                Object.assign(params, this.queryParam)
            );
        },
        loadAppVersionList: async function (appId) {
            this.appVersionList = await this.$http.get('/app/version/sct', {
                appId: appId
            });
            console.log(this.appVersionList);
        },
        loadChannelList: async function () {
            this.channelList = await this.$http.get('/release/channel/sct', {});
            console.log(this.channelList);
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
.panel-appActivity {
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
