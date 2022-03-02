<template>
    <div class="panel panel-sys-user">
        <a-row :gutter="12">
            <a-col :span="4">
                <a-card :bordered="true" title="应用信息" class="tree-list" :loading="treeloading">
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
                <a-card :bordered="true" class="card-search card-list">
                    <a-form class="act-bar" :form="form" id="form" ref="form" layout="inline">
                        <div class="l" v-if="currentApp!=null">
                            <a-form-item lable="付费类型">
                                <a-select placeholder="付费类型" v-model="queryParam.isFree" style="width:120px">
                                    <a-select-option :value="2">全部</a-select-option>
                                    <a-select-option :value="0">免费</a-select-option>
                                    <a-select-option :value="1">付费</a-select-option>
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
                            <a-form-item style="margin-left: 10px" label="价格（千字）:">
                                <a-input-number
                                    v-if="pricePer1000"
                                    :min="1"
                                    :max="100"
                                    style="margin: -5px 0"
                                    :value="pricePer1000 || 1"
                                    @change="val => handlePriceChange(val)"
                                />
                            </a-form-item>
                            <a-form-item label="会员折扣：">
                                <a-input-number
                                    v-if="vipDiscount"
                                    :min="1"
                                    :max="100"
                                    style="margin: -5px 0"
                                    :value="vipDiscount || 8"
                                    @change="val => handleDiscountChange(val)"
                                />
                            </a-form-item>
                        </div>
                        <div class="r" v-if="currentApp!=null">
                            <a-button-group class="btn-grp-margin-top">
                                <a-button
                                    icon="sync"
                                    v-action="['cms:book:query']"
                                    @click="$refs.table.refresh(false)"
                                />
                                <a-button
                                    icon="plus"
                                    v-action="['cms:book:create']"
                                    @click="$refs.modal.add(currentApp)"
                                >新增</a-button>
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
                        <span slot="actionSlot" slot-scope="text, record">
                            <a v-action="['cms:book:edit']" @click="$refs.modal.edit(record, currentApp)">编辑</a>
                            <a-divider type="vertical" />
                            <a v-action="['cms:book:delete']" @click="onDelete(record)">删除</a>
                        </span>
                        <template slot="isVipSlot" slot-scope="text">
                            <span v-if="text===0">所有</span>
                            <span v-else-if="text===1">会员</span>
                        </template>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <book-manager-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import BookManagerModal from '@/views/cms/modules/book-manager-modal';

const columns = [
    {
        title: '书名',
        dataIndex: 'name',
        width: 200
    },
    {
        title: '真实阅读数',
        dataIndex: 'readCount'
    },
    {
        title: '虚拟阅读数',
        dataIndex: 'fakeReadCount'
    },
    {
        title: '会员专区',
        dataIndex: 'isVip',
        scopedSlots: { customRender: 'isVipSlot' }
    },
    {
        title: '解锁次数',
        dataIndex: 'lockCount'
    },
    {
        title: '收藏次数',
        dataIndex: 'collection'
    },
    {
        title: '平均阅读数',
        dataIndex: 'avgCount'
    },
    {
        title: '字数',
        dataIndex: 'size'
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'actionSlot' }
    }
];

const url = '/cms/book/manager';
const url2 = '/cms/book/price';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        BookManagerModal,
        STree
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 表头
            columns,
            pricePer1000: 1,
            vipDiscount: 8,
            // TreeData
            queryParam: {},
            treeloading: false,
            appTreeData: [],
            currentApp: null,
            loadData: this.loadDataList
        };
    },
    created () {},
    mounted () {
        this.loadAppTreeData();
    },
    computed: {},
    methods: {
        onDelete: function (record) {
            var params = [];
            if (record !== undefined) {
                params.push(record.id);
            } else {
                // params = this.selectedRowKeys;
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
        loadDataList: function (params) {
            return this.$http.get(
                url + '?appId=' + this.currentApp.key,
                Object.assign(params, this.queryParam)
            );
        },
        loadHeadData () {
            this.$http.get(
                url2 + '?appId=' + this.currentApp.key
            ).then(res => {
                this.pricePer1000 = res.pricePer1000;
                this.vipDiscount = res.vipDiscount;
            });
        },
        handlePriceChange (value) {
            this.$http.post(url2,
            { pricePer1000: value,
              vipDiscount: this.vipDiscount,
              appId: this.currentApp.key })
            .then(this.pricePer1000 = value);
        },
        handleDiscountChange (value) {
            this.$http.post(url2,
            { pricePer1000: this.pricePer1000,
              vipDiscount: value,
              appId: this.currentApp.key })
            .then(this.vipDiscount = value);
        },
        onQueryDict: function (item) {
            this.currentApp = item;
            this.loadHeadData();
            this.$refs.table.refresh(true);
        },
        loadAppTreeData: async function () {
            this.treeloading = true;
            try {
                const result = await this.$http.get(
                    '/app/entity/item',
                    { 'type': 4 }
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

<style lang="less" scoped>
.banner-img {
    width: 180px;
}
</style>
