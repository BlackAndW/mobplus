<template>
    <div class="panel panel-appMobileConf">
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
                            <a-form-item label="状态">
                                <a-select placeholder="状态" v-model="queryParam.status" style="width:120px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option :value="1">未启用</a-select-option>
                                    <a-select-option :value="2">已启用</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入配置名称"
                                    v-model="queryParam.name"
                                />
                            </a-form-item>
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="请输入配置键"
                                    v-model="queryParam.key"
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
                                    v-action="['app:config:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['app:config:create']"
                                    @click="$refs.modal.add(currentApp)"
                                >新增</a-button>
                                <a-button
                                    icon="delete"
                                    v-action="['app:config:delete']"
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
                                    <li><label>备注</label><span>{{ record.remark }}</span></li>
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
                                v-action="['app:config:edit']"
                                @click="$refs.modal.edit(record,currentApp)"
                            >编辑</a>
                            <a-divider type="vertical" />
                            <a
                                v-action="['app:config:delete']"
                                @click="onDelete(record)"
                            >删除</a>
                        </span>
                        <span slot="actionVPN" slot-scope="text, record">
                            <a
                                v-action="['app:config:edit']"
                                @click="$refs.VPNmodal.show(record,currentApp)"
                            >编辑</a>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <appMobileConf-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
        <appVPN-list-modal ref="VPNmodal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AppMobileConfModal from '@/views/app/modules/appMobileConf-modal';
import AppVPNListModal from '@/views/app/modules/appVPN-list-modal';

const columns = [
    { title: '配置名称', dataIndex: 'name' },
    { title: '配置键', dataIndex: 'key' },
    { title: '配置值', dataIndex: 'value' },
    {
        title: '状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
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
        scopedSlots: { customRender: 'action' }
    }
];

const vpnColumn = {
    title: 'VPN管理',
    align: 'center',
    scopedSlots: { customRender: 'actionVPN' }
};

const url = '/app/mobile/conf';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AppMobileConfModal,
        AppVPNListModal
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
                url + '?appId=' + this.currentApp.key,
                Object.assign(params, this.queryParam)
            );
        },
        onQueryDict: function (item) {
            this.currentApp = item;
            // 根据app名称判断，带有vpn的应用添加配置列
            if (this.currentApp.title.toLocaleLowerCase().indexOf('vpn') !== -1) {
                if (columns.length === 6) {
                    columns.push(vpnColumn);
                }
            } else {
                columns.splice(6, 1);
            }
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
.panel-appMobileConf {
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
