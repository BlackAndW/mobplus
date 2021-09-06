<template>
    <div class="panel panel-menu">
        <a-card :bordered="true" class="card-search card-list">
            <!--       -->
            <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                <div class="l" v-action="['sys:menu:query']">
                    <a-form-item>
                        <a-input
                            type="text"
                            placeholder="请输入关键字"
                            v-model.trim="queryParam.keyword"
                        />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" icon="search" @click="$refs.table.refresh(true)">查询</a-button>
                    </a-form-item>
                </div>
                <div class="r">
                    <a-button-group class="btn-grp-margin-top">
                        <a-button
                            icon="sync"
                            v-action="['sys:menu:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['sys:menu:create']"
                            @click="$refs.modal.add()"
                        >新增</a-button>
                        <a-button icon="export" v-action="['sys:menu:export']">导出</a-button>
                        <a-button icon="printer" v-action="['sys:menu:print']">打印</a-button>
                        <a-button
                            icon="delete"
                            v-action="['sys:menu:delete']"
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
                :showPagination="queryParam.keyword==''?false:'auto'"
                :columns="columns"
                :data="loadData"
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            >
                <template slot="iconSlot" slot-scope="text">
                    <a-icon v-if="text" :type="text" />
                </template>
                <template slot="keepAliveSlot" slot-scope="text">
                    <a-switch :checked="text" />
                </template>
                <template slot="hiddenSlot" slot-scope="text">
                    <a-switch :checked="text" />
                </template>
                <template slot="statusSlot" slot-scope="text">
                    <e-badge :value="text" :items="$GeneralStatus"/>
                </template>
                <span slot="action" slot-scope="text, record">
                    <a v-action="['sys:menu:edit']" @click="$refs.modal.edit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <a-dropdown v-action="['sys:menu:delete', 'sys:menu:create']">
                        <a class="ant-dropdown-link">
                            更多
                            <a-icon type="down" />
                        </a>
                        <a-menu slot="overlay">
                            <a-menu-item v-action="['sys:menu:create']">
                                <a @click="$refs.modal.add(record)">添加子菜单</a>
                            </a-menu-item>
                            <a-menu-item v-action="['sys:menu:perm']">
                                <a @click="$refs.permModal.show(record)">查看授权</a>
                            </a-menu-item>
                            <a-menu-item
                                v-if="record.children==null || record.children.length==0"
                                v-action="['sys:menu:perm']"
                            >
                                <a @click="$refs.permModal.edit(record)">菜单授权</a>
                            </a-menu-item>
                            <a-menu-item v-action="['sys:menu:delete']">
                                <a @click="onDelete(record)">删除</a>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>
                </span>
            </s-table>
        </a-card>
        <sys-menu-modal ref="modal" @close="(refresh)=>{ refresh && $refs.table.refresh(false) }" />
        <sys-menu-perm-modal ref="permModal" />
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, EBadge } from '@/components';
import SysMenuModal from '@/views/sys/modules/menu-modal';
import SysMenuPermModal from '@/views/sys/modules/menu-perm-modal';

const columns = [
    { title: '菜单标题', dataIndex: 'title' },
    // { title: '英文标题', dataIndex: 'titleEn' },
    // { title: '路由名称', dataIndex: 'name' },
    {
        title: '图标',
        dataIndex: 'icon',
        align: 'center',
        scopedSlots: { customRender: 'iconSlot' }
    },
    // { title: '描述', dataIndex: 'memo' },
    { title: '路径', dataIndex: 'path' },
    // { title: '组件', dataIndex: 'component' },
    // { title: '跳转', dataIndex: 'redirect' },
    // {
    //     title: '缓存',
    //     dataIndex: 'keepAlive',
    //     scopedSlots: { customRender: 'keepAliveSlot' }
    // },
    {
        title: '隐藏',
        dataIndex: 'hidden',
        scopedSlots: { customRender: 'hiddenSlot' }
    },
    {
        title: '状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusSlot' }
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        width: 150,
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/sys/menu';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        EBadge,
        SysMenuModal,
        SysMenuPermModal
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
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {},
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
            return this.$http.get(url, Object.assign(params, this.queryParam));
        }
    }
};
</script>

<style lang="less" scoped>
</style>
