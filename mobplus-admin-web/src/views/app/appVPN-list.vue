<template>
    <div class="panel panel-appVersion">
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
                <div v-if="currentApp!=null">
                    <a-tabs default-active-key="0" v-model="queryParam.type" @change="callbackTabs">
                        <a-tab-pane key="0" tab="普通节点"></a-tab-pane>
                        <a-tab-pane key="1" tab="VIP节点"></a-tab-pane>
                        <a-tab-pane key="2" tab="备用节点"></a-tab-pane>
                    </a-tabs>
                </div>
                <a-card :bordered="true" class="card-list">
                    <div class="button-area" v-if="currentApp!=null">
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    v-action="['app:config:create']"
                                    @click="$refs.confModal.edit(currentApp)"
                                >APP配置</a-button>
                            </a-button-group>
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    icon="sync"
                                    v-action="['app:config:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['app:config:create']"
                                    @click="$refs.modal.add()"
                                >新增</a-button>
                            </a-button-group>
                    </div>
                <!-- <a-spin :spinning="confirmLoading"> -->
                    <s-table
                        bordered
                        ref="table"
                        size="small"
                        class="card-table"
                        @dataChanged="dataChanged"
                        :columns="columns"
                        :data="loadData"
                        :lazy="true"
                        :scroll="{y:435}"
                        :pageSize="99999999"
                        :showPagination="false"
                        :rowSelection="{
                            selectedRowKeys: selectedRowKeys,
                            onChange: onSelectChange
                        }"
                    >
                    <template slot="iconSlot" slot-scope="text">
                        <a-avatar shape="square" :src="text" v-if="text && text.length > 0"/>
                    </template>
                    <template slot="statusSlot" slot-scope="text">
                        <span v-if="text === 1" class="red">暂停</span>
                        <span v-else-if="text === 2">启用</span>
                    </template>
                    <template slot="onlineSlot" slot-scope="text, record">
                        <span v-if="text > record.maxConn" class="red">{{ text }}</span>
                        <span v-if="text <= record.maxConn">{{ text }}</span>
                    </template>
                    <span slot="action" slot-scope="text, record">
                        <a
                            v-action="['app:config:edit']"
                            @click="$refs.modal.edit(record)"
                        >编辑</a>
                        <a-divider type="vertical" />
                    </span>
                    </s-table>
                </a-card>
                <!-- </a-spin> -->
            </a-col>
        </a-row>
        <appVPN-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
        <appVPN-conf-modal ref="confModal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AppVPNModal from '@/views/app/modules/appVPN-modal';
import AppVPNConfModal from '@/views/app/modules/appVPN-conf-modal';

const columns = [
    {
        title: '节点名称',
        dataIndex: 'b02'
    },
    {
        title: '地区',
        dataIndex: 'region'
    },
    {
        title: '图标',
        dataIndex: 'b04',
        width: 100,
        scopedSlots: { customRender: 'iconSlot' }
    },
    {
        title: '节点状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
    },
    {
        title: '在线人数',
        dataIndex: 'onlineConn',
        scopedSlots: { customRender: 'onlineSlot' }
    },
    {
        title: '连接总数',
        dataIndex: 'totalConn'
    },
    {
        title: '节点设置',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'action' }
    }
];

export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AppVPNModal,
        AppVPNConfModal
    },
    data () {
        return {
            title: '节点',
            // modalWidth: 900,
            visible: false,
            confirmLoading: false,

            // 查询参数
            queryParam: { 'type': '0' },
            // 表头
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            currentApp: null,
            loadData: this.loadDataList,
            treeloading: false,
            appTreeData: [],
            model: {}
        };
    },
    computed: {},
    mounted () {
        this.loadAppTreeData();
        const timer = setInterval(() => {
            this.$refs.table.refresh(false);
        }, 5000);
        this.$once('hook:beforeDestroy', () => {
            clearInterval(timer);
        });
    },
    methods: {
        show: function (record) {
            this.reload();
            this.visible = true;
        },
        onCancel: function () {
            this.close();
        },
        close: function () {
            this.$emit('close');
            this.visible = false;
            this.confirmLoading = false;
            this.selectedRowKeys = [];
            this.selectedRows = [];
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
                    { 'vpn': 1 }
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
        },
        loadDataList: function (params) {
            return this.$http.get('/app/vpn/' + this.currentApp.key + '?', Object.assign(params, this.queryParam));
        },
        reload: function () {
            if (this.$refs.table) {
                this.$refs.table.refresh(true);
            }
        },
        onSelectChange: function (selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        },
        callbackTabs: function () {
            this.reload();
        },
        dataChanged (data) {
            data.forEach(ele => {
                if (ele.checked) {
                    this.selectedRowKeys.push(ele.id);
                    this.selectedRows.push(ele);
                }
            });
        }
    }
};
</script>

<style lang="less" scoped>
    .button-area {
        display: flex;
        justify-content: space-between;
    }

    .red {
        color: red;
    }
</style>
