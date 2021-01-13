<template>
    <a-drawer
        :title="title"
        :maskClosable="maskClosable"
        :width="width"
        :placement="placement"
        :closable="closable"
        :visible="visible"
        @close="onCancel"
    >
        <div class="drawer-body">
        <slot/>
        </div>
        <slot name="footer">
            <div class="drawer-footer">
                <a-button @click="onCancel">{{ cancelText }}</a-button>
                <a-button @click="onOK" type="primary" class="ok">{{ okText }}</a-button>
            </div>
        </slot>
    </a-drawer>
</template>

<script>
export default {
    props: {
        maskClosable: { type: Boolean, default: true },
        closable: { type: Boolean, default: true },
        visible: { type: Boolean, default: false },
        placement: { type: String, default: 'right' },
        width: { type: Number, default: 700 },
        title: { type: String, default: '' },
        cancelText: { type: String, default: '取消' },
        okText: { type: String, default: '确定' }
    },
    created () {},
    computed: {},
    methods: {
        resetScreenSize: function () {
            const screenWidth = document.body.clientWidth;
            if (screenWidth < 500) {
                this.width = screenWidth;
            } else {
                this.width = 700;
            }
        },
        onCancel: function () {
            this.$emit('cancel');
        },
        onOK: function () {
            this.$emit('ok');
        }
    }
};
</script>

<style lang="less" scoped>
.ant-drawer-body{
    .drawer-body{
        padding-bottom: 50px;
    }
    .drawer-footer {
        position: absolute;
        bottom: 0px;
        left: 0;
        width: 100%;
        border-radius: 0 0 2px 2px;
        padding: 10px 16px;

        text-align: right;
        border-top: 1px solid #e8e8e8;
        background: #fff;

        .ok {
            margin-left: 8px;
        }
    }
}

</style>
