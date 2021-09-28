<template>
    <div class="panel panel-sys-user">
        <a-card :bordered="true" class="card-search card-list">
            <!--       -->
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="l">
                    <a-form-item label="素材类别">
                        <a-select placeholder="素材类别" v-model="queryParam.style" style="width:120px">
                            <a-select-option :value="0">不限</a-select-option>
                            <a-select-option :value="1">视频</a-select-option>
                            <a-select-option :value="2">壁纸</a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-button-group class="btn-grp-margin-top">
                        <a-button
                            icon="sync"
                            v-action="['cms:charge:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            type="primary"
                            icon="search"
                            @click="$refs.table.refresh(true)"
                        >查询</a-button>
                    </a-button-group>
                </div>

                <div class="r">
                    <a-button-group class="btn-grp-margin-top">
                        <a-button
                            icon="sync"
                            v-action="['cms:charge:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['cms:charge:create']"
                            @click="$refs.modal.add()"
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
                    <a v-action="['cms:charge:edit']" @click="$refs.modal.edit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <a v-action="['cms:charge:delete']" @click="onDelete(record)">删除</a>
                </span>
                <template slot="styleSlot" slot-scope="text">
                    <span v-if="text===1">视频</span>
                    <span v-else-if="text===2">壁纸</span>
                </template>
                <template slot="columnSlot" slot-scope="text">
                    <span v-if="text==='createdAt'">日期</span>
                    <span v-else-if="text==='weight'">权重</span>
                    <span v-else-if="text==='useNum'">使用次数</span>
                </template>
                <template slot="orderSlot" slot-scope="text">
                    <span v-if="text===1">降序</span>
                    <span v-else-if="text===2">升序</span>
                </template>
            </s-table>
        </a-card>
        <mtype-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import MtypeModal from '@/views/cms/modules/charge-m-type-modal';

const columns = [
    {
        title: '类型名称',
        dataIndex: 'name'
    },
    {
        title: '英文名',
        dataIndex: 'enName'
    },
    {
        title: '素材类别',
        dataIndex: 'style',
        scopedSlots: { customRender: 'styleSlot' }
    },
    {
        title: '排序依据',
        dataIndex: 'orderColumn',
        scopedSlots: { customRender: 'columnSlot' }
    },
    {
        title: '升降序',
        dataIndex: 'order',
        scopedSlots: { customRender: 'orderSlot' }
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

const url = '/cms/charge/mtype';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        MtypeModal
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
        loadDataList: function (params) {
            return this.$http.get(url, Object.assign(params, this.queryParam));
        }
    }
};
</script>

<style lang="less" scoped>
.banner-img {
    width: 180px;
}
</style>
