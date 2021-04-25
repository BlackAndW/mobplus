<template>
    <div class="panel panel-ad-position">
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
                        <div class="l" v-action="['ad:position:query']" v-if="currentApp!=null">
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
                            <a-form-item label="广告平台">
                                <a-select placeholder="请选择广告平台" v-model="queryParam.advId" style="width:150px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option
                                        v-for="adv in advList"
                                        :key="adv.id"
                                        :value="adv.id"
                                    >{{ adv.name }}</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item label="广告位">
                                <a-input type="text" placeholder="广告位代码" v-model="queryParam.code" />
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" icon="search" @click="$refs.table.refresh(true)">查询</a-button>
                            </a-form-item>
                        </div>
                        <div class="r" v-if="currentApp!=null">
                            <a-button-group>
                                <a-button
                                    icon="sync"
                                    v-action="['ad:position:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['ad:position:create']"
                                    @click="$refs.modal.add(currentApp)"
                                >新增</a-button>
                                <a-button
                                    icon="delete"
                                    v-action="['ad:position:delete']"
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
                        <template slot="advSlot" slot-scope="text, record">
                            <span>{{ text }}</span><br />
                            <span v-if="record.advName == 'mintegral'">unitId: {{ record.mintegralUnitId }} </span>
                        </template>
                        <template slot="typeSlot" slot-scope="text">
                            {{ $GetDictLabel($AdType, text) }}
                        </template>
                        <span slot="action" slot-scope="text, record">
                            <a
                                v-action="['ad:position:edit']"
                                @click="$refs.modal.edit(record, currentApp)"
                            >编辑</a>
                            <a-divider type="vertical" />
                            <a
                                v-action="['ad:position:delete']"
                                @click="onDelete(record)">删除
                            </a>

                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <adPosition-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import AdPositionModal from '@/views/ad/modules/adPosition-modal';
const columns = [
    {
        title: '广告位代码',
        dataIndex: 'code'
    },
    {
        title: '广告位名称',
        dataIndex: 'name'
    },
    {
        title: '广告平台',
        dataIndex: 'advName',
        scopedSlots: { customRender: 'advSlot' }

    },
    {
        title: '类型',
        dataIndex: 'type',
        scopedSlots: { customRender: 'typeSlot' }
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

const url = '/ad/position';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        AdPositionModal
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
            advList: []
        };
    },
    created () {},
    mounted () {
        this.loadAppTreeData();
        this.loadAdvList();
    },
    computed: {
        AdType: function () {
            return this.$DictFilterExclude(this.$AdType, [0]);
        }
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
        loadAdvList: async function () {
            this.advList = await this.$http.get('/ad/adv/sct', {});
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
.panel-ad-position {
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
