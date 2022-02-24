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
                            <a-form-item>
                                <a-range-picker
                                    v-decorator="[ 'time', { initialValue: [moment(moment(), 'YYYY-MM-DD'), moment(moment(), 'YYYY-MM-DD')] } ]"
                                    @change="onChangeDate" />
                            </a-form-item>
                            <a-form-item label="书名">
                                <a-select
                                    placeholder="书名"
                                    @change="onChangeBook"
                                    v-model="queryParam.bookId"
                                    style="width:200px">
                                    <a-select-option
                                        v-for="book in bookList"
                                        :key="book.id"
                                        :value="book.id"
                                    >{{ book.name }}</a-select-option>
                                </a-select>
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
                                    @click="$refs.modal.add(queryParam.bookId)"
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
                        <template slot="dateSlot" slot-scope="text">{{ text | moment }}</template>
                        <span slot="actionSlot" slot-scope="text, record">
                            <a v-action="['cms:book:edit']" @click="$refs.modal.edit(record, queryParam.bookId, )">编辑</a>
                            <a-divider type="vertical" />
                            <a v-action="['cms:book:delete']" @click="onDelete(record)">删除</a>
                        </span>
                        <template slot="isLockSlot" slot-scope="text">
                            <span v-if="text===0">否</span>
                            <span v-else-if="text===1">是</span>
                        </template>
                    </s-table>
                </a-card>
            </a-col>
        </a-row>
        <book-chapter-modal ref="modal" @close="refresh => { refresh ? $refs.table.refresh(false) : (a = 1); }"/>
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, STree, ETag } from '@/components';
import BookChapterModal from '@/views/cms/modules/book-chapter-modal';
import moment from 'moment';

const columns = [
    {
        title: '章节名称',
        dataIndex: 'name'
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
        title: '需要解锁',
        dataIndex: 'isLock',
        scopedSlots: { customRender: 'isLockSlot' }
    },
    {
        title: '解锁次数',
        dataIndex: 'lockCount'
    },
    {
        title: '跳出率',
        dataIndex: 'exitCountPer'
    },
    {
        title: '上传时间',
        dataIndex: 'createdAt',
        scopedSlots: { customRender: 'dateSlot' },
        customRender: (text, record) => {
            if (text === 0) {
                return moment(moment()).format('YYYY-MM-DD');
            }
            return moment(text).format('YYYY-MM-DD');
        }
    },
    {
        title: '操作',
        dataIndex: 'action',
        align: 'center',
        scopedSlots: { customRender: 'actionSlot' }
    }
];

const url = '/cms/book/chapter';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        BookChapterModal,
        STree,
        moment
    },
    data () {
        return {
            form: this.$form.createForm(this),
            advanceSearch: false,
            // 表头
            columns,
            // TreeData
            queryParam: {},
            treeloading: false,
            appTreeData: [],
            bookList: [],
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
        moment,
        onChangeDate (date, dateString) {
            if (date.length > 0) {
                this.queryParam.startTimeStr = dateString[0] + ' 00:00:00';
                this.queryParam.endTimeStr = dateString[1] + ' 23:59:59';
                this.dateString = dateString;
            } else {
                this.queryParam.startTimeStr = this.queryParam.endTimeStr = '';
            }
        },
        onChangeBook (e) {
            console.log(e);
            this.$refs.table.refresh(true);
        },
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
            this.loadBookList(this.currentApp.key);
            return this.$http.get(
                url + '?appId=' + this.currentApp.key,
                Object.assign(params, this.queryParam)
            );
        },
        loadBookList: async function () {
            this.$http.get('/cms/book/manager', { appId: this.currentApp.key }).then(res => {
                this.bookList = res.data;
            });
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
</style>
