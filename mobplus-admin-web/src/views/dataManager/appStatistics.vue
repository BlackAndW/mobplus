<template>
    <div>
        <a-row :gutter="12">
            <a-col :span="4">
                <a-card :bordered="true" title="应用信息" :loading="treeloading">
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
                    <!--       -->
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l" v-action="['dataManager:query']" v-if="currentApp!=null">
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="用户ip"
                                    v-model="queryParam.userIp"
                                />
                            </a-form-item>
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="展示位代码"
                                    v-model="queryParam.appPositionCode"
                                />
                            </a-form-item>
                            <a-form-item>
                                <a-input
                                    type="text"
                                    placeholder="广告平台"
                                    v-model="queryParam.adChannel"
                                />
                            </a-form-item>
                            <a-form-item>
                                <a-range-picker @change="onChangeDate"/>
                            </a-form-item>
                            <a-form-item class="float-right">
                                <div>
                                    <a-button
                                        icon="download"
                                        @click="downloadFile"
                                    >导出</a-button><br />
                                </div>
                            </a-form-item>
                            <a-button-group class="btn-grp-margin-top" v-if="currentApp!=null">
                                <a-button
                                    icon="sync"
                                    v-action="['dataManager:query']"
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
                        :lazy="true"
                    >
                        <template slot="timeSlot" slot-scope="text">
                            {{ text | moment }}
                        </template>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';

const columns = [
    {
        title: 'ip地址',
        dataIndex: 'userIp'
    },
    {
        title: '展示位代码',
        dataIndex: 'appPositionCode'
    },
    {
        title: '广告平台',
        dataIndex: 'adChannel'
    },
    {
        title: '请求次数',
        dataIndex: 'adRequestCount'
    },
    {
        title: '展示次数',
        dataIndex: 'adShowCount'
    },
    {
        title: '点击次数',
        dataIndex: 'adClickCount'
    },
    {
        title: '连接日期',
        dataIndex: 'createdAt',
        defaultSortOrder: 'descend',
        sorter: (a, b) => a.createdAt - b.createdAt,
        scopedSlots: { customRender: 'timeSlot' }
    }
];

const url = '/data/appStatistics';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 查询参数
            queryParam: {},
            // 表头
            columns,
            // 加载数据方法
            loadData: this.loadDataList,
            // 分类树数据
            currentApp: null,
            downloadUrl: '',
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
        loadDataList: function (params) {
            return this.$http.get(
                url + '?appId=' + this.currentApp.key,
                Object.assign(params, this.queryParam)
            );
        },
        onQueryDict: function (item) {
            this.currentApp = item;
            this.$refs.table.refresh(true);
        },
        downloadFile (params) {
            const requestUrl = url + '/data2excel';
            this.$http.get(
                requestUrl + '?appId=' + this.currentApp.key,
                Object.assign(params, this.queryParam)
            ).then(res => {
                this.downloadUrl = res;
                console.log(this.downloadUrl);
                window.location.href = this.downloadUrl;
            });
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
    .float-right {
        float: right;
    }
</style>
