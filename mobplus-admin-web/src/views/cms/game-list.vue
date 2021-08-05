<template>
    <div class="panel">
        <a-row :gutter="12">
            <a-card :bordered="true" class="card-search card-list">
                <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                    <div class="r">
                        <a-button-group>
                            <a-button
                                icon="sync"
                                v-action="['cms:game:query']"
                                @click="$refs.table.refresh(false)"
                            />
                            <a-button
                                icon="plus"
                                v-action="['cms:game:create']"
                                @click="$refs.modal.add()"
                            >新增</a-button>
                            <a-button
                                icon="delete"
                                v-action="['cms:game:delete']"
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
                        <a v-action="['cms:game:edit']" @click="$refs.modal.edit(record)">编辑</a>
                        <a-divider type="vertical" />
                        <a v-action="['cms:game:delete']" @click="onDelete(record)">删除</a>
                    </span>
                    <template slot="imgSlot" slot-scope="text">
                            <img
                                class="link-img"
                                v-if="text && text.length > 0"
                                :src="text"
                            />
                    </template>
                </s-table>
            </a-card>
        </a-row>
        <game-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import GameModal from '@/views/cms/modules/game-modal';

const columns = [
    {
        title: '编号',
        dataIndex: 'id',
        width: 100
    },
    {
        title: '名称',
        dataIndex: 'name',
        width: 200
    },
    {
        title: '预览图',
        dataIndex: 'thumbUrl',
        width: 200,
        scopedSlots: { customRender: 'imgSlot' }
    },
    {
        title: '点击次数',
        dataIndex: 'clickNum',
        width: 100
    },
    {
        title: '展示次数',
        dataIndex: 'showNum',
        width: 100
    },
    // {
    //     title: '添加时间',
    //     dataIndex: 'createdAt',
    //     scopedSlots: { customRender: 'dateSlot' }
    // },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'actionSlot' }
    }
];

const url = '/cms/game/';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        STree,
        ETag,
        GameModal
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            queryParam: {},
            // 表头
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],
            treeloading: false,
            appTreeData: [],
            // 加载数据方法
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {
        // this.loadAppTreeData();
    },
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
            return this.$http.get(
                url,
                Object.assign(params, this.queryParam)
            );
        }
        // loadAppTreeData: async function () {
        //     this.treeloading = true;
        //     try {
        //         const result = await this.$http.get(
        //             '/app/entity/item',
        //             { }
        //         );
        //         this.appTreeData = this.convertTreeData(result.data);
        //     } finally {
        //         this.treeloading = false;
        //     }
        // },
        // convertTreeData: function (data) {
        //     return data.map(ele => {
        //         const item = {
        //             title: ele.name,
        //             key: ele.id
        //         };
        //         if (ele.children) {
        //             item.children = this.convertTreeData(ele.children);
        //         }
        //         return item;
        //     });
        // }
    }
};
</script>

<style lang="less" scoped>
.link-img {
    width: 180px;
}
</style>
