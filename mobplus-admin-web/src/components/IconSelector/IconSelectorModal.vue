<template>
    <a-modal :visible="visible" @ok="onSubmit" @cancel="onCancel">
        <icon-selector :value="currentSelectedIcon" @change="handleIconChange" />
    </a-modal>
</template>

<script>
import IconSelector from './IconSelector';

export default {
    components: {
        IconSelector
    },
    data () {
        return {
            visible: false,
            currentSelectedIcon: 'pause-circle'
        };
    },
    methods: {
        show: function (icon) {
            this.currentSelectedIcon = icon;
            this.visible = true;
        },
        close: function (success) {
            this.visible = false;
        },
        onCancel: function () {
            this.close(false);
        },
        onSubmit: function () {
            this.$emit('change', this.currentSelectedIcon);
            this.close(false);
        },
        handleIconChange (icon) {
            this.currentSelectedIcon = icon;
            this.onSubmit();
        }
    }
};
</script>
