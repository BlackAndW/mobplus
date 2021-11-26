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
                <template slot="advSlot" slot-scope="text, record">
                    <span>{{ text }}</span><br />
                    <span v-if="record.adPos.advName == 'mintegral'">unitId: {{ record.adPos.mintegralUnitId }} </span>
                </template>
                <template slot="orderSlot" slot-scope="text, record">
                        <a-input :value="text" @change="e => handleCellChange(e, 'order', record)"/>
                </template>
                <template slot="limitShowCountSlot" slot-scope="text, record">
                    <span v-if="model.limitShowConfig === 0">
                        <a-input :value="text" @change="e => handleCellChange(e, 'limitShowCount', record)"/>
                    </span>
                    <span v-else>{{ text }}</span>
                </template>
                <template slot="limitClickCountSlot" slot-scope="text, record">
                    <span v-if="model.limitClickConfig === 0">
                        <a-input :value="text" @change="e => handleCellChange(e, 'limitClickCount', record)"/>
                    </span>
                    <span v-else>{{ text }}</span>
                </template>
                <template slot="ratioSlot" slot-scope="text, record">
                    <a-input-number
                        v-if="record.ratioFlag"
                        :min="1"
                        :max="99"
                        style="margin: -5px 0"
                        :value="text || 1"
                        @change="val => handleRatioCellChange(val, 'ratio', record)"
                    />
                </template>
                <template slot="typeRatioSlot" slot-scope="text, record">
                    <a-input-number
                        :min="1"
                        :max="99"
                        style="margin: -5px 0"
                        :value="text || 1"
                        @change="val => handleNumCellChange(val, 'typeRatio', record)"
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
    {
        title: '所属平台',
        dataIndex: 'adPos.advName',
        width: 125,
        scopedSlots: { customRender: 'advSlot' }
    },
    {
        title: '类型',
        width: 200,
        dataIndex: 'adPos.type',
        scopedSlots: { customRender: 'typeSlot' }
    },
    {
        title: '限制展示次数',
        width: 60,
        dataIndex: 'limitShowCount',
        scopedSlots: { customRender: 'limitShowCountSlot' }
    },
    {
        title: '限制点击次数',
        width: 60,
        dataIndex: 'limitClickCount',
        scopedSlots: { customRender: 'limitClickCountSlot' }
    },
    {
        title: '平台权重',
        width: 120,
        dataIndex: 'ratio',
        scopedSlots: { customRender: 'ratioSlot' }
    },
    {
        title: '类型权重',
        width: 120,
        dataIndex: 'typeRatio',
        scopedSlots: { customRender: 'typeRatioSlot' }
    },
    {
        title: '排序',
        width: 50,
        dataIndex: 'order',
        scopedSlots: { customRender: 'orderSlot' }
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
            modalWidth: 1200,
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
            this.title = '关联广告位 展示位:' + record.name;
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
                // console.log(ele);
                params.push({
                    adPos: {
                        id: ele.adPos.id
                    },
                    advName: ele.adPos.advName,
                    ratio: ele.ratio,
                    ratioFlag: ele.ratioFlag,
                    typeRatio: ele.typeRatio,
                    limitShowCount: ele.limitShowCount,
                    limitClickCount: ele.limitClickCount,
                    order: ele.order
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
        handleRatioCellChange (value, column, record) {
            record[column] = value;
            // 修改平台权重时，同平台的其他行也会一并修改
            const dataList = this.$refs.table._data.localDataSource;
            dataList.forEach(item => {
                if (item.adPos.advName === record.adPos.advName) {
                    item.ratio = value;
                }
            });
            this.$refs.table.$forceUpdate();
        },
        handleNumCellChange (value, column, record) {
            record[column] = value;
            this.$refs.table.$forceUpdate();
        },
        handleCellChange (e, column, record) {
            record[column] = e.target.value;
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
