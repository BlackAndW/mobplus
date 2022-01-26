<template>
    <div class="panel panel-app">
        <a-card :bordered="true" class="card-search card-list">
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="l" v-action="['app:entity:query']">
                    <a-form-item label="运行环境">
                        <a-select placeholder="运行环境" v-model="queryParam.runtime" style="width:120px">
                            <a-select-option :value="0">不限</a-select-option>
                            <a-select-option :value="1">android</a-select-option>
                            <a-select-option :value="2">ios</a-select-option>
                            <a-select-option :value="3">h5</a-select-option>
                            <a-select-option :value="4">wechat</a-select-option>
                            <a-select-option :value="5">qq</a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item label="应用类型">
                        <a-select placeholder="应用类型" v-model="queryParam.type" style="width:120px">
                            <a-select-option :value="0">不限</a-select-option>
                            <a-select-option :value="1">应用</a-select-option>
                            <a-select-option :value="2">游戏</a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item label="状态">
                        <a-select placeholder="状态" v-model="queryParam.status" style="width:120px">
                            <a-select-option :value="0">不限</a-select-option>
                            <a-select-option :value="1">未启用</a-select-option>
                            <a-select-option :value="2">已启用</a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item style="width: 100px">
                        <a-input type="text" placeholder="应用名称" v-model="queryParam.name" />
                    </a-form-item>
                    <a-form-item style="width: 150px">
                        <a-input type="text" placeholder="AppID" v-model="queryParam.appId" />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" icon="search" @click="$refs.table.refresh(true)">查询</a-button>
                    </a-form-item>
                </div>
                <div class="r">
                    <a-button-group class="btn-grp-margin-top">
                        <a-button
                            icon="sync"
                            v-action="['app:entity:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['app:entity:create']"
                            @click="$refs.modal.add()"
                        >新增</a-button>
                    </a-button-group>
                </div>
            </a-form>
            <s-table
                bordered
                ref="table"
                class="card-table"
                :columns="columns"
                :data="loadData"
            >
                <a-row slot="expandedRowRender" slot-scope="record" class="table-expand">
                    <a-col :span="24">
                        <ul>
                            <li><label>备注</label><span>{{ record.remark }}</span></li>
                        </ul>
                    </a-col>
                </a-row>

                <template slot="iconSlot" slot-scope="text">
                    <a-avatar shape="square" :src="text" v-if="text && text.length > 0"/>
                </template>
                <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                <template slot="typeSlot" slot-scope="text">
                    <span v-if="text===1">应用</span>
                    <span v-else-if="text===2">游戏</span>
                    <span v-else-if="text===3">壁纸</span>
                </template>
                <template slot="runtimeSlot" slot-scope="text">
                    <span v-if="text===1">android</span>
                    <span v-else-if="text===2">ios</span>
                    <span v-else-if="text===3">h5</span>
                    <span v-else-if="text===4">wechat</span>
                    <span v-else-if="text===5">qq</span>
                </template>
                <template slot="statusSlot" slot-scope="text">
                    <span v-if="text===1">未启用</span>
                    <span v-else-if="text===2">已启用</span>
                </template>
                <span slot="actionSlot" slot-scope="text, record">
                    <a v-action="['app:entity:edit']" @click="$refs.modal.edit(record)">修改</a>
                    <a-divider type="vertical" />
                    <a v-action="['app:entity:delete']" @click="onDelete(record)">删除</a>
                </span>
            </s-table>
        </a-card>
        <app-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import AppModal from '@/views/app/modules/app-modal';

const columns = [
    {
        title: '应用名称',
        dataIndex: 'name',
        width: '8%'
    },
    {
        title: '应用包名',
        dataIndex: 'pkgName',
        width: '12%'
    },
    {
        title: 'AppID',
        dataIndex: 'appId'
    },
    {
        title: '运行环境',
        dataIndex: 'runtime',
        scopedSlots: { customRender: 'runtimeSlot' }
    },
    {
        title: '应用类型',
        dataIndex: 'type',
        scopedSlots: { customRender: 'typeSlot' }
    },
    {
        title: '图标',
        dataIndex: 'iconPath',
        scopedSlots: { customRender: 'iconSlot' }
    },
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
        scopedSlots: { customRender: 'actionSlot' }
    }

];

const url = '/app/entity';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        AppModal
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 查询参数
            queryParam: {
                name: '',
                appId: '',
                type: 0,
                status: 0,
                runtime: 0
            },
            // 表头
            columns,
            // 加载数据方法
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {},
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
