<template>
    <div class="panel panel-sys-user">
        <a-card :bordered="true" class="card-search card-list">
            <a-form
                class="act-bar"
                :form="form"
                id="form"
                ref="form"
                layout="inline"
            >
                <div class="l" v-action="['sys:user:query']">
                    <a-form-item>
                        <a-input
                            type="text"
                            placeholder="请输入关键字"
                            v-model="queryParam.keyword"
                        />
                    </a-form-item>
                    <a-form-item>
                        <a-button
                            type="primary"
                            icon="search"
                            @click="$refs.table.refresh(true)"
                            >查询</a-button
                        >
                    </a-form-item>
                </div>
                <div class="r">
                    <a-button-group>
                        <a-button
                            icon="sync"
                            v-action="['sys:user:query']"
                            @click="$refs.table.refresh(false)"
                        />
                        <a-button
                            icon="plus"
                            v-action="['sys:user:create']"
                            @click="$refs.modal.add()"
                            >新增</a-button
                        >
                        <a-button icon="export" v-action="['sys:user:export']">导出</a-button>
                        <a-button icon="printer" v-action="['sys:user:print']" >打印</a-button >
                        <a-button
                            icon="delete"
                            v-action="['sys:user:delete']"
                            v-if="selectedRowKeys.length > 0"
                            @click="onDelete()"
                            >删除</a-button
                        >
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
                :rowSelection="{
                    selectedRowKeys: selectedRowKeys,
                    onChange: onSelectChange
                }"
            >
                <a-row
                    slot="expandedRowRender"
                    slot-scope="record"
                    class="table-expand"
                >
                    <a-col :span="12">
                        <label>用户账号</label>
                        <span>{{ record.userName }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>用户昵称</label>
                        <span>{{ record.displayName }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>姓名</label>
                        <span>{{ record.name }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>性别</label>
                        <span>
                            {{ $GetDictLabel($Gender, text) }}
                        </span>
                    </a-col>
                    <a-col :span="12">
                        <label>电话号码</label>
                        <span>{{ record.mobile }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>邮箱</label>
                        <span>{{ record.email }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>生日</label>
                        <span>
                            {{ record.birthday | moment("YYYY-MM-dd") }}
                        </span>
                    </a-col>
                    <a-col :span="12">
                        <label>状态</label>
                        <span>
                            <e-tag :value="record.status" :items="$GeneralStatus"/>
                        </span>
                    </a-col>
                    <a-col :span="12">
                        <label>备注</label>
                        <span colspan="3">{{ record.remark }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>创建时间</label>
                        <span>{{ record.createdAt | moment }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>创建人</label>
                        <span>{{ record.createdName }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>更新时间</label>
                        <span>{{ record.modifiedAt | moment }}</span>
                    </a-col>
                    <a-col :span="12">
                        <label>更新人</label>
                        <span>{{ record.modifiedName }}</span>
                    </a-col>
                </a-row>
                <template slot="avatarSlot" slot-scope="text">
                    <a-avatar shape="square" :src="text" v-if="text && text.length > 0"/>
                </template>
                <template slot="genderSlot" slot-scope="text">
                    {{ $GetDictLabel($Gender, text) }}
                </template>
                <template slot="statusSlot" slot-scope="text">
                    <e-tag :value="text" :items="$GeneralStatus"/>
                </template>
                <span slot="action" slot-scope="text, record">
                    <a
                        v-action="['sys:user:edit']"
                        @click="$refs.modal.edit(record)"
                        >编辑</a
                    >
                    <a-divider type="vertical" />
                    <a-dropdown
                        v-action="[
                            'sys:user:delete',
                            'sys:user:role',
                            'sys:user:passwd'
                        ]"
                    >
                        <a class="ant-dropdown-link">
                            更多
                            <a-icon type="down" />
                        </a>
                        <a-menu slot="overlay">
                            <a-menu-item v-action="['sys:user:passwd']">
                                <a
                                    @click="$refs.passwdModal.show(record)"
                                    >重置密码</a
                                >
                            </a-menu-item>
                            <a-menu-item
                                v-if="record.id != 0"
                                v-action="['sys:user:delete']"
                            >
                                <a @click="onDelete(record)">删除</a>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>
                </span>
            </s-table>
        </a-card>
        <sys-user-modal
            ref="modal"
            @close="
                refresh => {
                    refresh ? $refs.table.refresh(false) : (a = 1);
                }
            "
        />
        <sys-passwd-modal ref="passwdModal" />
    </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin';
import { STable, ETag } from '@/components';
import SysUserModal from '@/views/sys/modules/user-modal';
import SysPasswdModal from '@/views/sys/modules/passwd-reset-modal';

const columns = [
    {
        title: '用户账号',
        dataIndex: 'userName'
    },
    {
        title: '用户昵称',
        dataIndex: 'displayName'
    },
    {
        title: '真实姓名',
        dataIndex: 'name'
    },
    {
        title: '头像',
        dataIndex: 'avatar',
        scopedSlots: { customRender: 'avatarSlot' }
    },
    {
        title: '性别',
        dataIndex: 'gender',
        scopedSlots: { customRender: 'genderSlot' }
    },
    {
        title: '电话号码',
        dataIndex: 'mobile'
    },
    {
        title: '邮箱',
        dataIndex: 'email'
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
        scopedSlots: { customRender: 'action' }
    }
];

const url = '/sys/user';
export default {
    mixins: [mixinDevice],
    components: {
        STable,
        ETag,
        SysUserModal,
        SysPasswdModal
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

<style lang="less"></style>
