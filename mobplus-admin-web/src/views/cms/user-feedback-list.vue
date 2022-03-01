<template>
    <div class="panel">
        <a-row :gutter="12">
            <!-- <a-col :span="4">
                <a-card :bordered="true" title="应用信息" class="tree-list" :loading="treeloading">
                    <a-icon
                        slot="extra"
                        type="sync"
                        @click="loadAppTreeData"
                        style="cursor: pointer"
                    />
                    <s-tree class="tree-list-style" :dataSource="appTreeData" @add="onQueryDict" />
                </a-card>
            </a-col> -->
            <a-col :span="24">
                <a-card :bordered="true" class="card-search card-list">
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l">
                            <a-form-item>
                                <a-range-picker
                                    v-decorator="[ 'time', { initialValue: [moment(moment(), 'YYYY/MM/DD'), moment(moment(), 'YYYY/MM/DD')] } ]"
                                    @change="onChangeDate" />
                            </a-form-item>
                            <a-form-item label="反馈类型">
                                <a-select placeholder="反馈类型" v-model="queryParam.type" style="width:120px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option :value="1">应用崩溃</a-select-option>
                                    <a-select-option :value="2">无响应</a-select-option>
                                    <a-select-option :value="3">功能异常</a-select-option>
                                    <a-select-option :value="4">消耗流量</a-select-option>
                                    <a-select-option :value="5">投诉</a-select-option>
                                    <a-select-option :value="6">广告</a-select-option>
                                    <a-select-option :value="7">通知</a-select-option>
                                    <a-select-option :value="8">建议</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item label="处理状态">
                                <a-select placeholder="处理状态" v-model="queryParam.status" style="width:120px">
                                    <a-select-option :value="0">不限</a-select-option>
                                    <a-select-option :value="1">未处理</a-select-option>
                                    <a-select-option :value="2">已处理</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item label="应用">
                                <a-select placeholder="应用名" v-model="queryParam.appId" style="width:120px">
                                    <a-select-option :value="0">全部</a-select-option>
                                    <a-select-option
                                        v-for="appData in appTreeData"
                                        :value="appData.id"
                                        :key="appData.id"
                                    >
                                        {{ appData.title }}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    icon="sync"
                                    v-action="['cms:feedback:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    type="primary"
                                    icon="search"
                                    @click="$refs.table.refresh(true)"
                                >查询</a-button>
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
                        :lazy="false"
                        :scroll="{ x: 1800 }"
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
                            <span v-if="text===1">应用崩溃</span>
                            <span v-else-if="text===2">无响应</span>
                            <span v-else-if="text===3">功能异常</span>
                            <span v-else-if="text===4">消耗流量</span>
                            <span v-else-if="text===5">投诉</span>
                            <span v-else-if="text===6">广告</span>
                            <span v-else-if="text===7">通知</span>
                            <span v-else-if="text===8">建议</span>
                        </template>
                        <template slot="statusSlot" slot-scope="text">
                            <span v-if="text===1">未处理</span>
                            <span v-else-if="text===2">已处理</span>
                        </template>
                        <span slot="actionSlot" slot-scope="text, record">
                            <a v-action="['cms:feedback:edit']" @click="$refs.modal.edit(record,currentApp)"> 编辑</a>
                        </span>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <user-feedback-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import UserFeedbackModal from '@/views/cms/modules/user-feedback-modal';
import moment from 'moment';

const columns = [
    {
        title: '应用名',
        dataIndex: 'appName',
        width: 150,
        fixed: 'left'
    },
    {
        title: '版本',
        dataIndex: 'version',
        align: 'center',
        width: 80
    },
    {
        title: '设备名',
        align: 'center',
        dataIndex: 'device'
    },
    {
        title: '系统',
        dataIndex: 'os',
        align: 'center',
        width: 50
    },
    {
        title: 'ip',
        dataIndex: 'ip',
        align: 'center',
        width: 120
    },
    {
        title: '地区',
        dataIndex: 'area',
        align: 'center',
        width: 150
    },
    {
        title: '留言评价内容',
        dataIndex: 'content',
        width: 400
    },
    {
        title: '联系邮箱',
        dataIndex: 'email',
        align: 'center'
    },
    {
        title: '反馈类型',
        dataIndex: 'type',
        align: 'center',
        scopedSlots: { customRender: 'typeSlot' }
    },
    {
        title: '处理状态',
        dataIndex: 'status',
        align: 'center',
        scopedSlots: { customRender: 'statusSlot' }
    },
    {
        title: '处理结果',
        dataIndex: 'result'
    },
    {
        title: '日期',
        dataIndex: 'createdAt',
        align: 'center',
        scopedSlots: { customRender: 'dateSlot' },
        customRender: (text, record) => {
            return moment(text).format('YYYY-MM-DD');
        }
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        width: 80,
        fixed: 'right',
        scopedSlots: { customRender: 'actionSlot' }
    }
];

const url = '/cms/user/feedback/';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        UserFeedbackModal,
        moment
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            queryParam: {},
            // 表头
            columns,
            currentApp: null,
            loadData: this.loadDataList,
            treeloading: false,
            appTreeData: []
        };
    },
    created () {},
    mounted () {
        this.loadAppTreeData();
    },
    computed: {},
    methods: {
        moment,
        onChangeDate (date, dateString) {
            if (date.length > 0) {
                this.queryParam.startTimeStr = dateString[0] + ' 00:00:00';
                this.queryParam.endTimeStr = dateString[1] + ' 23:59:59';
            } else {
                this.queryParam.startTimeStr = this.queryParam.endTimeStr = '';
            }
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
        onQueryDict: function (item) {
            this.currentApp = item;
            this.$refs.table.refresh(true);
        },
        loadAppTreeData: async function () {
            this.treeloading = true;
            try {
                const result = await this.$http.get(
                    '/app/entity/item'
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
            return this.$http.get(
                url + 'list',
                Object.assign(params, this.queryParam)
            );
        }
    }
};
</script>

<style lang="less" scoped>
.link-img {
    width: 180px;
}
</style>
