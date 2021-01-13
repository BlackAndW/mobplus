<template>
    <div class="panel panel-channel">
        <a-card :bordered="true" class="card-search card-list">
            <!--       -->
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="l" v-action="['release:channel:query']">
                    <a-form-item label="渠道类型">
                        <a-select placeholder="渠道类型" v-model="queryParam.type" style="width:120px">
                            <a-select-option
                                v-for="item in ChannelType"
                                :key="item.value"
                                :value="item.value"
                            >{{ item.label }}</a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item label="渠道状态">
                        <a-select placeholder="渠道状态" v-model="queryParam.status" style="width:120px">
                            <a-select-option
                                v-for="item in GenStatus"
                                :key="item.value"
                                :value="item.value"
                            >{{ item.label }}</a-select-option>
                        </a-select>
                    </a-form-item>
                     <a-form-item>
                        <a-input type="text" placeholder="请输入渠道名称" v-model="queryParam.name" />
                    </a-form-item>
                    <a-form-item>
                        <a-input type="text" placeholder="请输入渠道编码" v-model="queryParam.code" />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" icon="search" @click="$refs.table.refresh(true)">查询</a-button>
                    </a-form-item>
                </div>
                <div class="r">
                    <a-button-group>
                        <a-button
                            icon="sync"
                            v-action="['release:channel:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['release:channel:create']"
                            @click="$refs.modal.add()"
                        >新增</a-button>
                        <a-button
                            icon="delete"
                            v-action="['release:channel:edit']"
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
                <a-row slot="expandedRowRender" slot-scope="record" class="table-expand">
                    <a-col :span="24">
                        <ul>
                            <li><label>备注</label><span>{{ record.remark }}</span></li>
                        </ul>
                    </a-col>
                </a-row>
                <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                <template slot="typeSlot" slot-scope="text">
                    <span v-if="text===1">应用市场</span>
                    <span v-else>厂商渠道</span>
                </template>
                <template slot="statusSlot" slot-scope="text">
                    <span v-if="text===1">未启用</span>
                    <span v-else>已启用</span>
                </template>
                <span slot="actionSlot" slot-scope="text, record">
                    <a v-action="['release:channel:edit']" @click="$refs.modal.edit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <a v-action="['release:channel:delete']" @click="onDelete(record)">删除</a>
                </span>
            </s-table>
        </a-card>
        <channel-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import ChannelModal from '@/views/release/modules/channel-modal';

const columns = [
    {
        title: '渠道名称',
        dataIndex: 'name'
    },
    {
        title: '渠道编码',
        dataIndex: 'code'
    },
    {
        title: '渠道类型',
        dataIndex: 'type',
        scopedSlots: { customRender: 'typeSlot' }
    },
    {
        title: '渠道状态',
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
        scopedSlots: { customRender: 'actionSlot' }
    }
];

const url = '/release/channel';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        ChannelModal
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 查询参数
            queryParam: {
                name: '',
                code: '',
                type: 0,
                status: 0
            },
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
    computed: {
        ChannelType: function () {
            return this.$DictFilter(this.$ChannelType, [0, 1, 2]);
        },
        GenStatus: function () {
            return this.$DictFilter(this.$GenStatus, [0, 1, 2]);
        }
    },
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
            return this.$http.get(url, Object.assign(params, this.queryParam));
        }
    }
};
</script>

<style lang="less" scoped>
.table-expand ul div, .tb_exp_item{
    display: inline-block;
    vertical-align: text-top;
    padding: 0px;
    width: 80%;
}
.tb_exp_item li{
    list-style: none;
}
</style>
