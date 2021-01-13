<template>
    <a-modal
        :title="title"
        :width="modalWidth"
        :visible="visible"
        :confirmLoading="confirmLoading"
        @ok="onSubmit"
        @cancel="onCancel"
        cancelText="关闭"
    >
        <a-spin :spinning="confirmLoading">
            <!--       -->
            <a-alert :showIcon="true" style="margin-bottom: 10px">
                <template slot="message">
                    <span style="margin-right: 12px">
                        已选择:
                        <a style="font-weight: 600">{{
                            selectedRows.length
                        }}</a>
                    </span>
                </template>
            </a-alert>
            <s-table
                bordered
                ref="table"
                size="small"
                class="card-table"
                @dataChanged="dataChanged"
                :columns="columns"
                :data="loadData"
                :scroll="{y:300}"
                :pageSize="99999999"
                :showPagination="false"
                :rowSelection="{
                    selectedRowKeys: selectedRowKeys,
                    onChange: onSelectChange
                }"
            >
                <template slot="ratioSlot" slot-scope="text, record">
                    <a-input-number
                        :min="1"
                        :max="99"
                        style="margin: -5px 0"
                        :value="text || 1"
                        @change="val => handleCellChange(val, record.key, 'ratio', record)"
                    />
                </template>
                <template slot="typeSlot" slot-scope="text">
                    {{ $GetDictLabel($AdType, text) }}
                </template>
            </s-table>
        </a-spin>
    </a-modal>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';

const columns = [
    { title: '广告位代码', dataIndex: 'adPos.code', width: 155 },
    { title: '广告位名称', dataIndex: 'adPos.name' },
    { title: '所属平台', dataIndex: 'adPos.advName' },
    {
        title: '类型',
        dataIndex: 'adPos.type',
        scopedSlots: { customRender: 'typeSlot' }
    },
    {
        title: '权重',
        dataIndex: 'ratio',
        width: 108,
        scopedSlots: { customRender: 'ratioSlot' }
    }
];

export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag
    },
    data () {
        return {
            title: '关联广告位',
            modalWidth: 900,
            visible: false,
            confirmLoading: false,

            // 查询参数
            queryParam: {},
            // 表头
            columns,
            // 选中记录
            selectedRowKeys: [],
            selectedRows: [],

            loadData: this.loadDataList,

            // ad pos
            model: {}
        };
    },
    computed: {
        AdType: function () {
            return this.$DictFilterExclude(this.$AdType, [0]);
        }
    },
    methods: {
        show: function (record) {
            this.title = '关联广告位 展示位:' + record.name + ' 类型:' + record.typeName;
            this.model = record;
            this.reload();
            this.visible = true;
        },
        onCancel: function () {
            this.close();
        },
        close: function () {
            this.$emit('close');
            this.visible = false;
            this.confirmLoading = false;
            this.selectedRowKeys = [];
            this.selectedRows = [];
        },
        onSubmit: function () {
            const params = [];
            this.selectedRows.forEach(ele => {
                params.push({
                    adPos: {
                        id: ele.adPos.id
                    },
                    ratio: ele.ratio
                });
            });
            if (params.length === 0) {
                this.$message.warning('请选择广告位!');
                return;
            }
            this.confirmLoading = true;
            this.$http
                .put(
                    '/app/position/ad/' + this.model.id,
                    params
                )
                .then(data => {
                    this.$message.success(data || '广告分配成功!');
                    this.close();
                })
                .catch(err => {
                    if (err) {
                        console.log(err.stack);
                    }
                    this.confirmLoading = false;
                });
        },
        loadDataList: function (params) {
            return this.$http.get(
                '/app/position/ad/' + this.model.id,
                Object.assign(params, this.queryParam)
            );
        },
        reload: function () {
            if (this.$refs.table) {
                this.$refs.table.refresh(true);
            }
        },
        onSelectChange: function (selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        },
        handleCellChange (value, key, column, record) {
            record[column] = value;
            this.$refs.table.$forceUpdate();
        },
        dataChanged (data) {
            data.forEach(ele => {
                if (ele.checked) {
                    this.selectedRowKeys.push(ele.id);
                    this.selectedRows.push(ele);
                }
            });
        }
    }
};
</script>

<style scoped>

</style>
