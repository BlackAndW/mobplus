<template>
    <div class="panel panel-sys-user">
        <a-card :bordered="true" class="card-search card-list">
            <!--       -->
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="l">
                    <a-button
                        v-action="['cms:charge:query']"
                        @click="change2test"
                    >切换到{{ changeType }}模式
                    </a-button>
                </div>
                <div class="r">
                    <a-button-group>
                        <a-button
                            icon="sync"
                            v-action="['cms:charge:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['cms:charge:create']"
                            @click="$refs.modal.add(queryParam)"
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
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            >
                <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                <span slot="actionSlot" slot-scope="text, record">
                    <a v-action="['cms:charge:edit']" @click="$refs.modal.edit(record, queryParam)">编辑</a>
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
        <material-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import MaterialModal from '@/views/cms/modules/charge-material-modal';

const columns = [
    {
        title: '编号',
        dataIndex: 'id'
    },
    {
        title: '分类',
        dataIndex: 'type',
        width: 50
    },
    {
        title: '封面预览',
        dataIndex: 'videoCover',
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
        MaterialModal
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 表头
            columns,
            queryParam: {},
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            changeType: '测试',
            // 加载数据方法
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {},
    computed: {},
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
            console.log(params);
            return this.$http.get(url, Object.assign(params, this.queryParam));
        }
    }
};
</script>

<style lang="less" scoped>
.cover-img {
    width: 80px;
}
</style>
